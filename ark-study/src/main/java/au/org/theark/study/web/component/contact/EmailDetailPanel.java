package au.org.theark.study.web.component.contact;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.contact.form.ContainerForm;
import au.org.theark.study.web.component.contact.form.EmailDetailForm;

public class EmailDetailPanel extends Panel {
	private static final long serialVersionUID = 6698569647044327113L;
	private EmailDetailForm detailForm;
	private FeedbackPanel feedBackPanel;
	private ContainerForm containerForm;
	private ArkCrudContainerVO arkCrudContainerVO;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer editButtonContainer;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public EmailDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
	}

	public void initialisePanel() {
		replacedExsistingWebMarkUps();
		detailForm = new EmailDetailForm("emailDetailsForm", feedBackPanel, arkCrudContainerVO, containerForm);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	/**
	 * This will replace the abstract level web mark ups to suit to phone
	 * details form.
	 */
	private void replacedExsistingWebMarkUps() {
		// Contains the controls of the Detail form
		detailPanelFormContainer = new WebMarkupContainer("emailDetailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		arkCrudContainerVO.setDetailPanelFormContainer(detailPanelFormContainer);
		// Web markup to hold the buttons visible when in "Edit" mode
		editButtonContainer = new WebMarkupContainer("emailEditButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		arkCrudContainerVO.setEditButtonContainer(editButtonContainer);
	}
}
