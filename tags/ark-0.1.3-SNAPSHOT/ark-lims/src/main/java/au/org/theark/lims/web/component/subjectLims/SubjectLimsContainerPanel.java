package au.org.theark.lims.web.component.subjectLims;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.web.component.subjectLims.lims.LimsContainerPanel;
import au.org.theark.lims.web.component.subjectLims.subject.SubjectContainerPanel;

/**
 * @author elam
 * 
 */
public class SubjectLimsContainerPanel extends Panel
{
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -1L;
	private static final Logger			log	= LoggerFactory.getLogger(SubjectLimsContainerPanel.class);

	private WebMarkupContainer			arkContextMarkup;
	private SubjectContainerPanel		subjectContainerPanel;
	private WebMarkupContainer			limsContainerWMC;
	private Panel						limsContainerPanel;

	public SubjectLimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup; 
		initialisePanel();
	}
	
	public void initialisePanel() {
		subjectContainerPanel = new SubjectContainerPanel("subjectContainerPanel", arkContextMarkup);
		this.add(subjectContainerPanel);
		
		limsContainerWMC = new WebMarkupContainer("limsContainerWMC");
		limsContainerWMC.setOutputMarkupPlaceholderTag(true);
		
//		limsContainerPanel = new LimsContainerPanel("limsContainerPanel", arkContextMarkup);
		limsContainerPanel = new EmptyPanel("limsContainerPanel");
		limsContainerWMC.add(limsContainerPanel);
		this.add(limsContainerWMC);
		
		subjectContainerPanel.setContextUpdateLimsWMC(limsContainerWMC);
	}
	
	@Override
	public void onBeforeRender() {
		//Get the Study and SubjectUID in Context
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			if (limsContainerPanel instanceof EmptyPanel) {
				Panel limsContainerPanel = new LimsContainerPanel("limsContainerPanel", arkContextMarkup);
				limsContainerWMC.addOrReplace(limsContainerPanel);
			}
		}
		super.onBeforeRender();
	}

}
