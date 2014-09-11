package au.org.theark.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.disease.web.component.affection.AffectionContainerPanel;
import au.org.theark.disease.web.component.disease.DiseaseContainerPanel;
import au.org.theark.disease.web.component.gene.GeneContainerPanel;
import au.org.theark.lims.service.IInventoryService;

public class DiseaseSubMenuTab extends AbstractArkTabPanel {

	private static final long	serialVersionUID	= 1L;
	private transient static Logger	log = LoggerFactory.getLogger(DiseaseSubMenuTab.class);

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private WebMarkupContainer	arkContextMarkup;

	/**
	 * @param id
	 */
	public DiseaseSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	@SuppressWarnings( { "serial", "unchecked" })
	public void buildTabs() {

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();

		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_DISEASE);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module
		log.info("Ark function list: " + arkFunctionList);
		for (final ArkFunction arkFunction : arkFunctionList) {
			log.info("ARK Function: " + arkFunction.getName());
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = new EmptyPanel(panelId);
					if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE)) {
						log.info("DISEASE");
						panelToReturn = new DiseaseContainerPanel(panelId, arkContextMarkup);
					} else if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_GENE)) {
						log.info("GENE");
						panelToReturn = new GeneContainerPanel(panelId, arkContextMarkup);
					} else if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS)) {
						panelToReturn = new au.org.theark.core.web.component.customfield.CustomFieldContainerPanel(panelId, true, arkFunction);
						log.info(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS);
					} else if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS_DISPLAY)) {
						log.info(arkFunction.getName());
					} else if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE_AFFECTION)) {
						log.info(arkFunction.getName());
						panelToReturn = new AffectionContainerPanel(panelId, arkContextMarkup);
					}
					return panelToReturn;
				}

				@Override
				public boolean isVisible() {
					return true;
				}
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_DISEASE_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}

	/**
	 * @param arkContextMarkup the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}
}

