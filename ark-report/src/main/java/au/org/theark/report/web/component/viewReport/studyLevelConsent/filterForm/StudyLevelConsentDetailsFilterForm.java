/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.report.web.component.viewReport.studyLevelConsent.filterForm;

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
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;
import au.org.theark.report.web.component.viewReport.studyLevelConsent.StudyLevelConsentReportDataSource;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class StudyLevelConsentDetailsFilterForm extends AbstractReportFilterForm<ConsentDetailsReportVO> {

	protected TextField<String>					tfSubjectUID;
	protected DropDownChoice<SubjectStatus>	ddcSubjectStatus;
	protected DropDownChoice<ConsentStatus>	ddcConsentStatus;
	protected DateTextField							dtfConsentDate;

	public StudyLevelConsentDetailsFilterForm(String id, CompoundPropertyModel<ConsentDetailsReportVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	protected void onGenerateProcess(AjaxRequestTarget target) {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		cpModel.getObject().getLinkSubjectStudy().setStudy(study);

		String consentType = "Study-level Consent";
		String reportTitle = study.getName() + " - Consent Details Report - " + consentType;

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
			// System.out.println(" design -- created " );
			if (design != null) {
				design.setName(reportTitle); // set the output file name to match report title
				if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
					design.setIgnorePagination(true); // don't paginate CSVs
				}
				report = JasperCompileManager.compileReport(design);
				// System.out.println(" design -- compiled " );
			}
		}
		catch (JRException e) {
			reportFile = null;
			e.printStackTrace();
		}
		// templateIS = getClass().getResourceAsStream("/reportTemplates/WebappReport.jrxml");
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BaseDir", new File(context.getRealPath("/reportTemplates")));
		parameters.put("ReportTitle", reportTitle);
		Subject currentUser = SecurityUtils.getSubject();
		String userName = "(unknown)";
		if (currentUser.getPrincipal() != null) {
			userName = (String) currentUser.getPrincipal();
		}
		parameters.put("UserName", userName);
		StudyLevelConsentReportDataSource reportDS = new StudyLevelConsentReportDataSource(reportService, cpModel.getObject());

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
			target.addComponent(reportOutputPanel);
		}
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		tfSubjectUID = new TextField<String>(Constants.LINKSUBJECTSTUDY_SUBJECTUID);
		add(tfSubjectUID);
		initialiseConsentDatePicker();
		initialiseSubjectStatusDropDown();
		initialiseConsentStatusDropDown();
	}

	protected void initialiseConsentDatePicker() {
		dtfConsentDate = new DateTextField(Constants.CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dtfConsentDate);
		dtfConsentDate.add(datePicker);
		add(dtfConsentDate);
	}

	protected void initialiseSubjectStatusDropDown() {
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> defaultChoiceRenderer = new ChoiceRenderer<SubjectStatus>("name", "id");
		ddcSubjectStatus = new DropDownChoice<SubjectStatus>(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, subjectStatusList, defaultChoiceRenderer);
		add(ddcSubjectStatus);
	}

	protected void initialiseConsentStatusDropDown() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatus();
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>("name", "id");
		ddcConsentStatus = new DropDownChoice<ConsentStatus>(Constants.CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
		add(ddcConsentStatus);
	}

}
