/*******************************************************************************
initialiseFeedBackPanel * Copyright (c) 2011  University of Western Australia. All rights reserved.
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
package au.org.theark.phenotypic.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.customfieldgroup.CustomFieldGroupContainerPanel;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadContainerPanel;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoCollectionDataEntryContainerPanel;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadContainerPanel;
import au.org.theark.phenotypic.web.component.summary.SummaryContainerPanel;


@SuppressWarnings({ "serial", "unused" })
public class PhenotypicSubMenuTab extends AbstractArkTabPanel {
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private transient Logger			log					= LoggerFactory.getLogger(PhenotypicSubMenuTab.class);
	private transient Subject			currentUser;
	private transient Long				studyId;
	private WebMarkupContainer			arkContextMarkup;
	private List<ITab>					moduleSubTabsList	= new ArrayList<ITab>();

	public PhenotypicSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		buildTabs(arkContextMarkup);
	}

	public void buildTabs(final WebMarkupContainer arkContextMarkup) {
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);
		for (final ArkFunction menuArkFunction : arkFunctionList) {
			AbstractTab tab = new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(final String panelId) {
					return panelToReturn(menuArkFunction, panelId);
				}
			};
			moduleSubTabsList.add(tab);
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	protected Panel panelToReturn(final ArkFunction arkFunction, String panelId) {
		Panel panelToReturn = null;

		// Clear cache to determine permissions
		processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC, arkFunction);

		if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_SUMMARY)) {
			panelToReturn = new SummaryContainerPanel(panelId);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY)) {
			// attach the fields to this "Data Dictionary" function
			panelToReturn = new CustomFieldContainerPanel(panelId, false, arkFunction);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY_UPLOAD)) {
			ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
			panelToReturn = new FieldUploadContainerPanel(panelId, function);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION)) {

			ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
			panelToReturn = new CustomFieldGroupContainerPanel(panelId, function);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA)) {
			panelToReturn = new PhenoCollectionDataEntryContainerPanel(panelId).initialisePanel();
//			panelToReturn = new FieldDataContainerPanel(panelId);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD)) {
			panelToReturn = new FieldDataUploadContainerPanel(panelId);
		}

		return panelToReturn;
	}
}
