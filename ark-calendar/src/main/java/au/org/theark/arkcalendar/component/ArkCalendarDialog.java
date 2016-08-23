package au.org.theark.arkcalendar.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDateTime;

import au.org.theark.arkcalendar.component.dataentry.CheckGroupDataEntryPanel;
import au.org.theark.arkcalendar.component.dataentry.DateDataEntryPanel;
import au.org.theark.arkcalendar.component.dataentry.DropDownChoiceDataEntryPanel;
import au.org.theark.arkcalendar.component.dataentry.EncodedValueVO;
import au.org.theark.arkcalendar.component.dataentry.NumberDataEntryPanel;
import au.org.theark.arkcalendar.component.dataentry.TextDataEntryPanel;
import au.org.theark.arkcalendar.dao.ArkCalendarDao;
import au.org.theark.arkcalendar.data.ArkCalendarEvent;
import au.org.theark.arkcalendar.data.ArkCalendarEvent.Category;
import au.org.theark.arkcalendar.data.CalendarCustomFieldData;
import au.org.theark.arkcalendar.util.Constants;
import au.org.theark.arkcalendar.util.DateDependencyInputValidator;
import au.org.theark.arkcalendar.util.SignIn2Session;

import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.kendo.ui.form.datetime.local.DateTimePicker;

public abstract class ArkCalendarDialog extends AbstractFormDialog<ArkCalendarEvent>
{
	private static final long serialVersionUID = 1L;
	
	protected final DialogButton btnSubmit = new DialogButton(SUBMIT, Model.of("Save"));
	
	protected final DialogButton btnDelete = new DialogButton("DELETE", Model.of("Delete"));
	
	protected final DialogButton btnClose = new DialogButton(CLOSE, Model.of("Close"));
	
	private Logger log = LoggerFactory.getLogger(ArkCalendarDialog.class);
	
	protected CompoundPropertyModel<ArkCalendarEvent> cpModel;
	
	protected RequiredTextField<String> subjectUidTextField;
	
	protected DateTimePicker startDateTimePicker;
	
	protected DateTimePicker endDateTimePicker;
	
	protected RepeatingView repeatingView;
	
		
	static IModel<ArkCalendarEvent> emptyModel()
	{	
		return Model.of(new ArkCalendarEvent(0, "", Category.PUBLIC, LocalDateTime.now()));
	}

	private Form<?> form;
	private FeedbackPanel feedback;

	public ArkCalendarDialog(String id, String title)
	{
		super(id, title, emptyModel(), true);
		
		SignIn2Session session =(SignIn2Session)getSession();
		
		int calendarId = (int)getSession().getAttribute("calendarId");
		String user = session.getUser();
		
		
		this.cpModel = new CompoundPropertyModel<ArkCalendarEvent>(this.getModel());

		this.form = new Form<ArkCalendarEvent>("form", cpModel);
		this.add(this.form);

		this.form.add(new RequiredTextField<String>("title"));
		
		this.subjectUidTextField = new RequiredTextField<String>("subjectUID");
		
		this.subjectUidTextField.add(new IValidator<String>() {
			
			@Override
			public void validate(IValidatable<String> validatable) {
				String uid = validatable.getValue();
				int calendarId = (int)getSession().getAttribute("calendarId");
				
				if(!ArkCalendarDao.isSubjectUIDExists(calendarId,uid)){
					error(validatable, "invalidUID");
				}
			}
			
			private void error(IValidatable<String> validatable, String errorKey) {
				ValidationError error = new ValidationError();
				error.addKey(getClass().getSimpleName() + "." + errorKey);
				error.setMessage("Subject UID is not exist in the study");
				validatable.error(error);
			}
		
		});
		
		this.form.add(subjectUidTextField);
//		this.form.add(new RadioChoice<Category>("category", Arrays.asList(Category.values())));

		// DateTimePickers //
		startDateTimePicker = new DateTimePicker("start");
		
		startDateTimePicker.setRequired(true);
		
		
		endDateTimePicker = new DateTimePicker("end");
		
		endDateTimePicker.setRequired(true);

		this.form.add(startDateTimePicker);
		this.form.add(endDateTimePicker);
		
		this.form.add(new DateDependencyInputValidator(startDateTimePicker, endDateTimePicker, false));

		// All-day checkbox //
		CheckBox checkAllDay = new AjaxCheckBox("allDay") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure()
			{
				super.onConfigure();

				Boolean allday = this.getModelObject();
				startDateTimePicker.setTimePickerEnabled(!allday);
				endDateTimePicker.setTimePickerEnabled(!allday);
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				Boolean allday = this.getModelObject();
				startDateTimePicker.setTimePickerEnabled(target, !allday);
				endDateTimePicker.setTimePickerEnabled(target, !allday);
			}
		};

		this.form.add(checkAllDay.setOutputMarkupId(true));
		this.form.add(new Label("label", "All day?").add(AttributeModifier.append("for", checkAllDay.getMarkupId())));
		
		
		
		
		initCalendarDialog();
		
