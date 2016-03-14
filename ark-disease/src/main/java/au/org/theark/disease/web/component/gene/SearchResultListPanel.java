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
package au.org.theark.disease.web.component.gene;

import java.util.ArrayList;
import java.util.List;

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
import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.GeneVO;
import au.org.theark.disease.web.component.gene.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= -8517602411833622907L;

	private static final Logger log = LoggerFactory.getLogger(SearchResultListPanel.class);
	
	private WebMarkupContainer	arkContextMarkup;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;


	public SearchResultListPanel(String id, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id);
		this.containerForm = containerForm;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
		
	}

	public DataView<GeneVO> buildDataView(ArkDataProvider<GeneVO, IArkDiseaseService> diseaseProvider) {

		DataView<GeneVO> studyCompDataView = new DataView<GeneVO>("diseaseList", diseaseProvider) {

			@Override
			protected void populateItem(final Item<GeneVO> item) {
				Gene gene = item.getModelObject().getGene();
				
				item.add(new Label("gene.id", gene.getId().toString()));
				
				item.add(buildLink(item.getModelObject()));
								
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

	
	public PageableListView<GeneVO> buildListView(IModel iModel) {

		PageableListView<GeneVO> listView = new PageableListView<GeneVO>("diseaseList", iModel, iArkCommonService.getUserConfig(Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			@Override
			protected void populateItem(final ListItem<GeneVO> item) {
				
				Gene gene = item.getModelObject().getGene();
				
				item.add(new Label("gene.id", gene.getId().toString()));
				
				item.add(buildLink(item.getModelObject()));
				
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

	private AjaxLink buildLink(final GeneVO geneVO) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("disease.name") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
	
				List<Disease> availableDiseases = iArkDiseaseService.getAvailableDiseasesForStudy(iArkCommonService.getStudy(sessionStudyId));
				List<Disease> selectedDiseases = new ArrayList<Disease>(geneVO.getGene().getDiseases());

				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				containerForm.setModelObject(geneVO);
				containerForm.getModelObject().setAvailableDiseases(availableDiseases);
				containerForm.getModelObject().setSelectedDiseases(selectedDiseases);
				
			}
		};
		Label nameLinkLabel = new Label("gene.name", geneVO.getGene().getName());
		link.add(nameLinkLabel);
		return link;
	}
}
