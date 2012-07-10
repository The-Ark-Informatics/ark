package au.org.theark.worktracking.web.component.billableitemtype;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.billableitemtype.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	public PageableListView<BillableItemType> buildPageableListView(IModel iModel) {

		PageableListView<BillableItemType> sitePageableListView = new PageableListView<BillableItemType>("billableItemTypeList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<BillableItemType> item) {

				BillableItemType billableItemType = item.getModelObject();

				if (billableItemType.getId() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_ID, billableItemType.getId().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_ID, ""));
				}

				item.add(buildLink(billableItemType));				
				if (billableItemType.getQuantityPerUnit() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT, billableItemType.getQuantityPerUnit().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT, ""));
				}
				
				if (billableItemType.getUnitPrice() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE, billableItemType.getUnitPrice().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE, ""));
				}
				
				if (billableItemType.getGst() != null) {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_GST, billableItemType.getGst().toString()));
				}
				else {
					item.add(new Label(Constants.BILLABLE_ITEM_TYPE_GST, ""));
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
	private AjaxLink buildLink(final BillableItemType billableItemType) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.BILLABLE_ITEM_TYPE_ITEM_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {

				BillableItemTypeVo billableItemTypeVo = containerForm.getModelObject();
				billableItemTypeVo.setMode(Constants.MODE_EDIT);
				billableItemTypeVo.setBillableItemType(billableItemType);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				
				if(Constants.BILLABLE_ITEM_TYPE_DEFAULT.equalsIgnoreCase( containerForm.getModelObject().getBillableItemType().getType())){
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(false);
					
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(false);
				}
				else{
					arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(true);
					if(containerForm.getModelObject().getBillableItemType().getId()!=null){
						arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.DELETE).setEnabled(true);
					}
				}

			}
		};
		Label nameLinkLabel = new Label("nameLbl", billableItemType.getItemName());
		link.add(nameLinkLabel);
		return link;

	}

}