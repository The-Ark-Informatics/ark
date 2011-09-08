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

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.wicketstuff.jasperreports.JRResource;

import au.org.theark.report.model.vo.ReportSelectVO;

public class ReportOutputPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	public AbstractLink				downloadReportLink;
	public AjaxButton					downloadReportButton;
	public Form<ReportSelectVO>	downloadReportForm;

	public ReportOutputPanel(String id) {
		super(id);
	}

	public void initialisePanel() {
		downloadReportLink = new ExternalLink("linkToReport", "", "");
		downloadReportLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
		add(downloadReportLink);
		CompoundPropertyModel<ReportSelectVO> cpModel = new CompoundPropertyModel<ReportSelectVO>(new ReportSelectVO());
		downloadReportForm = new Form<ReportSelectVO>("downloadReportForm", cpModel);
		downloadReportButton = new AjaxButton("downloadReportButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -559527968475678014L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};

		downloadReportForm.add(downloadReportButton);
		add(downloadReportForm);
		// Button had caching issues
		downloadReportForm.setVisible(false);

		this.setVisible(false); // start off invisible
	}

	public void setReportResource(JRResource reportResource) {
		if (reportResource != null) {
			ResourceLink<Void> newLink = new ResourceLink<Void>("linkToReport", reportResource);
			newLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
			addOrReplace(newLink);
			downloadReportForm.addOrReplace(buildDownloadButton(reportResource));
			downloadReportLink = newLink;
		}
		else {
			if (!downloadReportLink.getClass().equals(ExternalLink.class)) {
				ExternalLink newLink = new ExternalLink("linkToReport", "", "");
				newLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
				downloadReportLink.replaceWith(newLink);
				downloadReportLink = newLink;
			}
		}
	}

	private AjaxButton buildDownloadButton(final JRResource reportResource) {
		AjaxButton ajaxButton = new AjaxButton("downloadReportButton", new StringResourceModel("downloadKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4454605760895668351L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// This code would emulate a file download as if clicked by the user
				ResourceReference ref = new ResourceReference(reportResource.getFileName()) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 3758643436893809637L;

					protected Resource newResource() {
						return reportResource;
					}
				};
				String url = getRequestCycle().urlFor(ref).toString();
				getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);
		ajaxButton.setOutputMarkupPlaceholderTag(true);
		return ajaxButton;
	}

	@Override
	public boolean isVersioned() {
		return false;
	}
}
