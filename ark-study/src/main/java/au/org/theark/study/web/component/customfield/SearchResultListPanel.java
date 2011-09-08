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
package au.org.theark.study.web.component.customfield;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.customfield.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {

	protected ArkCrudContainerVO	arkCrudContainerVO;
	protected ContainerForm			containerForm;

	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;

		// TODO Auto-generated constructor stub
	}

	public PageableListView<SubjectCustmFld> buildPageableListView(IModel iModel) {

		PageableListView<SubjectCustmFld> pageableListView = new PageableListView<SubjectCustmFld>(Constants.SEARCH_RESULT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			@Override
			protected void populateItem(ListItem<SubjectCustmFld> item) {

				SubjectCustmFld customField = item.getModelObject();

				item.add(buildLink(customField));

				if (customField.getName() != null) {
					item.add(new Label("name", customField.getName()));
				}
				else {
					item.add(new Label("name", ""));
				}
			}
		};

		return pageableListView;
	}

	private AjaxLink buildLink(final SubjectCustmFld customField) {

		Label nameLinkLabel = new Label(Constants.CUSTOM_FIELD_LABEL, customField.getFieldTitle());

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("fieldTitle") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				CustomFieldVO customFieldVO = containerForm.getModelObject();

			}
		};

		link.add(nameLinkLabel);
		return link;
	}

}
