package au.org.theark.lims.web.component.subjectLims.lims.bioCollection;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.bioCollection.form.CollectionModalDetailForm;

public class CollectionModalDetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8745753185256494362L;
	
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;
	
	private FeedbackPanel		detailFeedbackPanel;
	private ModalWindow 			modalWindow;
	private CollectionModalDetailForm			detailForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	protected CompoundPropertyModel<LimsVO> cpModel;

	public CollectionModalDetailPanel(String id, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel)
	{
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		this.setModalWindow(modalWindow);
		this.arkCrudContainerVo = new ArkCrudContainerVO();
		this.cpModel = cpModel;
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
		detailForm = new CollectionModalDetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, cpModel);
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

}