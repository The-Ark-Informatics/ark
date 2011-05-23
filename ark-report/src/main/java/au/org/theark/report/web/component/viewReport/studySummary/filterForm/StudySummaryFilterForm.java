package au.org.theark.report.web.component.viewReport.studySummary.filterForm;

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
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.GenericReportViewVO;
import au.org.theark.report.service.IReportService;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsReportDataSource;
import au.org.theark.report.web.component.viewReport.studySummary.StudySummaryReportDataSource;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class StudySummaryFilterForm extends AbstractContainerForm<GenericReportViewVO>{
	
	@SpringBean(name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	private IReportService reportService;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private DropDownChoice<ReportOutputFormat>	outputFormatChoices;
	private AjaxButton generateButton;
	
	private CompoundPropertyModel<GenericReportViewVO> cpModel;
	private ReportOutputPanel reportOutputPanel;
	private FeedbackPanel feedbackPanel;
	
	public StudySummaryFilterForm(String id, CompoundPropertyModel<GenericReportViewVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	public void initialiseFilterForm(FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		this.reportOutputPanel = reportOutputPanel;
		this.feedbackPanel = feedbackPanel;
		initialiseComponents();
	}
	
	protected void initialiseComponents() {
		generateButton = new IndicatingAjaxButton(Constants.GENERATE_BUTTON, new StringResourceModel("generateKey", this, null)) {
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onGenerateProcess(target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				onErrorProcess(target);
			}
			
		    @Override
		    protected IAjaxCallDecorator getAjaxCallDecorator() {
		        return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
		            private static final long serialVersionUID = 1L;
		 
		            @Override
		            public CharSequence postDecorateScript(CharSequence script) {
		                return script + "document.getElementById('" + getMarkupId() + "').disabled = true;"
		                		+ "wicketHide('"+ reportOutputPanel.getMarkupId() + "');";
		            }
		            @Override
		            public CharSequence postDecorateOnFailureScript(CharSequence script) {
		                return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
		            }
		            
		            @Override
		            public CharSequence postDecorateOnSuccessScript(CharSequence script) {
		                return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
		            }
		        };
		    }
		};
		
		initiliaseOutputFormatChoice();
		this.add(outputFormatChoices);
		this.add(generateButton);
	}
	
	private void initiliaseOutputFormatChoice() {
		
		GenericReportViewVO grvVO = cpModel.getObject();
		grvVO.setListReportOutputFormats(reportService.getOutputFormats());

		PropertyModel<ReportOutputFormat> outputChoicePM = new PropertyModel<ReportOutputFormat>(cpModel, "selectedOutputFormat");
		ChoiceRenderer<ReportOutputFormat> defaultChoiceRenderer = new ChoiceRenderer<ReportOutputFormat>(Constants.REPORT_OUTPUT_NAME);
		outputFormatChoices = new DropDownChoice<ReportOutputFormat>(Constants.REPORT_OUTPUT_DROP_DOWN_CHOICE, outputChoicePM, 
				grvVO.getListReportOutputFormats(), defaultChoiceRenderer);
		outputFormatChoices.setRequired(true);

	}

	protected void onGenerateProcess(AjaxRequestTarget target) {
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);

		ReportTemplate reportTemplate = cpModel.getObject().getSelectedReportTemplate();
		ReportOutputFormat reportOutputFormat = cpModel.getObject().getSelectedOutputFormat();

		// show report
		ServletContext context = ((WebApplication)getApplication()).getServletContext();
		File reportFile = null;

		reportFile = new File(context.getRealPath("/reportTemplates/" + reportTemplate.getTemplatePath()));
		JasperDesign design = null;
		JasperReport report = null;
		try {
			design = JRXmlLoader.load(reportFile);
//			System.out.println(" design -- created " );
			if (design != null) {
				report = JasperCompileManager.compileReport(design);
//				System.out.println(" design -- compiled " );
			}
		} catch (JRException e) {
			reportFile = null;
			e.printStackTrace();
		}
//		templateIS = getClass().getResourceAsStream("/reportTemplates/WebappReport.jrxml");
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BaseDir", new File(context.getRealPath("/reportTemplates")));
		parameters.put("ReportTitle", study.getName() + " - Study Summary Report");
		StudySummaryReportDataSource reportDS = new StudySummaryReportDataSource(reportService, study);
		
		JRResource reportResource = null;
		if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.PDF_REPORT_FORMAT)) {
			final JRResource pdfResource = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler());
			pdfResource.setJasperReport(report);
			pdfResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user 
			// clicked on the download link, but unfortunately it seems to 
			// stuff up the Indicator (not hidden upon completion).
//			ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
//					protected Resource newResource() {
//						return pdfResource;
//					}
//			};
//			String url = getRequestCycle().urlFor(ref).toString();
//			getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
//			add(new ResourceLink<Void>("linkToPdf", pdfResource));		
			reportResource = pdfResource;
		}
		else if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
			final JRResource csvResource = new JRConcreteResource<CsvResourceHandler>(new CsvResourceHandler());
			csvResource.setJasperReport(report);
			csvResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user 
			// clicked on the download link, but unfortunately it seems to 
			// stuff up the Indicator (not hidden upon completion).
//			ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
//				protected Resource newResource() {
//					return csvResource;
//				}
//			};
//			String url = getRequestCycle().urlFor(ref).toString();
//			getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
//			add(new ResourceLink<Void>("linkToCsv", csvResource));
			reportResource = csvResource;
		}
		if (reportResource != null) {
			reportOutputPanel.setReportResource(reportResource);
			reportOutputPanel.setVisible(true);
			target.addComponent(reportOutputPanel);
		}
	}
	
	protected void onErrorProcess(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}
}
