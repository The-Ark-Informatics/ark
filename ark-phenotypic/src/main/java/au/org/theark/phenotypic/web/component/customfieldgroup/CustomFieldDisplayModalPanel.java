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
public class CustomFieldDisplayModalPanel extends Panel {


	private static final long									serialVersionUID	= 1L;
	private ModalWindow											modalWindow;
	private CompoundPropertyModel<CustomFieldGroupVO>	cpmModel;
	private FeedbackPanel										detailFeedbackPanel;
	private Boolean												flag;

	/**
	 * @param id
	 */
	public CustomFieldDisplayModalPanel(String id, ModalWindow modalWindow, CompoundPropertyModel<CustomFieldGroupVO> cpmModel, FeedbackPanel feedbackPanel, Boolean flag) {
		super(id);
		this.modalWindow = modalWindow;
		this.cpmModel = cpmModel;
		this.flag = flag;
		initialiseFeedBackPanel();
		initialisePanel();
	}

	protected void initialiseFeedBackPanel() {
		/* Feedback Panel */
		detailFeedbackPanel = new FeedbackPanel("detailFeedback");
		detailFeedbackPanel.setOutputMarkupId(true);
	}

	public void initialisePanel() {
		CustomFieldDisplayForm cfdForm = new CustomFieldDisplayForm("customFieldDisplayForm", cpmModel, modalWindow, detailFeedbackPanel, flag);
		add(cfdForm);
		add(detailFeedbackPanel);
	}
}
