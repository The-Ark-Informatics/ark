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
package au.org.theark.admin.web.component.rolePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;

/**
 * @author cellis
 * 
 */
public class RolePolicyContainerPanel extends AbstractContainerPanel<AdminVO> {

	private static final long														serialVersionUID	= 442185554812824590L;
	protected transient Logger														log					= LoggerFactory.getLogger(RolePolicyContainerPanel.class);
	private ContainerForm															containerForm;
	private SearchPanel																searchPanel;
	private DetailPanel																detailPanel;
	private SearchResultsPanel														searchResultsPanel;
	private DataView<ArkRoleModuleFunctionVO>									dataView;
	@SuppressWarnings("unchecked")
	private ArkDataProvider<ArkRoleModuleFunctionVO, IAdminService>	dataProvider;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>												iArkCommonService;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>													iAdminService;

	/**
	 * @param id
	 */
	public RolePolicyContainerPanel(String id) {
		super(id);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<AdminVO>(new AdminVO());

		initCrudContainerVO();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResults());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, containerForm, cpModel, arkCrudContainerVO);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, feedBackPanel, arkCrudContainerVO);
		initialiseDataView();
		dataView = searchResultsPanel.buildDataView(dataProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private void initialiseDataView() {
		// Data provider to paginate resultList
		dataProvider = new ArkDataProvider<ArkRoleModuleFunctionVO, IAdminService>(iAdminService) {
			public int size() {
				return (int)service.getArkRoleModuleFunctionVOCount(model.getObject());
			}

			public Iterator<ArkRoleModuleFunctionVO> iterator(int first, int count) {
				List<ArkRoleModuleFunctionVO> listCollection = new ArrayList<ArkRoleModuleFunctionVO>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableArkRoleModuleFunctionVO(model.getObject(), first, count);

					for (Iterator iterator = listCollection.iterator(); iterator.hasNext();) {
						ArkRoleModuleFunctionVO arkRoleModuleFunctionVO = (ArkRoleModuleFunctionVO) iterator.next();
						// Get all permission rows, and assign to VO accordingly
						try {
							Collection<String> arkPermissions = iArkCommonService.getArkRolePermission(arkRoleModuleFunctionVO.getArkFunction(), arkRoleModuleFunctionVO.getArkRole().getName(),
									arkRoleModuleFunctionVO.getArkModule());
							// Set up boolean references in VO
							arkRoleModuleFunctionVO.setArkCreatePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.CREATE));
							arkRoleModuleFunctionVO.setArkReadPermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.READ));
							arkRoleModuleFunctionVO.setArkUpdatePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.UPDATE));
							arkRoleModuleFunctionVO.setArkDeletePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.DELETE));
						}
						catch (EntityNotFoundException e) {
							log.error(e.getMessage());
						}
					}

				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		dataProvider.setModel(new LoadableDetachableModel<ArkRoleModuleFunctionVO>() {
			@Override
			protected ArkRoleModuleFunctionVO load() {
				return cpModel.getObject().getArkRoleModuleFunctionVo();
			}
		});
	}
}
