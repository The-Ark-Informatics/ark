package au.org.theark.study.web.component.pedigree.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.PedigreeConfigurationContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeDisplayPanel;
import au.org.theark.study.web.component.pedigree.PedigreeParentContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeTwinContainerPanel;

public class SearchForm extends Form<PedigreeVo> {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	private WebMarkupContainer				arkContextMarkup;
	protected WebMarkupContainer			studyNameMarkup;
	protected WebMarkupContainer			studyLogoMarkup;

	protected FeedbackPanel					feedbackPanel;

	protected ArkCrudContainerVO			arkCrudContainerVO;

	protected AjaxButton						fatherButton;
	protected AjaxButton						motherButton;
	protected AjaxButton						twinButton;
	protected AjaxButton						viewButton;
	protected AjaxButton						configButton;

	protected AbstractDetailModalWindow	modalWindow;

	public SearchForm(String id, CompoundPropertyModel<PedigreeVo> cpmModel, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup,
			ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id, cpmModel);

		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		this.setMultiPart(true);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject in context. Please bring a subject into context via the Subject tab.");
		disableSaveButtons();
	}

	protected void addSearchComponentsToForm() {
		add(fatherButton);
		add(motherButton);
		add(twinButton);
		add(viewButton);
		add(configButton);
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
				modalWindow.setContent(new PedigreeParentContainerPanel("content", arkContextMarkup, studyNameMarkup, studyLogoMarkup, modalWindow, Constants.MALE, getFormModelObject()
						.getRelationshipList()));
				modalWindow.show(target);
			}

		};

		motherButton = new AjaxButton(au.org.theark.core.Constants.MOTHER) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Mother");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				modalWindow.setContent(new PedigreeParentContainerPanel("content", arkContextMarkup, studyNameMarkup, studyLogoMarkup, modalWindow, Constants.FEMALE, getFormModelObject()
						.getRelationshipList()));
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
			}
			else {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
		else {
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
		}
	}
}
