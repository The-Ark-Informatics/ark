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
package au.org.theark.admin.web.component.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.function.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;

/**
 * @author cellis
 * 
 */
public class FunctionContainerPanel extends AbstractContainerPanel<AdminVO> {
	/**
	 * 
	 */
	private static final long										serialVersionUID	= 442185554812824590L;
	private ContainerForm											containerForm;
	private SearchPanel												searchPanel;
	private DetailPanel												detailPanel;
	private SearchResultsPanel										searchResultsPanel;
	private DataView<ArkFunction>									dataView;
	private ArkDataProvider<ArkFunction, IAdminService>	dataProvider;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>									iAdminService;

	/**
	 * @param id
	 */
	public FunctionContainerPanel(String id) {
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
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, arkCrudContainerVO);
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
		dataProvider = new ArkDataProvider<ArkFunction, IAdminService>(iAdminService) {
			public int size() {
				return service.getArkFunctionCount(model.getObject());
			}

			public Iterator<ArkFunction> iterator(int first, int count) {
				List<ArkFunction> listCollection = new ArrayList<ArkFunction>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableArkFunctions(model.getObject(), first, count);
				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		dataProvider.setModel(new LoadableDetachableModel<ArkFunction>() {
			@Override
			protected ArkFunction load() {
				return cpModel.getObject().getArkFunction();
			}
		});
	}
}
