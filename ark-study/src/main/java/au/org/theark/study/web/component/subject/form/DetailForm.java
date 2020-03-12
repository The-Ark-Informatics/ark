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
package au.org.theark.study.web.component.subject.form;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.util.DateFromToValidator;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.component.panel.ConfirmationAnswer;
import au.org.theark.core.web.component.panel.YesNoPanel;
import au.org.theark.core.web.component.panel.collapsiblepanel.CollapsiblePanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consenthistory.LinkSubjectStudyConsentHistoryPanel;
import au.org.theark.study.web.component.subject.ChildStudyPalettePanel;
import au.org.theark.study.web.component.subject.ChildStudySubjectPanel;

public class DetailForm extends AbstractDetailForm<SubjectVO> {
    private static Logger log = LoggerFactory.getLogger(DetailForm.class);

    private static final long serialVersionUID = -9196914684971413116L;

    // Dependencies
    @SpringBean(name = Constants.STUDY_SERVICE)
    private IStudyService studyService;

    @SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
    private IArkCommonService iArkCommonService;

    @SpringBean(name = Constants.LIMS_SERVICE)
    private ILimsService iLimsService;

    // State
    protected Study study;
    private String currentLastName;

    // Components
    private WebMarkupContainer arkContextMarkupContainer;
    private TextField<String> subjectUIDTxtFld;
    private TextField<String> familyIdTxtFld;
    private TextField<String> firstNameTxtFld;
    private TextField<String> middleNameTxtFld;
    private TextField<String> lastNameTxtFld;
    private TextField<String> previousLastNameTxtFld;
    private TextField<String> preferredNameTxtFld;
    private DateTextField dateOfBirthTxtFld;
    private DateTextField dateOfDeathTxtFld;
    private DateTextField dateLastKnownAliveTxtFld;
    private TextField<String> causeOfDeathTxtFld;
    private TextArea<String> commentTxtAreaFld;
    private TextField<String> heardAboutStudyTxtFld;
    private DropDownChoice<YesNo> consentDownloadedChoice;
    private DropDownChoice<ConsentOption> consentToActiveContactDdc;
    private DropDownChoice<ConsentOption> consentToUseDataDdc;
    private DropDownChoice<ConsentOption> consentToPassDataGatheringDdc;
    private DropDownChoice<TitleType> titleTypeDdc;
    private DropDownChoice<VitalStatus> vitalStatusDdc;
    private DropDownChoice<GenderType> genderTypeDdc;
    private DropDownChoice<SubjectStatus> subjectStatusDdc;
    private DropDownChoice<MaritalStatus> maritalStatusDdc;
    private DropDownChoice<PersonContactMethod> personContactMethodDdc;
    private DropDownChoice<ConsentStatus> consentStatusChoice;
    private DropDownChoice<ConsentType> consentTypeChoice;
    private DateTextField consentDateTxtFld;
    private CollapsiblePanel consentHistoryPanel;
    private WebMarkupContainer wmcDeathDetailsContainer;
    private ChildStudyPalettePanel<SubjectVO> childStudyPalettePanel;
    private AbstractListEditor<OtherID> otherIdListView;
    private WebMarkupContainer otherIdWebMarkupContainer;
    private HistoryButtonPanel historyButtonPanel;
    private Label currentOrDeathAgeLabel;
    private String dateOfDeathInput;
    private DateTextField consentExpiryDateTxtFld;
    private DateTextField consentDateOfLastChangeTxtFld;

    private ModalWindow confirmModal;
    private ConfirmationAnswer confirmationAnswer;
    private final static String modalText = "<p align='center'>The last name of subject # has changed from @ to $.  </p>"
            + "</br>"
            + "<p align='center'>Would you like to record @ as a previous last name?</p>"
            + "</br>";

