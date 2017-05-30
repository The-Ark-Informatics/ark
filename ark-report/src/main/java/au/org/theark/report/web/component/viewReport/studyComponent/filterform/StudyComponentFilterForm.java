package au.org.theark.report.web.component.viewReport.studyComponent.filterform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.util.DateFromToValidator;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.report.model.vo.StudyComponentReportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;
import au.org.theark.report.web.component.viewReport.studyComponent.StudyComponentReportDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public class StudyComponentFilterForm extends AbstractReportFilterForm<StudyComponentReportVO> {
	private static final long serialVersionUID = 1L;
	protected DropDownChoice<StudyComp>			ddcStudyComp;
	protected DropDownChoice<ConsentStatus>	ddcConsentStatus;
	protected DateTextField						fromDatePicker;
	protected DateTextField						toDatePicker;
	protected DropDownChoice<StudyCompStatus>	ddcStudyCompStatus;
	private 	Study 								study;
	protected WebMarkupContainer					wmcFromDate;
	protected WebMarkupContainer					wmcToDate;
	private  Label                     				fromDateLabel;
	private  Label                     				toDateLabel;
	
	protected WebMarkupContainer					panelFromDateTwoDate;
	
	
	public StudyComponentFilterForm(String id,CompoundPropertyModel<StudyComponentReportVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		initialiseStudyCompDropDown();
		initialiseStudyCompStatusDropDown(null);
		initialiseDateRange();	
		initialiseConsentStatusDropDown(null);
		addDetailFormComponents();
		addValidators();
	}
	private void addValidators() {
		fromDatePicker.setRequired(true);
		toDatePicker.setRequired(true);
		this.add(new DateFromToValidator(fromDatePicker, toDatePicker,"Valid from date","Valid to date"));
	}

	@Override
	protected void onGenerateProcess(AjaxRequestTarget target) {
		ReportTemplate reportTemplate = cpModel.getObject().getSelectedReportTemplate();
		ReportOutputFormat reportOutputFormat = cpModel.getObject().getSelectedOutputFormat();
		String reportTitle = study.getName() + " - Study Component Details Report";
		if (cpModel.getObject().getConsent().getStudyComp() != null) {
			String studyComponent = cpModel.getObject().getConsent().getStudyComp().getName();
			reportTitle += " - " + studyComponent;
		}
		// show report
		ServletContext context = ((WebApplication) getApplication()).getServletContext();
		File reportFile = null;

		reportFile = new File(context.getRealPath("/reportTemplates/" + reportTemplate.getTemplatePath()));
		JasperDesign design = null;
		JasperReport report = null;
		try {
			design = JRXmlLoader.load(reportFile);
			if (design != null) {
				design.setName(au.org.theark.report.service.Constants.STUDY_COMP_REPORT_NAME); // set the output file name to match report title
				if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
					design.setIgnorePagination(true); // don't paginate CSVs
				}
				report = JasperCompileManager.compileReport(design);
			}
		}
		catch (JRException e) {
			//reportFile = null;
			this.error("A system error has occurred when creating the report. Please contact the system administrator.");
			onErrorProcess(target);
			e.printStackTrace();
		}
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BaseDir", new File(context.getRealPath("/reportTemplates")));
		parameters.put("ReportTitle", reportTitle);
		Subject currentUser = SecurityUtils.getSubject();
		String userName = "(unknown)";
		if (currentUser.getPrincipal() != null) {
			userName = (String) currentUser.getPrincipal();
		}
		parameters.put("UserName", userName);
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId); 
		
		getModelObject().getConsent().setStudy(study);
		StudyComponentReportDataSource reportDS = null;
		try {
			reportDS = new StudyComponentReportDataSource(reportService, getModelObject());
		} catch (ArkSystemException | EntityNotFoundException e) {
			this.error("A system error has occurred when creating the report. Please contact the system administrator.");
			onErrorProcess(target);
			e.printStackTrace();
		}
		
		JRResource reportResource = null;
		if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.PDF_REPORT_FORMAT)) {
			final JRResource pdfResource = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler());
			pdfResource.setJasperReport(report);
			pdfResource.setReportParameters(parameters).setReportDataSource(reportDS);
			reportResource = pdfResource;
		}
		else if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
			final JRResource csvResource = new JRConcreteResource<CsvResourceHandler>(new CsvResourceHandler());
			csvResource.setJasperReport(report);
			csvResource.setReportParameters(parameters).setReportDataSource(reportDS);
			reportResource = csvResource;
		}
		if (reportResource != null) {
			reportOutputPanel.setReportResource(reportResource);
			reportOutputPanel.setVisible(true);
			target.add(reportOutputPanel);
		}
	}
	
	private void initialiseDateRange(){
		wmcFromDate = new WebMarkupContainer(Constants.WMC_FROMDATE);
		wmcFromDate.setOutputMarkupPlaceholderTag(true);
		
		wmcToDate = new WebMarkupContainer(Constants.WMC_TODATE);
		wmcToDate.setOutputMarkupPlaceholderTag(true);
		
		fromDateLabel=new Label(Constants.FROM_DATE_LABEL);
		fromDateLabel.setOutputMarkupId(true);
		toDateLabel=new Label(Constants.TO_DATE_LABEL);
		toDateLabel.setOutputMarkupId(true);

		fromDatePicker = new DateTextField(Constants.FROM_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker arkFromDatePicker = new ArkDatePicker();
		arkFromDatePicker.bind(fromDatePicker);
		fromDatePicker.add(arkFromDatePicker);
		wmcFromDate.add(fromDatePicker);
		wmcFromDate.add(fromDateLabel);
		
		toDatePicker = new DateTextField(Constants.TO_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker arkToDatePicker = new ArkDatePicker();
		arkToDatePicker.bind(toDatePicker);
		toDatePicker.add(arkToDatePicker);
		wmcToDate.add(toDatePicker);
		wmcToDate.add(toDateLabel);
		 
		panelFromDateTwoDate = new WebMarkupContainer("panelFromDateTwoDate");
		panelFromDateTwoDate.setOutputMarkupPlaceholderTag(true);
		panelFromDateTwoDate.setVisible(false);
		
	}
	
	private void initialiseStudyCompDropDown() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		List<StudyComp> studyCompLst = new ArrayList<StudyComp>(study.getStudyComps());
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>("name", "id");
		ddcStudyComp = new DropDownChoice<StudyComp>(Constants.CONSENT_STUDY_COMP, studyCompLst, defaultChoiceRenderer);
		ddcStudyComp.add(new AjaxFormComponentUpdatingBehavior("onclick") {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				initialiseStudyCompStatusDropDown(ddcStudyComp.getModelObject());
				target.add(ddcStudyCompStatus);
				panelFromDateTwoDate.setVisible(false);
				target.add(panelFromDateTwoDate);
		    }
		});
		ddcStudyComp.setRequired(true);
		add(ddcStudyComp);
	}
	private void initialiseStudyCompStatusDropDown(StudyComp studyComp) {
		List<StudyCompStatus> studyComponentStatusLst = iArkCommonService.getConsentStudyComponentStatusForStudyAndStudyComp(study, studyComp);
		ChoiceRenderer<StudyCompStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyCompStatus>("name", "id");
		ddcStudyCompStatus = new DropDownChoice<StudyCompStatus>(Constants.CONSENT_STUDY_COMP_STATUS, studyComponentStatusLst, defaultChoiceRenderer);
		ddcStudyCompStatus.add(new AjaxFormComponentUpdatingBehavior("onclick") {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				initialiseConsentStatusDropDown(ddcStudyCompStatus.getModelObject());
				target.add(ddcConsentStatus);
				if(ddcStudyCompStatus.getModelObject()!=null && ddcStudyCompStatus.getModelObject().getName().equals(Constants.STUDY_STATUS_COMPLETED)||
						ddcStudyCompStatus.getModelObject().getName().equals(Constants.STUDY_STATUS_REQUESTED)||
						ddcStudyCompStatus.getModelObject().getName().equals(Constants.STUDY_STATUS_RECEIVED)){
					updateComponentStatus(target,true);
				}else{
					updateComponentStatus(target,false);
				}
		    }
		});
		ddcStudyCompStatus.setRequired(true);
		addOrReplace(ddcStudyCompStatus);
	}
	private void updateComponentStatus(AjaxRequestTarget target ,Boolean status) {
		if(ddcStudyCompStatus.getModelObject()!=null){getModelObject().setFromDateLabel(ddcStudyCompStatus.getModelObject().getName()+" from:" );}
		if(ddcStudyCompStatus.getModelObject()!=null){getModelObject().setToDateLabel(ddcStudyCompStatus.getModelObject().getName()+" to:" );}
		panelFromDateTwoDate.setVisible(status);
		target.add(panelFromDateTwoDate);
	}
	private void initialiseConsentStatusDropDown(StudyCompStatus studyCompStatus) {
		List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatusForStudyStudyCompAndStudyCompStatus(study,ddcStudyComp.getModelObject(),studyCompStatus);
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>("name", "id");
		ddcConsentStatus = new DropDownChoice<ConsentStatus>(Constants.CONSENT_CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
		addOrReplace(ddcConsentStatus);
	}

	private void addDetailFormComponents(){
		panelFromDateTwoDate.add(wmcFromDate);
		panelFromDateTwoDate.add(wmcToDate);
		add(panelFromDateTwoDate);
	}

}
