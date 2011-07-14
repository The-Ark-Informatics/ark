package au.org.theark.study.web.component.manageuser;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;
import au.org.theark.study.web.component.manageuser.form.DetailForm;

public class DetailPanel  extends Panel{

	private ContainerForm containerForm;
	private FeedbackPanel feedbackPanel;
	private DetailForm detailForm;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	
	public DetailPanel(String id, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO,ContainerForm containerForm) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedbackPanel;
	}

	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm",feedbackPanel,arkCrudContainerVO,containerForm);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}
	
	

}
