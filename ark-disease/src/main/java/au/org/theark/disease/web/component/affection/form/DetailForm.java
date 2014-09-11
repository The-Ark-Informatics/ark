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
package au.org.theark.disease.web.component.affection.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionCustomDataVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.web.component.affection.AffectionCustomDataDataViewPanel;

public class DetailForm extends AbstractDetailForm<AffectionVO> {
	static Logger log = LoggerFactory.getLogger(DetailForm.class);

	private static final long								serialVersionUID	= -9196914684971413116L;

	private WebMarkupContainer	arkContextMarkupContainer;

	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	private Long sessionStudyId;
	
	private DropDownChoice<Disease> diseaseDDC;
	private DropDownChoice<AffectionStatus> affectionStatusDDC;
	private DateTextField recordDateTxtFld;
	private AbstractCustomDataEditorForm<AffectionCustomDataVO> customFieldForm;
	private AffectionCustomDataDataViewPanel dataViewPanel;
	
	private Study study;
	private LinkSubjectStudy lss;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			lss = iArkCommonService.getSubject(sessionPersonId, study);
		}
		catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		containerForm.getModelObject().getAffection().setLinkSubjectStudy(lss);
		containerForm.getModelObject().getAffection().setStudy(study);
		log.info("Constructor: " + containerForm.getModelObject().getAffection());
		this.cpModel = new CompoundPropertyModel<AffectionVO>(containerForm.getModel());
	}

	@Override
	public void onBeforeRender() {
		log.info("onBeforeRender: " + containerForm.getModelObject().getAffection());
//		initDiseaseDDC();
//		initAffectionStatusDDC();
		List<AffectionCustomFieldData> acfdset = iArkDiseaseService.getAffectionCustomFieldData(containerForm.getModelObject().getAffection());
		AffectionCustomDataVO acdvo = new AffectionCustomDataVO(acfdset);
		customFieldForm.setModelObject(acdvo);
//		iArkDiseaseService.save(containerForm.getModelObject().getAffection());
		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {
		
		initDiseaseDDC();
		initAffectionStatusDDC();
		
		PropertyModel<Date> recordDateModel = new PropertyModel<Date>(containerForm.getModel(), "affection.recordDate");
		recordDateTxtFld = new DateTextField("recordDate", recordDateModel){
			@Override
			protected void onBeforeRender() {
				this.setModel(new PropertyModel<Date>(containerForm.getModel(), "affection.recordDate"));
				super.onBeforeRender();
			}
		};
		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);
		
		AffectionCustomDataVO vo = new AffectionCustomDataVO();
		List<AffectionCustomFieldData> l = new ArrayList<AffectionCustomFieldData>(cpModel.getObject().getAffection().getAffectionCustomFieldDataSets());
		vo.setCustomFieldDataList(l);
		log.info("vo: " + vo.getCustomFieldDataList());
		log.info(cpModel.getObject().getAffection().toString());
		
		final CompoundPropertyModel<AffectionCustomDataVO> affectionCustomDataModel = new CompoundPropertyModel<AffectionCustomDataVO>(vo);
		dataViewPanel = new AffectionCustomDataDataViewPanel("dataViewPanel", affectionCustomDataModel).initialisePanel(iArkCommonService.getCustomFieldsPerPage());
		customFieldForm = new AbstractCustomDataEditorForm<AffectionCustomDataVO>("customFieldForm", affectionCustomDataModel, feedBackPanel) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onEditSave(AjaxRequestTarget target, Form<?> form) {
				//TODO:to finish implementing
				log.info("oneditsave called");
				for(AffectionCustomFieldData acfd : this.cpModel.getObject().getCustomFieldDataList()) {
					log.info("" + acfd);
					iArkDiseaseService.save(acfd);
				}
			}
			
			@Override
			public void onBeforeRender() {
				if(!isNew()) {
					this.setModelObject(new AffectionCustomDataVO(iArkDiseaseService.getAffectionCustomFieldData(containerForm.getModelObject().getAffection())));
					log.info("affcustview obr: " + this.getModelObject().getCustomFieldDataList());
				}
				this.buttonsPanelWMC.setVisible(false);
				super.onBeforeRender();
			}
		}.initialiseForm();
		
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataViewPanel.getDataView()) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(customFieldForm.getDataViewWMC());
				target.add(this);
			}
		};
		pageNavigator.setOutputMarkupId(true);
		customFieldForm.getDataViewWMC().add(dataViewPanel);
