package au.org.theark.report.web.component.viewReport.biospecimensummary.filterform;

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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.vo.BiospecimenSummaryReportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.biospecimensummary.BiospecimenSummaryReportDataSource;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;

public class BiospecimenSummaryFilterForm extends AbstractReportFilterForm<BiospecimenSummaryReportVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DropDownChoice<Study> studyDropDown;
	private TextField<String> subjectUidTextField;

	public BiospecimenSummaryFilterForm(String id,
			CompoundPropertyModel<BiospecimenSummaryReportVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		this.subjectUidTextField=new TextField<String>(Constants.BIOSPECIMEN_SUMMARY_REPORT_SUBJECT_UID);
		initStudyDropDown();
		addFilterFormComponents();
		addValidators();
	}
	
	private void initStudyDropDown() {
		List<Study> studies =null;
		try{
			studies= reportService.getStudyList();
		}catch(EntityNotFoundException ene){
			
			ene.printStackTrace();
		}
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(au.org.theark.core.Constants.NAME, au.org.theark.core.Constants.ID);
		this.studyDropDown = new DropDownChoice(Constants.BIOSPECIMEN_SUMMARY_REPORT_STUDY, studies, defaultChoiceRenderer);
	}
	
	private void addFilterFormComponents() {
		this.add(studyDropDown);
		this.add(subjectUidTextField);
	}

	private void addValidators() {
		this.studyDropDown.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BIOSPECIMEN_SUMMARY_REPORT_STUDY_REQUIRED, studyDropDown, new Model<String>(Constants.BIOSPECIMEN_SUMMARY_REPORT_STUDY_TAG)));		
	}

	@Override
	protected void onGenerateProcess(AjaxRequestTarget target) {
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
				design.setName(au.org.theark.report.service.Constants.LIMS_BIOSPECIMEN_SUMMARY_REPORT_NAME); // set the output file name to match report title
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
		
		Study selectedStudy =  getModelObject().getStudy();
		parameters.put("studyName", selectedStudy.getName());
		
		BiospecimenSummaryReportVO biospecimenSummaryReportVO = new BiospecimenSummaryReportVO();
		biospecimenSummaryReportVO.setStudy(getModelObject().getStudy());
		biospecimenSummaryReportVO.setSubjectUID(getModelObject().getSubjectUID());
		
		BiospecimenSummaryReportDataSource reportDS= new BiospecimenSummaryReportDataSource(reportService, biospecimenSummaryReportVO);
		
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

}