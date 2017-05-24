package au.org.theark.core.web.component.settings;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.*;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.util.EventPayload;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by george on 31/1/17.
 */
public class ArkSettingDataView<T extends Setting> extends DataView<Setting> {
    private static final long	serialVersionUID	= 1L;

    private List<AjaxButton> saveButtons;
    private Class<T> teir;
    private FeedbackPanel feedbackPanel;

    @SpringBean(name = Constants.ARK_COMMON_SERVICE)
    private IArkCommonService iArkCommonService;

    @SpringBean(name = Constants.ARK_SETTING_SERVICE)
    private IArkSettingService iArkSettingService;

    public ArkSettingDataView(String id, IDataProvider<Setting> dataProvider, Class<T> teir) {
        super(id, dataProvider);
        this.teir = teir;
        saveButtons = new ArrayList<>();
    }

    public ArkSettingDataView(String id, IDataProvider<Setting> dataProvider, Class teir, FeedbackPanel feedbackPanel) {
        this(id, dataProvider, teir);
        this.feedbackPanel = feedbackPanel;
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
                valuePanel = new SettingFilePanel("propertyValue", model);
                break;
        }
        valuePanel.setOutputMarkupId(true);
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
                FeedbackMessages me = Session.get().getFeedbackMessages();

                Iterator<FeedbackMessage> entryIter = me.iterator();
                while (entryIter.hasNext()) {
                    FeedbackMessage entry =
                            (FeedbackMessage)entryIter.next();
                    System.out.println("Message: " +
                            entry.toString());
                    entry.markRendered();
                }
                target.add(feedbackPanel);

                if (!validateInput(finalValuePanel, currentModel.getObject() == null ? setting : currentModel.getObject())) {
                    target.add(this);
                    target.add(feedbackPanel);
                    return;
                }
                if (teir == SystemWideSetting.class) {
                    getPropertyValueAsString(setting, finalValuePanel);
                    iArkSettingService.saveSetting(setting);
                } else if (teir == StudySpecificSetting.class) {
                    if (currentModel.getObject() != null) {
                        StudySpecificSetting s = (StudySpecificSetting) currentModel.getObject();
                        getPropertyValueAsString(s, finalValuePanel);
                        iArkSettingService.saveSetting(s);
                    }
                } else if (teir == UserSpecificSetting.class) {
                    if (currentModel.getObject() != null) {
                        UserSpecificSetting s = (UserSpecificSetting) currentModel.getObject();
                        getPropertyValueAsString(s, finalValuePanel);
                        iArkSettingService.saveSetting(s);
                    }
                }
                target.add(this);
                target.add(finalValuePanel);
                super.onSubmit(target, form);
                this.send(getWebPage(), Broadcast.DEPTH, new EventPayload(Constants.EVENT_RELOAD_LOGO_IMAGES, target));
            }
        };
        saveButton.setDefaultFormProcessing(false);
        item.add(saveButton);
        saveButtons.add(saveButton);
        if(teir == UserSpecificSetting.class) {
            saveButton.setVisible(false);
        }

        item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            private static final long	serialVersionUID	= 5761909841047153853L;

            @Override
            public String getObject() {
                return (item.getIndex() % 2 == 1) ? "even" : "odd";
            }
        }));
    }

    private void getPropertyValueAsString(Setting setting, Panel valuePanel) {
        if(setting.getPropertyType() == PropertyType.FILE) {
            String fileId = null;
            try {
                fileId = ((SettingFilePanel) valuePanel).onSave();
            } catch (ArkSystemException e) {
                e.printStackTrace();
                return;
            }
            setting.setPropertyValue(fileId);
        } else {
            setting.setPropertyValue(((TextField) valuePanel.get("propertyValue")).getValue());
        }
    }


    //TODO: Add date handling.
    private boolean validateInput(Panel finalValuePanel, Setting setting) {
        String value = null;
        if (finalValuePanel.get("propertyValue") instanceof FileUploadField) {
            value = ((FileUploadField) finalValuePanel.get("propertyValue")).getValue();
        } else {
            value = ((TextField) finalValuePanel.get("propertyValue")).getValue();
        }
        switch(finalValuePanel.getClass().getSimpleName()) {
            case "SettingNumberPanel":
                if(!NumberUtils.isNumber(value)) {
                    return false;
                }
                return executeValidators(iArkSettingService.getSettingValidatorsForSetting(setting), value);
            case "SettingCharacterPanel":
                return executeValidators(iArkSettingService.getSettingValidatorsForSetting(setting), value);
            case "SettingFilePanel":
                return executeValidators(iArkSettingService.getSettingValidatorsForSetting(setting), value);
            default:
                return false;
        }
    }

    private boolean executeValidators(List<SettingValidator> settingValidators, String value) {
        for(SettingValidator sv : settingValidators) {
            switch (sv.getType()) {
                case NUMBER_GREATER_THAN:
                    int input = NumberUtils.toInt(value);
                    int svValue = NumberUtils.toInt(sv.getValue());
                    if (!(input > svValue)) {
                        this.error("Number entered is less than or equal to " + sv.getValue());
                        return false;
                    }
                    break;
                case NUMBER_LESS_THAN:
                    input = NumberUtils.toInt(value);
                    svValue = NumberUtils.toInt(sv.getValue());
                    if (!(input < svValue)) {
                        this.error("Number entered is greater than or equal to " + sv.getValue());
                        return false;
                    }
                    break;
                case NUMBER_GREATER_OR_EQUAL_THAN:
                    input = NumberUtils.toInt(value);
                    svValue = NumberUtils.toInt(sv.getValue());
                    if(!(input >= svValue)) {
                        this.error("Number entered is less than " + sv.getValue());
                        return false;
                    }
                    break;
                case NUMBER_LESS_OR_EQUAL_THAN:
                    input = NumberUtils.toInt(value);
                    svValue = NumberUtils.toInt(sv.getValue());
                    if(!(input <= svValue)) {
                        this.error("Number entered is greater than " + sv.getValue());
                        return false;
                    }
                    break;
                case CHAR_NON_EMPTY:
                    if(value.isEmpty()) {
                        this.error("Text entered is empty");
                        return false;
                    }
                    break;
                case CHAR_MIN_LENGTH:
                    if(value.length() < NumberUtils.toInt(sv.getValue())) {
                        this.error("Text entered is less than the minimum required length");
                        return false;
                    }
                case CHAR_MAX_LENGTH:
                    if(value.length() > NumberUtils.toInt(sv.getValue())) {
                        this.error("Text entered is longer than the maximum allowed length");
                        return false;
                    }
                    break;
                case FILE_EXISTS:
                    break;
                case DIR_EXISTS:
                    if(Files.notExists(Paths.get(value))) {
                        this.error("Directory entered doesn't exist.");
                        return false;
                    }
                    break;
                case FILE_NON_EMPTY:
                    break;
            }
        }
        return true;
    }

    public void save(AjaxRequestTarget target, Form<?> form) {

        for(AjaxButton button: saveButtons) {
            Class clazz = button.getClass();
            Method onSubmit = null;
            try {
                onSubmit = clazz.getDeclaredMethod("onSubmit", AjaxRequestTarget.class, Form.class);
                onSubmit.invoke(button, target, form);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        this.send(getWebPage(), Broadcast.DEPTH, new EventPayload(Constants.EVENT_RELOAD_LOGO_IMAGES, target));
    }
}
