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
package au.org.theark.registry.web.component.invoice.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<Pipeline> {

	private static final long						serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private PageableListView<Pipeline>		listView;
	private CompoundPropertyModel<Pipeline>	cpmModel;

	private TextField<String>						id;
	private TextField<String>						name;
	private TextField<String>						description;

	public SearchForm(String id, CompoundPropertyModel<Pipeline> model, PageableListView<Pipeline> listView, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, model, feedBackPanel, arkCrudContainerVO);
		this.cpmModel = model;
		this.listView = listView;
		initialiseFieldForm();

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study selected. Please select a study.");
	}

	public void initialiseFieldForm() {
		id = new TextField<String>("id");
		name = new TextField<String>("name");
		description = new TextField<String>("description");

		// Set up fields on the form
		addFieldComponents();
	}

	private void addFieldComponents() {
		// Add the field components
		add(id);
		add(name);
		add(description);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		preProcessDetailPanel(target);
	}
}