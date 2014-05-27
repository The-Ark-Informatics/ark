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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;

import org.apache.wicket.markup.html.list.ListView;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger							log					= LoggerFactory.getLogger(DataTablePanel.class);

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
//		this.headerList = dataSet.remove(0);  doesnt use?
		this.data = dataSet;
		
		add(table);
		data = transpose(data);
//		initHeader();
		initBody();
		table.add(new AttributeModifier("class", ""));
	}

	static <T> List<List<T>> transpose(List<List<T>> table) {
		if(table.size() == 0) return table;
		List<List<T>> ret = new ArrayList<List<T>>();
		final int N = table.get(0).size();
		for (int i = 0; i < N; i++) {
			List<T> col = new ArrayList<T>();
			for (List<T> row : table) {
				col.add(row.get(i));
			}
			ret.add(col);
		}
		return ret;
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
            	 //left align the header text
                 response.write("<th align='left'>" + col.toUpperCase() + "</th>\n");
             }
         }
     });
	}

	private void initBody() {
		// Using a nested ListViews
//TODO Constant for page size
		PageableListView<List<String>> listView = new PageableListView<List<String>>("data", data, 15) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(ListItem<List<String>> item) {
				IModel model = item.getModel();
				item.add(new ListView<String>("cols", model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add( new Label("value", item.getModelObject()));
					}
					
				});
				
			}
		};
		table.add(listView);
		add(new PagingNavigator("navigator", listView));
	}
	
}