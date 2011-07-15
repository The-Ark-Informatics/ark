package au.org.theark.admin.web.component.module;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.admin.web.component.module.form.ContainerForm;
import au.org.theark.admin.web.component.module.form.DetailForm;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class DetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4102920895345707174L;
	private FeedbackPanel		feedbackPanel;
	private ContainerForm		containerForm;
	private DetailForm			detailForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param studyCrudContainerVO
	 * @param containerForm
	 */
	public DetailPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", feedbackPanel, containerForm, arkCrudContainerVo);
		detailForm.initialiseDetailForm();
		add(detailForm); // Add the form to the panel
	}
}