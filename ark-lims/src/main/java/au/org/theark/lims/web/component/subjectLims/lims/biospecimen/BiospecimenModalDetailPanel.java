package au.org.theark.lims.web.component.subjectLims.lims.biospecimen;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.biospecimen.form.BiospecimenModalDetailForm;

public class BiospecimenModalDetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1755709689461138709L;
	
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;
	
	private FeedbackPanel		detailFeedbackPanel;
	private ModalWindow 			modalWindow;
	private BiospecimenModalDetailForm			detailForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	protected CompoundPropertyModel<LimsVO> cpModel;

	public BiospecimenModalDetailPanel(String id, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel)
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
//		CompoundPropertyModel<LimsVO> cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
//		Biospecimen biospecimenBackend;
//		try {
//			biospecimenBackend = iLimsService.getBiospecimen(biospecimen.getId());
//			cpModel.getObject().setBiospecimen(biospecimenBackend);
//		} catch (EntityNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		detailForm = new BiospecimenModalDetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, cpModel);
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

}