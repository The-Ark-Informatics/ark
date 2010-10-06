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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.StudyComp;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;

public class SearchResultList extends Panel{
	
	
	private WebMarkupContainer detailsPanelContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	
	
	public SearchResultList(String id, WebMarkupContainer  detailPanelContainer, WebMarkupContainer searchPanelContainer,ContainerForm studyCompContainerForm){
		super(id);
		this.detailsPanelContainer = detailPanelContainer;

	}
		
	/**
	 * 
	 * @param iModel
	 * @param searchContainer
	 * @return
	 */
	public PageableListView<StudyComp> buildPageableListView(IModel iModel){
		
		PageableListView<StudyComp> sitePageableListView = new PageableListView<StudyComp>("studyCompList", iModel, 10) {
			@Override
			protected void populateItem(final ListItem<StudyComp> item) {
				
				StudyComp studyComponent = item.getModelObject();
				
				/* The Component ID */
				if(studyComponent.getStudyCompKey() != null){
					//Add the study Component Key here
					item.add(new Label("studyComponent.studyCompKey", studyComponent.getStudyCompKey().toString()));	
				}else{
					item.add(new Label("studyComponent.studyCompKey",""));
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
				//TODO when displaying text escape any special characters\
				
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
		
		AjaxLink link = new AjaxLink("studyComponent.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				StudyCompVo studyCompVo  = containerForm.getModelObject();
				studyCompVo.setMode(Constants.MODE_EDIT);
				studyCompVo.setStudyComponent(studyComponent);//Sets the selected object into the model
				detailsPanelContainer.setVisible(true);
				//TODO make the ID and Name field disabled
				searchPanelContainer.setVisible(false);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};
		
		//Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", studyComponent.getName());
		link.add(nameLinkLabel);
		return link;

	}
	

}
