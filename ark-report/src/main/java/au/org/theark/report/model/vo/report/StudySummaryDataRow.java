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
package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class StudySummaryDataRow implements Serializable {

	private static final long	serialVersionUID	= 1L;

	protected String				section;
	protected String				status;
	protected Number				subjectCount;

	public StudySummaryDataRow(String section, String status, Number subjectCount) {
		this.section = section;
		this.status = status;
		this.subjectCount = subjectCount;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Number getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Number subjectCount) {
		this.subjectCount = subjectCount;
	}

}
