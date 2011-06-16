package au.org.theark.study.web.component.correspondence;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.component.correspondence.form.ContainerForm;





public class SearchResultListPanel extends Panel {

	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	
	public SearchResultListPanel(String id,
		WebMarkupContainer detailPanelContainer,
		WebMarkupContainer detailPanelFormContainer,
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
	

	public PageableListView<Correspondences> buildPageableListView(IModel iModel) {
		
		PageableListView<Correspondences> pageableListView = new PageableListView<Correspondences>("correspondenceList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			
			@Override
			protected void populateItem(ListItem<Correspondences> item) {
				Correspondences correspondence = item.getModelObject();
				
				// set the date to be the link to details
				item.add(buildLink(correspondence));
			
				if(correspondence.getCorrespondenceStatusType() != null) {
					item.add(new Label("correspondenceStatusType.name", correspondence.getCorrespondenceStatusType().getName()));
				} else {
					item.add(new Label("correspondenceStatusType.name", ""));
				}
				
				if(correspondence.getOperator() != null) {
					item.add(new Label("operator.ldapUserName", correspondence.getOperator().getLdapUserName()));
				}else {
					item.add(new Label("operator.ldapUserName", ""));
				}

				if(correspondence.getCorrespondenceModeType() != null) {
					item.add(new Label("correspondenceModeType.name", correspondence.getCorrespondenceModeType().getName()));
				}else {
					item.add(new Label("correspondenceModeType.name", ""));
				}
				
				if(correspondence.getCorrespondenceDirectionType() != null) {
					item.add(new Label("correspondenceDirectionType.name", correspondence.getCorrespondenceDirectionType().getName()));
				}
				else {
					item.add(new Label("correspondenceDirectionType.name", ""));
				}
				
				if(correspondence.getCorrespondenceOutcomeType() != null) {
					item.add(new Label("correspondenceOutcomeType.name", correspondence.getCorrespondenceOutcomeType().getName()));
				}else {
					item.add(new Label("correspondenceOutcomeType.name", ""));
				}
			}
		};
		
		return pageableListView;
	}

	
	private AjaxLink buildLink(final Correspondences correspondence) {
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("correspondence") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				containerForm.getModelObject().setCorrespondence(correspondence);
				
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
		
		Label nameLinkLabel = new Label("correspondenceLabel", correspondence.getDate().toString());
		link.add(nameLinkLabel);
		return link;
	}
	
	
}
