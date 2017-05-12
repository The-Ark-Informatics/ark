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
package au.org.theark.admin.web.component.settings;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.settings.form.ContainerForm;
import au.org.theark.core.Constants;
import au.org.theark.core.dao.IArkSettingDao;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.config.entity.UserSpecificSetting;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cellis
 * 
 */
public class SettingsContainerPanel extends AbstractContainerPanel<Setting> {

	private static final long										serialVersionUID	= 442185554812824590L;
	private ContainerForm											containerForm;
	private SearchPanel searchPanel;
	private DetailPanel detailPanel;
	private SearchResultsPanel searchResultsPanel;
	private DataView<Setting>								settingDataView;
	@SuppressWarnings("unchecked")
	private ArkDataProvider<Setting, IArkSettingService>	dataProvider;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>									iAdminService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	@SpringBean(name = Constants.ARK_SETTING_SERVICE)
	private IArkSettingService iArkSettingService;

	private Class teir;
	/**
	 * @param id
	 */
	public SettingsContainerPanel(String id) {
		super(id);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<Setting>(new Setting());
		initCrudContainerVO();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		add(containerForm);
	}

	public SettingsContainerPanel(String id, Class teir) {
		this(id);
		this.teir = teir;
		if(teir == SystemWideSetting.class) {
			cpModel = new CompoundPropertyModel<Setting>(new SystemWideSetting());
			cpModel.getObject().setHighestType("system");
		}
		if(teir == StudySpecificSetting.class) {
			cpModel = new CompoundPropertyModel<Setting>(new StudySpecificSetting());
			cpModel.getObject().setHighestType("study");
			Long studyId=(Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study= iArkCommonService.getStudy(studyId);
			((StudySpecificSetting) cpModel.getObject()).setStudy(study);
		}
		if(teir == UserSpecificSetting.class) {
			cpModel = new CompoundPropertyModel<Setting>(new UserSpecificSetting());
			cpModel.getObject().setHighestType("user");
			try {
				ArkUser arkUser = iArkCommonService.getArkUser(SecurityUtils.getSubject().getPrincipal().toString());
				((UserSpecificSetting) cpModel.getObject()).setArkUser(arkUser);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResults());
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
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, arkCrudContainerVO, teir, feedBackPanel);
		initialiseDataView();
		settingDataView = searchResultsPanel.buildDataView(dataProvider);
		settingDataView.setItemsPerPage(iArkCommonService.getRowsPerPage());
		PagingNavigator pageNavigator = new PagingNavigator("navigator", settingDataView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(settingDataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private void initialiseDataView() {
		// Data provider to paginate resultList
		dataProvider = new ArkDataProvider<Setting, IArkSettingService>(iArkSettingService) {
			public int size() {
				return service.getSettingsCount(model.getObject());
			}

			public Iterator<Setting> iterator(int first, int count) {
				List<Setting> listCollection = new ArrayList<Setting>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableSettings(model.getObject(), first, count);
				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		dataProvider.setModel(new LoadableDetachableModel<Setting>() {
			@Override
			protected Setting load() {
				return cpModel.getObject();
			}
		});
	}
}
