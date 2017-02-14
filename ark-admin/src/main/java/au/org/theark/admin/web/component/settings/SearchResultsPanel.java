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

import au.org.theark.admin.web.component.settings.form.ContainerForm;
import au.org.theark.core.Constants;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.settings.ArkSettingDataView;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultsPanel extends Panel {

	private static final long		serialVersionUID	= 5237384531161620862L;
	protected transient Logger		log					= LoggerFactory.getLogger(SearchResultsPanel.class);

	@SpringBean(name = Constants.ARK_SETTING_SERVICE)
	private IArkSettingService iArkSettingService;

	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private ContainerForm			containerForm;
	private ArkCrudContainerVO		arkCrudContainerVo;
	private Class teir;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo, Class teir) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.teir = teir;
	}

	@SuppressWarnings("unchecked")
	public DataView<Setting> buildDataView(ArkDataProvider<Setting, IArkSettingService> dataProvider) {
		ArkSettingDataView dataView = new ArkSettingDataView<SystemWideSetting>("settingList", dataProvider, teir);
		return dataView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final Setting setting) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVo);
				// Refresh base container form to remove any feedBack messages
				target.add(containerForm);
			}
		};

		// Add the label for the link
//		Label linkLabel = new Label("arkModuleRole.arkModule", arkModuleRole.getArkModule().getName());
//		link.add(linkLabel);
		return link;
	}
}
