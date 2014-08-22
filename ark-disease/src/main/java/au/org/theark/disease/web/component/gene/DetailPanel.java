package au.org.theark.disease.web.component.gene;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.disease.web.component.gene.form.ContainerForm;
import au.org.theark.disease.web.component.gene.form.DetailForm;

public class DetailPanel extends Panel {
	private static final long	serialVersionUID	= -5633334040450350186L;
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
	}

	public DetailPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO,
			WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
	}

	public void initialisePanel() {

		detailsForm = new DetailForm("detailsForm", feedBackPanel, arkContextContainer, containerForm, arkCrudContainerVO);
		detailsForm.initialiseDetailForm();
		add(detailsForm);
	}

}
