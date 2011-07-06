package au.org.theark.lims.web.component.subjectSub.biospecimen;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectSub.biospecimen.form.BiospecimenModalDetailForm;

public class BiospecimenModalDetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1755709689461138709L;
	private FeedbackPanel		detailFeedbackPanel;
	private ModalWindow 			modalWindow;
	private BiospecimenModalDetailForm			detailForm;
	private Form<LimsVO> containerForm;
	private ArkCrudContainerVO	arkCrudContainerVo;
	private ListDetailPanel		listDetailPanel;

	public BiospecimenModalDetailPanel(String id, ModalWindow modalWindow, Form<LimsVO> containerForm, ListDetailPanel listDetailPanel)
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
		detailForm = new BiospecimenModalDetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, containerForm, listDetailPanel);
		detailForm.initialiseDetailForm();
		add(detailFeedbackPanel);
		add(detailForm);
	}

	/**
	 * @return the modalWindow
	 */
	public ModalWindow getModalWindow()
	{
		return modalWindow;
	}
	
	/**
	 * @param modalWindow the modalWindow to set
	 */
	public void setModalWindow(ModalWindow modalWindow)
	{
		this.modalWindow = modalWindow;
	}

	/**
	 * @return the detailForm
	 */
	public BiospecimenModalDetailForm getDetailForm()
	{
		return detailForm;
	}

	/**
	 * @param detailForm the detailForm to set
	 */
	public void setDetailForm(BiospecimenModalDetailForm detailForm)
	{
		this.detailForm = detailForm;
	}
}