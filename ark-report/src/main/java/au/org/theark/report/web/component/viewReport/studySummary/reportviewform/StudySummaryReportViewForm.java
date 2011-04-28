package au.org.theark.report.web.component.viewReport.studySummary.reportviewform;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.GenericReportViewVO;
import au.org.theark.report.web.component.viewReport.form.AbstractReportViewForm;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class StudySummaryReportViewForm extends AbstractReportViewForm<GenericReportViewVO>{
	
	public StudySummaryReportViewForm(String id, CompoundPropertyModel<GenericReportViewVO> model) {
		super(id, model);
	}

}
