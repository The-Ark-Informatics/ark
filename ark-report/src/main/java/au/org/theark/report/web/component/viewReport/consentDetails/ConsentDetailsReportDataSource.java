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
package au.org.theark.report.web.component.viewReport.consentDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.service.IReportService;

/**
 * Based on ...
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: WebappDataSource.java 2692 2009-03-24 17:17:32Z teodord $
 * 
 * @author elam
 */
public class ConsentDetailsReportDataSource implements Serializable, JRDataSource {
	/**
	 *
	 */
	private static final long				serialVersionUID	= 1L;

	private List<ConsentDetailsDataRow>	data					= null;

	private int									index					= -1;

	/**
	 *
	 */
	public ConsentDetailsReportDataSource(IReportService reportService, ConsentDetailsReportVO cdrVO) {
		data = reportService.getStudyCompConsentDetailsList(cdrVO);
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

		if ("SubjectUID".equals(fieldName)) {
			value = data.get(index).getSubjectUID();
		}
		else if ("ConsentStatus".equals(fieldName)) {
			value = data.get(index).getConsentStatus();
		}
		else if ("SubjectStatus".equals(fieldName)) {
			value = data.get(index).getSubjectStatus();
		}
		else if ("Title".equals(fieldName)) {
			value = data.get(index).getTitle();
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
		else if ("WorkPhone".equals(fieldName)) {
			value = data.get(index).getWorkPhone();
		}
		else if ("HomePhone".equals(fieldName)) {
			value = data.get(index).getHomePhone();
		}
		else if ("Email".equals(fieldName)) {
			value = data.get(index).getEmail();
		}
		else if ("Sex".equals(fieldName)) {
			value = data.get(index).getSex();
		}
		else if ("ConsentDate".equals(fieldName)) {
			value = data.get(index).getConsentDate();
		}

		return value;
	}

}
