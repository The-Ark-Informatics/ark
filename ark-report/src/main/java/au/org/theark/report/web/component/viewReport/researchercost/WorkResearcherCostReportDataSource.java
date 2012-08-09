package au.org.theark.report.web.component.viewReport.researchercost;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.core.model.report.entity.ResearcherBillableItemTypeCostDataRow;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.service.IReportService;


public class WorkResearcherCostReportDataSource  implements Serializable, JRDataSource{
	
	private static final long				serialVersionUID	= 1L;

	private List<ResearcherBillableItemTypeCostDataRow>	data	= null;

	private int									index			= -1;
	
	public WorkResearcherCostReportDataSource(final IReportService reportService, final ResearcherCostResportVO  researcherCostResportVO) {
		data =  reportService.getBillableItemTypeCostData(researcherCostResportVO);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();
		if ("costType".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getCostType();
		}
		else if ("totalCost".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getTotalCost();
		}
		else if ("totalGST".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getTotalGST();
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
