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
package au.org.theark.study.web.component.phone;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.study.web.component.phone.form.ContainerForm;
import au.org.theark.study.web.component.phone.form.SearchForm;

/**
 * @author Nivedan
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {

	private FeedbackPanel				feedBackPanel;
	private PageableListView<Phone>	pageableListView;
	private ContainerForm				containerForm;
	
	private ArkCrudContainerVO	arkCrudContainerVO;
	

	public SearchPanel(String id,ArkCrudContainerVO arkCrudContainerVO,FeedbackPanel feedBackPanel,  PageableListView<Phone> listView){
		super(id);
		this.pageableListView = listView;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
	}

	public void initialisePanel(CompoundPropertyModel<PhoneVO> phoneVOCpm) {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, phoneVOCpm, arkCrudContainerVO,feedBackPanel, pageableListView);
		add(searchForm);
	}
}
