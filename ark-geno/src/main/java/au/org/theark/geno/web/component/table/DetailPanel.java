package au.org.theark.geno.web.component.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.geno.web.component.table.form.ContainerForm;
import au.org.theark.geno.web.component.table.form.DetailForm;

public class DetailPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private DetailForm			detailsForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	arkContextContainer;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialisePanel();
	}

	public void initialisePanel() {
		detailsForm = new DetailForm("detailsForm", feedBackPanel, arkContextContainer, containerForm, arkCrudContainerVO);
//		detailsForm.initialiseForm();
		add(detailsForm);
	}


}
