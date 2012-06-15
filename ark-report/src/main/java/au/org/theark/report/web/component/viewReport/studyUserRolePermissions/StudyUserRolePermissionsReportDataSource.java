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
package au.org.theark.report.web.component.viewReport.studyUserRolePermissions;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.vo.report.StudyUserRolePermissionsDataRow;
import au.org.theark.report.service.IReportService;

/**
 * Based on ...
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: WebappDataSource.java 2692 2009-03-24 17:17:32Z teodord $
 * 
 * @author cellis
 * @author elam
 */
public class StudyUserRolePermissionsReportDataSource implements Serializable, JRDataSource {
	/**
	 *
	 */
	private static final long				serialVersionUID	= 1L;

	private List<StudyUserRolePermissionsDataRow>	data					= null;

	private int									index					= -1;

	/**
	 *
	 */
	public StudyUserRolePermissionsReportDataSource(IReportService reportService, Study study) {
		data = reportService.getStudyUserRolePermissions(study);
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

		if ("study_name".equals(fieldName)) {
			value = data.get(index).getStudyName();
		}
		else if ("user_name".equals(fieldName)) {
			value = data.get(index).getUserName();
		}
		else if ("role".equals(fieldName)) {
			value = data.get(index).getRoleName();
		}
		else if ("module".equals(fieldName)) {
			value = data.get(index).getModuleName();
		}
		else if ("create".equals(fieldName)) {
			value = data.get(index).getCreate();
		}
		else if ("read".equals(fieldName)) {
			value = data.get(index).getRead();
		}
		else if ("update".equals(fieldName)) {
			value = data.get(index).getUpdate();
		}
		else if ("delete".equals(fieldName)) {
			value = data.get(index).getDelete();
		}

		return value;
	}

}
