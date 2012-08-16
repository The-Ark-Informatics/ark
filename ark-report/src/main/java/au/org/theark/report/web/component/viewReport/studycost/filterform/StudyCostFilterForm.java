package au.org.theark.report.web.component.viewReport.studycost.filterform;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.validation.validator.PatternValidator;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.StudyCostReportVo;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;
import au.org.theark.report.web.component.viewReport.studycost.StudyCostReportDataSource;

public class StudyCostFilterForm extends AbstractReportFilterForm<StudyCostReportVo> {
private static final long	serialVersionUID	= -6917137603826043554L;
	
	private TextField<String> yearTxtField;
	

	public StudyCostFilterForm(String id, CompoundPropertyModel<StudyCostReportVo> model) {
		super(id, model);
		this.cpModel = model;
	}

	protected void onGenerateProcess(AjaxRequestTarget target) {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);

		ReportTemplate reportTemplate = cpModel.getObject().getSelectedReportTemplate();
		ReportOutputFormat reportOutputFormat = cpModel.getObject().getSelectedOutputFormat();

		// show report
		ServletContext context = ((WebApplication) getApplication()).getServletContext();
		File reportFile = null;

		reportFile = new File(context.getRealPath("/reportTemplates/" + reportTemplate.getTemplatePath()));
		JasperDesign design = null;
		JasperReport report = null;
		try {
			design = JRXmlLoader.load(reportFile);
			if (design != null) {
				design.setName(au.org.theark.report.service.Constants.WORK_STUDY_DETAIL_COST_REPORT_NAME); // set the output file name to match report title
				if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
					design.setIgnorePagination(true); // don't paginate CSVs
				}
				report = JasperCompileManager.compileReport(design);
			}
		}
		catch (JRException e) {
			reportFile = null;
			e.printStackTrace();
		}
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("baseDir", new File(context.getRealPath("/reportTemplates")));
		
		String studyIdParam= sessionStudyId+"";
		parameters.put("studyId", studyIdParam);
		parameters.put("studyName", study.getName());
		String selectedYear= getModelObject().getYear();
		parameters.put("reportYear", selectedYear);
		
		
		ResearcherCostResportVO researcherCostResportVO= new ResearcherCostResportVO();
		researcherCostResportVO.setInvoice("Y");
		researcherCostResportVO.setStudyId(sessionStudyId);
		researcherCostResportVO.setYear(selectedYear);
		
		StudyCostReportDataSource reportDS= new StudyCostReportDataSource(reportService, researcherCostResportVO);
		

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

	protected void onErrorProcess(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		yearTxtField = new TextField<String>(Constants.RESEARCHER_COST_REPORT_YEAR);
		this.addFilterFormComponents();
		this.addValidators();
	}

	private void addFilterFormComponents() {
		this.add(yearTxtField);
	}

	private void addValidators() {
		yearTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_RESEARCHER_COST_REPORT_YEAR_REQUIRED, yearTxtField, new Model<String>(Constants.RESEARCHER_COST_REPORT_YEAR_TAG)));
		yearTxtField.add(new PatternValidator(Constants.RESEARCHER_COST_REPORT_YEAR_PATTERN));
		
	}


}
