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
package au.org.theark.study.web.component.calendar.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<StudyCalendarVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;
	private Study					study;

	private TextField<String>	calendarIdTxtFld;
	private TextField<String>	calendarNameTxtFld;
	private TextArea<String>	calendarDescription;
	private DropDownChoice<StudyComp> calendarStudyCompDDL;
	private DateTextField studyCalendarStartDateFld;
	private DateTextField studyCalendarEndDateFld;
	private CheckBox overLappingBooking;
	
	
	private Palette<CustomField>	customFieldPalette;

	private FeedbackPanel		feedBackPanel;

//	/**
//	 * 
//	 * @param id
//	 * @param feedBackPanel
//	 * @param arkCrudContainerVO
//	 * @param containerForm
//	 */
//	public DetailForm(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<StudyCalendarVo> cpModel, ArkCrudContainerVO arkCrudContainerVO) {
//
//		super(id, feedBackPanel,cpModel, arkCrudContainerVO);
//		this.feedBackPanel = feedBackPanel;
//	}
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id, feedBackPanel,containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}	

	public void onBeforeRender() {
		// Disable overlapping if it has aleady been set
		boolean enabled = (isNew());
		overLappingBooking.setEnabled(enabled);
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {

		calendarIdTxtFld = new TextField<String>(Constants.STUDY_CALENDAR_ID);
		calendarIdTxtFld.setEnabled(false);
		calendarNameTxtFld = new TextField<String>(Constants.STUDY_CALENDAR_NAME);
		calendarNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		calendarDescription = new TextArea<String>(Constants.STUDY_CALENDAR_DESCRIPTION);
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(study);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		calendarStudyCompDDL = new DropDownChoice<StudyComp>(Constants.STUDY_CALENDAR_STUDY_COMP,studyCompList,defaultChoiceRenderer);
		calendarStudyCompDDL.setOutputMarkupId(true);
		
		studyCalendarStartDateFld =new DateTextField(Constants.STUDY_CALENDAR_START_DATE,new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(studyCalendarStartDateFld);
		studyCalendarStartDateFld.add(startDatePicker);
		
		studyCalendarEndDateFld =new DateTextField(Constants.STUDY_CALENDAR_END_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(studyCalendarEndDateFld);
		studyCalendarEndDateFld.add(endDatePicker);
		
		overLappingBooking = new CheckBox("studyCalendar.allowOverlapping");
		
		initCustomFieldPalette();
		
		addDetailFormComponents();
		attachValidators();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(calendarIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(calendarNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(calendarDescription);
		arkCrudContainerVO.getDetailPanelFormContainer().add(calendarStudyCompDDL);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyCalendarStartDateFld);		
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyCalendarEndDateFld);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(overLappingBooking);
		
	}
	
	private void initCustomFieldPalette() {
		
		CompoundPropertyModel<StudyCalendarVo> searchCPM = (CompoundPropertyModel<StudyCalendarVo>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<Collection<CustomField>> selectedCustomFieldsPm = new PropertyModel<Collection<CustomField>>(searchCPM, "selectedCustomFields");// "selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Collection<CustomField> availableCustomFields = iStudyService.getStudySubjectCustomFieldList(studyId);
		containerForm.getModelObject().setAvailableCustomFields(availableCustomFields);

		PropertyModel<Collection<CustomField>> availableCustomFieldsPm = new PropertyModel<Collection<CustomField>>(searchCPM, "availableCustomFields");
		customFieldPalette = new ArkPalette("selectedCustomFields", selectedCustomFieldsPm, availableCustomFieldsPm, renderer, au.org.theark.core.Constants.PALETTE_ROWS, false);
		customFieldPalette.setOutputMarkupId(true);
		
		
		
		
//		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
//		List<CustomField> selectedCustomFieldList = cpModel.getObject().getSelectedCustomFields();
//
//		PropertyModel<Collection<CustomField>> availablePm = new PropertyModel<Collection<CustomField>>(cpModel, "availableCustomFields");
//		Collections.sort((List<CustomField>)availablePm.getObject(), new Comparator<CustomField>(){
//			public int compare(CustomField arg0, CustomField arg1) {
//				return arg0.getId().compareTo(arg1.getId());
//			}
//		});
//		customFieldPalette = new ArkPalette("selectedCustomFields", new ListModel(selectedCustomFieldList), availablePm, renderer, au.org.theark.core.Constants.PALETTE_ROWS, false);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		
		calendarNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.calendar.name.required", calendarNameTxtFld, new Model<String>("Study Calendar Name")));
		calendarDescription.setRequired(true).setLabel(new StringResourceModel("error.study.calendar.description.required", calendarDescription, new Model<String>("Study Calendar Description")));
		studyCalendarStartDateFld.setRequired(true).setLabel(new StringResourceModel("error.study.calendar.startdate.required", studyCalendarStartDateFld, new Model<String>("Study Calendar Start Date")));
		studyCalendarEndDateFld.setRequired(true).setLabel(new StringResourceModel("error.study.calendar.enddate.required", studyCalendarEndDateFld, new Model<String>("Study Calendar End Date")));
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {

		StudyCalendarVo studyCalendarVo = new StudyCalendarVo();
		containerForm.setModelObject(studyCalendarVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<StudyCalendarVo> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		

			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			study = iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getStudyCalendar().setStudy(study);

			if (containerForm.getModelObject().getStudyCalendar().getId() == null) {

				iStudyService.saveOrUpdate(containerForm.getModelObject());
				this.saveInformation();
				//this.info("Study Calendar " + containerForm.getModelObject().getStudyCalendar().getName() + " was created successfully");
				processErrors(target);

			}
			else {

				iStudyService.saveOrUpdate(containerForm.getModelObject());
				this.updateInformation();
				//this.info("Study Calendar " + containerForm.getModelObject().getStudyCalendar().getName() + " was updated successfully");
				processErrors(target);

			}

			onSavePostProcess(target);

		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {	
			iStudyService.delete(containerForm.getModelObject().getStudyCalendar());
			StudyCalendarVo studyCalendarVo = new StudyCalendarVo();
			containerForm.setModelObject(studyCalendarVo);
			//containerForm.info("The Study Component was deleted successfully.");
			this.deleteInformation();
			editCancelProcess(target);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getStudyCalendar().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

}
