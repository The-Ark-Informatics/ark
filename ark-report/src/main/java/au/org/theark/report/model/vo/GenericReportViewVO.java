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
import java.util.List;
import java.util.Properties;

import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;

/**
 * @author elam
 * 
 */
public class GenericReportViewVO implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;

	private ReportTemplate				selectedReportTemplate;
	private List<ReportOutputFormat>	listReportOutputFormats;
	private ReportOutputFormat			selectedOutputFormat;

	public GenericReportViewVO() {
	}

	public void setSelectedReportTemplate(ReportTemplate selectedReportTemplate) {
		this.selectedReportTemplate = selectedReportTemplate;
	}

	public ReportTemplate getSelectedReportTemplate() {
		return selectedReportTemplate;
	}

	public List<ReportOutputFormat> getListReportOutputFormats() {
		return listReportOutputFormats;
	}

	public void setListReportOutputFormats(List<ReportOutputFormat> listReportOutputFormats) {
		this.listReportOutputFormats = listReportOutputFormats;
	}

	public void setSelectedOutputFormat(ReportOutputFormat selectedOutputFormat) {
		this.selectedOutputFormat = selectedOutputFormat;
	}

	public ReportOutputFormat getSelectedOutputFormat() {
		return selectedOutputFormat;
	}

}
