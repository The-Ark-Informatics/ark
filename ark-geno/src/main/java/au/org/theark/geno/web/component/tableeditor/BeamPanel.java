package au.org.theark.geno.web.component.tableeditor;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.geno.web.component.tableeditor.form.BeamContainerForm;
import au.org.theark.geno.web.component.tableeditor.form.BeamForm;

public class BeamPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private BeamForm			beamForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	arkContextContainer;
	private BeamContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;
	protected AbstractDetailModalWindow modalWindow;
	
	public BeamPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, BeamContainerForm beamContainerForm, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = beamContainerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;
		initialisePanel();
	}

	public void initialisePanel() {
		beamForm = new BeamForm("beamForm", feedBackPanel, arkContextContainer, containerForm, arkCrudContainerVO, modalWindow);
//		detailsForm.initialiseForm();
		add(beamForm);
//		add(feedBackPanel);
	}
}