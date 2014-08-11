package au.org.theark.geno.web.component.tableeditor;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.geno.web.component.tableeditor.form.BeamForm;
import au.org.theark.geno.web.component.tableeditor.form.RowContainerForm;
import au.org.theark.geno.web.component.tableeditor.form.RowForm;

public class RowPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private RowForm				rowForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	arkContextContainer;
	private RowContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;
	private AbstractDetailModalWindow modalWindow;

	public RowPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, RowContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;
		initialisePanel();
	}

	public void initialisePanel() {
		rowForm = new RowForm("rowForm", feedBackPanel, arkContextContainer, containerForm, arkCrudContainerVO, modalWindow);
		add(rowForm);
	}
}