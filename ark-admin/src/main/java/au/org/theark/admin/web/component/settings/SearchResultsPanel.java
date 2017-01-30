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

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.settings.form.ContainerForm;
import au.org.theark.core.Constants;
import au.org.theark.core.model.config.entity.PropertyType;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.*;
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

	private Panel createValuePanel(PropertyType type, IModel<Setting> model, String placeholder) {
		Panel valuePanel = new EmptyPanel("propertyValue");
		switch (type) {
			case CHARACTER:
				//Create standard text box
				valuePanel = new SettingCharacterPanel("propertyValue", model, placeholder);
				break;
			case DATE:
				//Create data selector text box
				valuePanel = new SettingDatePanel("propertyValue", model, placeholder);
				break;
			case NUMBER:
				//Create standard text box with number formatter
				valuePanel = new SettingNumberPanel("propertyValue", model, placeholder);
				break;
			case FILE:
				//Create file upload view
				break;
		}
		return valuePanel;
	}

	@SuppressWarnings("unchecked")
	public DataView<Setting> buildDataView(ArkDataProvider<Setting, IArkSettingService> dataProvider) {
		DataView<Setting> dataView = new DataView<Setting>("settingList", dataProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Setting> item) {
				Setting setting = item.getModelObject();
				Setting current;
				Model<Setting> currentModel = new Model<>();
				Panel valuePanel = new EmptyPanel("propertyValue");
				if(teir == SystemWideSetting.class) {
					valuePanel = createValuePanel(setting.getPropertyType(), item.getModel(), "");
					item.add(valuePanel);
				} else if (teir == StudySpecificSetting.class) {
					Long studyID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
					Study study = iArkCommonService.getStudy(studyID);
					if (iArkSettingService.getStudySpecificSetting(study, setting.getPropertyName()) != null) {
						current = iArkSettingService.getStudySpecificSetting(study, setting.getPropertyName());
						currentModel.setObject(current);
					} else {
						//This creates the new setting
						StudySpecificSetting sss = new StudySpecificSetting();
						sss.setStudy(study);
						sss.setPropertyName(setting.getPropertyName());
						sss.setPropertyType(setting.getPropertyType());
						sss.setHighestType(setting.getHighestType());
						currentModel.setObject(sss);
					}
					valuePanel = createValuePanel(setting.getPropertyType(), currentModel, setting.getPropertyValue());
					item.add(valuePanel);
				}

				//For parent and child both will be the same
				item.add(new Label("propertyName", setting.getPropertyName()));
				item.add(new Label("propertyType", setting.getPropertyType().name()));

				Panel finalValuePanel = valuePanel;
				AjaxButton saveButton = new AjaxButton("save") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						if(teir == SystemWideSetting.class) {
							iArkSettingService.save(setting);
						} else if(teir == StudySpecificSetting.class){
							if(currentModel.getObject() != null) {
								StudySpecificSetting s = (StudySpecificSetting) currentModel.getObject();
								s.setPropertyValue(((TextField) finalValuePanel.get("propertyValue")).getValue());
								iArkSettingService.save(s);
							}
						}
						super.onSubmit(target, form);
					}
				};
				saveButton.setDefaultFormProcessing(false);
				item.add(saveButton);

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 5761909841047153853L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
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
