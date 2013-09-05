package au.org.theark.report.web.component.viewReport.biospecimendetails;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.core.Constants;
import au.org.theark.report.model.vo.BiospecimenDetailsReportVO;
import au.org.theark.report.model.vo.report.BiospecimenDetailsDataRow;
import au.org.theark.report.service.IReportService;

public class BiospecimenDetailsReportDataSource implements Serializable,
	JRDataSource {
	
	
	private static final long				serialVersionUID	= 1L;

	private List<BiospecimenDetailsDataRow>          	data		= null;

	private int									index			= -1;

	public BiospecimenDetailsReportDataSource(final IReportService reportService, final BiospecimenDetailsReportVO biospecimenDetailReportVo) {
		data =  reportService.getBiospecimenDetailsData(biospecimenDetailReportVo);
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
		else if ("biocollectionUid".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getBiocollectionUid();
		}
		else if ("sampleDate".equalsIgnoreCase(fieldName)) {
			SimpleDateFormat sf = new SimpleDateFormat(Constants.DD_MM_YYYY);
			value = (data.get(index).getSampleDate()!=null)?data.get(index).getSampleDate():null;
			value = value==null?new String():sf.format(value);
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
		else if ("site".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getSite();
		}
		else if ("freezer".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getFreezer();
		}
		else if ("rack".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getRack();
		}
		else if ("box".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getBox();
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
