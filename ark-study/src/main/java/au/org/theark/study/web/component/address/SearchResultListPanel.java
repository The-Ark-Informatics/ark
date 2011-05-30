/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;
import au.org.theark.study.web.component.address.form.SearchForm;

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
		
		PageableListView<Address> pageableListView = new PageableListView<Address>(Constants.ADDRESS_LIST,iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			@Override
			protected void populateItem(final ListItem<Address> item) {
			
				Address address = item.getModelObject();
				item.add(buildLink(address));
				
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
				
				if(address.getAddressType() != null && address.getAddressType().getName()!= null){
					item.add(new Label("addressType.name",address.getAddressType().getName()));
				}else{
					item.add(new Label("addressType.name",""));
				}
				
				if(address.getDateReceived() != null){
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateReceived ="";
					dateReceived = simpleDateFormat.format(address.getDateReceived());
					item.add(new Label("address.dateReceived",dateReceived));
				}else{
					item.add(new Label("address.dateReceived",""));
				}
				
				if(address.getPreferredMailingAddress() != null && address.getPreferredMailingAddress() == true){
					item.add(new ContextImage("address.preferredMailingAddress", new Model<String>("images/icons/tick.png")));
				}else{
					item.add(new Label("address.preferredMailingAddress",""));
				}
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return pageableListView;
		
	}
	
	private AjaxLink buildLink(final Address address){
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("address") {

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
				
				// Update the state choices based on selected address pre-render...
				SearchForm searchForm = (SearchForm) ((SearchPanel) searchPanelContainer.get("searchComponentPanel")).get("searchForm");
				searchForm.updateDetailFormPrerender(address);
				
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
