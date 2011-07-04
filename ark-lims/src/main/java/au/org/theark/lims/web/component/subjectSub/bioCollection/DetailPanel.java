package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.web.component.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectSub.bioCollection.form.DetailForm;

public class DetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8745753185256494362L;
	private FeedbackPanel		detailFeedbackPanel;
	private ModalWindow 			modalWindow;
	private DetailForm			detailForm;
	private ContainerForm 	containerForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	public DetailPanel(String id)
	{
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		arkCrudContainerVo = new ArkCrudContainerVO();
		initialisePanel();
	}

	public DetailPanel(String id, ModalWindow modalWindow)
	{
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		this.setModalWindow(modalWindow);
		this.arkCrudContainerVo = new ArkCrudContainerVO();
		initialisePanel();
	}
	
	public DetailPanel(String id, ModalWindow modalWindow, ContainerForm containerForm)
	{
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		this.setModalWindow(modalWindow);
		this.arkCrudContainerVo = new ArkCrudContainerVO();
		this.containerForm = containerForm;
		initialisePanel();
	}
	
	protected FeedbackPanel initialiseFeedBackPanel(){
		/* Feedback Panel */
		detailFeedbackPanel= new FeedbackPanel("detailFeedback");
		detailFeedbackPanel.setOutputMarkupId(true);
		return detailFeedbackPanel;
	}

	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, containerForm);
		detailForm.initialiseDetailForm();
		add(detailFeedbackPanel);
		add(detailForm);
	}

	/**
	 * @param modalWindow the modalWindow to set
	 */
	public void setModalWindow(ModalWindow modalWindow)
	{
		this.modalWindow = modalWindow;
	}

	/**
	 * @return the modalWindow
	 */
	public ModalWindow getModalWindow()
	{
		return modalWindow;
	}

	public DetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(DetailForm detailForm)
	{
		this.detailForm = detailForm;
	}
}