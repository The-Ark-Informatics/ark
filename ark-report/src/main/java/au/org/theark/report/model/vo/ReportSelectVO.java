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
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author elam
 * 
 */
public class ReportSelectVO implements Serializable {


	private static final long		serialVersionUID	= 1L;

	private Study						study;
	private ReportTemplate			selectedReport;
	private List<ReportTemplate>	reportsAvailableList;

	public ReportSelectVO() {
		this.selectedReport = new ReportTemplate();
		this.reportsAvailableList = new ArrayList<ReportTemplate>();
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Study getStudy() {
		return study;
	}

	public ReportTemplate getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(ReportTemplate selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<ReportTemplate> getReportsAvailableList() {
		return reportsAvailableList;
	}

	public void setReportsAvailableList(List<ReportTemplate> reportsAvailableList) {
		this.reportsAvailableList = reportsAvailableList;
	}

}
