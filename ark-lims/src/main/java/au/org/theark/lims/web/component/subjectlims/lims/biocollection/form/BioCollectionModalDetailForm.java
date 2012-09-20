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
package au.org.theark.lims.web.component.subjectlims.lims.biocollection.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.BioCollectionCustomDataVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biocollectioncustomdata.BioCollectionCustomDataDataViewPanel;
import au.org.theark.lims.web.component.button.NumberOfLabelsPanel;
import au.org.theark.lims.web.component.button.zebra.biocollection.PrintBioCollectionLabelButton;
import au.org.theark.lims.web.component.button.zebra.biocollection.PrintBiospecimensForBioCollectionButton;

/**
 * @author cellis
 * 
 */
public class BioCollectionModalDetailForm extends AbstractModalDetailForm<LimsVO> {

	private static final long			serialVersionUID	= 2926069852602563767L;
	private static final Logger		log					= LoggerFactory.getLogger(BioCollectionModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextArea<String>			commentsTxtAreaFld;
	private DateTextField				collectionDateTxtFld;
	private DateTextField				surgeryDateTxtFld;
	private ModalWindow					modalWindow;
	private Panel 							bioCollectionCFDataEntryPanel;
	private AjaxButton					printBioCollectionLabelButton;
	private AjaxButton					printBiospecimensForBioCollectionButton;
	
	protected NumberOfLabelsPanel numberOfLabels;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param containerForm
	 * @param detailPanelContainer
	 */
	public BioCollectionModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
	}

	protected void refreshEntityFromBackend() {
		// Get BioCollection entity fresh from backend
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		if (bioCollection.getId() != null) {
			try {
				cpModel.getObject().setBioCollection(iLimsService.getBioCollection(bioCollection.getId()));
			}
			catch (EntityNotFoundException e) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				log.error(e.getMessage());
			}
		}		
	}


	public void onBeforeRender(){
		Study study = this.getModelObject().getBioCollection().getStudy();
		
		if(study!=null && !study.getAutoGenerateBiocollectionUid()){
			nameTxtFld.setEnabled(true);	
		}
		else{
			nameTxtFld.setEnabled(false);
		}
			super.onBeforeRender();
	}
	
	private boolean initialiseBioCollectionCFDataEntry() {
		boolean replacePanel = false;
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		if (!(bioCollectionCFDataEntryPanel instanceof BioCollectionCustomDataDataViewPanel)) {
			CompoundPropertyModel<BioCollectionCustomDataVO> bioCFDataCpModel = new CompoundPropertyModel<BioCollectionCustomDataVO>(new BioCollectionCustomDataVO());		
			bioCFDataCpModel.getObject().setBioCollection(bioCollection);
			bioCFDataCpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION));
			bioCollectionCFDataEntryPanel = new BioCollectionCustomDataDataViewPanel("bioCollectionCFDataEntryPanel", bioCFDataCpModel).initialisePanel(null);
			replacePanel = true;
		}
		return replacePanel;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		
		// bioCollection.name auto-generated, this read only
		nameTxtFld = new TextField<String>("bioCollection.name");
		nameTxtFld.setEnabled(false);
		
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		initialiseBioCollectionCFDataEntry();
		
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		
		numberOfLabels = new NumberOfLabelsPanel("numberOfLabels");
		
		printBioCollectionLabelButton = new PrintBioCollectionLabelButton("printBioCollectionLabel", bioCollection, (IModel<Number>) numberOfLabels.getDefaultModel()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		printBioCollectionLabelButton.setDefaultFormProcessing(false);
		
		printBiospecimensForBioCollectionButton = new PrintBiospecimensForBioCollectionButton("printBiospecimensForBioCollectionButton", bioCollection) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		printBioCollectionLabelButton.setDefaultFormProcessing(false);

		attachValidators();
		addComponents();
		
		// Focus on Collection Date
		collectionDateTxtFld.add(new ArkDefaultFormFocusBehavior());
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioCollection.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(collectionDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(surgeryDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioCollectionCFDataEntryPanel);
		
		add(numberOfLabels);
		add(printBioCollectionLabelButton);
		add(printBiospecimensForBioCollectionButton);
		
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}


	@Override
	protected void onSave(AjaxRequestTarget target) {

		try {
			if (cpModel.getObject().getBioCollection().getId() == null) {
				// Save
				
				iLimsService.createBioCollection(cpModel.getObject());
				
				this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was created successfully");
				
			}
			else {
				// Update
				iLimsService.updateBioCollection(cpModel.getObject());
				this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was updated successfully");
				
			}
			if (bioCollectionCFDataEntryPanel instanceof BioCollectionCustomDataDataViewPanel) {
				((BioCollectionCustomDataDataViewPanel) bioCollectionCFDataEntryPanel).saveCustomData();
			}
			// refresh the CF data entry panel (if necessary)
			if (initialiseBioCollectionCFDataEntry() == true) {
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioCollectionCFDataEntryPanel);
			}
			
			if (target != null) {
				onSavePostProcess(target);
			}
		} catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		
		if (target != null) {
			processErrors(target);
		}
		
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		if (!iLimsService.hasBiospecimens(cpModel.getObject().getBioCollection())) {

			iLimsService.deleteBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was deleted successfully");
			onClose(target);
		}
		else {
			this.error("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " can not be deleted because there are biospecimens attached");
			target.add(feedbackPanel);
		}
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getBioCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
