package au.org.theark.report.web.component.viewReport.studyComponent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.report.model.vo.StudyComponentReportVO;
import au.org.theark.report.model.vo.report.StudyComponentDetailsDataRow;
import au.org.theark.report.service.IReportService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class StudyComponentReportDataSource implements Serializable,JRDataSource {

	private static final long								serialVersionUID	= 1L;
	private List<StudyComponentDetailsDataRow>          	data				= null;
	private int												index				= -1;

	public StudyComponentReportDataSource(final IReportService reportService, final StudyComponentReportVO studyComponentReportVO) throws ArkSystemException, EntityNotFoundException {
		data =  reportService.getStudyComponentDataRow(studyComponentReportVO);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if ("SubjectUID".equals(fieldName)) {
			value = data.get(index).getSubjectUID();
		}
		else if ("SubjectStatus".equals(fieldName)) {
			value = data.get(index).getSubjectStatus();
		}
		else if ("FirstName".equals(fieldName)) {
			value = data.get(index).getFirstName();
		}
		else if ("LastName".equals(fieldName)) {
			value = data.get(index).getLastName();
		}
		else if ("StreetAddress".equals(fieldName)) {
			value = data.get(index).getStreetAddress();
		}
		else if ("Suburb".equals(fieldName)) {
			value = data.get(index).getSuburb();
		}
		else if ("State".equals(fieldName)) {
			value = data.get(index).getState();
		}
		else if ("Postcode".equals(fieldName)) {
			value = data.get(index).getPostcode();
		}
		else if ("Country".equals(fieldName)) {
			value = data.get(index).getCountry();
		}
		else if ("DateOfBirth".equals(fieldName)) {
			value = data.get(index).getDateOfBirth();
		}
		else if ("Country".equals(fieldName)) {
			value = data.get(index).getCountry();
		}
		else if ("DateOfCurrentStudyComponentStatus".equals(fieldName)) {
			if(data.get(index).getCompletedDate()!=null){
				value = data.get(index).getCompletedDate();
			}else if(data.get(index).getRequestedDate()!=null){
				value = data.get(index).getRequestedDate();
			}else if(data.get(index).getReceivedDate()!=null){
				value = data.get(index).getReceivedDate();
			}
		}
		else if("ComponentStatus".equals(fieldName)){
			value = data.get(index).getComponentStatus();
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
