package au.org.theark.worktracking.web.component.billableitem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.worktracking.model.vo.BillableItemVo;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.billableitem.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm workRequestContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = workRequestContainerForm;
	}

	public PageableListView<BillableItem> buildPageableListView(IModel iModel) {

		PageableListView<BillableItem> sitePageableListView = new PageableListView<BillableItem>("billableItemList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<BillableItem> item) {

				BillableItem billableItem = item.getModelObject();

				if (billableItem.getId() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_ID, billableItem.getId().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_ID, ""));
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);	
				String commenceDate = "";
				if (billableItem.getCommenceDate() != null) {
					commenceDate = simpleDateFormat.format(billableItem.getCommenceDate());
					item.add(new Label(Constants.BILLABLE_ITEM_COMMENCE_DATE	, commenceDate));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_COMMENCE_DATE, commenceDate));
				}
				
				if (billableItem.getWorkRequest() != null
							&& billableItem.getWorkRequest().getResearcher()!=null) {			
					Researcher researcher= billableItem.getWorkRequest().getResearcher();
					item.add(new Label(Constants.BILLABLE_ITEM_RESEARCHER_FULL_NAME	, researcher.getFirstName()+" "+researcher.getLastName() ));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_RESEARCHER_FULL_NAME, ""));
				}
				if (billableItem.getWorkRequest() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST, billableItem.getWorkRequest().getName()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST, ""));
				}
				
				item.add(buildLink(billableItem));
				
				if (billableItem.getQuantity() != null) {
					DecimalFormat qunatityFormat = new DecimalFormat("#0.##");
					item.add(new Label(Constants.BILLABLE_ITEM_QUANTITY, qunatityFormat.format( billableItem.getQuantity())));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_QUANTITY, ""));
				}
				DecimalFormat df = new DecimalFormat("#0.00");
				if (billableItem.getItemCost() != null) {
					
					item.add(new Label(Constants.BILLABLE_ITEM_ITEM_COST, df.format(billableItem.getItemCost())));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_ITEM_COST, ""));
				}
				if (billableItem.getWorkRequest()!=null && billableItem.getWorkRequest().getGstAllow() != null) {
					
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST_GST_ALLOW, billableItem.getWorkRequest().getGstAllow()?"Yes":"No"));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST_GST_ALLOW, "No"));
				}
				if (billableItem.getWorkRequest()!=null && billableItem.getWorkRequest().getGst() != null) {
					
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST_GST, df.format(billableItem.getWorkRequest().getGst())));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST_GST, ""));
				}
				if (billableItem.getTotalCost() != null) {
					
					item.add(new Label(Constants.BILLABLE_ITEM_TOTAL_COST, df.format(billableItem.getTotalCost())));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_TOTAL_COST, ""));
				}
				
				if (billableItem.getInvoice() != null) {
					String invoiceType= billableItem.getInvoice().toString().startsWith(Constants.Y)? Constants.YES : Constants.NO;
					item.add(new Label(Constants.BILLABLE_ITEM_INVOICE,invoiceType ));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_INVOICE, ""));
				}
				
				if (billableItem.getType()!=null) {
					String automatedType= Constants.BILLABLE_ITEM_AUTOMATED.equalsIgnoreCase(billableItem.getType())? Constants.YES : Constants.NO;
					item.add(new Label(Constants.BILLABLE_ITEM_ITEM_TYPE,automatedType ));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_ITEM_TYPE, ""));
				}
				
				if (billableItem.getAttachmentFilename()!=null) {
					
					item.add(new Label(Constants.BILLABLE_ITEM_ATTACHMENT_FILE_NAME,billableItem.getAttachmentFilename() ));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_ATTACHMENT_FILE_NAME, ""));
				}
				
				item.add(buildDownloadButton(billableItem));
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return sitePageableListView;
	}

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final BillableItem billableItem) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.BILLABLE_ITEM_DESCRIPTION) {
			@Override
			public void onClick(AjaxRequestTarget target) {

				BillableItemVo billableItemVo = containerForm.getModelObject();
				billableItemVo.setMode(Constants.MODE_EDIT);
				billableItemVo.setBillableItem(billableItem);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				if(Constants.BILLABLE_ITEM_AUTOMATED.equalsIgnoreCase( billableItemVo.getBillableItem().getType())){					
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(false);
				}
				else{
					if(billableItemVo.getBillableItem().getId()!=null){
						arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(true);
					}
				}
				
				if(Constants.Y.equalsIgnoreCase(billableItemVo.getBillableItem().getInvoice())){
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(false);
				}
				else{
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(true);
				}
			}
		};
		Label nameLinkLabel = new Label("nameLbl", billableItem.getDescription());
		link.add(nameLinkLabel);
		return link;

	}
	
	private AjaxButton buildDownloadButton(final BillableItem billableItem) {
		AjaxButton ajaxButton = new AjaxButton(Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 4494157023173040075L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				byte[] data = billableItem.getAttachmentPayload();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, billableItem.getAttachmentFilename()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO: log!
				System.err.println(" Error Downloading File ");
				this.error("There was an error while downloading file.  Please contact Administrator");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (billableItem.getAttachmentFilename() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}

}