package au.org.theark.study.web.component.familycustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.study.model.vo.FamilyCustomDataVO;
import au.org.theark.study.web.component.familycustomdata.form.FamilyCustomDataEditorForm;

public class FamilyCustomDataEditorPanel extends Panel {

	private static final long serialVersionUID = -1L;

	protected CompoundPropertyModel<FamilyCustomDataVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected AbstractCustomDataEditorForm<FamilyCustomDataVO> customDataEditorForm;
	protected FamilyCustomDataDataViewPanel dataViewPanel;
	protected Label warnSaveLabel;
	protected ModalWindow modalWindow;

	public FamilyCustomDataEditorPanel(String id, CompoundPropertyModel<FamilyCustomDataVO> cpModel, FeedbackPanel feedBackPanel,ModalWindow modalWindow) {
		super(id);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.modalWindow=modalWindow;
	}

	public FamilyCustomDataEditorPanel initialisePanel() {

		dataViewPanel = new FamilyCustomDataDataViewPanel("dataViewPanel", cpModel).initialisePanel(null, au.org.theark.core.Constants.FAMILY);

		customDataEditorForm = new FamilyCustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel,modalWindow).initialiseForm();
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
