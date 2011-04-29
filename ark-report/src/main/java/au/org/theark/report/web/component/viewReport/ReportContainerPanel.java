
package au.org.theark.report.web.component.viewReport;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.report.model.vo.ReportSelectVO;
import au.org.theark.report.service.IReportService;
import au.org.theark.report.web.component.viewReport.form.ReportSelectForm;
import au.org.theark.report.web.component.viewReport.studySummary.ReportParamsPanel;
import au.org.theark.report.web.component.viewReport.studySummary.ReportSelectPanel;
import au.org.theark.report.web.component.viewReport.studySummary.ReportViewPanel;

/**
 * @author elam
 *
 */
public class ReportContainerPanel extends Panel {
	
	private ReportParamsPanel reportParamsPanel;
	private ReportViewPanel reportViewPanel;
	private ReportSelectForm reportSelectForm;
	
	protected ReportContainerVO reportContainerVO = new ReportContainerVO();

	//	protected IModel<Object> iModel;
	protected CompoundPropertyModel<ReportSelectVO> reportSelectCPM;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	private IReportService reportService;
	
	/**
	 * @param id
	 */
	public ReportContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		reportSelectCPM = new CompoundPropertyModel<ReportSelectVO>(new ReportSelectVO());
		this.reportContainerVO.setArkContextMarkup(arkContextMarkup);
	}
	
	public void initialisePanel() {
				
		reportSelectForm = new ReportSelectForm("reportSelectForm", reportSelectCPM, reportContainerVO);
		prerenderContextCheck();
		add(reportSelectForm);
		
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		reportContainerVO.setFeedBackPanel(new FeedbackPanel("feedbackMessage"));
		reportContainerVO.getFeedBackPanel().setOutputMarkupId(true);
		return reportContainerVO.getFeedBackPanel();
	}

	protected void prerenderContextCheck() {		
		//Get the Person in Context and determine the Person Type
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

//		if ((sessionStudyId != null) && (sessionPersonId != null)) {
//
//			String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
//			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT))
//			{
//				Person person;
//				boolean contextLoaded = false;
//				try {
//					person = studyService.getPerson(sessionPersonId);
//					SubjectVO subjectVO = new SubjectVO();
//					subjectVO.getSubjectStudy().setPerson(person);	//must have Person id
//					subjectVO.getSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));	//must have Study id
//					List<SubjectVO> subjectList = (List<SubjectVO>) iArkCommonService.getSubject(subjectVO);
//					containerForm.setModelObject(subjectList.get(0));
//					contextLoaded = true;
//				} catch (EntityNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ArkSystemException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				if (contextLoaded) {
//					// Put into Detail View mode
//					searchPanelContainer.setVisible(false);
//					searchResultPanelContainer.setVisible(false);
//					detailPanelContainer.setVisible(true);
//					detailPanelFormContainer.setEnabled(false);
//					viewButtonContainer.setVisible(true);
//					editButtonContainer.setVisible(false);
//				}
//			}			
//		}
	}
	

//	
//	protected WebMarkupContainer initialiseReportViewPanel(){
//		
//		reportViewPanel = new ReportViewPanel("reportViewPanel",reportContainerVO.getFeedBackPanel(),searchResultPanelContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,reportContainerVO.getArkContextMarkup(),containerForm);
//		detailsPanel.initialisePanel();
//		detailPanelContainer.add(detailsPanel);
//		return detailPanelContainer;
//	}
//	
}
