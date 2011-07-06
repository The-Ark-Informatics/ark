package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.web.component.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectSub.bioCollection.form.CollectionModalDetailForm;

public class CollectionModalDetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8745753185256494362L;
	private FeedbackPanel		detailFeedbackPanel;
	private ModalWindow 			modalWindow;
	private CollectionModalDetailForm			detailForm;
	private ContainerForm 	containerForm;
	private ArkCrudContainerVO	arkCrudContainerVo;
	private ListDetailPanel listDetailPanel;

	public CollectionModalDetailPanel(String id, ModalWindow modalWindow, ContainerForm containerForm, ListDetailPanel listDetailPanel)
	{
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		this.setModalWindow(modalWindow);
		this.arkCrudContainerVo = new ArkCrudContainerVO();
		this.containerForm = containerForm;
		this.listDetailPanel = listDetailPanel;
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
		detailForm = new CollectionModalDetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, containerForm, listDetailPanel);
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

	public CollectionModalDetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(CollectionModalDetailForm detailForm)
	{
		this.detailForm = detailForm;
	}
}