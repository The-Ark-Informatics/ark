package au.org.theark.lims.web.component.subjectLims.lims;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.bioCollection.BioCollectionListPanel;
import au.org.theark.lims.web.component.subjectLims.lims.biospecimen.BiospecimenListPanel;
import au.org.theark.lims.web.component.subjectLims.lims.form.ContainerForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings("unchecked")
public class LimsContainerPanel extends Panel
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= -1L;
	private static final Logger				log	= LoggerFactory.getLogger(LimsContainerPanel.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;
	
	protected LimsVO 						limsVO = new LimsVO();
	protected CompoundPropertyModel<LimsVO> cpModel;

	protected FeedbackPanel					feedbackPanel;
	protected WebMarkupContainer			arkContextMarkup;
	protected ContainerForm					containerForm;
//	protected WebMarkupContainer			resultsListWMC;
	protected Panel							collectionListPanel;
	protected Panel 						biospecimenListPanel;
//	protected AbstractDetailModalWindow		modalWindow;

	public LimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup; 
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}
	
	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		
		prerenderContextCheck();

		this.add(containerForm);
	}

	protected void prerenderContextCheck() {		
		//Get the Study and SubjectUID in Context
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (contextLoaded) {
				BioCollectionListPanel biocollectionListPanel = new BioCollectionListPanel("biocollectionListPanel", feedbackPanel, cpModel);
				collectionListPanel = biocollectionListPanel;
//				collectionListPanel = new BioCollectionContainerPanel("biocollectionListPanel", arkContextMarkup);
//					resultsListWMC.addOrReplace(collectionListPanel);
				containerForm.add(collectionListPanel);
				
				BiospecimenListPanel bioSpecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel);
				biospecimenListPanel = bioSpecimenListPanel;
//				biospecimenListPanel = new BiospecimenContainerPanel("biospecimenListPanel", arkContextMarkup);
//					resultsListWMC.addOrReplace(biospecimenListPanel);
				containerForm.add(biospecimenListPanel);
			}
			else {
				containerForm.info("Could not load subject in context - record is invalid (e.g. deleted)");
				collectionListPanel = new EmptyPanel("biocollectionListPanel");
				collectionListPanel.setOutputMarkupId(true);
//					resultsListWMC.addOrReplace(collectionListPanel);
				containerForm.add(collectionListPanel);
				
				biospecimenListPanel = new EmptyPanel("biospecimenListPanel");
				biospecimenListPanel.setOutputMarkupId(true);
//					resultsListWMC.addOrReplace(biospecimenListPanel);
				containerForm.add(biospecimenListPanel);
			}
		}
	}

	protected WebMarkupContainer initialiseFeedBackPanel()
	{
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

}
