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
package au.org.theark.phenotypic.web.component.fieldData.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenotypicValidator;
import au.org.theark.phenotypic.web.component.fieldData.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO> {
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			fieldDataIdTxtFld;
	private TextField<String>			fieldDataCollectionTxtFld;
	private TextField<String>			fieldDataSubjectUidTxtFld;
	private DateTextField				fieldDataDateCollectedDteFld;
	private TextField<String>			fieldDataFieldTxtFld;
	private TextField<String>			fieldDataValueTxtFld;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailPanel
	 * @param listContainer
	 * @param detailsContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailFormContainer
	 * @param searchPanelContainer
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer, ContainerForm containerForm,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailFormContainer, WebMarkupContainer searchPanelContainer) {

		super(id, feedBackPanel, listContainer, detailsContainer, detailFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);
	}

	public void initialiseDetailForm() {
		fieldDataIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_ID);
		fieldDataCollectionTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION_NAME);
		fieldDataSubjectUidTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID);
		fieldDataDateCollectedDteFld = new DateTextField(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
		fieldDataFieldTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD_NAME);
		fieldDataValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE);
		fieldDataValueTxtFld.add(new ArkDefaultFormFocusBehavior());

		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(fieldDataDateCollectedDteFld);
		fieldDataDateCollectedDteFld.add(datePicker);

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
	}

	private void addComponents() {
		detailPanelFormContainer.add(fieldDataIdTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataCollectionTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataSubjectUidTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataDateCollectedDteFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataFieldTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataValueTxtFld);

		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getFieldData().getId() == null) {
			// Save the Field data
			iPhenotypicService.createFieldData(containerForm.getModelObject().getFieldData());
			this.info("Field Data " + containerForm.getModelObject().getFieldData().getId() + " was created successfully");

			onSavePostProcess(target);
		}
		else {
			// Validate field data confirms to data dictionary
			Collection<String> dataValidation = new ArrayList<String>();
			boolean passedQualityControl = PhenotypicValidator.fieldDataPassesQualityControl(containerForm.getModelObject().getFieldData(), dataValidation);
			containerForm.getModelObject().getFieldData().setPassedQualityControl(passedQualityControl);

			if (!passedQualityControl) {
				if (dataValidation != null) {
					for (Iterator<String> iterator = dataValidation.iterator(); iterator.hasNext();) {
						String errorMessage = (String) iterator.next();
						this.error(errorMessage);
					}
				}
			}
			else {
				// Update the Field data
				iPhenotypicService.updateFieldData(containerForm.getModelObject().getFieldData());
				this.info("Field Data " + containerForm.getModelObject().getFieldData().getId() + " was updated successfully");
				onSavePostProcess(target);
			}
		}
		processErrors(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		// Force refresh of search results
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		FieldData fieldData = new FieldData();
		containerForm.setModelObject(phenoCollectionVo);
		containerForm.getModelObject().setStudy(study);
		containerForm.getModelObject().setFieldData(fieldData);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	/**
	 * 
	 */
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow) {
		// TODO:(CE) To handle Business and System Exceptions here
		iPhenotypicService.deleteFieldData(containerForm.getModelObject().getFieldData());
		this.info("Field data " + containerForm.getModelObject().getFieldData().getId() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

		// Close the confirm modal window
		selectModalWindow.close(target);

		// Force refresh of search results
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		FieldData fieldData = new FieldData();

		containerForm.getModelObject().setStudy(study);
		containerForm.getModelObject().setFieldData(fieldData);
		containerForm.setModelObject(phenoCollectionVo);

		editCancelProcess(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getFieldData().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
}
