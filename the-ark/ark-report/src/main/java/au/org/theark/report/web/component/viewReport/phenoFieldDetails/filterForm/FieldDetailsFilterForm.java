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
package au.org.theark.report.web.component.viewReport.phenoFieldDetails.filterForm;

import java.io.File;
import java.util.ArrayList;
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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;
import au.org.theark.report.web.component.viewReport.phenoFieldDetails.FieldDetailsReportDataSource;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class FieldDetailsFilterForm extends AbstractReportFilterForm<FieldDetailsReportVO> {

	protected DropDownChoice<PhenoCollection>	ddcPhenoCollection;
	protected CheckBox								chkboxFieldDataAvailable;

	public FieldDetailsFilterForm(String id, CompoundPropertyModel<FieldDetailsReportVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	protected void onGenerateProcess(AjaxRequestTarget target) {
		Study study = cpModel.getObject().getStudy();
		String reportTitle = study.getName() + " - Phenotypic Field Details Report";
		if (cpModel.getObject().getPhenoCollection() != null) {
			String phenoCollection = cpModel.getObject().getPhenoCollection().getName();
			reportTitle += " - " + phenoCollection;
		}

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
		FieldDetailsReportDataSource reportDS = new FieldDetailsReportDataSource(reportService, cpModel.getObject());

		JRResource reportResource = null;
		if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.PDF_REPORT_FORMAT)) {
			final JRResource pdfResource = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler());
			pdfResource.setJasperReport(report);
			pdfResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user
			// clicked on the download link, but unfortunately it seems to
			// stuff up the Indicator (not hidden upon completion).
			// ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
			// protected Resource newResource() {
			// return pdfResource;
			// }
			// };
			// String url = getRequestCycle().urlFor(ref).toString();
			// getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
			// add(new ResourceLink<Void>("linkToPdf", pdfResource));
			reportResource = pdfResource;
		}
		else if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
			final JRResource csvResource = new JRConcreteResource<CsvResourceHandler>(new CsvResourceHandler());
			csvResource.setJasperReport(report);
			csvResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user
			// clicked on the download link, but unfortunately it seems to
			// stuff up the Indicator (not hidden upon completion).
			// ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
			// protected Resource newResource() {
			// return csvResource;
			// }
			// };
			// String url = getRequestCycle().urlFor(ref).toString();
			// getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
			// add(new ResourceLink<Void>("linkToCsv", csvResource));
			reportResource = csvResource;
		}
		if (reportResource != null) {
			reportOutputPanel.setReportResource(reportResource);
			reportOutputPanel.setVisible(true);
			target.add(reportOutputPanel);
		}
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		cpModel.getObject().setStudy(study);

		initialisePhenoCollectionDropDown();
		initialiseFieldDataAvailableCheckBox();
	}

	protected void initialisePhenoCollectionDropDown() {
		List<PhenoCollection> collectionList = reportService.getPhenoCollectionList(cpModel.getObject().getStudy());
		ChoiceRenderer<PhenoCollection> defaultChoiceRenderer = new ChoiceRenderer<PhenoCollection>("name", "id");
		ddcPhenoCollection = new DropDownChoice<PhenoCollection>(Constants.PHENO_COLLECTION, collectionList, defaultChoiceRenderer);
		add(ddcPhenoCollection);
	}

	protected void initialiseFieldDataAvailableCheckBox() {
		chkboxFieldDataAvailable = new CheckBox(Constants.FIELD_DATA_AVAILABLE);
		add(chkboxFieldDataAvailable);
	}

}
