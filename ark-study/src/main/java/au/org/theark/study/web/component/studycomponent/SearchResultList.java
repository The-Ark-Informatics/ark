package au.org.theark.study.web.component.studycomponent;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;

public class SearchResultList extends Panel{
	
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;

	private ContainerForm containerForm;

	/**
	 * Constructor for SearchResultList
	 * @param id
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param searchResultContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param studyCompContainerForm
	 * @param details
	 */
	public SearchResultList(String id, 
							WebMarkupContainer  detailPanelContainer,
							WebMarkupContainer  detailPanelFormContainer, 
							WebMarkupContainer searchPanelContainer,
							WebMarkupContainer searchResultContainer,
							WebMarkupContainer viewButtonContainer,
							WebMarkupContainer editButtonContainer,
							ContainerForm studyCompContainerForm){
		super(id);
		this.detailPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
	}
		
	/**
	 * 
	 * @param iModel
	 * @param searchContainer
	 * @return
	 */
	public PageableListView<StudyComp> buildPageableListView(IModel iModel){
		
		PageableListView<StudyComp> sitePageableListView = new PageableListView<StudyComp>("studyCompList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<StudyComp> item) {
				
				StudyComp studyComponent = item.getModelObject();
				
				/* The Component ID */
				if(studyComponent.getId() != null){
					//Add the study Component Key here
					item.add(new Label("studyComponent.id", studyComponent.getId().toString()));	
				}else{
					item.add(new Label("studyComponent.id",""));
				}
				/* Component Name Link */
				item.add(buildLink(studyComponent));
				
				//TODO when displaying text escape any special characters
				/* Description */
				if(studyComponent.getDescription() != null){
					item.add(new Label("studyComponent.description", studyComponent.getDescription()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("studyComponent.description", ""));//the ID here must match the ones in mark-up
				}
				
				/* Add the Keyword */
				//TODO when displaying text escape any special characters
				if(studyComponent.getKeyword() != null){
					item.add(new Label("studyComponent.keyword", studyComponent.getKeyword()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("studyComponent.keyword", ""));//the ID here must match the ones in mark-up
				}
				
				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return sitePageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final StudyComp studyComponent) {
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("studyComponent.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				StudyCompVo studyCompVo  = containerForm.getModelObject();
				studyCompVo.setMode(Constants.MODE_EDIT);
				studyCompVo.setStudyComponent(studyComponent);//Sets the selected object into the model
				
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
		
		//Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", studyComponent.getName());
		link.add(nameLinkLabel);
		return link;

	}
	

}
