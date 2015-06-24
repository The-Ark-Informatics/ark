package au.org.theark.study.web.component.pedigree;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.study.web.component.subjectcustomdata.SubjectCustomDataContainerPanel;

public class FamilyCustomDataContainerPanel extends SubjectCustomDataContainerPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FamilyCustomDataContainerPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public SubjectCustomDataContainerPanel initialisePanel(ModalWindow modalWindow) {
		add(initialiseFeedbackPanel());
		add(initialiseCustomDataEditorWMC(modalWindow));
		if (!ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
			customDataEditorWMC.setVisible(false);
		}
		return this;
	}

	
}
