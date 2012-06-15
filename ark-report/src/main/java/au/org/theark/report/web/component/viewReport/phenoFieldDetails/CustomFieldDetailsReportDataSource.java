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
package au.org.theark.report.web.component.viewReport.phenoFieldDetails;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.model.vo.report.CustomFieldDetailsDataRow;
import au.org.theark.report.service.IReportService;

/**
 * @author elam
 */
public class CustomFieldDetailsReportDataSource implements Serializable, JRDataSource {
	/**
	 *
	 */
	private static final long				serialVersionUID	= 1L;

	private List<CustomFieldDetailsDataRow>	data					= null;

	private int									index					= -1;

	/**
	 *
	 */
	public CustomFieldDetailsReportDataSource(IReportService reportService, CustomFieldDetailsReportVO fdVO) {
		data = reportService.getPhenoCustomFieldDetailsList(fdVO);
	}

	/**
	 *
	 */
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

	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if ("Questionnaire".equals(fieldName)) {
			value = data.get(index).getQuestionnaire();
		}
		else if ("FieldName".equals(fieldName)) {
			value = data.get(index).getFieldName();
		}
		else if ("Description".equals(fieldName)) {
			value = data.get(index).getDescription();
		}
		else if ("MinValue".equals(fieldName)) {
			value = data.get(index).getMinValue();
		}
		else if ("MaxValue".equals(fieldName)) {
			value = data.get(index).getMaxValue();
		}
		else if ("EncodedValues".equals(fieldName)) {
			value = data.get(index).getEncodedValues();
		}
		else if ("MissingValue".equals(fieldName)) {
			value = data.get(index).getMissingValue();
		}
		else if ("Units".equals(fieldName)) {
			value = data.get(index).getUnits();
		}
		else if ("Type".equals(fieldName)) {
			value = data.get(index).getType();
		}

		return value;
	}

}
