package au.org.theark.worktracking.web.component.billableitemtype;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.worktracking.web.component.billableitemtype.form.ContainerForm;
import au.org.theark.worktracking.web.component.billableitemtype.form.DetailForm;

public class DetailPanel extends Panel {
	private static final long	serialVersionUID	= 1L;

	private DetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {

		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;

	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailsForm", feedBackPanel, arkCrudContainerVO, containerForm);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailForm) {
		this.detailForm = detailForm;
	}

}
