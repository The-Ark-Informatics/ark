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
package au.org.theark.study.web.component.studycomponent;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;

public class SearchResultList extends Panel {

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	/**
	 * 
	 * @param id
	 * @param crudContainerVO
	 * @param studyCompContainerForm
	 */
	public SearchResultList(String id, ArkCrudContainerVO crudContainerVO,ContainerForm studyCompContainerForm){
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}
	/**
	 * 
	 * @param iModel
	 * @param searchContainer
	 * @return
	 */
	public PageableListView<StudyComp> buildPageableListView(IModel iModel) {

		PageableListView<StudyComp> sitePageableListView = new PageableListView<StudyComp>("studyCompList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<StudyComp> item) {

				StudyComp studyComponent = item.getModelObject();

				/* The Component ID */
				if (studyComponent.getId() != null) {
					// Add the study Component Key here
					item.add(new Label("studyComponent.id", studyComponent.getId().toString()));
				}
				else {
					item.add(new Label("studyComponent.id", ""));
				}
				/* Component Name Link */
				item.add(buildLink(studyComponent));

				// TODO when displaying text escape any special characters
				/* Description */
				if (studyComponent.getDescription() != null) {
					item.add(new Label("studyComponent.description", studyComponent.getDescription()));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("studyComponent.description", ""));// the ID here must match the ones in mark-up
				}

				/* Add the Keyword */
				// TODO when displaying text escape any special characters
				if (studyComponent.getKeyword() != null) {
					item.add(new Label("studyComponent.keyword", studyComponent.getKeyword()));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("studyComponent.keyword", ""));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final StudyComp studyComponent) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("studyComponent.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				StudyCompVo studyCompVo = containerForm.getModelObject();
				studyCompVo.setMode(Constants.MODE_EDIT);
				studyCompVo.setStudyComponent(studyComponent);// Sets the selected object into the model
				
				// Render the UI
				
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

		// Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", studyComponent.getName());
		link.add(nameLinkLabel);
		return link;

	}

}
