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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.BioCollectionCustomDataVO;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.button.NumberOfLabelsPanel;
import au.org.theark.lims.web.component.button.zebra.biocollection.PrintBioCollectionLabelButton;
import au.org.theark.lims.web.component.button.zebra.biocollection.PrintBiospecimensForBioCollectionButton;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionCustomDataDataViewPanel;


/**
 * @author cellis
 * 
 */
public class BioCollectionDataEntryModalDetailForm extends AbstractModalDetailForm<LimsVO> {

	private static final long			serialVersionUID	= 2926069852602563767L;
	private static final Logger		log					= LoggerFactory.getLogger(BioCollectionDataEntryModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService										iLimsService;

	private TextField<String>									idTxtFld;
	private TextField<String>									biocollectionUidTxtFld;
	private TextField<String>									nameTxtFld;
	private TextArea<String>									commentsTxtAreaFld;
	private DateTextField										collectionDateTxtFld;
	private ModalWindow											modalWindow;
	private Panel 												bioCollectionCFDataEntryPanel;
	private AjaxButton											printBioCollectionLabelButton;
	private AjaxButton											printBiospecimensForBioCollectionButton;
	private AjaxButton											printStrawBiospecimensForBioCollectionButton;
	protected NumberOfLabelsPanel 								numberOfLabels;
	private DropDownChoice<CustomFieldCategory>					customeFieldCategoryDdc;
	private WebMarkupContainer									dataEntryWMC;   
	private AjaxPagingNavigator						             dataEntryNavigator;    
	

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
	public BioCollectionDataEntryModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
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
				this.error("Can not edit this record; it has been invalidated (e.g. deleted).");
				log.error(e.getMessage());
			}
		}		
	}
	public void onBeforeRender(){
		Study study = this.getModelObject().getBioCollection().getStudy();
		
		if(study!=null && !study.getAutoGenerateBiocollectionUid()){
			biocollectionUidTxtFld.setEnabled(true);	
		}
		else{
			biocollectionUidTxtFld.setEnabled(false);
		}
			super.onBeforeRender();
	}
	/**
	 * 
	 */
	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		biocollectionUidTxtFld = new TextField<String>("bioCollection.biocollectionUid");
		nameTxtFld = new TextField<String>("bioCollection.name");
		
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(datePicker); 
		
		Collection<CustomFieldCategory> customFieldCategoryCollection=getOnlyAssignedCategoryListInStudyByCustomFieldType();
		List<CustomFieldCategory> customFieldCatLst=CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories((List<CustomFieldCategory>)customFieldCategoryCollection);
		ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID){
						@Override
						public Object getDisplayValue(Object object) {
						CustomFieldCategory cuscat=(CustomFieldCategory)object;
							return CustomFieldCategoryOrderingHelper.getInstance().preTextDecider(cuscat)+ super.getDisplayValue(object);
						}
		};
		customeFieldCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY, customFieldCatLst, customfieldCategoryRenderer);
		customeFieldCategoryDdc.setOutputMarkupId(true);
		customeFieldCategoryDdc.setNullValid(true);
		customeFieldCategoryDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				//Remove
				dataEntryWMC.remove(bioCollectionCFDataEntryPanel);
				dataEntryWMC.remove(dataEntryNavigator);
				arkCrudContainerVo.getDetailPanelFormContainer().remove(dataEntryWMC);
				//Create
				if(customeFieldCategoryDdc.getModelObject()!=null){
					initialiseBioCollectionCFDataEntry(customeFieldCategoryDdc.getModelObject());
				}
				//Add
				dataEntryWMC.add(bioCollectionCFDataEntryPanel);
				dataEntryWMC.add(dataEntryNavigator);
				arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);
				//target
				target.add(bioCollectionCFDataEntryPanel);
				target.add(dataEntryNavigator);
				target.add(dataEntryWMC);
			}
		});
		
		
		dataEntryWMC = new WebMarkupContainer("dataEntryWMC");
		dataEntryWMC.setOutputMarkupId(true);
		
		initialiseBioCollectionCFDataEntry(customeFieldCategoryDdc.getModelObject());
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		numberOfLabels = new NumberOfLabelsPanel("numberOfLabels");
		printBioCollectionLabelButton = new PrintBioCollectionLabelButton("printBioCollectionLabel", bioCollection, (IModel<Number>) numberOfLabels.getDefaultModel()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		printBioCollectionLabelButton.setDefaultFormProcessing(false);
		
		printBiospecimensForBioCollectionButton = new PrintBiospecimensForBioCollectionButton("printBiospecimensForBioCollectionButton", bioCollection, "zebra biospecimen", (IModel<Number>) numberOfLabels.getDefaultModel()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		printBioCollectionLabelButton.setDefaultFormProcessing(false);

		printStrawBiospecimensForBioCollectionButton = new PrintBiospecimensForBioCollectionButton("printStrawBiospecimensForBioCollectionButton", bioCollection, "straw barcode", (IModel<Number>) numberOfLabels.getDefaultModel()) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		printStrawBiospecimensForBioCollectionButton.setDefaultFormProcessing(false);
		
		attachValidators();
		addComponents();
		
		// Focus on Collection Date
		collectionDateTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	private void initialiseBioCollectionCFDataEntry(CustomFieldCategory customFieldCategory) {
		BioCollectionCustomDataDataViewPanel local_bioCollectionCFDataEntryPanel;
		BioCollectionCustomDataVO bioCollectionCustomDataVO=cpModel.getObject().getBioCollectionCustomDataVO();
		bioCollectionCustomDataVO.setBioCollection(cpModel.getObject().getBioCollection());
		bioCollectionCustomDataVO.setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD));
		if(customFieldCategory!=null){
			local_bioCollectionCFDataEntryPanel = new BioCollectionCustomDataDataViewPanel("bioCollectionCFDataEntryPanel",new CompoundPropertyModel<BioCollectionCustomDataVO>(bioCollectionCustomDataVO)).initialisePanel(iArkCommonService.getCustomFieldsPerPage(),customFieldCategory);
		}else{
			local_bioCollectionCFDataEntryPanel = new BioCollectionCustomDataDataViewPanel("bioCollectionCFDataEntryPanel", new CompoundPropertyModel<BioCollectionCustomDataVO>(bioCollectionCustomDataVO)).initialisePanel(iArkCommonService.getCustomFieldsPerPage(),null);
		}
		dataEntryNavigator = new AjaxPagingNavigator("dataEntryNavigator", local_bioCollectionCFDataEntryPanel.getDataView()) {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataEntryWMC);
			}
		};
		bioCollectionCFDataEntryPanel = local_bioCollectionCFDataEntryPanel;
		
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		biocollectionUidTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioCollection.biocollectionUid.required", this, new Model<String>("BioCollectionUid")));
		nameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(biocollectionUidTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(collectionDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(customeFieldCategoryDdc);
		dataEntryWMC.add(bioCollectionCFDataEntryPanel);
		dataEntryWMC.add(dataEntryNavigator);
		arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);
		add(numberOfLabels);
		add(printBioCollectionLabelButton);
		add(printBiospecimensForBioCollectionButton);
		add(printStrawBiospecimensForBioCollectionButton);
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}


	@Override
	protected void onSave(AjaxRequestTarget target) {

		try {
			if (cpModel.getObject().getBioCollection().getId() == null) {
				// Save
				
				iLimsService.createBioCollection(cpModel.getObject());
				
				this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getBiocollectionUid() + " was created successfully.");
				
			}
			else {
				// Update
				iLimsService.updateBioCollection(cpModel.getObject());
				this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getBiocollectionUid() + " was updated successfully.");
				
			}
			if (bioCollectionCFDataEntryPanel instanceof BioCollectionCustomDataDataViewPanel) {
				((BioCollectionCustomDataDataViewPanel) bioCollectionCFDataEntryPanel).saveCustomData();
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
		BioCollection bioCollection=cpModel.getObject().getBioCollection();
		//Ark-1606 bug fix for deleting bio-collection which has already data.  
		if(iLimsService.hasBiocllectionGotCustomFieldData(bioCollection)){
			this.error("Biospecimen collection " + bioCollection.getBiocollectionUid() + " can not be deleted because it has biocollection custom field data attached.");
			target.add(feedbackPanel);
		}
		if(iLimsService.hasBiospecimens(bioCollection)) {
			this.error("Biospecimen collection " + bioCollection.getBiocollectionUid() + " can not be deleted because there are biospecimens attached.");
			target.add(feedbackPanel);
		}
		if(!iLimsService.hasBiospecimens(bioCollection) &&(!iLimsService.hasBiocllectionGotCustomFieldData(bioCollection)))
		{
			iLimsService.deleteBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + bioCollection.getBiocollectionUid() + " was deleted successfully.");
			onClose(target);
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
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<CustomFieldCategory> getOnlyAssignedCategoryListInStudyByCustomFieldType(){
		
		/*Study study =cpModel.getObject().getBioCollection().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_CATEGORY);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.BIOCOLLECTION);
		Collection<CustomFieldCategory> customFieldCategoryCollection = null;
		try {
			customFieldCategoryCollection =  iArkCommonService.getAvailableAllCategoryListInStudyByCustomFieldType(study,arkFunction, customFieldType);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customFieldCategoryCollection;*/
		Study study =cpModel.getObject().getBioCollection().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.BIOCOLLECTION);
		Collection<CustomFieldCategory> customFieldCategoryCollection = null;
		try {
			customFieldCategoryCollection =  iArkCommonService.getCategoriesListInCustomFieldsByCustomFieldType(study, arkFunction, customFieldType);
			customFieldCategoryCollection=sortLst(remeoveDuplicates((List<CustomFieldCategory>)customFieldCategoryCollection));
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customFieldCategoryCollection;
	}
	
	/**
	 * Sort custom field list according to the order number.
	 * @param customFieldLst
	 * @return
	 */
	private  List<CustomFieldCategory> sortLst(List<CustomFieldCategory> customFieldLst){
		//sort by order number.
		Collections.sort(customFieldLst, new Comparator<CustomFieldCategory>(){
		    public int compare(CustomFieldCategory custFieldCategory1, CustomFieldCategory custFieldCatCategory2) {
		        return custFieldCategory1.getName().compareTo(custFieldCatCategory2.getName());
		    }
		});
				return customFieldLst;
	}
	/**
	 * Remove duplicates from list
	 * @param customFieldLst
	 * @return
	 */
	private  List<CustomFieldCategory> remeoveDuplicates(List<CustomFieldCategory> customFieldLst){
		Set<CustomFieldCategory> cusfieldCatSet=new HashSet<CustomFieldCategory>();
		List<CustomFieldCategory> cusfieldCatLst=new ArrayList<CustomFieldCategory>();
		cusfieldCatSet.addAll(customFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	
}
