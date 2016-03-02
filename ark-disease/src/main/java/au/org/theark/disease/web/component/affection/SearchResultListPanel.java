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
package au.org.theark.disease.web.component.affection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.web.component.affection.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= -8517602411833622907L;
	
	private WebMarkupContainer	arkContextMarkup;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService	iArkDiseaseService;

	public SearchResultListPanel(String id, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id);
		this.containerForm = containerForm;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
		
	}

	public DataView<AffectionVO> buildDataView(final ArkDataProvider<AffectionVO, IArkDiseaseService> diseaseProvider) {

		DataView<AffectionVO> studyCompDataView = new DataView<AffectionVO>("affectionList", diseaseProvider, iArkCommonService.getUserConfig(Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			@Override
			protected void populateItem(final Item<AffectionVO> item) {
				Affection affection = item.getModelObject().getAffection();

				item.add(new Label("affection.id", affection.getId().toString()));
				item.add(buildLink(item.getModelObject()));
				item.add(new Label("affection.recordDate",affection.getRecordDate().toString()));
				item.add(new Label("affection.status", affection.getAffectionStatus().getName()));
				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return studyCompDataView;
	}

	
	public PageableListView<AffectionVO> buildListView(IModel iModel) {

		PageableListView<AffectionVO> listView = new PageableListView<AffectionVO>("affectionList", iModel, iArkCommonService.getUserConfig(Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			@Override
			protected void populateItem(final ListItem<AffectionVO> item) {
				Affection affection= item.getModelObject().getAffection();
				item.add(new Label("affection.id", affection.getId().toString()));
				item.add(buildLink(item.getModelObject()));				
				item.add(new Label("affection.recordDate",affection.getRecordDate().toString()));
				item.add(new Label("affection.status", affection.getAffectionStatus().getName()));
				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return listView;
	}

	private AjaxLink buildLink(final AffectionVO affectionVO) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("disease.name") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				
				containerForm.setModelObject(affectionVO);
			}
		};
		Label nameLinkLabel = new Label("affection.name", affectionVO.getAffection().getDisease().getName());
		link.add(nameLinkLabel);
		return link;
	}
}