    public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
        super(id, feedBackPanel, containerForm, arkCrudContainerVO);
        this.arkContextMarkupContainer = arkContextContainer;
    }

    @Override
    public void onBeforeRender() {
        childStudyPalettePanel = new ChildStudyPalettePanel<>("childStudyPalette", containerForm.getModel());
        arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(childStudyPalettePanel);

        ChildStudySubjectPanel childStudySubjectPanel = new ChildStudySubjectPanel("childStudySubjectPanel", containerForm.getModel(), arkContextMarkupContainer, (ContainerForm) containerForm, arkCrudContainerVO);
        arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(childStudySubjectPanel);

        consentHistoryPanel.setVisible(!isNew());

        if (isNew()) {
            containerForm.getModelObject().getLinkSubjectStudy().setConsentStatus(iArkCommonService.getConsentStatusByName("Pending"));
        }
        historyButtonPanel.setVisible(!isNew());

        //Decide the Death of Date to be enable or disable according to the (vital status and new/existing)
        wmcDeathDetailsContainer.setEnabled(!isNew() && containerForm.getModelObject().getLinkSubjectStudy().getPerson().getVitalStatus().getName().equalsIgnoreCase("DECEASED"));
        currentLastName = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getLastName();
        super.onBeforeRender();
    }

    public void initialiseDetailForm() {
        subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeRender() {
                boolean isNew = isNew();
                boolean autoGenerate = containerForm.getModelObject().getLinkSubjectStudy().getStudy().getAutoGenerateSubjectUid();
                setEnabled(isNew && !autoGenerate);
                super.onBeforeRender();
            }
        };
        subjectUIDTxtFld.setOutputMarkupId(true);

        familyIdTxtFld = new TextField<>(Constants.FAMILY_UID);

        firstNameTxtFld = new TextField<>(Constants.PERSON_FIRST_NAME);
        middleNameTxtFld = new TextField<>(Constants.PERSON_MIDDLE_NAME);
        lastNameTxtFld = new TextField<>(Constants.PERSON_LAST_NAME);
        previousLastNameTxtFld = new TextField<String>(Constants.SUBJECT_PREVIOUS_LAST_NAME) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeRender() {
                if (!isNew()) {
                    String previousLastName = iArkCommonService.getPreviousLastname(containerForm.getModelObject().getLinkSubjectStudy().getPerson());
                    containerForm.getModelObject().setSubjectPreviousLastname(previousLastName);
                }
                setEnabled(isNew());
                super.onBeforeRender();
            }
        };
        preferredNameTxtFld = new TextField<>(Constants.PERSON_PREFERRED_NAME);

        otherIdWebMarkupContainer = new WebMarkupContainer("otherIDWMC");
        otherIdListView = new AbstractListEditor<OtherID>("linkSubjectStudy.person.otherIDs") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onPopulateItem(ListItem<OtherID> item) {
                PropertyModel<String> otherIDpropModel = new PropertyModel<>(item.getModelObject(), "otherID");
                PropertyModel<String> otherIDSourcepropModel = new PropertyModel<>(item.getModelObject(), "otherID_Source");
                TextField<String> otherIdTxtFld = new TextField<>("otherid", otherIDpropModel);
                otherIdTxtFld.setRequired(true);
                TextField<String> otherIdSourceTxtFld = new TextField<>("otherid_source", otherIDSourcepropModel);
                otherIdSourceTxtFld.setRequired(true);
                item.add(otherIdTxtFld);
                item.add(otherIdSourceTxtFld);
                item.add(new AjaxButton("delete") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        studyService.delete(item.getModelObject());
                        otherIdListView.getModelObject().remove(item.getIndex());

                        target.add(otherIdWebMarkupContainer);
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {
                        onSubmit(target, form);
                    }
                });
                item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
                    @Override
                    public String getObject() {
                        return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
                    }
                }));

            }
        };
        otherIdWebMarkupContainer.setOutputMarkupId(true);

        AjaxButton addNewOtherIdBtn = new AjaxButton("newOtherID") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                OtherID newOtherID = new OtherID();
                newOtherID.setPerson(containerForm.getModelObject().getLinkSubjectStudy().getPerson());
                otherIdListView.getModelObject().add(newOtherID);
                target.add(otherIdWebMarkupContainer);
                super.onSubmit(target, form);
            }
        };
        addNewOtherIdBtn.setDefaultFormProcessing(false);
        otherIdWebMarkupContainer.add(otherIdListView);
        otherIdWebMarkupContainer.add(addNewOtherIdBtn);

        heardAboutStudyTxtFld = new TextField<>(Constants.SUBJECT_HEARD_ABOUT_STUDY_FROM);
        dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        ArkDatePicker dobDatePicker = new ArkDatePicker();
        dobDatePicker.bind(dateOfBirthTxtFld);
        dateOfBirthTxtFld.add(dobDatePicker);
        currentOrDeathAgeLabel = new Label(Constants.PERSON_CURRENT_OR_DEATH_AGE);
        currentOrDeathAgeLabel.setOutputMarkupId(true);

        dateOfBirthTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                //update label at date of birth change.
                setCurrentOrDeathAgeLabel();
                setDeathDetailsContainer();
                target.add(wmcDeathDetailsContainer);
                target.add(currentOrDeathAgeLabel);
            }
        });

        dateLastKnownAliveTxtFld = new DateTextField("linkSubjectStudy.person.dateLastKnownAlive", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        ArkDatePicker dateLastKnownAlivePicker = new ArkDatePicker();
        dateLastKnownAlivePicker.bind(dateLastKnownAliveTxtFld);
        dateLastKnownAliveTxtFld.add(dateLastKnownAlivePicker);

        dateOfDeathTxtFld = new DateTextField(Constants.PERSON_DOD, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        causeOfDeathTxtFld = new TextField<>(Constants.PERSON_CAUSE_OF_DEATH);
        ArkDatePicker dodDatePicker = new ArkDatePicker();
        dodDatePicker.bind(dateOfDeathTxtFld);
        dateOfDeathTxtFld.add(dodDatePicker);

        dateOfDeathTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                //update label at date of text change.
                dateOfDeathInput = dateOfDeathTxtFld.getInput();
                setCurrentOrDeathAgeLabel();
                setDeathDetailsContainer();
                target.add(wmcDeathDetailsContainer);
                target.add(currentOrDeathAgeLabel);
            }
        });

        commentTxtAreaFld = new TextArea<>(Constants.PERSON_COMMENT);

        wmcDeathDetailsContainer = new WebMarkupContainer("deathDetailsContainer");
        wmcDeathDetailsContainer.setOutputMarkupId(true);

        // Default death details to disabled (enable onChange of vitalStatus)
        setDeathDetailsContainer();

        // Initialise Drop Down Choices
        // We can also have the reference data populated on Application start
        // and refer to a static list instead of hitting the database

        // Title
        Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
        ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        titleTypeDdc = new DropDownChoice<>(Constants.PERSON_TITLE_TYPE, (List<TitleType>) titleTypeList, defaultChoiceRenderer);
        titleTypeDdc.add(new ArkDefaultFormFocusBehavior());

        // Vital Status
        Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
        ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        vitalStatusDdc = new DropDownChoice<>(Constants.PERSON_VITAL_STATUS, (List<VitalStatus>) vitalStatusList, vitalStatusRenderer);
        vitalStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                //update label at vital status change.
                setCurrentOrDeathAgeLabel();
                setDeathDetailsContainer();
                target.add(wmcDeathDetailsContainer);
                target.add(currentOrDeathAgeLabel);
            }
        });

        //initialise the current or death label.
        setCurrentOrDeathAgeLabel();

        // Gender Type
        Collection<GenderType> genderTypeList = iArkCommonService.getGenderTypes();
        ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        genderTypeDdc = new DropDownChoice<>(Constants.PERSON_GENDER_TYPE, (List<GenderType>) genderTypeList, genderTypeRenderer);

        // Subject Status
        List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
        ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.SUBJECT_STATUS_ID);
        subjectStatusDdc = new DropDownChoice<>(Constants.SUBJECT_STATUS, subjectStatusList, subjectStatusRenderer);
        subjectStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (subjectStatusDdc.getModelObject().getName().equalsIgnoreCase("Archive")) {
                    Biospecimen biospecimenCriteria = new Biospecimen();
                    biospecimenCriteria.setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
                    biospecimenCriteria.setStudy(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
                    // check no biospecimens exist
                    long count = iLimsService.getBiospecimenCount(biospecimenCriteria);
                    if (count > 0) {
                        error("You cannot archive this subject as there are Biospecimens associated with it.");
                        target.focusComponent(subjectStatusDdc);
                    }
                }
                processErrors(target);
            }
        });

        // Marital Status
        Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus();
        ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        maritalStatusDdc = new DropDownChoice<>(Constants.PERSON_MARITAL_STATUS, (List<MaritalStatus>) maritalStatusList, maritalStatusRender);

        // Person Contact Method
        List<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList();
        ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        personContactMethodDdc = new DropDownChoice<>(Constants.PERSON_CONTACT_METHOD, contactMethodList, contactMethodRender);

        initConsentFields();
        initConfirmModel();
        attachValidators();
        addDetailFormComponents();

        deleteButton.setVisible(false);

        historyButtonPanel = new HistoryButtonPanel(containerForm, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer(), feedBackPanel);
    }

    /**
     * Initialise the Consent Status Drop Down Choice Control
     */
    @SuppressWarnings("unchecked")
    private void initialiseConsentStatusChoice() {
        List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
        ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        consentStatusChoice = new DropDownChoice<>(Constants.SUBJECT_CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
    }

    @SuppressWarnings("unchecked")
    private void initialiseConsentTypeChoice() {
        List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
        ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
        consentTypeChoice = new DropDownChoice<>(Constants.SUBJECT_CONSENT_TYPE, consentTypeList, defaultChoiceRenderer);
    }

    // Death details dependent on Vital Status selected to "Deceased"
    private void setDeathDetailsContainer() {
        VitalStatus vitalStatus = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getVitalStatus();
        if (vitalStatus != null) {
            String vitalStatusName = vitalStatus.getName();
            wmcDeathDetailsContainer.setEnabled(vitalStatusName.equalsIgnoreCase("DECEASED"));
        } else {
            wmcDeathDetailsContainer.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    private void initConsentFields() {
        consentDateTxtFld = new DateTextField(Constants.PERSON_CONSENT_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        ArkDatePicker consentDatePicker = new ArkDatePicker();
        consentDatePicker.bind(consentDateTxtFld);
        consentDateTxtFld.add(consentDatePicker);

        consentExpiryDateTxtFld = new DateTextField(Constants.PERSON_CONSENT_EXPIRY_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        ArkDatePicker consentExpiryDatePicker = new ArkDatePicker();
        consentExpiryDatePicker.bind(consentExpiryDateTxtFld);
        consentExpiryDateTxtFld.add(consentExpiryDatePicker);


        List<YesNo> yesNoListSource = iArkCommonService.getYesNoList();
        ChoiceRenderer<YesNo> yesNoRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);
        consentDownloadedChoice = new DropDownChoice<>(Constants.PERSON_CONSENT_DOWNLOADED, yesNoListSource, yesNoRenderer);

        List<ConsentOption> consentOptionList = iArkCommonService.getConsentOptionList();
        ChoiceRenderer<ConsentOption> consentOptionRenderer = new ChoiceRenderer<>(Constants.NAME, Constants.ID);

        consentToActiveContactDdc = new DropDownChoice<>(Constants.SUBJECT_CONSENT_TO_ACTIVE_CONTACT, (List) consentOptionList, consentOptionRenderer);
        consentToUseDataDdc = new DropDownChoice<>(Constants.SUBJECT_CONSENT_TO_USEDATA, (List) consentOptionList, consentOptionRenderer);

        //consent date changed.
        consentDateOfLastChangeTxtFld = new DateTextField(Constants.SUBJECT_CONSENT_DATE_OF_CHANGE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY, false));
        ArkDatePicker consentDateOfLastChangeDatePicker = new ArkDatePicker();
        consentDateOfLastChangeDatePicker.bind(consentDateOfLastChangeTxtFld);
        consentDateOfLastChangeTxtFld.add(consentDateOfLastChangeDatePicker);

        consentToPassDataGatheringDdc = new DropDownChoice<>(Constants.SUBJECT_CONSENT_PASSIVE_DATA_GATHER, (List) consentOptionList, consentOptionRenderer);

        initialiseConsentStatusChoice();
        initialiseConsentTypeChoice();

        consentHistoryPanel = new CollapsiblePanel("consentHistoryPanel", new Model<>("Consent History"), false) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Panel getInnerPanel(String markupId) {
                return new LinkSubjectStudyConsentHistoryPanel(markupId, new CompoundPropertyModel<>(new LssConsentHistory()));
            }
        };
    }

    public void addDetailFormComponents() {
        arkCrudContainerVO.getDetailPanelFormContainer().add(subjectUIDTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(familyIdTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(titleTypeDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(middleNameTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(previousLastNameTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(preferredNameTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(dateOfBirthTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(currentOrDeathAgeLabel);
        arkCrudContainerVO.getDetailPanelFormContainer().add(dateLastKnownAliveTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(commentTxtAreaFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(heardAboutStudyTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(vitalStatusDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(otherIdWebMarkupContainer);

        // Death details only be edited when vital status set to deceased
        wmcDeathDetailsContainer.add(dateOfDeathTxtFld);
        wmcDeathDetailsContainer.add(causeOfDeathTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(wmcDeathDetailsContainer);

        arkCrudContainerVO.getDetailPanelFormContainer().add(genderTypeDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(subjectStatusDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(maritalStatusDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(personContactMethodDdc);

        // Add consent fields into the form container.
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentToActiveContactDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentToUseDataDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentDateOfLastChangeTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentToPassDataGatheringDdc);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentStatusChoice);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentTypeChoice);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentDateTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentExpiryDateTxtFld);
        arkCrudContainerVO.getDetailPanelFormContainer().add(consentDownloadedChoice);

        arkCrudContainerVO.getDetailPanelFormContainer().add(consentHistoryPanel);
    }

    @Override
    protected void processErrors(AjaxRequestTarget target) {
        target.add(feedBackPanel);
    }

    protected void onCancel(AjaxRequestTarget target) {
        subjectUIDTxtFld.setEnabled(true);
        SubjectVO subjectVO = new SubjectVO();

        // Set study in context
        Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
        Study study = iArkCommonService.getStudy(sessionStudyId);
        subjectVO.getLinkSubjectStudy().setStudy(study);
        subjectVO.setStudyList(containerForm.getModelObject().getStudyList());
        containerForm.setModelObject(subjectVO);

        otherIdListView.setModelObject(new ArrayList<>());
        otherIdListView.removeAll();

        // Clear subject in context
        ContextHelper contextHelper = new ContextHelper();
        contextHelper.resetContextLabel(target, arkContextMarkupContainer);
        contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);

        SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, null);
        SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, null);
    }

    @Override
    protected void attachValidators() {
        subjectUIDTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.uid.required", this, null));
        dateOfBirthTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.person.dateOfBirth.DateValidator.maximum", this, null));
        subjectStatusDdc.setRequired(true).setLabel(new StringResourceModel("subject.status.required", this, null));
        consentDateTxtFld.setLabel(new StringResourceModel("consentDate", this, null));
        consentDateTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.consentDate.DateValidator.maximum", this, null));
        dateLastKnownAliveTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.person.dateLastKnownAlive.DateValidator.maximum", this, null));

        consentStatusChoice.setRequired(true).setLabel(new StringResourceModel("consentStatus.required", this, null));
        firstNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
        middleNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
        lastNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
        previousLastNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
        preferredNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
        causeOfDeathTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));
        commentTxtAreaFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500));
        heardAboutStudyTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500));

        this.add(new DateFromToValidator(dateOfBirthTxtFld, dateOfDeathTxtFld, "Date of birth", "Date of death"));
        this.add(new DateFromToValidator(dateOfBirthTxtFld, dateLastKnownAliveTxtFld, "Date of birth", "Last known alive date"));
        this.add(new DateFromToValidator(dateLastKnownAliveTxtFld, dateOfDeathTxtFld, "Last known alive date", "Date of death"));
        this.add(new DateFromToValidator(dateOfBirthTxtFld, consentDateTxtFld, "Date of birth", "Consent date"));
        this.add(new DateFromToValidator(consentDateTxtFld, dateOfDeathTxtFld, "Consent date", "Date of death"));
        this.add(new DateFromToValidator(consentDateTxtFld, consentExpiryDateTxtFld, "Consent date", "Consent expiry date"));

        consentDateOfLastChangeTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.consentDateOfLastChange.DateValidator.maximum", this, null));
    }

    @SuppressWarnings("unused")
    private boolean validateCustomFields(Long fieldToValidate, String message, AjaxRequestTarget target) {
        boolean validFlag = true;
        Calendar calendar = Calendar.getInstance();
        int calYear = calendar.get(Calendar.YEAR);
        if (fieldToValidate > calYear) {
            validFlag = false;
            this.error(message);
            processErrors(target);
        }

        return validFlag;
    }

    private void saveUpdateProcess(SubjectVO subjectVO, AjaxRequestTarget target) {
        //Setting the today's date if consent date of last change not entered or edited.
        if (subjectVO.getLinkSubjectStudy().getConsentDateOfLastChange() == null) {
            subjectVO.getLinkSubjectStudy().setConsentDateOfLastChange(new Date());
        }

        if (subjectVO.getLinkSubjectStudy().getPerson().getId() == null || containerForm.getModelObject().getLinkSubjectStudy().getPerson().getId() == 0) {

            subjectVO.getLinkSubjectStudy().setStudy(study);

            //Setting the today's date if consent date of last change not entered.
            if (subjectVO.getLinkSubjectStudy().getConsentDateOfLastChange() == null) {
                subjectVO.getLinkSubjectStudy().setConsentDateOfLastChange(new Date());
            }
            try {
                studyService.createSubject(subjectVO);

                onSavePostProcess(target);
                this.saveInformation();

                // Set new Subject into context
                SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectVO.getLinkSubjectStudy().getSubjectUID());
                SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subjectVO.getLinkSubjectStudy().getPerson().getId());
                SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
            } catch (ArkUniqueException ex) {
                this.error("Subject UID must be unique.");
            } catch (ArkSubjectInsertException ex) {
                this.error(ex.getMessage());
            }

        } else {
            boolean errorFlag = false;
            if (subjectStatusDdc.getModelObject().getName().equalsIgnoreCase("Archive")) {
                Biospecimen biospecimenCriteria = new Biospecimen();
                biospecimenCriteria.setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
                biospecimenCriteria.setStudy(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
                // check no biospecimens exist
                long count = iLimsService.getBiospecimenCount(biospecimenCriteria);
                if (count > 0) {
                    error("You cannot archive this subject as there are Biospecimens associated with it.");
                    target.focusComponent(subjectStatusDdc);
                    errorFlag = true;
                }
            }
            if (!errorFlag) {
                try {
                    if (isLastNameChanged(lastNameTxtFld.getValue())) {
                        confirmModal.setContent(new YesNoPanel(confirmModal.getContentId(), modalText.replace("#", subjectVO.getLinkSubjectStudy().getSubjectUID()).replace("@", currentLastName).replace("$", lastNameTxtFld.getInput())
                                , "Warning", confirmModal, confirmationAnswer));
                        confirmModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                            private static final long serialVersionUID = 1L;

                            public void onClose(AjaxRequestTarget target) {
                                subjectVO.setChangingLastName(confirmationAnswer.isAnswer());
                                try {
                                    studyService.updateSubject(subjectVO);
                                } catch (ArkUniqueException | EntityNotFoundException e) {
                                    log.error("Unable to update subject", e);
                                }
                                target.add(lastNameTxtFld);
                                onSavePostProcess(target);
                                updateInformation();
                            }
                        });
                        arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(confirmModal);
                        confirmModal.show(target);
                    } else {
                        studyService.updateSubject(subjectVO);
                        onSavePostProcess(target);
                        updateInformation();
                    }
                } catch (ArkUniqueException e) {
                    this.error("Subject UID must be unique.");
                } catch (EntityNotFoundException enf) {
                    this.error("Cannot find the selected Subject.");
                }
            }
        }

        processErrors(target);
    }

    @Override
    protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {
        target.add(arkCrudContainerVO.getDetailPanelContainer());

        Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
        if (studyId == null) {
            // No study in context
            this.error("There is no study selected. Please select a study to manage a subject.");
            processErrors(target);
        } else {
            createWarningForUnformattedDate(target, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getDateOfBirth(), dateOfBirthTxtFld.getInput());
            createWarningForUnformattedDate(target, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getDateLastKnownAlive(), dateLastKnownAliveTxtFld.getInput());
            createWarningForUnformattedDate(target, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getDateOfDeath(), dateOfDeathInput);
            createWarningForUnformattedDate(target, containerForm.getModelObject().getLinkSubjectStudy().getConsentDate(), consentDateTxtFld.getInput());
            createWarningForLifeSpan(containerForm, target);
            study = iArkCommonService.getStudy(studyId);
            saveUpdateProcess(containerForm.getModelObject(), target);
            ContextHelper contextHelper = new ContextHelper();
            contextHelper.resetContextLabel(target, arkContextMarkupContainer);
            contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);
            contextHelper.setSubjectContextLabel(target, containerForm.getModelObject().getLinkSubjectStudy().getSubjectUID(), arkContextMarkupContainer);
            contextHelper.setSubjectNameContextLabel(target, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getFullName(), arkContextMarkupContainer);

            SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getId());
            // We specify the type of person here as Subject
            SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
            arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
        }
    }

    /**
     * Life span warning when higher than Constants.MAXIMUM_ACCEPTABLE_AGE
     *
     * @param containerForm
     * @param target
     */
    private void createWarningForLifeSpan(Form<SubjectVO> containerForm,
                                          AjaxRequestTarget target) {
        Date bday = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getDateOfBirth();
        Date dateOfDeathDay = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getDateOfDeath();

        if (bday != null && dateOfDeathDay != null &&
                (getYearsBetweenDates(bday, dateOfDeathDay) > Constants.MAXIMUM_ACCEPTABLE_AGE || getYearsBetweenDates(bday, dateOfDeathDay) < 0)) {
            this.warn("Warning: The difference between the date of death and date of birth is greater than 125 years.");
            processErrors(target);
        }
    }

    private void createWarningForUnformattedDate(AjaxRequestTarget target, Date date, String dateTextFieldInput) {
        if (dateTextFieldInput != null && !dateTextFieldInput.isEmpty() && !isDateValid(dateTextFieldInput)) {
            DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
            this.warn("Warning: The specified date" + dateTextFieldInput + " has been transformed to" + dff.format(date));
            processErrors(target);
        }
    }

    private boolean isDateValid(String strDate) {
        String DATE_FORMAT = "dd/MM/yyyy";
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(strDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
        onCancel(target);
    }

    @Override
    protected boolean isNew() {
        return containerForm.getModelObject().getLinkSubjectStudy().getId() == null;
    }

    public TextField<String> getSubjectUIDTxtFld() {
        return subjectUIDTxtFld;
    }

    public void setSubjectUIDTxtFld(TextField<String> subjectUIDTxtFld) {
        this.subjectUIDTxtFld = subjectUIDTxtFld;
    }

    /**
     * @return the childStudyPalettePanel
     */
    public ChildStudyPalettePanel<SubjectVO> getChildStudyPalettePanel() {
        return childStudyPalettePanel;
    }

    /**
     * @param childStudyPalettePanel the childStudyPalettePanel to set
     */
    public void setChildStudyPalettePanel(ChildStudyPalettePanel<SubjectVO> childStudyPalettePanel) {
        this.childStudyPalettePanel = childStudyPalettePanel;
    }

    /**
     * Calculate the age knowing birthDay
     *
     * @param birthDay
     * @return
     */
    private Map<String, Integer> calculateAgeDifference(LocalDate birthDay, LocalDate nowOrDeathAge) {
        Map<String, Integer> timeMap = new HashMap<>();
        Period period = Period.between(birthDay, nowOrDeathAge);
        timeMap.put("years", period.getYears());
        timeMap.put("months", period.getMonths());
        return timeMap;
    }

    private void setCurrentOrDeathAgeLabel() {
        Person person = containerForm.getModelObject().getLinkSubjectStudy().getPerson();
        VitalStatus virStatus = vitalStatusDdc.getModelObject();
        if (person != null && virStatus != null) {
            if ((person.getDateOfBirth() != null) && (virStatus.getName().equalsIgnoreCase("Alive"))) {
                Map<String, Integer> timeMap = calculateAgeDifference(LocalDate.parse(new SimpleDateFormat(au.org.theark.core.Constants.yyyy_MM_dd).format(person.getDateOfBirth())), LocalDate.now());
                person.setCurrentOrDeathAge("Current age:" + timeMap.get("years") + " years" + ((timeMap.get("months").equals(0)) ? "." : ", " + timeMap.get("months") + " months"));
            }
            if ((person.getDateOfBirth() != null) && (virStatus.getName().equalsIgnoreCase("Deceased")) && (person.getDateOfDeath() != null)) {
                Map<String, Integer> timeMap = calculateAgeDifference(LocalDate.parse(new SimpleDateFormat(au.org.theark.core.Constants.yyyy_MM_dd).format(person.getDateOfBirth())),
                        LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(person.getDateOfDeath())));
                person.setCurrentOrDeathAge("Age at death:" + timeMap.get("years") + " years" + ((timeMap.get("months").equals(0)) ? "." : ", " + timeMap.get("months") + " months"));
            }
        }
    }

    private int getYearsBetweenDates(Date first, Date second) {
        Calendar firstCal = GregorianCalendar.getInstance();
        Calendar secondCal = GregorianCalendar.getInstance();
        firstCal.setTime(first);
        secondCal.setTime(second);
        secondCal.add(Calendar.DAY_OF_YEAR, -firstCal.get(Calendar.DAY_OF_YEAR));
        return secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR);
    }

    private void initConfirmModel() {
        confirmationAnswer = new ConfirmationAnswer(false);
        confirmModal = new ModalWindow("confirmModal");
        confirmModal.setCookieName("yesNoPanel");
        confirmModal.setContent(new YesNoPanel(confirmModal.getContentId(), modalText, "Changed the last name.", confirmModal, confirmationAnswer));
        arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(confirmModal);
    }

    private boolean isLastNameChanged(String newLastName) {
        return currentLastName != null && !currentLastName.equals(newLastName);
    }
}
