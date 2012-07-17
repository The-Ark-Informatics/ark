package au.org.theark.worktracking.web.component.billableitem;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.worktracking.entity.BillableItem;
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
				item.add(buildLink(billableItem));				
				if (billableItem.getWorkRequest() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST, billableItem.getWorkRequest().getName()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_WORK_REQUEST, ""));
				}
				if (billableItem.getQuantity() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_QUANTITY, billableItem.getQuantity().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_QUANTITY, ""));
				}
				if (billableItem.getInvoice() != null) {
					String invoiceType= billableItem.getInvoice().toString().startsWith(Constants.Y)? Constants.YES : Constants.NO;
					item.add(new Label(Constants.BILLABLE_ITEM_INVOICE,invoiceType ));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_INVOICE, ""));
				}
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
				if(Constants.BILLABLE_ITEM_AUTOMATED.equalsIgnoreCase( containerForm.getModelObject().getBillableItem().getType())){
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(false);
					
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(false);
				}
				else{
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(true);
					if(containerForm.getModelObject().getBillableItem().getId()!=null){
						arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(true);
					}
				}
			}
		};
		Label nameLinkLabel = new Label("nameLbl", billableItem.getDescription());
		link.add(nameLinkLabel);
		return link;

	}

}