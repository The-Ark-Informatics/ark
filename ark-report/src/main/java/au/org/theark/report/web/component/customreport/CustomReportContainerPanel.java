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
package au.org.theark.report.web.component.customreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.report.web.component.datasourcetable.DataSourceTablePanel;

/**
 * @author cellis
 * 
 */
public class CustomReportContainerPanel extends Panel {
	/**
	 * 
	 */
	private static final long			serialVersionUID		= 8174158471514357770L;
	private Form							form						= new Form("form");
	private FeedbackPanel				feedbackPanel;
	private DropDownChoice<String>	dbNameDdc				= new DropDownChoice<String>("dbName");
	private TextArea<String>			query						= new TextArea<String>("query");
	private DataSourceTablePanel 		datasourceTablePanel	= new DataSourceTablePanel("datasourceTablePanel", "lims", "select * from barcode_label");

	/**
	 * @param id
	 */
	public CustomReportContainerPanel(String id) {
		super(id);
	}

	public void initialisePanel() {
		add(initFeedBackPanel());
		form.add(initDbNameSelect());
		form.add(query);
		AjaxButton runQuery = new AjaxButton("runQuery") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (dbNameDdc.getRawInput() != null && query.getRawInput() != null) {
					datasourceTablePanel = new DataSourceTablePanel("datasourceTablePanel", "lims", query.getRawInput());
					datasourceTablePanel.setVisible(true);
					form.addOrReplace(datasourceTablePanel);
					this.info("Query submitted");
					target.add(feedbackPanel);
					target.add(datasourceTablePanel);
				}
				else {
					this.error("Database name and a legitimate query are required");
					target.add(feedbackPanel);
				}
			}
		};
		runQuery.setDefaultFormProcessing(false);
		
		form.add(runQuery);
		
		form.add(datasourceTablePanel);
		datasourceTablePanel.setVisible(false);
		add(form);
	}

	private DropDownChoice<String> initDbNameSelect() {
		List<String> choices = new ArrayList<String>(0);
		choices.add("lims");
		dbNameDdc.setChoices(choices);
		return dbNameDdc;
	}

	protected WebMarkupContainer initFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true); // required for Ajax updates
		return feedbackPanel;
	}
}