//		this.add(customFieldForm);
//		this.add(pageNavigator);
		arkCrudContainerVO.getDetailPanelFormContainer().add(pageNavigator);
		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	private void initAffectionStatusDDC() {
		CompoundPropertyModel<AffectionVO> affectionCPM = (CompoundPropertyModel<AffectionVO>) containerForm.getModel();
		PropertyModel<Affection> affectionPM = new PropertyModel<Affection>(affectionCPM, "affection");
		PropertyModel<AffectionStatus> affectionStatusPM = new PropertyModel<AffectionStatus>(affectionPM, "affectionStatus");
		ChoiceRenderer affectionStatusRenderer = new ChoiceRenderer("name", "id");
		List<AffectionStatus> possibleAffectionStatus = iArkDiseaseService.getAffectionStatus();
		affectionStatusDDC = new DropDownChoice<AffectionStatus>("affection.affectionStatus", affectionStatusPM, possibleAffectionStatus, affectionStatusRenderer){
			@Override
			protected void onBeforeRender() {
				if(!isNew()) {
					log.info("affection status ddc: " + !isNew() + " " + containerForm.getModelObject().getAffection().getAffectionStatus().getName()); 
					this.setModelObject(containerForm.getModelObject().getAffection().getAffectionStatus());
					log.info("after attempt: " + this.getModelObject().getName());
				} else {
					this.setModel(null);
					log.info("affection status dds isnew: " + this.getModelObject() + " " + this.getModel());
				}
				super.onBeforeRender();
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void initDiseaseDDC() {
		CompoundPropertyModel<AffectionVO> affectionCpm = (CompoundPropertyModel<AffectionVO>) containerForm.getModel();
		PropertyModel<Affection> affectionPm = new PropertyModel<Affection>(affectionCpm, "affection");
		PropertyModel<Disease> diseasePm = new PropertyModel<Disease>(affectionPm, "disease");
		log.info("getAvailableDiseasesForStudy before");
		Collection<Disease> diseases = iArkDiseaseService.getAvailableDiseasesForStudy(iArkCommonService.getStudy(sessionStudyId)); 
		log.info("getAvailableDiseasesForStudy after");
		ChoiceRenderer diseaseRenderer = new ChoiceRenderer("name", "id");
		diseaseDDC = new DropDownChoice<Disease>("affection.disease", diseasePm, (List) diseases, diseaseRenderer){
			@Override
			protected void onBeforeRender() {
				if(!isNew()) {
					log.info("disease ddc: " + !isNew() + " " + containerForm.getModelObject().getAffection().getDisease().getName());
					setModelObject(containerForm.getModelObject().getAffection().getDisease());
					this.setEnabled(false);
				} else {
					this.setEnabled(true);
					this.setModel(null);
					log.info("disease dds isnew: " + this.getModelObject() + " " + this.getModel());
				}
				super.onBeforeRender();
			}
		};
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(diseaseDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(affectionStatusDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(recordDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(customFieldForm);
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

	protected void onCancel(AjaxRequestTarget target) {
		AffectionVO vo = new AffectionVO();
		vo.getAffection().setLinkSubjectStudy(lss);
		vo.getAffection().setStudy(study);
		containerForm.setModelObject(vo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		recordDateTxtFld.setRequired(true);
		diseaseDDC.setRequired(true);
		affectionStatusDDC.setRequired(true);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<AffectionVO> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId == null) {
			// No study in context
			this.error("There is no study in Context. Please select a study to manage diseases.");
			processErrors(target);
		}
		else {
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
			log.info("to save:");
			if(isNew()) {
				iArkDiseaseService.save(containerForm.getModelObject().getAffection());
			} else {
				iArkDiseaseService.update(containerForm.getModelObject().getAffection());
			}
			for(AffectionCustomFieldData acfd : customFieldForm.getModelObject().getCustomFieldDataList()) {
				acfd.setAffection(containerForm.getModelObject().getAffection());
			}
			customFieldForm.onEditSave(target, containerForm);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		log.info("DELETE HIT ");
		editCancelProcess(target);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		return containerForm.getModelObject().getAffection() == null || containerForm.getModelObject().getAffection().getId() == null;
	}
}
