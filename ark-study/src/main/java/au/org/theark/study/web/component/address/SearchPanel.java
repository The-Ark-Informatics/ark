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
package au.org.theark.study.web.component.address;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.address.form.ContainerForm;
import au.org.theark.study.web.component.address.form.SearchForm;

/**
 * @author nivedann
 * 
 */
public class SearchPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private FeedbackPanel	feedBackPanel;
	private PageableListView<Address>	listView;
	
	/**
	 * 
	 * @param id
	 * @param crudContainerVO
	 * @param feedBackPanel
	 * @param containerForm
	 * @param listView
	 */
	public SearchPanel(String id,ArkCrudContainerVO crudContainerVO, FeedbackPanel feedBackPanel, 
								ContainerForm containerForm, PageableListView<Address> listView) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		this.feedBackPanel = feedBackPanel; 
		this.listView = listView;
	}
	
	public void initialisePanel(CompoundPropertyModel<AddressVO> addressVoCpm) {

		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, addressVoCpm, arkCrudContainerVO, feedBackPanel, listView);

		add(searchForm);
	}

}
