/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.phone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class SearchResultListPanel extends Panel{

	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	
	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, 
			WebMarkupContainer  detailPanelContainer,
			WebMarkupContainer  detailPanelFormContainer, 
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer searchResultContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			ContainerForm containerForm) {
		
		super(id);
		// TODO Auto-generated constructor stub
	 	this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
	}
	
	public PageableListView<Phone> buildPageableListView(IModel iModel){
		
		PageableListView<Phone> pageableListView = new PageableListView<Phone>(Constants.PHONE_LIST,iModel,10) {

			@Override
			protected void populateItem(ListItem<Phone> item) {
				// TODO Auto-generated method stub
				Phone phone = item.getModelObject();
				
				item.add(buildLink(phone));
				
				if(phone.getAreaCode() != null){
					item.add(new Label("areaCode",phone.getAreaCode()));	
				}else{
					item.add(new Label("areaCode",""));
				}
				item.add(buildLink(phone));
				if(phone.getPhoneType() != null && phone.getPhoneType().getName() != null){
					item.add(new Label("phoneType.name",phone.getPhoneType().getName()));	
				}else{
					item.add(new Label("phoneType.name",""));
				}
			}
		};
		return pageableListView;
		
	}
	
	private AjaxLink buildLink(final Phone phone){
		
		AjaxLink link = new AjaxLink("phone.phoneNumber") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setPhone(phone);
				
				detailPanelContainer.setVisible(true);
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				editButtonContainer.setVisible(false);
				
				target.addComponent(searchResultContainer);
				target.addComponent(detailPanelContainer);
				target.addComponent(detailPanelFormContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}
			
		};
		Label nameLinkLabel = new Label(Constants.PHONE_NUMBER_LABEL, phone.getPhoneNumber());
		link.add(nameLinkLabel);
		return link;
	}
	

}
