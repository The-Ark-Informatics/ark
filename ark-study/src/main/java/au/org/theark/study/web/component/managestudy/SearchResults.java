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
import org.apache.wicket.markup.html.image.ContextImage;
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

public class SearchResults extends Panel{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer detailsMarkupContainer;
	private WebMarkupContainer saveBtnContainer;
	private WebMarkupContainer editBtnContainer;
	private WebMarkupContainer detailSummaryContainer;
	private WebMarkupContainer detailFormContainer;
	private WebMarkupContainer studyNameMarkup;
	private WebMarkupContainer studyLogoMarkup;
	private WebMarkupContainer studyLogoImageContainer;
	private WebMarkupContainer arkContextMarkup;
	
	private ContextImage noStudyLogoImage;
	private NonCachingImage studyLogoImage;
	private transient StudyHelper studyHelper;
	
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
							WebMarkupContainer studyNameMarkup,
							WebMarkupContainer studyLogoMarkup,
							WebMarkupContainer studyLogoImageContainer,
							WebMarkupContainer arkContextMarkup,
							Container containerForm){
		super(id);
		searchMarkupContainer = searchWebMarkupContainer;
		detailsMarkupContainer = detailsWebMarkupContainer;
		saveBtnContainer =saveButtonContainer;
		editBtnContainer = editButtonContainer;
		detailSummaryContainer = detailSumContainer;
		detailFormContainer = detailFormCompContainer;
		studyContainerForm = containerForm;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.studyLogoImageContainer = studyLogoImageContainer;
		this.arkContextMarkup = arkContextMarkup;
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
				
				searchResultsContainer.setVisible(false);//List view container
				searchMarkupContainer.setVisible(false);//Hide the Search panel container
				detailsMarkupContainer.setVisible(true);
				detailFormContainer.setEnabled(false);
				saveBtnContainer.setVisible(false);
				editBtnContainer.setVisible(true);
				editBtnContainer.setEnabled(true);
				detailSummaryContainer.setVisible(true);
				
				studyHelper = new StudyHelper();
				studyHelper.setStudyLogo(searchStudy, target, studyNameMarkup, studyLogoMarkup);
				studyHelper.setStudyLogoImage(searchStudy, "study.studyLogoImage", studyLogoImageContainer);

				ContextHelper contextHelper = new ContextHelper();
				contextHelper.resetContextLabel(target, arkContextMarkup);
				contextHelper.setStudyContextLabel(target, searchStudy.getName(), arkContextMarkup);
				
				target.addComponent(studyLogoImageContainer);
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