		// FeedbackPanel //
		this.feedback = new JQueryFeedbackPanel("feedback");
		this.form.add(this.feedback);
		
		if("arksuperuser@ark.org.au".equals(user)){
			btnSubmit.setEnabled(true);
			btnDelete.setEnabled(true);
			btnClose.setEnabled(true);
			form.setEnabled(true);
		}
		else{
			String role= ArkCalendarDao.getUserRole(user, calendarId);
			if("Calendar Administrator".equals(role)){
				btnSubmit.setEnabled(true);
				btnDelete.setEnabled(true);
				form.setEnabled(true);
			}else if("Calendar Data Manager".equals(role)){
				btnSubmit.setEnabled(true);
				btnDelete.setEnabled(false);
				form.setEnabled(true);
			}else if("Calendar Read-Only User".equals(role)){
				btnSubmit.setEnabled(false);
				btnDelete.setEnabled(false);
				form.setEnabled(false);
			}else{
				btnSubmit.setEnabled(false);
				btnDelete.setEnabled(false);
				form.setEnabled(false);
			}
		}
	}
	
	public void initCalendarDialog(){
		
		if(cpModel.getObject().getSubjectUID() !=null){
			subjectUidTextField.setEnabled(false);
		}else{
			subjectUidTextField.setEnabled(true);
		}
		
		this.repeatingView = new RepeatingView("repeater");
		this.form.addOrReplace(repeatingView);		
		
		for(CalendarCustomFieldData field:cpModel.getObject().getCustomFieldData()){
			AbstractItem item = new AbstractItem(repeatingView.newChildId(), new CompoundPropertyModel<CalendarCustomFieldData>(field));			
			this.repeatingView.add(item);
			String labelModel = new String();
			if (field.getFieldLabel() != null) {
				labelModel = field.getFieldLabel();
			}
			else {
				// Defaults to name if no fieldLabel
				labelModel = field.getName();
			}
			Label fieldLabelLbl = new Label("fieldLabel", labelModel);
			
			item.add(fieldLabelLbl);
			item.add(populateItem(field));
		}

	}
	
	protected Component populateItem(CalendarCustomFieldData customFieldData) {
		
		// Determine label of component, also used for error messages
		String labelModel = new String();
		if (customFieldData.getFieldLabel() != null) {
			labelModel = customFieldData.getFieldLabel();
		}
		else {
			// Defaults to name if no fieldLabel
			labelModel = customFieldData.getName();
		}
//		Label fieldLabelLbl = new Label("fieldLabel", labelModel);
		
		Panel dataValueEntryPanel=null;
		String fieldTypeName = customFieldData.getFieldType();
		String encodedValues = customFieldData.getEncodedValues();
		Boolean requiredField = customFieldData.getRequired();
		if (fieldTypeName.equals(Constants.DATE_FIELD_TYPE_NAME)) {
			if(customFieldData.getDefaultValue() != null && customFieldData.getDateDataValue() == null) {
				try {
					customFieldData.setDateDataValue(new SimpleDateFormat(Constants.DD_MM_YYYY).parse(customFieldData.getDefaultValue()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DateDataEntryPanel dateDataEntryPanel = new DateDataEntryPanel("dataValueEntryPanel", 
														new PropertyModel<Date>(customFieldData, "dateDataValue"),
														new Model<String>(labelModel));
			dateDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(customFieldData, "errorDataValue"));
			dateDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(customFieldData, "unitType"));

			if (customFieldData.getMinValue() != null && !customFieldData.getMinValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date minDate = (Date) dateConverter.convertToObject(customFieldData.getMinValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.minimum(minDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					log.error("Unexpected error: customfield.minValue is not in the DD/MM/YYYY date format");
					this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
					getParent().setEnabled(false);
				}
			}
			if (customFieldData.getMaxValue() != null && !customFieldData.getMaxValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date maxDate = (Date) dateConverter.convertToObject(customFieldData.getMaxValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.maximum(maxDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					log.error("Unexpected error: customfield.maxValue is not in the DD/MM/YYYY date format");
					this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
					getParent().setEnabled(false);
				}
			}
			if (requiredField != null && requiredField == true) {
				dateDataEntryPanel.setRequired(true);
			}
			dataValueEntryPanel = dateDataEntryPanel;
		}
		else {//ie if its not a date...
			if (encodedValues != null && !encodedValues.isEmpty()) {
				// The presence of encodedValues means it should be a DropDownChoice
				List<String> encodeKeyValueList = Arrays.asList(encodedValues.split(";"));
				ArrayList<EncodedValueVO> choiceList = new ArrayList<EncodedValueVO>();
				for (String keyValue : encodeKeyValueList) {
					// Only split for the first instance of the '=' (allows the '=' character in the actual value)
					String[] keyValueArray = keyValue.split("=", 2);
					EncodedValueVO encodedValueVo = new EncodedValueVO();
					encodedValueVo.setKey(keyValueArray[0]);
					encodedValueVo.setValue(keyValueArray[1]);
					choiceList.add(encodedValueVo);
				}				
				

				ChoiceRenderer<EncodedValueVO> choiceRenderer = new ChoiceRenderer<EncodedValueVO>("value", "key");
				
 				if(customFieldData.getAllowMultiselect()){

 					CheckGroupDataEntryPanel cgdePanel = new CheckGroupDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(customFieldData, "textDataValue"), 
															new Model<String>(labelModel), choiceList, choiceRenderer); 
					
					cgdePanel.setErrorDataValueModel(new PropertyModel<String>(customFieldData, "errorDataValue"));
					cgdePanel.setUnitsLabelModel(new PropertyModel<String>(customFieldData, "unitType"));

					if (customFieldData.getMissingValue() != null && !customFieldData.getMissingValue().isEmpty()) {
						cgdePanel.setMissingValue(customFieldData.getMissingValue());
					}
					if (requiredField != null && requiredField == true) {
						cgdePanel.setRequired(true);
					}
					
					dataValueEntryPanel = cgdePanel;

				}
				else{
					if(customFieldData.getDefaultValue() != null && customFieldData.getTextDataValue() == null) {
						customFieldData.setTextDataValue(customFieldData.getDefaultValue());
					}
					DropDownChoiceDataEntryPanel ddcPanel = 
								new DropDownChoiceDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(customFieldData, "textDataValue"), 
																				new Model<String>(labelModel), choiceList, choiceRenderer);
					ddcPanel.setErrorDataValueModel(new PropertyModel<String>(customFieldData, "errorDataValue"));
					ddcPanel.setUnitsLabelModel(new PropertyModel<String>(customFieldData, "unitType"));
					
					if (customFieldData.getMissingValue() != null && !customFieldData.getMissingValue().isEmpty()) {
						ddcPanel.setMissingValue(customFieldData.getMissingValue());
					}
					if (requiredField != null && requiredField == true) {
						ddcPanel.setRequired(true);
					}
					dataValueEntryPanel = ddcPanel;
				}
			}
			else {
				if (fieldTypeName.equals(Constants.CHARACTER_FIELD_TYPE_NAME)) {
					// Text data
					
					if(customFieldData.getDefaultValue() != null && customFieldData.getTextDataValue() == null) {
						customFieldData.setTextDataValue(customFieldData.getDefaultValue());
					}
					TextDataEntryPanel textDataEntryPanel = new TextDataEntryPanel("dataValueEntryPanel", 
																										new PropertyModel<String>(customFieldData, "textDataValue"), 
																										new Model<String>(labelModel));
					textDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(customFieldData, "errorDataValue"));
					textDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(customFieldData, "unitType"));
					textDataEntryPanel.setTextFieldSize(40);
					
					if (requiredField != null && requiredField == true) {
						 textDataEntryPanel.setRequired(true);
					}
					dataValueEntryPanel = textDataEntryPanel;
				}
				else if (fieldTypeName.equals(Constants.NUMBER_FIELD_TYPE_NAME)) {
					// Number data
					if(customFieldData.getDefaultValue() != null && customFieldData.getNumberDataValue() == null) {
						customFieldData.setNumberDataValue(Double.parseDouble(customFieldData.getDefaultValue()));;
					}
					NumberDataEntryPanel numberDataEntryPanel = new NumberDataEntryPanel("dataValueEntryPanel", 
																						new PropertyModel<Double>(customFieldData, "numberDataValue"), 
																						new Model<String>(labelModel));
					numberDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(customFieldData, "errorDataValue"));
					numberDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(customFieldData, "unitType"));
										
					if (customFieldData.getMinValue() != null && !customFieldData.getMinValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double minNumber = (Double) doubleConverter.convertToObject(customFieldData.getMinValue(), getLocale());
							numberDataEntryPanel.addValidator(RangeValidator.minimum(minNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							log.error("Unexpected error: customfield.maxValue is not in a valid number format");
							this.error("An unexpected error occurred loading the field validators from database. Please contact your System Administrator.");
//							getParentContainer().setEnabled(false);
							getParent().setEnabled(false);
						}
					}
					if (customFieldData.getMaxValue() != null && !customFieldData.getMaxValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double maxNumber = (Double) doubleConverter.convertToObject(customFieldData.getMaxValue(), getLocale());
							numberDataEntryPanel.addValidator(RangeValidator.maximum(maxNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							log.error("Unexpected error: customfield.maxValue is not in a valid number format");
							this.error("An unexpected error occurred loading the field validators from database. Please contact your System Administrator.");
							getParent().setEnabled(false);
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
			
		return dataValueEntryPanel;
	}
	
	

	// AbstractFormDialog //
	@Override
	protected List<DialogButton> getButtons()
	{
		return Arrays.asList(this.btnSubmit, this.btnDelete, this.btnClose);
	}

	@Override
	public DialogButton getSubmitButton()
	{
		return this.btnSubmit;
	}

	@Override
	public Form<?> getForm()
	{
		return this.form;
	}

	// Events //
	@Override
	protected void onOpen(IPartialPageRequestHandler handler)
	{
		handler.add(this.form);
	}

	@Override
	public void onError(AjaxRequestTarget target)
	{
		target.add(this.feedback);
	}
}
