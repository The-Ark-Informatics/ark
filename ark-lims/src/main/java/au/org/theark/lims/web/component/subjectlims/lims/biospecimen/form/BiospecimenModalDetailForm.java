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
package au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.BioCollectionCustomDataVO;
import au.org.theark.lims.model.vo.BiospecimenCustomDataVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biospecimencustomdata.BiospecimenCustomDataDataViewPanel;

/**
 * @author elam
 * @author cellis
 * 
 */
public class BiospecimenModalDetailForm extends AbstractModalDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 2727419197330261916L;
	private static final Logger				log					= LoggerFactory.getLogger(BiospecimenModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	private TextField<String>					idTxtFld;
	private TextField<String>					biospecimenIdTxtFld;
	private TextArea<String>					commentsTxtAreaFld;
	private DateTextField						sampleDateTxtFld;
	private DropDownChoice<BioSampletype>	sampleTypeDdc;
	private DropDownChoice<BioCollection>	bioCollectionDdc;
	private TextField<String>					quantityTxtFld;
	private CheckBox								barcodedChkBox;

	private Panel 							biospecimenCFDataEntryPanel;
	private ModalWindow							modalWindow;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param listDetailPanel
	 */
	public BiospecimenModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
	}
	
	protected void refreshEntityFromBackend() {
		// Get the Biospecimen entity fresh from backend
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (biospecimen.getId() != null) {
			try {
				cpModel.getObject().setBiospecimen(iLimsService.getBiospecimen(biospecimen.getId()));
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				log.error(e.getMessage());
			}
		}		
	}

	private boolean initialiseBiospecimenCFDataEntry() {
		boolean replacePanel = false;
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (biospecimen.getId() == null) {
			// New Biospecimen record, so Biospecimen CF data entry is disallowed
			if (!(biospecimenCFDataEntryPanel instanceof EmptyPanel)) {
				biospecimenCFDataEntryPanel = new EmptyPanel("biospecimenCFDataEntryPanel");
				replacePanel = true;
			}
		}
		else {
			// Editing an existing record, CF data entry is ok
			if (!(biospecimenCFDataEntryPanel instanceof BiospecimenCustomDataDataViewPanel)) {
				CompoundPropertyModel<BiospecimenCustomDataVO> bioCFDataCpModel = new CompoundPropertyModel<BiospecimenCustomDataVO>(new BiospecimenCustomDataVO());		
				bioCFDataCpModel.getObject().setBiospecimen(biospecimen);
				bioCFDataCpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN));
				biospecimenCFDataEntryPanel = new BiospecimenCustomDataDataViewPanel("biospecimenCFDataEntryPanel", bioCFDataCpModel).initialisePanel(null);
				replacePanel = true;
			}
		}
		return replacePanel;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenIdTxtFld = new TextField<String>("biospecimen.biospecimenUid");
		commentsTxtAreaFld = new TextArea<String>("biospecimen.comments");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);
		quantityTxtFld = new TextField<String>("biospecimen.quantity");

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);

		initSampleTypeDdc();
		initBioCollectionDdc();
		
		barcodedChkBox = new CheckBox("biospecimen.barcoded");
		barcodedChkBox.setVisible(true);

		initialiseBiospecimenCFDataEntry();
		
		attachValidators();
		addComponents();
		
		// Focus on Sample Type
		sampleTypeDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initSampleTypeDdc() {
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> sampleTypeRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", (List<BioSampletype>) sampleTypeList, sampleTypeRenderer);
	}

	private void initBioCollectionDdc() {
		// Get a list of collections for the subject in context by default
		BioCollection bioCollection = new BioCollection();
		bioCollection.setLinkSubjectStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy());
		bioCollection.setStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy().getStudy());
		try {
			cpModel.getObject().setBioCollectionList(iLimsService.searchBioCollection(bioCollection));

			ChoiceRenderer<BioCollection> bioCollectionRenderer = new ChoiceRenderer<BioCollection>(Constants.NAME, Constants.ID);
			bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", cpModel.getObject().getBioCollectionList(), bioCollectionRenderer);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
			this.error("Operation could not be performed - if this persists, contact your Administrator or Support");
		}
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		biospecimenIdTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.biospecimenId.required", this, new Model<String>("Name")));
		sampleTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.sampleType.required", this, new Model<String>("Name")));
		bioCollectionDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.bioCollection.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenIdTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTypeDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioCollectionDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(quantityTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(barcodedChkBox);
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenCFDataEntryPanel);
		
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getBiospecimen().getId() == null) {
			// Save
			iLimsService.createBiospecimen(cpModel.getObject());
			this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iLimsService.updateBiospecimen(cpModel.getObject());
			this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was updated successfully");
			processErrors(target);
			if (biospecimenCFDataEntryPanel instanceof BiospecimenCustomDataDataViewPanel) {
				((BiospecimenCustomDataDataViewPanel) biospecimenCFDataEntryPanel).saveCustomData();
			}
		}
		// refresh the CF data entry panel (if necessary)
		if (initialiseBiospecimenCFDataEntry() == true) {
			arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenCFDataEntryPanel);
		}

		onSavePostProcess(target);
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		iLimsService.deleteBiospecimen(cpModel.getObject());
		this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was deleted successfully");

		onClose(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getBiospecimen().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
