package au.org.theark.study.web.component.managestudy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.managestudy.form.Container;

public class SearchResults extends Panel{

	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer detailsMarkupContainer;
	private WebMarkupContainer saveBtnContainer;
	private WebMarkupContainer editBtnContainer;
	private WebMarkupContainer detailSummaryContainer;
	private WebMarkupContainer detailFormContainer;
	@SpringBean( name = "userService")
	private IUserService userService;
	private Container studyContainerForm;
	public SearchResults(	String id, 
							WebMarkupContainer searchWebMarkupContainer, 
							WebMarkupContainer detailsWebMarkupContainer,
							WebMarkupContainer saveButtonContainer,
							WebMarkupContainer editButtonContainer, 
							WebMarkupContainer detailSumContainer,
							WebMarkupContainer detailFormCompContainer,
							Container containerForm){
		super(id);
		searchMarkupContainer = searchWebMarkupContainer;
		detailsMarkupContainer = detailsWebMarkupContainer;
		saveBtnContainer =saveButtonContainer;
		editBtnContainer = editButtonContainer;
		detailSummaryContainer = detailSumContainer;
		detailFormContainer = detailFormCompContainer;
		studyContainerForm = containerForm;
	}

	public PageableListView<Study> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer){
		
		PageableListView<Study> studyPageableListView = new PageableListView<Study>("studyList", iModel, 10) {
			@Override
			protected void populateItem(final ListItem<Study> item) {
				
				Study study = item.getModelObject();
				
				if(study.getStudyKey() != null){
					item.add(new Label("studyKey", study.getStudyKey().toString()));	
				}else{
					item.add(new Label("studyKey",""));
				}
				
				item.add(buildLink(study,searchResultsContainer));
				
				if(study.getContactPerson() != null){
					item.add(new Label("contact", study.getContactPerson()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("contact", ""));//the ID here must match the ones in mark-up
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);//TODO Change this to DATE_FORMAT_AU
				String dateOfApplication ="";
				if(study.getDateOfApplication() != null){
					dateOfApplication = simpleDateFormat.format(study.getDateOfApplication());
					item.add(new Label("dateOfApplication",dateOfApplication));
				}else{
					item.add(new Label("dateOfApplication",dateOfApplication));
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return studyPageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final Study study, final WebMarkupContainer searchResultsContainer) {
		
		AjaxLink link = new AjaxLink("studyName") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				//Place the selected study in session context for the user
				SecurityUtils.getSubject().getSession().setAttribute("studyId", study.getStudyKey());
				studyContainerForm.getModelObject().setStudy(study);
				List<ModuleVO> modules = new ArrayList<ModuleVO>();
				Collection<ModuleVO> modulesLinkedToStudy = new  ArrayList<ModuleVO>();
				try {
					modules = userService.getModules(true);//source this from a static list or on application startup 
					modulesLinkedToStudy  = userService.getModulesLinkedToStudy(study.getName(), true);
					studyContainerForm.getModelObject().setModulesSelected(modulesLinkedToStudy);
					studyContainerForm.getModelObject().setModulesAvailable(modules);
					
				} catch (ArkSystemException e) {
					//log the error message and notify sys admin to take appropriate action
					this.error("A system error has occured. Please try after some time.");
				}
				
				searchResultsContainer.setVisible(false);//List view container
				searchMarkupContainer.setVisible(false);//Hide the Search panel container
				detailsMarkupContainer.setVisible(true);
				detailFormContainer.setEnabled(false);
				saveBtnContainer.setVisible(false);
				editBtnContainer.setVisible(true);
				editBtnContainer.setEnabled(true);
				detailSummaryContainer.setVisible(true);
				
				target.addComponent(searchMarkupContainer);
				target.addComponent(detailsMarkupContainer);
				target.addComponent(searchResultsContainer);
				target.addComponent(saveBtnContainer);
				target.addComponent(editBtnContainer);
				target.addComponent(detailSummaryContainer);
				target.addComponent(detailFormContainer);
			}
		};
		
		//Add the label for the link
		Label studyNameLinkLabel = new Label("studyNameLink", study.getName());
		link.add(studyNameLinkLabel);
		return link;

	}
	
}
