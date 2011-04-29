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
import au.org.theark.report.web.component.viewReport.studySummary.ReportSelectPanel;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class ReportSelectForm extends AbstractContainerForm<ReportSelectVO>{
	
	private ReportSelectPanel reportSelectPanel;
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
		
		initialiseSearchForm();
	}
	
	protected void initialiseSearchForm() {
				
		this.add(initialiseSelectPanelWMC());

	}
	
	protected WebMarkupContainer initialiseSelectPanelWMC() {
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		Collection<SubjectVO> subjectVOCollection = new ArrayList<SubjectVO>();
		if(sessionStudyId != null && sessionStudyId > 0) {			
			this.getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}

		reportSelectPanel = new ReportSelectPanel("reportSelectPanel",
									reportContainerVO,
									this);

		initialiseComponents();
		
		reportContainerVO.getReportSelectWMC().add(reportSelectPanel);

		return reportContainerVO.getReportSelectWMC();
	}
//	

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
		reportSelectPanel.add(reportTemplateChoices);
		reportSelectPanel.add(loadButton);
	}
	
	private void initiliaseReportChoice() {
		
		ReportSelectVO reportSelectVO = cpmModel.getObject();
		//TODO: Fix the arkUser to be pulled out of the context
		ArkUser arkUser;
		try {
			arkUser = iArkCommonService.getArkUser("arkuser1@ark.org.au");
			
			// Retrieve the list of report templates allowed for this user
			reportSelectVO.setReportsAvailableList(reportService.getReportsAvailableList(reportSelectVO.getStudy(), arkUser));
			
			PropertyModel<LinkStudyReportTemplate> reportChoicePM = new PropertyModel<LinkStudyReportTemplate>(cpmModel, "selectedReport");
			ChoiceRenderer<LinkStudyReportTemplate> defaultChoiceRenderer = new ChoiceRenderer<LinkStudyReportTemplate>(Constants.REPORT_NAME, Constants.LINK_STUDY_REPORT_TEMPLATE_KEY);
			reportTemplateChoices = new DropDownChoice<LinkStudyReportTemplate>(Constants.REPORT_DROP_DOWN_CHOICE, reportChoicePM, 
																					reportSelectVO.getReportsAvailableList(), defaultChoiceRenderer);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
	
	protected void onLoadProcess(AjaxRequestTarget target) {
		
	}
	
	protected void onErrorProcess(AjaxRequestTarget target) {
		
	}

}
