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
package au.org.theark.phenotypic.web.component.phenodatacategory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataSetCategoryOrderingHelper;
import au.org.theark.phenotypic.web.component.phenodatacategory.form.ContainerForm;



/**
 * 
 * @author smaddumarach
 *
 */
@SuppressWarnings("unchecked")
public class PhenoDataCategoryContainerPanel extends AbstractContainerPanel<PhenoDataSetCategoryVO> {

	private static final long							serialVersionUID	= -1L;
	private static final Logger							log					= LoggerFactory.getLogger(PhenoDataCategoryContainerPanel.class);

	private ContainerForm								containerForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<?>							iArkCommonService;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;

	private DataView<PhenoDataSetCategory>						dataView;
	private ArkDataProvider2<PhenoDataSetCategory, PhenoDataSetCategory>	phenoDataSetCategoryProvider;
	
	
	
	/**
	 * 
	 * @param id
	 * @param useCustomFieldDisplay
	 * @param associatedPrimaryFn
	 */
	public PhenoDataCategoryContainerPanel(String id, boolean useCustomFieldCategoryDisplay, ArkFunction associatedPrimaryFn) {
		super(id);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoDataSetCategoryVO>(new PhenoDataSetCategoryVO());
		cpModel.getObject().getPhenoDataSetCategory().setArkFunction(associatedPrimaryFn);
		//cpModel.getObject().setUseCustomFieldCategoryDisplay(useCustomFieldCategoryDisplay);

		prerenderContextCheck();

		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);

		if (associatedPrimaryFn == null) {
			this.error("An internal error occurred.  Please report this to your System Administrator.");
			log.error("Internal error: associatedPrimaryFn should never be null");
			containerForm.setEnabled(false);
		}
	}

	protected void prerenderContextCheck() {
		// Get the Study and Module out of context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		if ((sessionStudyId != null) && (sessionModuleId != null)) {
			Study study = null;
			study = iArkCommonService.getStudy(sessionStudyId);
			ArkModule arkModule = iArkCommonService.getArkModuleById(sessionModuleId);

			if (study != null && arkModule != null) {
				cpModel.getObject().getPhenoDataSetCategory().setStudy(study);
				// TODO: Maybe check that the primary function supplied is of the same module?
			}
		}
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		SearchPanel searchPanel = new SearchPanel("searchPanel", cpModel, arkCrudContainerVO, feedBackPanel);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		Panel detailPanel = new EmptyPanel("detailPanel");
		detailPanel.setOutputMarkupPlaceholderTag(true); // ensure this is replaceable
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchResults() {
		SearchResultListPanel searchResultListPanel = new SearchResultListPanel("resultListPanel", cpModel, arkCrudContainerVO, feedBackPanel);
		// Data providor to paginate resultList
		phenoDataSetCategoryProvider = new ArkDataProvider2<PhenoDataSetCategory, PhenoDataSetCategory>() {
			private static final long	serialVersionUID	= 1L;
			public int size() {
					return (int)iPhenotypicService.getPhenoDataSetCategoryCount(criteriaModel.getObject());//todo safe int conversion
			}
			public Iterator<PhenoDataSetCategory> iterator(int first, int count) {
				List<PhenoDataSetCategory> listCustomFieldCategories = new ArrayList<PhenoDataSetCategory>();
				
				if (isActionPermitted()) {
					listCustomFieldCategories = iPhenotypicService.searchPageablePhenoDataSetCategories(criteriaModel.getObject(), first, count);
					
				}
				return (listCustomFieldCategories).iterator();
			}
		};
		// Set the criteria for the data provider
		phenoDataSetCategoryProvider.setCriteriaModel(new PropertyModel<PhenoDataSetCategory>(cpModel, "phenoDataSetCategory"));
		dataView = searchResultListPanel.buildDataView(phenoDataSetCategoryProvider);
		dataView.setItemsPerPage(iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue());
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
		private static final long	serialVersionUID	= 1L;
		@Override
		protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
			}
		};
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	
}
