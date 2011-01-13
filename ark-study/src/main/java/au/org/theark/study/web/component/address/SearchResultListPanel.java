/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;

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
		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
		
	}

	
	public PageableListView<Address> buildPageableListView(IModel iModel){
		
		PageableListView<Address> pageableListView = new PageableListView<Address>(Constants.ADDRESS_LIST,iModel,5) {

			@Override
			protected void populateItem(ListItem<Address> item) {
			
				Address address = item.getModelObject();
				item.add(buildLink(address));
				
//				if(address.getStreetAddress() != null){
//					item.add(new Label("streetAddress",address.getStreetAddress()));
//				}
				
				if(address.getCity() != null){
					item.add(new Label("city",address.getCity()));	
				}else{
					item.add(new Label("city",""));
				}
				
				if(address.getCountryState() != null && address.getCountryState().getState() != null){
					item.add(new Label("countryState.state",address.getCountryState().getState()));	
				}else{
					item.add(new Label("countryState.state",""));
				}
				
				if(address.getPostCode() != null){
					item.add(new Label("postCode",address.getPostCode()));	
				}else{
					item.add(new Label("postCode",""));
				}
				
				if(address.getCountry() != null && address.getCountry().getName() != null){
					item.add(new Label("country.name",address.getCountry().getName()));	
				}else{
					item.add(new Label("country.name",""));
				}
				
			}
		};
		return pageableListView;
		
	}
	
	private AjaxLink buildLink(final Address address){
		
		AjaxLink link = new AjaxLink("address") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setAddress(address);
				
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
		Label nameLinkLabel = new Label(Constants.ADDRESS_LABEL, address.getStreetAddress());
		link.add(nameLinkLabel);
		return link;
	}
	
	
}
