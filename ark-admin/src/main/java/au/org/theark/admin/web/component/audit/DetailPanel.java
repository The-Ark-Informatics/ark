package au.org.theark.admin.web.component.audit;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.admin.web.component.audit.form.ContainerForm;
import au.org.theark.admin.web.component.audit.form.DetailForm;
import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class DetailPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedbackPanel;
	private ContainerForm containerForm;
	private DetailForm detailForm;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	public DetailPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", feedbackPanel, containerForm, arkCrudContainerVO);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}
	
}
