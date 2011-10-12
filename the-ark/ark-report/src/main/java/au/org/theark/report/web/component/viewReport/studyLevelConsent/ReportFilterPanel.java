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
package au.org.theark.report.web.component.viewReport.studyLevelConsent;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.component.viewReport.studyLevelConsent.filterForm.StudyLevelConsentDetailsFilterForm;

/**
 * 
 * @author elam
 */
public class ReportFilterPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5654177575562262548L;
	
	public ReportFilterPanel(String id) {
		super(id);
	}

	public void initialisePanel(CompoundPropertyModel<ConsentDetailsReportVO> cpModel, FeedbackPanel feedbackPanel, au.org.theark.report.web.component.viewReport.ReportOutputPanel reportOutputPanel) {
		StudyLevelConsentDetailsFilterForm consentDetailsFilterForm = new StudyLevelConsentDetailsFilterForm("filterForm", cpModel);
		consentDetailsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(consentDetailsFilterForm);
	}
}
