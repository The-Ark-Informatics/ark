package au.org.theark.report.web.component.viewReport.researcherdetailcost.filterform;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
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
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;
import au.org.theark.report.web.component.viewReport.researcherdetailcost.WorkResearcherDetailCostReportDataSource;


public class ResearcherDetailCostFilterForm  extends AbstractReportFilterForm<ResearcherCostResportVO> {

	private static final long	serialVersionUID	= -6917137603826043554L;
	
	private DropDownChoice<Researcher> researcherDropDown;
	
	private TextField<String> yearTxtField;
	

	public ResearcherDetailCostFilterForm(String id, CompoundPropertyModel<ResearcherCostResportVO> model) {
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
				design.setName(au.org.theark.report.service.Constants.WORK_RESEARCHER_DETAIL_COST_REPORT_NAME); // set the output file name to match report title
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
		Researcher selectedResearcher =  getModelObject().getResearcher();
		parameters.put("studyId", studyIdParam);
		parameters.put("studyName", study.getName());
		String researcherName=selectedResearcher.getTitleType().getName()+" "+selectedResearcher.getFirstName()+" "+ selectedResearcher.getLastName();
		parameters.put("researcherName", researcherName);
		String selectedYear= getModelObject().getYear();
		parameters.put("reportYear", selectedYear);
		
		
		ResearcherCostResportVO researcherCostResportVO= new ResearcherCostResportVO();
		researcherCostResportVO.setInvoice("Y");
		researcherCostResportVO.setResearcherId(selectedResearcher.getId());
		researcherCostResportVO.setStudyId(sessionStudyId);
		researcherCostResportVO.setYear(selectedYear);
		
		WorkResearcherDetailCostReportDataSource reportDS= new WorkResearcherDetailCostReportDataSource(reportService, researcherCostResportVO);
		

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
		this.initResearcherDropDown();
		this.addFilterFormComponents();
		this.addValidators();
	}

	private void addFilterFormComponents() {
		this.add(yearTxtField);
		this.add(researcherDropDown);
	}

	private void addValidators() {
		yearTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_RESEARCHER_COST_REPORT_YEAR_REQUIRED, yearTxtField, new Model<String>(Constants.RESEARCHER_COST_REPORT_YEAR_TAG)));
		yearTxtField.add(new PatternValidator(Constants.RESEARCHER_COST_REPORT_YEAR_PATTERN));
		researcherDropDown.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_RESEARCHER_COST_REPORT_RESEARCHER_REQUIRED, researcherDropDown, new Model<String>(Constants.RESEARCHER_COST_REPORT_RESEARCHER_TAG)));
	}

	private void initResearcherDropDown() {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<Researcher> researchers = reportService.searchResearcherByStudyId(studyId);
		IChoiceRenderer customChoiceRenderer = new IChoiceRenderer<Researcher>() {

			public Object getDisplayValue(Researcher researcher) {
				return researcher.getFirstName()+" "+researcher.getLastName();
			}

			public String getIdValue(Researcher researcher, int index) {
				return researcher.getId().toString();
			}
			
		};
		researcherDropDown = new DropDownChoice(Constants.RESEARCHER_COST_REPORT_RESEARCHE, researchers, customChoiceRenderer);
	}

}
