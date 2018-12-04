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
package au.org.theark.admin.web.component.settings.form;

import java.util.Arrays;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.Constants;
import au.org.theark.core.model.config.entity.PropertyType;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<Setting> {


	private static final long	serialVersionUID	= 6727664998186871910L;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>				iAdminService;

	@SpringBean(name = Constants.ARK_SETTING_SERVICE)
	private IArkSettingService iArkSettingService;

	private CompoundPropertyModel<Setting>	cpmModel;
	private ArkCrudContainerVO				arkCrudContainerVo;
	private ContainerForm					containerForm;
	private FeedbackPanel					feedbackPanel;

	private TextField<String> 				propertyNameTxtFld;
	private TextField<String>				propertyValueTxtFld;
	private DropDownChoice<PropertyType>	propertyTypeDropDown;

	public SearchForm(String id, CompoundPropertyModel<Setting> cpmModel, ArkCrudContainerVO arkCrudContainerVo, FeedbackPanel feedbackPanel, ContainerForm containerForm) {
		super(id, cpmModel, feedbackPanel, arkCrudContainerVo);

		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.feedbackPanel = feedbackPanel;
		this.cpmModel = cpmModel;
		setMultiPart(true);


		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected class PlaceholderBehaviour extends Behavior {
		private final String placeholder;

		public PlaceholderBehaviour(String placeholder) {
			this.placeholder = placeholder;
		}

		@Override
		public void onComponentTag(Component component, ComponentTag tag) {
			tag.put("placeholder", this.placeholder);
		}
	}

	protected void initialiseSearchForm() {

	    disableNewButton();

		propertyNameTxtFld = new TextField<String>("propertyName", new PropertyModel<>(containerForm.getModelObject(), "propertyName"));
		propertyValueTxtFld = new TextField<String>("propertyValue", new PropertyModel<>(containerForm.getModelObject(), "propertyValue"));
		propertyTypeDropDown = new DropDownChoice<PropertyType>("propertyType", Arrays.asList(PropertyType.values()));

	}

	protected void disableNewButton() {
		newButton = new AjaxButton(Constants.NEW) {
			@Override
			public boolean isVisible() {
				return false;
			}
		};
		addOrReplace(newButton);
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);

		long count = iArkSettingService.getSettingsCount(containerForm.getModelObject());
		if (count == 0l) {
			this.info("There are no settings that match your query.");
			target.add(feedbackPanel);
		}

		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVo.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm() {
		add(propertyNameTxtFld);
		add(propertyValueTxtFld);
		add(propertyTypeDropDown);
	}

	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		containerForm.setModelObject(new Setting());

		preProcessDetailPanel(target);
		
		// Refresh base container form to remove any feedBack messages
		target.add(containerForm);
	}
}
