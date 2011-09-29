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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {

	private ContainerForm		containerForm;
	protected ArkCrudContainerVO arkCrudContainerVO;
	
	/**
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO,ContainerForm containerForm){
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}
	
	/**
	 * 
	 * @param iModel
	 * @return
	 */
	public PageableListView<Phone> buildPageableListView(IModel iModel) {

		PageableListView<Phone> pageableListView = new PageableListView<Phone>(Constants.PHONE_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			@Override
			protected void populateItem(final ListItem<Phone> item) {
				// TODO Auto-generated method stub
				Phone phone = item.getModelObject();

				item.add(buildLink(phone));

				if (phone.getId() != null) {
					item.add(new Label("id", phone.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				if (phone.getAreaCode() != null) {
					item.add(new Label("areaCode", phone.getAreaCode()));
				}
				else {
					item.add(new Label("areaCode", ""));
				}
				if (phone.getPhoneType() != null && phone.getPhoneType().getName() != null) {
					item.add(new Label("phoneType.name", phone.getPhoneType().getName()));
				}
				else {
					item.add(new Label("phoneType.name", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return pageableListView;

	}

	private AjaxLink buildLink(final Phone phone) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("phoneNumberLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setPhone(phone);
				
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);// saveBtn
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		
				target.add(arkCrudContainerVO.getSearchPanelContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getViewButtonContainer());
				target.add(arkCrudContainerVO.getEditButtonContainer());
				target.add(arkCrudContainerVO.getDetailPanelFormContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
			}

		};
		Label nameLinkLabel = new Label(Constants.PHONE_NUMBER_VALUE, phone.getPhoneNumber());
		link.add(nameLinkLabel);
		return link;
	}

}
