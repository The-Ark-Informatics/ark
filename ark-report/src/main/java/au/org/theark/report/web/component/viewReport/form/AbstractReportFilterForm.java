package au.org.theark.report.web.component.viewReport.form;

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
import au.org.theark.core.web.component.ArkAjaxButton;
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
public abstract class AbstractReportFilterForm<T extends GenericReportViewVO> extends AbstractContainerForm<T> {
	
	@SpringBean(name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	protected IReportService reportService;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;
	
	protected DropDownChoice<ReportOutputFormat>	outputFormatChoices;
	protected AjaxButton generateButton;
	
	protected CompoundPropertyModel<T> cpModel;
	protected ReportOutputPanel reportOutputPanel;
	protected FeedbackPanel feedbackPanel;
	
	public AbstractReportFilterForm(String id, CompoundPropertyModel<T> model) {
		super(id, model);
		this.cpModel = model;
	}

	public void initialiseFilterForm(FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		this.reportOutputPanel = reportOutputPanel;
		this.feedbackPanel = feedbackPanel;
		initialiseComponents();
	}
	
	private void initialiseComponents() {
		generateButton = new ArkAjaxButton(Constants.GENERATE_BUTTON, new StringResourceModel("generateKey", this, null)) {
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
		                return script + "wicketHide('" + reportOutputPanel.getMarkupId() + "');";
		            }
		        };
		    }
		};
		
		initialiseOutputFormatChoice();
		initialiseCustomFilterComponents();
		this.add(outputFormatChoices);
		this.add(generateButton);
	}
	
	private void initialiseOutputFormatChoice() {
		
		T grvVO = cpModel.getObject();
		grvVO.setListReportOutputFormats(reportService.getOutputFormats());

		PropertyModel<ReportOutputFormat> outputChoicePM = new PropertyModel<ReportOutputFormat>(cpModel, "selectedOutputFormat");
		ChoiceRenderer<ReportOutputFormat> defaultChoiceRenderer = new ChoiceRenderer<ReportOutputFormat>(Constants.REPORT_OUTPUT_NAME);
		outputFormatChoices = new DropDownChoice<ReportOutputFormat>(Constants.REPORT_OUTPUT_DROP_DOWN_CHOICE, outputChoicePM, 
				grvVO.getListReportOutputFormats(), defaultChoiceRenderer);
		outputFormatChoices.setRequired(true);

	}

	/**
	 * *DO NOT* call manually!
	 * This method will automatically be called as part of initialiseFiterForm(..)
	 */
	protected abstract void initialiseCustomFilterComponents();
	
	/**
	 * Called when the Generate button is clicked
	 */
	protected abstract void onGenerateProcess(AjaxRequestTarget target);
	
	protected void onErrorProcess(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}
}
