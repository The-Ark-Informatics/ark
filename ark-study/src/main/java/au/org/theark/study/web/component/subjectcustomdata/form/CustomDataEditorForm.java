package au.org.theark.study.web.component.subjectcustomdata.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectcustomdata.EditModeButtonsPanel;
import au.org.theark.study.web.component.subjectcustomdata.IEditModeEventHandler;
import au.org.theark.study.web.component.subjectcustomdata.IViewModeEventHandler;
import au.org.theark.study.web.component.subjectcustomdata.SubjectCustomDataEditorPanel;
import au.org.theark.study.web.component.subjectcustomdata.SubjectCustomDataVO;
import au.org.theark.study.web.component.subjectcustomdata.ViewModeButtonsPanel;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class CustomDataEditorForm extends Form<SubjectCustomDataVO> implements IViewModeEventHandler, IEditModeEventHandler {

	private static final Logger log = LoggerFactory.getLogger(CustomDataEditorForm.class);

	private CompoundPropertyModel<SubjectCustomDataVO>			cpModel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;
	
	protected FeedbackPanel	feedbackPanel;
	protected WebMarkupContainer dataViewWMC;
	protected WebMarkupContainer buttonsPanelWMC;
	
	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor formVisitor = new ArkFormVisitor();

	public CustomDataEditorForm(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedbackPanel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		setOutputMarkupPlaceholderTag(true);
	}

	public CustomDataEditorForm initialiseForm() {
		dataViewWMC = new WebMarkupContainer("dataViewWMC") {
			// this WMC must have the visitor since the form itself isn't always repainted
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				visitChildren(formVisitor);	// automatically mark the required fields
			}
		};
		dataViewWMC.setOutputMarkupId(true);
		dataViewWMC.setEnabled(false);	//default to View mode
		this.add(dataViewWMC);
		
		buttonsPanelWMC = new WebMarkupContainer("buttonsPanelWMC");
		buttonsPanelWMC.setOutputMarkupPlaceholderTag(true);
		initialiseViewButtonsPanel();
		this.add(buttonsPanelWMC);
		
		return this;
	}

	private void initialiseViewButtonsPanel() {
		ViewModeButtonsPanel buttonsPanel = new ViewModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setCancelButtonVisible(false);	//hide the Cancel button in view mode
		buttonsPanel.setCancelButtonEnabled(false);	//disable the Cancel button in view mode
		buttonsPanelWMC.addOrReplace(buttonsPanel);
	}

	public WebMarkupContainer getDataViewWMC() {
		return dataViewWMC;
	}

	@Override
	public void onEditCancel(AjaxRequestTarget target, Form<?> form) {
		this.info("Cancelling the edit");
		target.addComponent(feedbackPanel);
		
		initialiseViewButtonsPanel();	//put View mode buttons back
		dataViewWMC.setEnabled(false);
		target.addComponent(dataViewWMC);
		target.addComponent(buttonsPanelWMC);
	}

	@Override
	public void onEditCancelError(AjaxRequestTarget target, Form<?> form) {
		this.info("Error occurred with cancelling the edit");
		target.addComponent(feedbackPanel);
	}

	@Override
	public void onEditDelete(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onEditDelete() should be imposible");
	}

	@Override
	public void onEditDeleteError(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onEditDeleteError() should be imposible");
	}

	@Override
	public void onEditSave(AjaxRequestTarget target, Form<?> form) {
		studyService.createOrUpdateCustomFields(cpModel.getObject().getSubjectCustomFieldDataList());
		this.info("Saved the edits");
		target.addComponent(feedbackPanel);
	}

	@Override
	public void onEditSaveError(AjaxRequestTarget target, Form<?> form) {
		target.addComponent(feedbackPanel);
		target.addComponent(dataViewWMC);	// assumes each data entry field has a FeedbackBorder
	}

	@Override
	public void onViewCancel(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onViewCancel() should be imposible");
	}

	@Override
	public void onViewCancelError(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onViewCancelError() should be imposible");
	}

	@Override
	public void onViewEdit(AjaxRequestTarget target, Form<?> form) {
		//put Edit mode buttons in
		EditModeButtonsPanel buttonsPanel = new EditModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setDeleteButtonEnabled(false);	// delete button not used in data entry
		buttonsPanel.setDeleteButtonVisible(false);
		buttonsPanelWMC.addOrReplace(buttonsPanel);
		dataViewWMC.setEnabled(true);	//allow fields to be edited
		target.addComponent(dataViewWMC);
		target.addComponent(buttonsPanelWMC);
	}

	@Override
	public void onViewEditError(AjaxRequestTarget target, Form<?> form) {
	}
	
}
