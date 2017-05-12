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
package au.org.theark.core.web.component.customfieldcategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfieldcategory.form.ContainerForm;


/**
 * 
 * @author smaddumarach
 *
 */
@SuppressWarnings("unchecked")
public class CustomFieldCategoryContainerPanel extends AbstractContainerPanel<CustomFieldCategoryVO> {

	private static final long							serialVersionUID	= -1L;
	private static final Logger							log					= LoggerFactory.getLogger(CustomFieldCategoryContainerPanel.class);

	private ContainerForm								containerForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService							iArkCommonService;

	private DataView<CustomFieldCategory>						dataView;
	private ArkDataProvider2<CustomFieldCategory, CustomFieldCategory>	customFieldCategoryProvider;
	
	
	
	/**
	 * 
	 * @param id
	 * @param useCustomFieldDisplay
	 * @param associatedPrimaryFn
	 */
	public CustomFieldCategoryContainerPanel(String id, boolean useCustomFieldCategoryDisplay, ArkFunction associatedPrimaryFn) {
		super(id);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<CustomFieldCategoryVO>(new CustomFieldCategoryVO());
		cpModel.getObject().getCustomFieldCategory().setArkFunction(associatedPrimaryFn);
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
				cpModel.getObject().getCustomFieldCategory().setStudy(study);
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
		customFieldCategoryProvider = new ArkDataProvider2<CustomFieldCategory, CustomFieldCategory>() {
			private static final long	serialVersionUID	= 1L;
			public int size() {
				if(criteriaModel.getObject().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY)){
					criteriaModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
					return (int)iArkCommonService.getCustomFieldCategoryCount(criteriaModel.getObject());//todo safe int conversion
				}
				else{
					return (int)iArkCommonService.getCustomFieldCategoryCount(criteriaModel.getObject());//todo safe int conversion
				}
			}
			public Iterator<CustomFieldCategory> iterator(int first, int count) {
				List<CustomFieldCategory> listCustomFieldCategories = new ArrayList<CustomFieldCategory>();
				
				if (isActionPermitted()) {
					//if(criteriaModel.getObject().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY)){
						//listCustomFields = iArkCommonService.searchPageableCustomFieldsForPheno(criteriaModel.getObject(), first, count);
					//}
					//else{
					listCustomFieldCategories = iArkCommonService.searchPageableCustomFieldCategories(criteriaModel.getObject(), first, count);
					//}
					
				}
				return CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(listCustomFieldCategories).iterator();
				//return orderHierarchicalyCustomFieldCategories(listCustomFieldCategories).iterator();
			}
		};
		// Set the criteria for the data provider
		customFieldCategoryProvider.setCriteriaModel(new PropertyModel<CustomFieldCategory>(cpModel, "customFieldCategory"));
		dataView = searchResultListPanel.buildDataView(customFieldCategoryProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());
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
