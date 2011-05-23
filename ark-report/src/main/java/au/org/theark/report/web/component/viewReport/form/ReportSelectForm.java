package au.org.theark.report.web.component.viewReport.form;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;
import au.org.theark.report.model.entity.ReportSecurity;
import au.org.theark.report.model.vo.ReportSelectVO;
import au.org.theark.report.service.IReportService;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.ReportContainerVO;
import au.org.theark.report.web.component.viewReport.ReportSelectPanel;
import au.org.theark.report.web.component.viewReport.studySummary.StudySummaryReportContainer;
import au.org.theark.report.web.component.viewReport.studySummary.filterForm.StudySummaryFilterForm;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class ReportSelectForm extends AbstractContainerForm<ReportSelectVO>{
	
	private DropDownChoice<LinkStudyReportTemplate>	reportTemplateChoices;
	private ReportContainerVO reportContainerVO;
	private CompoundPropertyModel<ReportSelectVO> cpmModel;
	protected AjaxButton loadButton;

	@SpringBean(name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	private IReportService reportService;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	public ReportSelectForm(String id, CompoundPropertyModel<ReportSelectVO> cpmModel, ReportContainerVO reportContainerVO) {
		
		super(id, cpmModel);
		this.reportContainerVO = reportContainerVO;
		this.cpmModel = cpmModel;
	}
	
	public void initialiseSearchForm() {
				
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if(sessionStudyId != null && sessionStudyId > 0) {			
			this.getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}

		initialiseComponents();
	}
	
	/**
	 * 
	 */
	protected void initialiseComponents() {
		loadButton = new AjaxButton(Constants.LOAD_BUTTON, new StringResourceModel("loadKey", this, null)) {
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onLoadProcess(target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				onErrorProcess(target);
			}
		};
		
		initiliaseReportChoice();
		this.add(reportTemplateChoices);
		this.add(loadButton);
	}
	
	private void initiliaseReportChoice() {
		
		ReportSelectVO reportSelectVO = cpmModel.getObject();
		//TODO: Fix the arkUser to be pulled out of the context
		ArkUser arkUser;
		try {
			arkUser = iArkCommonService.getArkUser("arkuser1@ark.org.au");
			
			// Retrieve the list of report templates allowed for this user
			reportSelectVO.setLinkedStudyReportList(reportService.getReportsAvailableList(reportSelectVO.getStudy(), arkUser));
			
			PropertyModel<LinkStudyReportTemplate> reportChoicePM = new PropertyModel<LinkStudyReportTemplate>(cpmModel, "selectedReport");
			ChoiceRenderer<LinkStudyReportTemplate> defaultChoiceRenderer = new ChoiceRenderer<LinkStudyReportTemplate>(Constants.REPORT_NAME, Constants.LINK_STUDY_REPORT_TEMPLATE_KEY);
			reportTemplateChoices = new DropDownChoice<LinkStudyReportTemplate>(Constants.REPORT_DROP_DOWN_CHOICE, reportChoicePM, 
																					reportSelectVO.getLinkedStudyReportList(), defaultChoiceRenderer);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
	
	protected void onLoadProcess(AjaxRequestTarget target) {
		LinkStudyReportTemplate linkStudyReport = this.getModelObject().getSelectedLinkStudyReport();
		if (this.getModelObject().getSelectedReport() == null) {
			this.info("Please select a report to load.");
			target.addComponent(reportContainerVO.getFeedbackPanel());
			return;	//not allowed to proceed if selection not made
		}
		if (linkStudyReport.getReportTemplate().getName().equals("Study Summary Report")) {
			if (this.getModelObject().getStudy() == null) {
				this.error("This report requires a study in context. Please put a study in context first.");
			}
			else {
				StudySummaryReportContainer selectedReportPanel = new StudySummaryReportContainer("selectedReportContainerPanel");
				selectedReportPanel.setOutputMarkupId(true);
				// Replace the old selectedReportPanel with this new one
				reportContainerVO.getSelectedReportPanel().replaceWith(selectedReportPanel);
				reportContainerVO.setSelectedReportPanel(selectedReportPanel);
				selectedReportPanel.initialisePanel(reportContainerVO.getFeedbackPanel(), linkStudyReport.getReportTemplate());
				target.addComponent(reportContainerVO.getSelectedReportContainerWMC());
				this.info("Report template loaded");
			}
			target.addComponent(reportContainerVO.getFeedbackPanel());
		}
	}
	
	protected void onErrorProcess(AjaxRequestTarget target) {
		
	}

}
