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
package au.org.theark.report.web.component.viewReport;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.ReportSelectVO;

/**
 * @author elam
 * 
 */
public class ReportContainerPanel extends Panel {

	private static final long								serialVersionUID	= 8174158471514357770L;

	private ReportSelectPanel								reportSelectPanel;
	protected ReportContainerVO							reportContainerVO	= new ReportContainerVO();
	protected CompoundPropertyModel<ReportSelectVO>	reportSelectCPM;

	/**
	 * @param id
	 */
	public ReportContainerPanel(String id) {
		super(id);
		reportSelectCPM = new CompoundPropertyModel<ReportSelectVO>(new ReportSelectVO());
	}

	public void initialisePanel() {
		add(initialiseFeedBackPanel());
		reportSelectPanel = new ReportSelectPanel("reportSelectPanel", reportSelectCPM, reportContainerVO);
		reportSelectPanel.initialisePanel();

		WebMarkupContainer selectedReportContainerWMC = new WebMarkupContainer("selectedReportContainerWMC");
		selectedReportContainerWMC.setOutputMarkupPlaceholderTag(true);
		EmptySelectedReportContainer selectedReportPanel = new EmptySelectedReportContainer("selectedReportContainerPanel");
		selectedReportPanel.setOutputMarkupId(true);

		reportContainerVO.setSelectedReportContainerWMC(selectedReportContainerWMC);
		reportContainerVO.setSelectedReportPanel(selectedReportPanel);
		selectedReportContainerWMC.add(selectedReportPanel);

		add(reportSelectPanel);
		add(selectedReportContainerWMC);
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		reportContainerVO.setFeedbackPanel(new FeedbackPanel("feedbackMessage"));
		reportContainerVO.getFeedbackPanel().setOutputMarkupId(true); // required for Ajax updates
		return reportContainerVO.getFeedbackPanel();
	}
}
