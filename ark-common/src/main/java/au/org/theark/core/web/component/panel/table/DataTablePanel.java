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
package au.org.theark.core.web.component.panel.table;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.Response;
import org.wicketstuff.datatables.DemoDatatable;

/**
 * Panel that displays a dynamically created data table
 * @author cellis
 *
 */
public class DataTablePanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private DemoDatatable		table					= new DemoDatatable("table");
	private List<String>			headerList;
	private List<List<String>>	data;

	/**
	 * Panel that displays a dynamically created data table
	 * @param id markup identifier
	 * @param headerList header columns to display
	 * @param data data cells to display
	 */
	public DataTablePanel(String id, List<String> headerList, List<List<String>> data) {
		super(id);
		this.headerList = headerList;
		this.data = data;

		add(table);
		initHeader();
		initBody();
	}

	/**
	 * Panel that displays a dynamically created data table. Presumes first row in dataSet contains headers
	 * @param id markup identifier
	 * @param dataSet data cells to display
	 */
	public DataTablePanel(String id, List<List<String>> dataSet) {
		super(id);
		this.headerList = dataSet.remove(0);
		this.data = dataSet;
		
		add(table);
		initHeader();
		initBody();
	}

	private void initHeader() {
		// Raw Webcomponent to generate raw HTML with column name as text (DataTables then renders neatly accordingly)
		table.add(new WebComponent("headerList") {
         /**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
         public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
             Response response = getRequestCycle().getResponse();
             
             for (String col : headerList) {
                 response.write("<th>" + col.toUpperCase() + "</th>\n");
             }
         }
     });


	}

	private void initBody() {
		// Using a nested ListViews
		table.add(new ListView<List<String>>("data", data) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(ListItem<List<String>> item) {
				// Iterate through headerList for columns
				final List<String> row = item.getModelObject();
				item.add(new ListView<String>("column", headerList) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("value", row.get(item.getIndex()).toString()));
					}
				});
			}
		});
	}
}