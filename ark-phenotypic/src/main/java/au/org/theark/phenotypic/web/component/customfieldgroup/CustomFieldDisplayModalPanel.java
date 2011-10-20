package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.phenotypic.web.component.customfieldgroup.form.CustomFieldDisplayForm;

/**
 * @author nivedann
 *
 */
public class CustomFieldDisplayModalPanel extends Panel{

	private ModalWindow	modalWindow;
	private CompoundPropertyModel<CustomFieldGroupVO> cpmModel;
	private FeedbackPanel feedbackPanel;
	/**
	 * @param id
	 */
	public CustomFieldDisplayModalPanel(String id, ModalWindow modalWindow, CompoundPropertyModel<CustomFieldGroupVO> cpmModel,FeedbackPanel feedbackPanel) {
		super(id);
		this.modalWindow = modalWindow;
		this.cpmModel = cpmModel;
		this.feedbackPanel = feedbackPanel;
		initialisePanel();
		
	}
	
	public void initialisePanel(){
		
		CustomFieldDisplayForm cfdForm = new CustomFieldDisplayForm("customFieldDisplayForm",cpmModel,modalWindow,feedbackPanel);
		add(cfdForm);
	}

}
