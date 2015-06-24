package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.study.model.vo.SubjectCustomDataVO;
import au.org.theark.study.web.component.subjectcustomdata.form.CustomDataEditorForm;

public class FamilyCustomDataEditorPanel extends SubjectCustomDataEditorPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ModalWindow modalWindow;
	

	public FamilyCustomDataEditorPanel(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedBackPanel, ModalWindow modalWindow) {
		super(id, cpModel, feedBackPanel);
		this.modalWindow=modalWindow;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public FamilyCustomDataEditorPanel initialisePanel() {
		dataViewPanel = new SubjectCustomDataDataViewPanel("dataViewPanel", cpModel).initialisePanel(null,au.org.theark.study.web.Constants.FAMILY_CUSTOM_FIELD_TYPE);

		customDataEditorForm = new CustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel,modalWindow).initialiseForm();
		customDataEditorForm.setMultiPart(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataViewPanel.getDataView()) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(customDataEditorForm.getDataViewWMC());
				target.add(this);
			}
		};
		pageNavigator.setVisible(false);
		customDataEditorForm.getDataViewWMC().add(dataViewPanel);

		warnSaveLabel = new Label("warnSaveLabel", new ResourceModel("warnSaveLabel"));
		warnSaveLabel.setVisible(ArkPermissionHelper.isActionPermitted(Constants.NEW));

		this.add(customDataEditorForm);
		this.add(pageNavigator);
		this.add(warnSaveLabel);

		return this;
	}
	

}
