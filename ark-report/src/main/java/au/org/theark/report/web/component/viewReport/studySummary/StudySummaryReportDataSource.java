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
package au.org.theark.report.web.component.viewReport.studySummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.vo.report.StudySummaryDataRow;
import au.org.theark.report.service.IReportService;

/**
 * Based on ...
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: WebappDataSource.java 2692 2009-03-24 17:17:32Z teodord $
 * 
 * @author elam
 */
public class StudySummaryReportDataSource implements Serializable, JRDataSource {
	/**
	 *
	 */
	private static final long				serialVersionUID	= 1L;

	private List<StudySummaryDataRow>	data					= null;

	private int									index					= -1;

	/**
	 *
	 */
	public StudySummaryReportDataSource(IReportService reportService, Study study) {
		data = new ArrayList<StudySummaryDataRow>();
		data.add(new StudySummaryDataRow("Total Subjects", "", reportService.getTotalSubjectCount(study)));
		Map<String, Long> tmpStatusCounts = reportService.getSubjectStatusCounts(study);
		for (String statusKey : tmpStatusCounts.keySet()) {
			data.add(new StudySummaryDataRow("Subject Status", statusKey, tmpStatusCounts.get(statusKey)));
		}
		tmpStatusCounts = reportService.getStudyConsentCounts(study);
		for (String statusKey : tmpStatusCounts.keySet()) {
			data.add(new StudySummaryDataRow("Study Consent Status", statusKey, tmpStatusCounts.get(statusKey)));
		}
		for (StudyComp studyComp : study.getStudyComps()) {
			tmpStatusCounts = reportService.getStudyCompConsentCounts(study, studyComp);
			for (String statusKey : tmpStatusCounts.keySet()) {
				data.add(new StudySummaryDataRow("Study Component - " + studyComp.getName(), statusKey, tmpStatusCounts.get(statusKey)));
			}
		}
		data.add(new StudySummaryDataRow("Subjects Without Study Components", "", reportService.getWithoutStudyCompCount(study)));
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

		if ("Section".equals(fieldName)) {
			value = data.get(index).getSection();
		}
		else if ("Status".equals(fieldName)) {
			value = data.get(index).getStatus();
		}
		else if ("Subjects".equals(fieldName)) {
			value = data.get(index).getSubjectCount();
		}

		return value;
	}

}
