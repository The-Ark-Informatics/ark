package au.org.theark.report.web.component.viewReport.biospecimensummary;

import java.io.Serializable;
import java.util.List;

import au.org.theark.report.model.vo.BiospecimenSummaryReportVO;
import au.org.theark.report.model.vo.report.BiospecimenSummaryDataRow;
import au.org.theark.report.service.IReportService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class BiospecimenSummaryReportDataSource implements Serializable,
		JRDataSource {
	
	
	private static final long				serialVersionUID	= 1L;

	private List<BiospecimenSummaryDataRow>          	data			= null;

	private int									index			= -1;
	
	public BiospecimenSummaryReportDataSource(final IReportService reportService, final BiospecimenSummaryReportVO  biospecimenSummaryReportVo) {
		data =  reportService.getBiospecimenSummaryData(biospecimenSummaryReportVo);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if ("studyName".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getStudyName();
		}
		else if ("subjectUId".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getSubjectUId();
		}
		else if ("biospecimenId".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getBiospecimenId();
		}
		else if ("parentId".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getParentId();
		}
		else if ("sampleType".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getSampleType();
		}
		else if ("quantity".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getQuantity();
		}
		else if ("initialStatus".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getInitialStatus();
		}
		else if ("biospecimenUid".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getBiospecimenUid();
		}
		return value;
	}

	public boolean next() throws JRException {
		index++;
		// Need to return false for when (index == data.size())
		// so as to stop the current report from consuming any more data.
		// However, when another report attempts to consume data it will
		// have advanced the index and thus we can reset it automatically
		if (index > data.size()) {
			index = 0;
		}
		return (index < data.size());
	}

}
