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
package au.org.theark.lims.web.component.biospecimen.batchcreate.form;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.lims.model.vo.BatchBiospecimenVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class BatchCreateBiospecimenForm extends Form<BatchBiospecimenVO> {

	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(BatchCreateBiospecimenForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService										iLimsService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService								iInventoryService;

	protected CompoundPropertyModel<LimsVO>			cpModel;
	protected FeedbackPanel								feedbackPanel;
	private AbstractListEditor<BatchBiospecimenVO>	listEditor;
	
	private TextField<Number>								numberToCreateTxtFld;
	private DropDownChoice<BioCollection>				bioCollectionDdc;
	private DropDownChoice<BioSampletype>				sampleTypeDdc;
	private DateTextField									sampleDateTxtFld;
	private DateTimeField									sampleTimeTxtFld;
	private TextField<Double>								quantityTxtFld;
	private DropDownChoice<Unit>							unitDdc;
	private DropDownChoice<TreatmentType>				treatmentTypeDdc;

	public BatchCreateBiospecimenForm(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<LimsVO> cpModel, IModel<BatchBiospecimenVO> model) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
	}

	public void initialiseForm() {
		add(buildListEditor());
		add(new AjaxEditorButton(Constants.NEW) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				BatchBiospecimenVO item = new BatchBiospecimenVO();
				item.getBiospecimen().setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
				item.getBiospecimen().setStudy(cpModel.getObject().getStudy());
				listEditor.addItem(item);
			}
		}.setDefaultFormProcessing(false));
		
		add(new AjaxButton(Constants.SAVE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				info("Batch biospecimens saved");
				target.add(feedbackPanel);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
	}

	@Override
	public void onBeforeRender() {
		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				// Successfully loaded from backend
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setStudy(study);
			}
		}

		super.onBeforeRender();
	}

	/**
	 * 
	 * @return the listEditor of BatchBiospecimen(s)
	 */
	public AbstractListEditor<BatchBiospecimenVO> buildListEditor() {
		listEditor = new AbstractListEditor<BatchBiospecimenVO>("batchBiospecimenList", new PropertyModel(this, "batchBiospecimenVO.batchBiospecimenList")) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPopulateItem(final ListItem<BatchBiospecimenVO> item) {
				item.setOutputMarkupId(true);

				numberToCreateTxtFld = new TextField<Number>("numberToCreate");
				
				initBioCollectionDdc();
				initSampleTypeDdc();

				sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);
				ArkDatePicker sampleDatePicker = new ArkDatePicker();
				sampleDatePicker.bind(sampleDateTxtFld);
				sampleDateTxtFld.add(sampleDatePicker);

				sampleTimeTxtFld = new DateTimeField("biospecimen.sampleTime") {

					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onBeforeRender() {
						this.getDateTextField().setVisibilityAllowed(false);
						super.onBeforeRender();
					}

					@Override
					protected void convertInput() {
						// Slight change to not default to today's date
						Date modelObject = (Date) getDefaultModelObject();
						getDateTextField().setConvertedInput(modelObject != null ? modelObject : null);
						super.convertInput();
					}
				};

				quantityTxtFld = new TextField<Double>("biospecimen.quantity") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public <C> IConverter<C> getConverter(Class<C> type) {
						DoubleConverter doubleConverter = new DoubleConverter();
						NumberFormat numberFormat = NumberFormat.getInstance();
						numberFormat.setMinimumFractionDigits(1);
						doubleConverter.setNumberFormat(getLocale(), numberFormat);
						return (IConverter<C>) doubleConverter;
					}
				};
				initTreatmentTypeDdc();
				initUnitDdc();

				item.add(numberToCreateTxtFld);
				item.add(bioCollectionDdc);
				item.add(sampleTypeDdc);
				item.add(sampleDateTxtFld);
				item.add(sampleTimeTxtFld);
				item.add(quantityTxtFld);
				item.add(treatmentTypeDdc);
				item.add(unitDdc);

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}

		};
		return listEditor;
	}

	private void initBioCollectionDdc() {
		// Get a list of collections for the subject in context by default
		BioCollection bioCollection = new BioCollection();
		bioCollection.setLinkSubjectStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy());
		bioCollection.setStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy().getStudy());
		try {
			cpModel.getObject().setBioCollectionList(iLimsService.searchBioCollection(bioCollection));

			ChoiceRenderer<BioCollection> choiceRenderer = new ChoiceRenderer<BioCollection>(Constants.NAME, Constants.ID);
			bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", cpModel.getObject().getBioCollectionList(), choiceRenderer);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
			this.error("Operation could not be performed - if this persists, contact your Administrator or Support");
		}
	}

	private void initSampleTypeDdc() {
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> choiceRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", (List<BioSampletype>) sampleTypeList, choiceRenderer);
	}

	private void initTreatmentTypeDdc() {
		List<TreatmentType> treatmentTypeList = iLimsService.getTreatmentTypes();
		ChoiceRenderer<TreatmentType> choiceRenderer = new ChoiceRenderer<TreatmentType>(Constants.NAME, Constants.ID);
		treatmentTypeDdc = new DropDownChoice<TreatmentType>("biospecimen.treatmentType", (List<TreatmentType>) treatmentTypeList, choiceRenderer);
		treatmentTypeDdc.setNullValid(false);
	}

	private void initUnitDdc() {
		List<Unit> unitList = iLimsService.getUnits();
		ChoiceRenderer<Unit> choiceRenderer = new ChoiceRenderer<Unit>(Constants.NAME, Constants.ID);
		unitDdc = new DropDownChoice<Unit>("biospecimen.unit", (List<Unit>) unitList, choiceRenderer);
		unitDdc.setNullValid(false);
	}
}