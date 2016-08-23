package au.org.theark.study.web.component.pedigree.form;

import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.familycustomdata.FamilyCustomDataContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeConfigurationContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeDisplayPanel;
import au.org.theark.study.web.component.pedigree.PedigreeParentContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeTwinContainerPanel;

public class SearchForm extends Form<PedigreeVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService iStudyService;

	private WebMarkupContainer arkContextMarkup;
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;

	protected FeedbackPanel feedbackPanel;

	protected ArkCrudContainerVO arkCrudContainerVO;

	protected AjaxButton fatherButton;
	protected AjaxButton motherButton;
	protected AjaxButton twinButton;
	protected AjaxButton viewButton;
	protected AjaxButton configButton;
	protected AjaxButton familyButton;
	
	private  Long sessionStudyId;
	private  String sessionSubjectUID;
	protected AbstractDetailModalWindow modalWindow;
	
	protected CompoundPropertyModel<PedigreeVo> cpmModel;

	public SearchForm(String id, CompoundPropertyModel<PedigreeVo> cpmModel, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id, cpmModel);
		
		this.cpmModel= cpmModel;

		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		this.setMultiPart(true);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;


		initialiseSearchForm();
		addSearchComponentsToForm();
		
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		StudyPedigreeConfiguration pedigreeConfig = iStudyService.getStudyPedigreeConfiguration(sessionStudyId);
		boolean inbreedAllowed = false;
		if (pedigreeConfig != null && BooleanUtils.isTrue(pedigreeConfig.getInbreedAllowed())) {
			inbreedAllowed = true;
		} 
		SecurityUtils.getSubject().getSession().setAttribute(Constants.INBREED_ALLOWED, inbreedAllowed);

		disableSearchForm(sessionPersonId, au.org.theark.core.Constants.MESSAGE_NO_SUBJECT_IN_CONTEXT);
		disableSaveButtons();
		disableFamilyDataButton(sessionStudyId, sessionSubjectUID);

	}

	protected void addSearchComponentsToForm() {
		add(fatherButton);
		add(motherButton);
		add(twinButton);
		add(viewButton);
		add(configButton);
		add(familyButton);
		add(modalWindow);
	}

	protected void initialiseSearchForm() {
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getSearchPanelContainer());

			}
		};

		fatherButton = new AjaxButton(au.org.theark.core.Constants.FATHER) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Father");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				modalWindow.setContent(new PedigreeParentContainerPanel("content", arkContextMarkup, studyNameMarkup, studyLogoMarkup, modalWindow, Constants.MALE, getFormModelObject().getRelationshipList()));
				modalWindow.show(target);
			}

		};

		motherButton = new AjaxButton(au.org.theark.core.Constants.MOTHER) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Mother");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				modalWindow.setContent(new PedigreeParentContainerPanel("content", arkContextMarkup, studyNameMarkup, studyLogoMarkup, modalWindow, Constants.FEMALE, getFormModelObject().getRelationshipList()));
				modalWindow.show(target);
			}

		};

		twinButton = new AjaxButton(au.org.theark.core.Constants.TWIN) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Twins");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				modalWindow.setContent(new PedigreeTwinContainerPanel("content", modalWindow));
				modalWindow.show(target);
			}

		};
		twinButton.setEnabled(false);

		viewButton = new AjaxButton(au.org.theark.core.Constants.VIEW) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Pedigree View");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(90);
				modalWindow.setContent(new PedigreeDisplayPanel("content"));
				modalWindow.show(target);
			}
		};

		configButton = new AjaxButton(au.org.theark.core.Constants.CONFIG) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Pedigree Configuration");
				modalWindow.setInitialWidth(35);
				modalWindow.setInitialHeight(60);
				modalWindow.setContent(new PedigreeConfigurationContainerPanel("content", modalWindow));
				modalWindow.setWindowClosedCallback(new WindowClosedCallback() {
					private static final long serialVersionUID = 1L; 
                        @Override 
                        public void onClose(AjaxRequestTarget target) 
                        { 
                        	disableFamilyDataButton(sessionStudyId, sessionSubjectUID);
                            target.add(familyButton); 
                        } 
                });
				modalWindow.show(target);
			}
		};

		familyButton = new AjaxButton(au.org.theark.core.Constants.FAMILY) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Family Data");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				modalWindow.setContent(new FamilyCustomDataContainerPanel("content", modalWindow).initialisePanel());
				modalWindow.show(target);
			}
		};

	}

	protected void disableSearchForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionId == null) {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				arkCrudContainerVO.getSearchResultPanelContainer().setEnabled(false);
				this.error(errorMessage);
			} else {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		} else {
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	private PedigreeVo getFormModelObject() {
		return getModelObject();
	}

	protected void disableSaveButtons() {
		if (!ArkPermissionHelper.isActionPermitted(Constants.SAVE)) {
			fatherButton.setEnabled(false);
			motherButton.setEnabled(false);
			twinButton.setEnabled(false);
			configButton.setEnabled(false);
			familyButton.setEnabled(false);
		}
	}

	protected void disableFamilyDataButton(Long studyId, String subjectUID) {
		String familyId = iStudyService.getSubjectFamilyId(studyId, subjectUID);
		if (familyId == null) {
			familyButton.setEnabled(false);
		} else {
			familyButton.setEnabled(true);
		}
	}
}
