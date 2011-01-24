/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.consent;

import java.text.SimpleDateFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.form.ContainerForm;


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
	
	public PageableListView<Consent> buildPageableListView(IModel iModel){
		
		PageableListView<Consent> pageableListView = new PageableListView<Consent>(Constants.CONSENT_LIST,iModel,5) {

			@Override
			protected void populateItem(ListItem<Consent> item) {
				// TODO Auto-generated method stub
				Consent consent = item.getModelObject();
				
				item.add(buildLink(consent));
				if(consent.getStudyComponentStatus() != null){
					item.add(new Label("studyComponentStatus.name", consent.getStudyComponentStatus().getName()));
				}else{
					item.add(new Label("studyComponentStatus.name", " "));
				}
				
				if(consent.getConsentStatus() != null){
					item.add(new Label("consentStatus.name",consent.getConsentStatus().getName()));
				}else{
					item.add(new Label("consentStatus.name"," "));
				}
				
				if(consent.getConsentType() != null){
					item.add( new Label("consentType.name", consent.getConsentType().getName()));
				}else{
					item.add( new Label("consentType.name", ""));
				}
				
				if(consent.getConsentedBy() != null){
					item.add( new Label("consentedBy", consent.getConsentedBy()));
				}else{
					item.add( new Label("consentedBy",""));
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String consentDate ="";
				
				if(consent.getConsentDate() != null){
					item.add( new Label("consentDate", simpleDateFormat.format(consent.getConsentDate())));
				}else{
					item.add( new Label("consentDate",consentDate));
				}
			}
		};
		return pageableListView;
		
	}
	
	private AjaxLink buildLink(final Consent consent){
		
		AjaxLink link = new AjaxLink("studyComp.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setConsent(consent);
				
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
		Label nameLinkLabel = new Label(Constants.CONSENT_COMPONENT_LABEL,consent.getStudyComp().getName());
		link.add(nameLinkLabel);
		return link;
	}
	

}
