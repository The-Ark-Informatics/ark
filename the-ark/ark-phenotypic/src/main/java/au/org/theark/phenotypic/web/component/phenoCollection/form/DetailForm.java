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
package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO> {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 8027046304220525464L;

	private transient Logger			log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private DropDownChoice<Status>	statusDdc;
	private TextArea<String>			descriptionTxtAreaFld;
	private DateTextField				startDateTxtFld;
	private DateTextField				endDateTxtFld;

	@SuppressWarnings("unchecked")
	private Palette						fieldsInCollectionPalette;
	private AjaxButton					clearCollectionButton;

	/**
	 * Constructor
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
	}

	@SuppressWarnings("unchecked")
	private void initStatusDdc() {
		java.util.Collection<Status> statusCollection = iPhenotypicService.getStatus();
		ChoiceRenderer<Status> statusRenderer = new ChoiceRenderer<Status>(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS, (List) statusCollection, statusRenderer);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID);
		nameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME);
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
		descriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION);
		startDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		endDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE, au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(startDateTxtFld);
		startDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(endDateTxtFld);
		endDateTxtFld.add(endDatePicker);

		clearCollectionButton = new AjaxDeleteButton("clearCollection", new StringResourceModel("button.clearCollection.confirm", this, null), new StringResourceModel("button.clearCollection", this,
				null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				int rowsDeleted = iPhenotypicService.clearPhenoCollection(containerForm.getModelObject().getPhenoCollection());
				this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was cleared successfully.");
				this.info(rowsDeleted + " field data rows deleted.");
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				return (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.DELETE));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when clearCollectionButton pressed");
			}
		};

		// Initialise Drop Down Choices
		initStatusDdc();

		// Initialise Field Palette
		initFieldPalette();

		attachValidators();
		addDetailFormComponents();
	}

	@SuppressWarnings("unchecked")
	private void initFieldPalette() {
		CompoundPropertyModel<PhenoCollectionVO> cpm = (CompoundPropertyModel<PhenoCollectionVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "id");
		PropertyModel<Collection<Field>> selectedModPm = new PropertyModel<Collection<Field>>(cpm, "fieldsSelected");
		PropertyModel<Collection<Field>> availableModPm = new PropertyModel<Collection<Field>>(cpm, "fieldsAvailable");

		fieldsInCollectionPalette = new ArkPalette(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_FIELD_PALETTE, selectedModPm, availableModPm, renderer, au.org.theark.core.Constants.ROWS_PER_PAGE, false) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected org.apache.wicket.request.resource.ResourceReference getCSS() {
				return null;
			}

			// TODO: ARK-177 implement disabling of selected fields if fieldData exist
		};
	}

	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.name.required", this, new Model<String>("Name")));
		statusDdc.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.status.required", this, new Model<String>("Status")));
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target) {

		if (containerForm.getModelObject().getPhenoCollection().getId() == null) {
			// Save
			iPhenotypicService.createCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iPhenotypicService.updateCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVo);

		java.util.Collection<PhenoCollection> phenoCollectionCollection = iPhenotypicService.searchPhenotypicCollection(phenoCollectionVo.getPhenoCollection());
		containerForm.getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iPhenotypicService.deleteCollection(containerForm.getModelObject());
		this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedBackPanel);
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
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
		if (containerForm.getModelObject().getPhenoCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	public AjaxButton getClearCollectionButton() {
		return clearCollectionButton;
	}

	public void setClearCollectionButton(AjaxButton clearCollectionButton) {
		this.clearCollectionButton = clearCollectionButton;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtAreaFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(statusDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(startDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(endDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldsInCollectionPalette);

		// Custom clear collection button
		arkCrudContainerVO.getEditButtonContainer().add(clearCollectionButton);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
		
	}
}
