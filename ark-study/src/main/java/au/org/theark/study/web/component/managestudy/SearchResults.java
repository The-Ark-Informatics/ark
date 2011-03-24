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
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class SearchResults extends Panel{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private NonCachingImage studyLogoImage;
	private transient StudyHelper studyHelper;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	private Container studyContainerForm;
	
	private StudyCrudContainerVO studyCrudContainerVO;
	public SearchResults(String id, StudyCrudContainerVO studyCrudContainerVO,Container containerForm){
		super(id);
		this.studyCrudContainerVO = studyCrudContainerVO;
		studyContainerForm = containerForm;
	}

	public PageableListView<Study> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer){
		
		PageableListView<Study> studyPageableListView = new PageableListView<Study>("studyList", iModel, 10) {
			@Override
			protected void populateItem(final ListItem<Study> item) {
				
				Study study = item.getModelObject();
				
				if(study.getId() != null){
					item.add(new Label("id", study.getId().toString()));	
				}else{
					item.add(new Label("id",""));
				}
				
				item.add(buildLink(study,searchResultsContainer));
				
				if(study.getContactPerson() != null){
					item.add(new Label("contact", study.getContactPerson()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("contact", ""));//the ID here must match the ones in mark-up
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);
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
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, study.getId());
				SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
				SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
				//We specify the type of person here as Subject
				
				Study searchStudy = iArkCommonService.getStudy(study.getId()); 
				studyContainerForm.getModelObject().setStudy(searchStudy);
				studyContainerForm.getModelObject().setSubjectUidExample(iArkCommonService.getSubjectUidExample(searchStudy));
				
				WebMarkupContainer wmc = (WebMarkupContainer) studyCrudContainerVO.getDetailPanelContainer();
				Details detailsPanel = (Details) wmc.get("detailsPanel");
				DetailForm detailForm = (DetailForm) detailsPanel.get("detailForm");
				
				// All SubjectUID generator fields grouped within a container(s)
				WebMarkupContainer autoSubjectUidcontainer = detailForm.getAutoSubjectUidContainer();
				WebMarkupContainer subjectUidcontainer = detailForm.getSubjectUidContainer();
				
				// Disable all SubjectUID generation fields is subjects exist
				if(iArkCommonService.getSubjectCount(searchStudy) > 0)
				{
					autoSubjectUidcontainer.setEnabled(false);
					subjectUidcontainer.setEnabled(false);
					target.addComponent(subjectUidcontainer);
				}
				
				// Example auto-generated SubjectUID
				Label subjectUidExampleLbl = detailForm.getSubjectUidExampleLbl();
				subjectUidExampleLbl.setDefaultModelObject(studyContainerForm.getModelObject().getSubjectUidExample());
				target.addComponent(subjectUidExampleLbl);
				
				List<ModuleVO> modules = new ArrayList<ModuleVO>();
				Collection<ModuleVO> modulesLinkedToStudy = new  ArrayList<ModuleVO>();
				try {
					modules = userService.getModules(true);//source this from a static list or on application startup 
					modulesLinkedToStudy  = userService.getModulesLinkedToStudy(searchStudy.getName(), true);
					studyContainerForm.getModelObject().setModulesSelected(modulesLinkedToStudy);
					studyContainerForm.getModelObject().setModulesAvailable(modules);
					
				} catch (ArkSystemException e) {
					//log the error message and notify sys admin to take appropriate action
					this.error("A system error has occured. Please try after some time.");
				}
				
				studyCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				studyCrudContainerVO.getSearchPanelContainer().setVisible(false);
				studyCrudContainerVO.getDetailPanelContainer().setVisible(true);
				studyCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				
				studyCrudContainerVO.getViewButtonContainer().setVisible(true);//saveBtn
				studyCrudContainerVO.getViewButtonContainer().setEnabled(true);//saveBtn
				
				studyCrudContainerVO.getEditButtonContainer().setVisible(false);
				
				studyCrudContainerVO.getSummaryContainer().setVisible(true);
				studyHelper = new StudyHelper();
				studyHelper.setStudyLogo(searchStudy, target,studyCrudContainerVO.getStudyNameMarkup(), studyCrudContainerVO.getStudyLogoMarkup());
				studyHelper.setStudyLogoImage(searchStudy, "study.studyLogoImage", studyCrudContainerVO.getStudyLogoImageContainer());
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.resetContextLabel(target, studyCrudContainerVO.getArkContextMarkup());
				contextHelper.setStudyContextLabel(target, searchStudy.getName(), studyCrudContainerVO.getArkContextMarkup());
				
				target.addComponent(studyCrudContainerVO.getStudyLogoImageContainer());
				target.addComponent(studyCrudContainerVO.getSearchPanelContainer());
				target.addComponent(studyCrudContainerVO.getDetailPanelContainer());
				target.addComponent(studyCrudContainerVO.getSearchResultPanelContainer());
				target.addComponent(studyCrudContainerVO.getViewButtonContainer());
				target.addComponent(studyCrudContainerVO.getEditButtonContainer());
				target.addComponent(studyCrudContainerVO.getSummaryContainer());
				target.addComponent(studyCrudContainerVO.getDetailPanelFormContainer());
	
			}
			
		};
		
		//Add the label for the link
		Label studyNameLinkLabel = new Label("studyNameLink", study.getName());
		link.add(studyNameLinkLabel);
		return link;

	}

	/**
	 * @param studyLogoImage the studyLogoImage to set
	 */
	public void setStudyLogoImage(NonCachingImage studyLogoImage)
	{
		this.studyLogoImage = studyLogoImage;
	}

	/**
	 * @return the studyLogoImage
	 */
	public NonCachingImage getStudyLogoImage()
	{
		return studyLogoImage;
	}
	
}
