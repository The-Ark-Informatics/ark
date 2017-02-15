package au.org.theark.core.web.component.settings;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.*;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by george on 31/1/17.
 */
public class ArkSettingDataView<T extends Setting> extends DataView<Setting> {
    private static final long	serialVersionUID	= 1L;

    private Class<T> teir;

    @SpringBean(name = Constants.ARK_COMMON_SERVICE)
    private IArkCommonService iArkCommonService;

    @SpringBean(name = Constants.ARK_SETTING_SERVICE)
    private IArkSettingService iArkSettingService;

    public ArkSettingDataView(String id, IDataProvider<Setting> dataProvider, Class<T> teir) {
        super(id, dataProvider);
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
        } else if (teir == UserSpecificSetting.class) {
            try {
                ArkUser arkUser = iArkCommonService.getArkUser(SecurityUtils.getSubject().getPrincipal().toString());
                if (iArkSettingService.getUserSpecificSetting(arkUser, setting.getPropertyName()) != null) {
                    current = iArkSettingService.getUserSpecificSetting(arkUser, setting.getPropertyName());
                    currentModel.setObject(current);
                } else if (SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID) != null) {
                    Long studyID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
                    Study study = iArkCommonService.getStudy(studyID);
                    if (iArkSettingService.getStudySpecificSetting(study, setting.getPropertyName()) != null) {
                        current = iArkSettingService.getStudySpecificSetting(study, setting.getPropertyName());
                        setting.setPropertyValue(current.getPropertyValue());
                    }
                } else {
                    //This creates the new setting
                    UserSpecificSetting uss = new UserSpecificSetting();
                    uss.setArkUser(arkUser);
                    uss.setPropertyName(setting.getPropertyName());
                    uss.setPropertyType(setting.getPropertyType());
                    uss.setHighestType(setting.getHighestType());
                    currentModel.setObject(uss);
                }
                valuePanel = createValuePanel(setting.getPropertyType(), currentModel, setting.getPropertyValue());
                item.add(valuePanel);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }

        //For parent and child both will be the same
        item.add(new Label("propertyName", setting.getPropertyName()));
        item.add(new Label("propertyType", setting.getPropertyType().name()));

        Panel finalValuePanel = valuePanel;
        AjaxButton saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!validateInput(finalValuePanel)) {
                    target.add(this);
                    return;
                }
                setting.setPropertyValue(((TextField) finalValuePanel.get("propertyValue")).getValue());
                if(teir == SystemWideSetting.class) {
                    iArkSettingService.saveSetting(setting);
                } else if(teir == StudySpecificSetting.class){
                    if(currentModel.getObject() != null) {
                        StudySpecificSetting s = (StudySpecificSetting) currentModel.getObject();
                        s.setPropertyValue(((TextField) finalValuePanel.get("propertyValue")).getValue());
                        iArkSettingService.saveSetting(s);
                    }
                } else if(teir == UserSpecificSetting.class) {
                    if(currentModel.getObject() != null) {
                        UserSpecificSetting s = (UserSpecificSetting) currentModel.getObject();
                        s.setPropertyValue(((TextField) finalValuePanel.get("propertyValue")).getValue());
                        iArkSettingService.saveSetting(s);
                    }
                }
                target.add(this);
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


    //TODO: Add date handling.
    private boolean validateInput(Panel finalValuePanel) {
        switch(finalValuePanel.getClass().getSimpleName()) {
            case "SettingNumberPanel":
                return StringUtils.isNumericSpace(((TextField) finalValuePanel.get("propertyValue")).getValue());
            case "SettingCharacterPanel":
                return true;
            default:
                return false;
        }
    }
}
