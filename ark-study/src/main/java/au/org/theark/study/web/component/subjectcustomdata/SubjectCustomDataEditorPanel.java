package au.org.theark.study.web.component.subjectcustomdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.DropDownChoiceDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.EncodedValueVO;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectcustomdata.form.CustomDataEditorForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SubjectCustomDataEditorPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(SubjectCustomDataEditorPanel.class);

	private CompoundPropertyModel<SubjectCustomDataVO>			cpModel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	protected FeedbackPanel				feedbackPanel;
	protected CustomDataEditorForm	customDataEditorForm;
	protected ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData, IStudyService> scdDataProvider;
	protected DataView<SubjectCustomFieldData> dataView;

	public SubjectCustomDataEditorPanel(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedBackPanel) {
		super(id);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
	}
	
	public SubjectCustomDataEditorPanel initialisePanel() {
//		fakeLoadFromHibernate();	//TODO: replace this with proper backend function
		
		initialiseDataView();
		customDataEditorForm = new CustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel).initialiseForm();
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(customDataEditorForm.getDataViewWMC());
				target.addComponent(this);
			}
		};
		pageNavigator.setOutputMarkupId(true);
		customDataEditorForm.getDataViewWMC().add(dataView);
		this.add(customDataEditorForm);
		this.add(pageNavigator);
		
		return this;
	}
	
	private void initialiseDataView() {

		// Data provider to paginate resultList
		scdDataProvider = new ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData, IStudyService>(studyService) {
			public int size() {
				LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
				ArkModule arkModule = criteriaModel.getObject().getArkModule();

				return studyService.getSubjectCustomFieldDataCount(lss, arkModule);
			}

			public Iterator<SubjectCustomFieldData> iterator(int first, int count) {
				LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
				ArkModule arkModule = criteriaModel.getObject().getArkModule();

				List<SubjectCustomFieldData> subjectCustomDataList = studyService.getSubjectCustomFieldDataList(lss, arkModule, first, count);
				cpModel.getObject().setSubjectCustomFieldDataList(subjectCustomDataList);
				return cpModel.getObject().getSubjectCustomFieldDataList().iterator();
			}
		};
		// Set the criteria for the data provider
		scdDataProvider.setCriteriaModel(new LoadableDetachableModel<SubjectCustomDataVO>() {
			@Override
			protected SubjectCustomDataVO load() {
				return cpModel.getObject();
			}
		});

		dataView = this.buildDataView(scdDataProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
	}
	
	public DataView<SubjectCustomFieldData> buildDataView(ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData, IStudyService> scdDataProvider2) {

		DataView<SubjectCustomFieldData> studyCompDataView = new DataView<SubjectCustomFieldData>("subjectCustomDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<SubjectCustomFieldData> item) {
				SubjectCustomFieldData subjectCustomData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link isn't there already
				if (subjectCustomData.getLinkSubjectStudy() == null) {
					subjectCustomData.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
				}
				CustomField cf = subjectCustomData.getCustomFieldDisplay().getCustomField();
				//TODO: Remove this test code that sets the field label with numbering
//				cf.setFieldLabel(cf.getFieldLabel() + (item.getIndex() + 1));
				
				Label indexLbl = new Label("index", item.getIndex() + ".");
				Label fieldLabelLbl; 
				if (cf.getFieldLabel() != null) {
					fieldLabelLbl = new Label("fieldLabel", cf.getFieldLabel());
				}
				else {
					fieldLabelLbl = new Label("fieldLabel", "");
				}
				Panel dataValueEntryPanel;
				String fieldTypeName = cf.getFieldType().getName();
				String encodedValues = cf.getEncodedValues();
				Boolean requiredField = subjectCustomData.getCustomFieldDisplay().getRequired();
				if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
					DateDataEntryPanel dateDataEntryPanel = new DateDataEntryPanel("dataValueEntryPanel", 
																new PropertyModel<Date>(item.getModel(), "dateDataValue"),
																new Model<String>(cf.getFieldLabel()));
					dateDataEntryPanel.setBadDateStringModel(new PropertyModel<String>(item.getModel(), "dataValue"));
					if (cf.getMinValue() != null && !cf.getMinValue().isEmpty()) {
						IConverter dateConverter = dateDataEntryPanel.getDateConverter();
						try {
							Date minDate = (Date) dateConverter.convertToObject(cf.getMinValue(), getLocale());
							dateDataEntryPanel.addValidator(DateValidator.minimum(minDate));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							log.error("Unexpected error: customfield.minValue is not in the DD/MM/YYYY date format");
							this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
							customDataEditorForm.setEnabled(false);
						}
					}
					if (cf.getMaxValue() != null && !cf.getMaxValue().isEmpty()) {
						IConverter dateConverter = dateDataEntryPanel.getDateConverter();
						try {
							Date maxDate = (Date) dateConverter.convertToObject(cf.getMaxValue(), getLocale());
							dateDataEntryPanel.addValidator(DateValidator.maximum(maxDate));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							log.error("Unexpected error: customfield.maxValue is not in the DD/MM/YYYY date format");
							this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
							customDataEditorForm.setEnabled(false);
						}
					}
					if (requiredField != null && requiredField == true) {
						dateDataEntryPanel.setRequired(true);
					}
					dataValueEntryPanel = dateDataEntryPanel;
				}
				else {
					if (encodedValues != null && !encodedValues.isEmpty()) {
						// The presence of encodedValues means it should be a DropDownChoice
						List<String> encodeKeyValueList = Arrays.asList(encodedValues.split(";"));
						List<EncodedValueVO> choiceList = new ArrayList();
						for (String keyValue : encodeKeyValueList) {
							String[] keyValueArray = keyValue.split("=");
							EncodedValueVO encodedValueVo = new EncodedValueVO();
							encodedValueVo.setKey(keyValueArray[0]);
							encodedValueVo.setValue(keyValueArray[1]);
							choiceList.add(encodedValueVo);
						}
						// TODO: Do the encodedValues required more validation???
						ChoiceRenderer<EncodedValueVO> ddcChoiceRender = new ChoiceRenderer<EncodedValueVO>("value", "key");
						DropDownChoiceDataEntryPanel ddcPanel = 
									new DropDownChoiceDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(item.getModel(), "dataValue"), 
																					new Model<String>(cf.getFieldLabel()), choiceList, ddcChoiceRender);
						if (cf.getMissingValue() != null && !cf.getMissingValue().isEmpty()) {
							ddcPanel.setMissingValue(cf.getMissingValue());
						}
						if (requiredField != null && requiredField == true) {
							ddcPanel.setRequired(true);
						}
						dataValueEntryPanel = ddcPanel;
					}
					else {
						if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.CHARACTER_FIELD_TYPE_NAME)) {
							// Text data
							TextDataEntryPanel textDataEntryPanel = new TextDataEntryPanel("dataValueEntryPanel", 
																												new PropertyModel<String>(item.getModel(), "dataValue"), 
																												new Model<String>(cf.getFieldLabel()));
							if (requiredField != null && requiredField == true) {
								 textDataEntryPanel.setRequired(true);
							}
							dataValueEntryPanel = textDataEntryPanel;
						}
						else if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.NUMBER_FIELD_TYPE_NAME)) {
							// Number data
							NumberDataEntryPanel numberDataEntryPanel = new NumberDataEntryPanel("dataValueEntryPanel", 
																														new PropertyModel<Double>(item.getModel(), "dataValue"), 
																														new Model<String>(cf.getFieldLabel()));
							if (cf.getMinValue() != null && !cf.getMinValue().isEmpty()) {
								IConverter doubleConverter = numberDataEntryPanel.getNumberConverter();
								try {
									Double minNumber = (Double) doubleConverter.convertToObject(cf.getMinValue(), getLocale());
									numberDataEntryPanel.addValidator(new MinimumValidator(minNumber));
								}
								catch (ConversionException ce) {
									// This should not occur because it means the data is corrupted on the backend database
									log.error("Unexpected error: customfield.maxValue is not in a valid number format");
									this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
									customDataEditorForm.setEnabled(false);
								}
							}
							if (cf.getMaxValue() != null && !cf.getMaxValue().isEmpty()) {
								IConverter doubleConverter = numberDataEntryPanel.getNumberConverter();
								try {
									Double maxNumber = (Double) doubleConverter.convertToObject(cf.getMaxValue(), getLocale());
									numberDataEntryPanel.addValidator(new MaximumValidator(maxNumber));
								}
								catch (ConversionException ce) {
									// This should not occur because it means the data is corrupted on the backend database
									log.error("Unexpected error: customfield.maxValue is not in a valid number format");
									this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
									customDataEditorForm.setEnabled(false);
								}
							}
							if (requiredField != null && requiredField == true) {
								numberDataEntryPanel.setRequired(true);
							}
							dataValueEntryPanel = numberDataEntryPanel;
						}
						else {
							// TODO: Unknown type should display an UnsupportedValueEntryPanel
							dataValueEntryPanel = new EmptyPanel("dataValueEntryPanel");
						}
					}
				}
				Label unitLabelLbl;
				if (cf.getUnitType() != null && cf.getUnitType().getName() != null) {
					unitLabelLbl = new Label("unitLabel", cf.getUnitType().getName());
				}
				else {
					unitLabelLbl = new Label("unitLabel", "");
				}
				
				item.add(indexLbl);
				item.add(fieldLabelLbl);
				item.add(dataValueEntryPanel);
				item.add(unitLabelLbl);
			}
		};
		return studyCompDataView;
	}
}