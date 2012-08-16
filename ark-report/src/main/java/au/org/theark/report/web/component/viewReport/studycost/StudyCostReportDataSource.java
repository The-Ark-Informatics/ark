package au.org.theark.report.web.component.viewReport.studycost;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.report.ResearcherDetailCostDataRow;
import au.org.theark.report.service.IReportService;

public class StudyCostReportDataSource implements Serializable, JRDataSource {

	private static final long serialVersionUID = 1L;

	private List<ResearcherDetailCostDataRow> data = null;

	private int index = -1;

	public StudyCostReportDataSource(final IReportService reportService, final ResearcherCostResportVO researcherCostResportVO) {
		data = reportService.getBillableItemDetailCostData(researcherCostResportVO);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();
		if ("description".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getDescription();
		} else if ("commencedDate".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getCommencedDate();
		} else if ("invoice".equalsIgnoreCase(fieldName)) {
			String invoice = data.get(index).getInvoice();
			if ("Y".equalsIgnoreCase(invoice)) {
				value = "INV";
			} else {
				value = "";
			}
		} else if ("quantity".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getQuantity();
		} else if ("totalAmount".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getTotalAmount();
		} else if ("totalGST".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getTotalGST();
		} else if ("itemType".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getItemType();
		} else if ("typeId".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getTypeId();
		} else if ("quantityType".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getQuantityType();
		} else if ("gstAllowed".equalsIgnoreCase(fieldName)) {
			value = data.get(index).getGstAllowed();
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