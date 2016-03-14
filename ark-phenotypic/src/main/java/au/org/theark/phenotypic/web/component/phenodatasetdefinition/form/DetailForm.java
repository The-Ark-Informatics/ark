package au.org.theark.phenotypic.web.component.phenodatasetdefinition.form;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.exception.ConstraintViolationException;

import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.LinkPhenoDataSetCategoryField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoCategoryFieldSummaryRecursivePanel;
import au.org.theark.phenotypic.util.PhenoDataSetCategoryOrderingHelper;
import au.org.theark.phenotypic.web.component.phenodatasetdefinition.DataDictionaryDisplayListPanel;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;


/**
 * 
 * @author smaddumarach
 *
 */
public class DetailForm extends AbstractDetailForm<PhenoDataSetFieldGroupVO> {

	private static final long														serialVersionUID	= 1L;
	private static final Logger													log = LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService														iPhenotypicService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService														iArkCommonService;
	private ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay>	cfdProvider;
	private DataView<PhenoDataSetFieldDisplay>										dataView;
	private TextField<String>														customFieldGroupTxtFld;
	private TextArea<String>														description;
	private CheckBox																publishedStatusCb;
	private Boolean																	addCustomFieldDisplayList;
	private FileUploadField															fileUploadField;
	private Button																	fileUploadButton;
	protected  WebMarkupContainer 													phenoDataSetListchoiceCategoryDetailWMC;
	//Available list
	private ListMultipleChoice<PhenoDataSetCategory> 								phenoDataSetCategoryAvailableListChoice; 
	private Button 																	addButtonCategory,removeButtonCategory,upButtonCat,downButtonCat,
				   																	upButtonField,downButtonField,leftButton,rightButton,
				   																	addButtonFieldsToCategory,removeButtonFieldsFromCategory;
	//Selected Category
	private ListMultipleChoice<PhenoDataSetCategory> 								phenoDataSetCategoryPickedListChoice;
	//Available Fields.
	private ListMultipleChoice<PhenoDataSetField> 	 								phenoDataSetFieldAvailableListChoice;
	private PropertyModel<List<PickedPhenoDataSetCategory>> 						pickedSelectedCategories;
	private PropertyModel<List<PickedPhenoDataSetCategory>> 						pickedAvailableCategories;
	//Linked fields.
	private ListMultipleChoice<PhenoDataSetField> 	 								linkedFieldsOfACategoryChoice;
	private Study 																	study;
	private ArkUser 																arkUser;
	private Panel																	categoryFieldSummarypanel;
	
	/**
	 * @param id
	 * @param feedBackPanel
	 * @param cpModel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpModel, ArkCrudContainerVO arkCrudContainerVO,
			ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay> cfdProvider, Boolean addCustomFieldDisplayList) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		this.addCustomFieldDisplayList = addCustomFieldDisplayList;
		this.cfdProvider = cfdProvider;
		setMultiPart(true);
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		if(!isNew()) {
			List<PhenoDataSetField> selectedCustomFieldList = iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(getModelObject().getPhenoDataSetGroup());

			// Disable Delete button if selected fields exist
			if (!selectedCustomFieldList.isEmpty()) {
				AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				deleteButton.setEnabled(false);
			}
			
			if(getModelObject().getPhenoDataSetGroup().getPublished()) {
				// Disable when published
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
			}
		}
	}

	private void initCustomFieldDataListPanel() {
		cfdProvider.setCriteriaModel(new PropertyModel<PhenoDataSetFieldDisplay>(cpModel, "phenoDataSetGroup"));
		List<PhenoDataSetField> selectedList = iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(getModelObject().getPhenoDataSetGroup());
		Boolean disableEditButton = false;
		if (getModelObject().getPhenoDataSetGroup().getPublished()) {
			for (PhenoDataSetField phenoDataSetField : selectedList) {
				if (phenoDataSetField.getPhenoFieldHasData()) {
					disableEditButton = true;
					break;
				}
			}
		}

		DataDictionaryDisplayListPanel cfdListPanel = new DataDictionaryDisplayListPanel("cfdListPanel", feedBackPanel, arkCrudContainerVO, disableEditButton);
		cfdListPanel.setOutputMarkupId(true);
		cfdListPanel.initialisePanel();
		dataView = cfdListPanel.buildDataView(cfdProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("cfDisplayNavigator", dataView) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
			}
		};
		cfdListPanel.addOrReplace(pageNavigator);
		cfdListPanel.addOrReplace(dataView);
		arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel().addOrReplace(cfdListPanel);
	}

	public void initialiseDetailForm() {
		
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		try {
			arkUser=iArkCommonService.getArkUser((String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_USERID));
		} catch (InvalidSessionException | EntityNotFoundException e) {
			e.printStackTrace();
		}
		customFieldGroupTxtFld = new TextField<String>("phenoDataSetGroup.name");
		description = new TextArea<String>("phenoDataSetGroup.description");
		publishedStatusCb = new CheckBox("phenoDataSetGroup.published");
		publishedStatusCb.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Check what was selected and then toggle
				if (publishedStatusCb.getModelObject().booleanValue()) {
					error("Custom Fields may not be added or removed once the Data Set is published.");
					target.add(feedBackPanel);
				}
				else {
					target.add(feedBackPanel);
				}
			}
		});
		
		fileUploadField = new FileUploadField("customFieldFileUploadField");
		fileUploadButton = new Button("uploadCustomFieldField"){

			private static final long	serialVersionUID	= 1L;
			@Override
			public void onSubmit() {
				setSelectedCustomFieldsFromFile();
			}
			
		};
		fileUploadButton.setDefaultFormProcessing(false);
		
		if (addCustomFieldDisplayList) {
			initCustomFieldDataListPanel();
		}
		else {
			arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel().addOrReplace(new EmptyPanel("cfdListPanel"));
		}
		initCategoryListChoiceContainer();
		initPhenoDataSetList();
		initCategoryFieldSummaryPanel();
		addDetailFormComponents();
		//arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel().addOrReplace(categoryFieldSummarypanel);
		attachValidators();
	}
	private void initCategoryFieldSummaryPanel(){
		categoryFieldSummarypanel=new PhenoCategoryFieldSummaryRecursivePanel("panels", createCategoryAndFieldList(cpModel.getObject().getPickedAvailableCategories()));
		categoryFieldSummarypanel.setOutputMarkupId(true);
	}
	private void updateCategoryFieldSummaryPanel(){
		phenoDataSetListchoiceCategoryDetailWMC.remove(categoryFieldSummarypanel);
		initCategoryFieldSummaryPanel();
		phenoDataSetListchoiceCategoryDetailWMC.add(categoryFieldSummarypanel);
	}
	private void initPhenoDataSetList(){
		cpModel.getObject().setAvailablePhenoDataSetFields(getAvailablePhenoFieldListNotInLinked());
		cpModel.getObject().setLinkedAvailablePhenoDataSetFields(getAllLinkPhenoDataSetFieldLst());
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "id");
		//Available phenodataset field.
		PropertyModel<List<PhenoDataSetField>> selectedPhenoDataSetFieldLst = new PropertyModel<List<PhenoDataSetField>>(cpModel, "selectedPhenoDataSetFields");
		PropertyModel<List<PhenoDataSetField>> availablePhenoDataSetFieldLst = new PropertyModel<List<PhenoDataSetField>>(cpModel, "availablePhenoDataSetFields");
		Collections.sort((List<PhenoDataSetField>)availablePhenoDataSetFieldLst.getObject(), new Comparator<PhenoDataSetField>(){
			public int compare(PhenoDataSetField arg0, PhenoDataSetField arg1) {
				return arg0.getId().compareTo(arg1.getId());
			}
		});
		availablePhenoDataSetFields(renderer,availablePhenoDataSetFieldLst,selectedPhenoDataSetFieldLst);
		addButtonFieldsToCategory(selectedPhenoDataSetFieldLst);
		//initialize
		addButtonFieldsToCategoryStatusAtInitialiseStage(getAvailablePhenoFieldListNotInLinked());
		//Pheno data set field for the select categories.
		PropertyModel<List<PhenoDataSetField>> linkedAvailablePhenoDataSetFields= new PropertyModel<List<PhenoDataSetField>>(cpModel, "linkedAvailablePhenoDataSetFields");                   
		PropertyModel<List<PhenoDataSetField>> linkedSelectedPhenoDataSetFields = new PropertyModel<List<PhenoDataSetField>>(cpModel, "linkedSelectedPhenoDataSetFields");
		linkedFieldsOfCategories(renderer, linkedAvailablePhenoDataSetFields, linkedSelectedPhenoDataSetFields);
		removeButtonCategoryFromFields(linkedSelectedPhenoDataSetFields);
		//initialize
		removeButtonFieldsFromCategoryStatusAtInitialiseStage(getAllLinkPhenoDataSetFieldLst());
		upButtonField(linkedSelectedPhenoDataSetFields);
		downButtonField(linkedSelectedPhenoDataSetFields);
	}
	
	private void unSelectAllPickedPhenoDataSetCategories() {
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=iPhenotypicService.getPickedPhenoDataSetCategories(study, arkFunction, arkUser);
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
			pickedPhenoDataSetCategory.setSelected(false);
			try {
				iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Available categories.
	 * @param renderer
	 * @param availableCategories
	 * @param selectedCategories
	 */
	private void availablePhenoDataSetFields(IChoiceRenderer<String> renderer,PropertyModel<List<PhenoDataSetField>> availableFields,PropertyModel<List<PhenoDataSetField>> selectedFields) {
		phenoDataSetFieldAvailableListChoice=new ListMultipleChoice("availablePhenoDataSetFields", selectedFields,availableFields,renderer);
		phenoDataSetFieldAvailableListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setSelectedPhenoDataSetFields(selectedFields.getObject());
            	target.add(phenoDataSetFieldAvailableListChoice);
            }
            });
	}
	/**
	 * Available categories.
	 * @param renderer
	 * @param availableCategories
	 * @param selectedCategories
	 */
	
	@Override
	protected void attachValidators() {
		customFieldGroupTxtFld.setRequired(true).setLabel(new StringResourceModel("phenoDataSetGroup.name", this, new Model<String>("Custom Field Group Name")));
		customFieldGroupTxtFld.add(StringValidator.maximumLength(1000));
	}
	@Override
	protected boolean isNew() {
		Boolean flag = false;
		if (getModelObject().getPhenoDataSetGroup() != null && getModelObject().getPhenoDataSetGroup().getId() == null) {
			flag = true;
		}
		return flag;
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		setModelObject(new PhenoDataSetFieldGroupVO());
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// Get a list of CustomFields for the given group
		ArrayList<PhenoDataSetField> selectedList = (ArrayList) iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(getModelObject().getPhenoDataSetGroup());

		Boolean allowDelete = true;
		for (PhenoDataSetField phenoDataSetField : selectedList) {
			if (phenoDataSetField.getPhenoFieldHasData()) {
				allowDelete = false;
				break;
			}
		}
		if (allowDelete) {
			iPhenotypicService.deletePhenoFieldDataSetGroup(getModelObject());
			this.info("Data Set has been deleted successfully.");
			editCancelProcess(target);

		}
		else {
			this.error("This Data Set cannot be deleted.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<PhenoDataSetFieldGroupVO> containerForm, AjaxRequestTarget target) {
		if (getModelObject().getPhenoDataSetGroup().getId() == null) {
			// Create
			ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
			getModelObject().getPhenoDataSetGroup().setArkFunction(arkFunction);
			getModelObject().getPhenoDataSetGroup().setStudy(study);

			try {
				iPhenotypicService.createPhenoFieldDataSetGroup(getModelObject());
				initCustomFieldDataListPanel();
				this.info("Data Set has been created successfully.");
			}
			catch (EntityExistsException e) {
				this.error("A Data Set with the same name already exisits. Please choose a unique one.");
			}
			catch (ArkSystemException e) {
				this.error("A System error occured. Please contact Administrator.");
			}
		}
		else {

			try {
				iPhenotypicService.updatePhenoFieldDataSetGroup(getModelObject());
				initCustomFieldDataListPanel();
				this.info("Data Set has been updated successfully.");

			}
			catch (EntityExistsException e) {
				this.error("A Data Set with the same name already exisits. Please choose a unique one.");
				e.printStackTrace();
			}
			catch (ArkSystemException e) {
				this.error("A System error occured. Please contact Administrator.");
				e.printStackTrace();
			}

		}
		target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());// Repaint this List of Custom Field Displays
		onSavePostProcess(target);// Post process

	}

	@SuppressWarnings("unchecked")
	private void setSelectedCustomFieldsFromFile() {
		if(fileUploadField.getFileUpload() != null) {
			ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
			ArrayList<PhenoDataSetField> selectedPhenoDataSetField = (ArrayList<PhenoDataSetField>) iArkCommonService.matchCustomFieldsFromInputFile(fileUploadField.getFileUpload(), study, arkFunction); 
			cpModel.getObject().setSelectedPhenoDataSetFields(selectedPhenoDataSetField);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldGroupTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(description);
		//arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(publishedStatusCb);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(fileUploadButton);
		
		phenoDataSetListchoiceCategoryDetailWMC=new WebMarkupContainer("phenoDataSetListchoiceCategoryDetailWMC");
		phenoDataSetListchoiceCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetListchoiceCategoryDetailWMC.setOutputMarkupId(true);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetCategoryAvailableListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonCategory);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonCategory);
		phenoDataSetListchoiceCategoryDetailWMC.add(upButtonCat);
		phenoDataSetListchoiceCategoryDetailWMC.add(downButtonCat);
		phenoDataSetListchoiceCategoryDetailWMC.add(upButtonField);
		phenoDataSetListchoiceCategoryDetailWMC.add(downButtonField);
		phenoDataSetListchoiceCategoryDetailWMC.add(leftButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(rightButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetCategoryPickedListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonFieldsToCategory);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonFieldsFromCategory);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetFieldAvailableListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(linkedFieldsOfACategoryChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(categoryFieldSummarypanel);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetListchoiceCategoryDetailWMC);
		addOrReplace(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	/**
	 * Remove duplicates from list
	 * @param phenoDataSetFieldLst
	 * @return
	 */
	private  List<PhenoDataSetField> removeDuplicatesField(List<PhenoDataSetField> phenoDataSetFieldLst){
		Set<PhenoDataSetField> cusfieldCatSet=new HashSet<PhenoDataSetField>();
		List<PhenoDataSetField> cusfieldCatLst=new ArrayList<PhenoDataSetField>();
		cusfieldCatSet.addAll(phenoDataSetFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	/**
	 * Remove duplicates from list
	 * @param phenoDataSetFieldLst
	 * @return
	 */
	private  List<PhenoDataSetCategory> removeDuplicatesCategory(List<PhenoDataSetCategory> phenoDataSetFieldLst){
		Set<PhenoDataSetCategory> cusfieldCatSet=new HashSet<PhenoDataSetCategory>();
		List<PhenoDataSetCategory> cusfieldCatLst=new ArrayList<PhenoDataSetCategory>();
		cusfieldCatSet.addAll(phenoDataSetFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	
	/*private  List<PhenoDataSetCategory> sortLst(List<PhenoDataSetCategory> phenoFieldLst){
		//sort by order number.
		Collections.sort(phenoFieldLst, new Comparator<PhenoDataSetCategory>(){
		    public int compare(PhenoDataSetCategory phenoFieldCat1, PhenoDataSetCategory phenoFieldFieldCat2) {
		        return phenoFieldCat1.getOrderNumber().compareTo(phenoFieldFieldCat2.getOrderNumber());
		    }
		});
				return phenoFieldLst;
	}*/
	/** This is for testing purpose please remove after that.
	 * Get phenoDataSet field category collection from model.
	 * @return
	 */
	/*private List<PhenoDataSetCategory> getMySortedSiblingList(PhenoDataSetCategory phenoDataSetFieldCategory){
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_CATEGORY);
		return sortLst(iPhenotypicService.getSiblingList(study, arkFunction, phenoDataSetFieldCategory));
		
	}*/
	/** This is for testing purpose please remove after that.
	 * Get pheno field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> upButtonUpdatePhenoFieldCategoryOrder(List<PhenoDataSetCategory> sortedSlibingLst,PhenoDataSetCategory phenoDataSetCategory){
		PhenoDataSetCategory aboveSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(phenoDataSetCategory)-1);
		List<PhenoDataSetCategory> updateList=new ArrayList<PhenoDataSetCategory>(0);
		//Long aboveSiblingOrdNumber=aboveSibling.getOrderNumber();
			//aboveSibling.setOrderNumber(phenoDataSetCategory.getOrderNumber());
			//phenoDataSetCategory.setOrderNumber(aboveSiblingOrdNumber);
			updateList.add(aboveSibling);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(aboveSibling);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateList.add(phenoDataSetCategory);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(phenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	/** This is for testing purpose please remove after that.
	 * Get phenoDataSet field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> downButtonUpdatePhenoFieldCategoryOrder(List<PhenoDataSetCategory> sortedSlibingLst,PhenoDataSetCategory phenoDataSetCategory){
		PhenoDataSetCategory benethSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(phenoDataSetCategory)+1);
		List<PhenoDataSetCategory> updateList=new ArrayList<PhenoDataSetCategory>(0);
		//Long benethSiblingOrdNumber=benethSibling.getOrderNumber();
			//benethSibling.setOrderNumber(phenoDataSetCategory.getOrderNumber());
			//phenoDataSetCategory.setOrderNumber(benethSiblingOrdNumber);
			updateList.add(benethSibling);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(benethSibling);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateList.add(phenoDataSetCategory);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(phenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	private void initCategoryListChoiceContainer(){
		unSelectAllPickedPhenoDataSetCategories();
		cpModel.getObject().setPhenoDataSetFieldCategoryLst(getAvailablePhenoCategoryListNotPicked());
		cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
		IChoiceRenderer<String> renderer = new ChoiceRenderer("name", "id");
		IChoiceRenderer<String> rendererPickedCategory = new ChoiceRenderer("phenoDataSetCategory.name", "id"){
			private static final long serialVersionUID = 1L;
		@Override
		public Object getDisplayValue(Object object) {
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory=(PickedPhenoDataSetCategory)object;
			return PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(pickedPhenoDataSetCategory)+ super.getDisplayValue(object);
		}
		};
		
		//Define all the list of categories.
		PropertyModel<List<PhenoDataSetCategory>> availableCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "phenoDataSetFieldCategoryLst");
		PropertyModel<List<PhenoDataSetCategory>> selectedCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "selectedCategories");
		pickedAvailableCategories = new PropertyModel<List<PickedPhenoDataSetCategory>>(cpModel, "pickedAvailableCategories");
		pickedSelectedCategories = new PropertyModel<List<PickedPhenoDataSetCategory>>(cpModel, "pickedSelectedCategories");
		
		//Initialize the list boxes.  
		availableCategories(renderer, availableCategories, selectedCategories);
		pickedAvailableCategories(rendererPickedCategory, pickedAvailableCategories,pickedSelectedCategories);
		addButtonCategory(selectedCategories);
		addButtonCategoryStatusAtInitialiseStage(getAvailablePhenoCategoryListNotPicked());//initialize
		removeButtonCategory(pickedSelectedCategories);
		removeButtonCategoryStatusAtInitialiseStage(getAllPickedCategories());//initialize
		upButtonCat(pickedSelectedCategories);
		downButtonCat(pickedSelectedCategories);
		leftButton(pickedSelectedCategories);
		rightButton(pickedSelectedCategories);
	
	}
	/**
	 * Available categories.
	 * @param renderer
	 * @param availableCategories
	 * @param selectedCategories
	 */
	private void availableCategories(IChoiceRenderer<String> renderer,PropertyModel<List<PhenoDataSetCategory>> availableCategories,PropertyModel<List<PhenoDataSetCategory>> selectedCategories) {
		phenoDataSetCategoryAvailableListChoice=new ListMultipleChoice("phenoDataSetCategoryLst", selectedCategories,availableCategories,renderer);
		phenoDataSetCategoryAvailableListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setSelectedCategories(selectedCategories.getObject());
            	target.add(phenoDataSetCategoryAvailableListChoice);
            }
     });
	}
	/**
	 * Picked categories
	 * 
	 * @param renderer
	 * @param pickedAvailableCategories
	 * @param pickedSelectedCategories
	 */
	private void pickedAvailableCategories(IChoiceRenderer<String> renderer,PropertyModel<List<PickedPhenoDataSetCategory>> pickedAvailableCategories,PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
		phenoDataSetCategoryPickedListChoice=new ListMultipleChoice("pickedAvailableCategories", pickedSelectedCategories,pickedAvailableCategories,renderer);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		phenoDataSetCategoryPickedListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setPickedSelectedCategories(pickedSelectedCategories.getObject());
            	//Set All to not selected
            	List<PickedPhenoDataSetCategory > allPickedPhenoDataSetCategory=iPhenotypicService.getPickedPhenoDataSetCategories(study, arkFunction, arkUser);
            	for (PickedPhenoDataSetCategory selectedPickedPhenoDataSetCategory : allPickedPhenoDataSetCategory) {
            		selectedPickedPhenoDataSetCategory.setSelected(false);
            		try {
						iPhenotypicService.updatePickedPhenoDataSetCategory(selectedPickedPhenoDataSetCategory);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
						e.printStackTrace();
					}
				}
            	//Update only the selected list to true.
            	for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedSelectedCategories.getObject()) {
            		PickedPhenoDataSetCategory pickedPhenoDataSetCategoryPersiting=iPhenotypicService.
            				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, pickedPhenoDataSetCategory.getPhenoDataSetCategory());
            		pickedPhenoDataSetCategoryPersiting.setSelected(true);
					try {
						iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategoryPersiting);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
						e.printStackTrace();
					}
				}
            	
            	buttonStatusForMultiSelectOfPickedCategories(pickedSelectedCategories.getObject(), target);
            	List<PhenoDataSetField> fieldsOfcategory=getLinkedPhenoDataSetFieldsForSelectedCategories(pickedSelectedCategories.getObject());
            	cpModel.getObject().setLinkedAvailablePhenoDataSetFields(fieldsOfcategory);
            	removeButtonCategoryStatusForPickedSelectedCategory(fieldsOfcategory, target);
            	removeButtonFieldsFromCategoryStatusWithAssignedFields(fieldsOfcategory, target);
            	target.add(phenoDataSetCategoryPickedListChoice);
            	target.add(linkedFieldsOfACategoryChoice);
            }
            });
	}
	/**
	 * Add to picked categories
	 * @param selectedCategories
	 */
	private void addButtonCategory(PropertyModel<List<PhenoDataSetCategory>> selectedCategories) {
		addButtonCategory = new AjaxButton("addButtonCategory") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				boolean errorStatus=false;
				for (PhenoDataSetCategory phenoDataSetFieldCategory : selectedCategories.getObject()) {
					PickedPhenoDataSetCategory pickedPhenoDataSetCategory=new PickedPhenoDataSetCategory();
					try {
						pickedPhenoDataSetCategory.setStudy(study);
						pickedPhenoDataSetCategory.setArkFunction(arkFunction);
						pickedPhenoDataSetCategory.setArkUser(arkUser);
						pickedPhenoDataSetCategory.setPhenoDataSetCategory(phenoDataSetFieldCategory);
						pickedPhenoDataSetCategory.setSelected(false);
						pickedPhenoDataSetCategory.setOrderNumber(iPhenotypicService.getNextAvailbleNumberForPickedCategory(study, arkFunction, arkUser));
						iPhenotypicService.createPickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException | EntityExistsException | ConstraintViolationException   e) {
						errorStatus=true;
					}
				}
					if(errorStatus){
						this.error("Please select the correct category before picked.");
						errorStatus=false;
					}
					cpModel.getObject().setPhenoDataSetFieldCategoryLst(getAvailablePhenoCategoryListNotPicked());
					cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
					//Checking the condition of the add button. 
					addButtonCategoryStatusWithAvailableCategory(getAvailablePhenoCategoryListNotPicked(), target);
					removeButtonCategoryStatusWithPickedCategory(getAllPickedCategories(), target);
				    target.add(phenoDataSetCategoryAvailableListChoice);
            	    target.add(phenoDataSetCategoryPickedListChoice);
            	    target.add(feedBackPanel);
                    super.onSubmit(target, form);
			}
		};
		addButtonCategory.setDefaultFormProcessing(false);
	}
	/**
	 * Remove from picked categories
	 * @param firstLevelSelectedCategories
	 */
	private void removeButtonCategory(PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
		removeButtonCategory = new AjaxButton("removeButtonCategory") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				boolean errorStatus=false;
			for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedSelectedCategories.getObject()) {
				
				if(iPhenotypicService.isPickedPhenoDataSetCategoryIsAParentOfAnotherCategory(pickedPhenoDataSetCategory)){
					errorStatus=true;
				}else{
					try {
						iPhenotypicService.deletePickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException |  EntityCannotBeRemoved | IllegalArgumentException  e) {
						errorStatus=true;
					}
				}
			}
			if(errorStatus){
				this.error("Check category for Non-parent category or non assigned fields before remove.");
				errorStatus=false;
			}
			cpModel.getObject().setPhenoDataSetFieldCategoryLst(getAvailablePhenoCategoryListNotPicked());
			cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
			//Checking the condition of the add button. 
			addButtonCategoryStatusWithAvailableCategory(getAvailablePhenoCategoryListNotPicked(), target);
			removeButtonCategoryStatusWithPickedCategory(getAllPickedCategories(), target);
		    target.add(phenoDataSetCategoryAvailableListChoice);
		    target.add(phenoDataSetCategoryPickedListChoice);
		    target.add(feedBackPanel);
	        super.onSubmit(target, form);
	}};
	removeButtonCategory.setDefaultFormProcessing(false);
	}
	/**
	 * Up button category.
	 * @param firstLevelSelectedCategories
	 */
	private void upButtonCat(PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
			upButtonCat = new AjaxButton("upButtonCat") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PickedPhenoDataSetCategory pickedPhenoDataSetCategory =pickedSelectedCategories.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PickedPhenoDataSetCategory pickedPhenoDataSetCategoryPersiting=iPhenotypicService.
        				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, pickedPhenoDataSetCategory.getPhenoDataSetCategory());
				PickedPhenoDataSetCategory pickedPhenoDataSetCategorySwap=iPhenotypicService
							.getSwapOverPickedPhenoDataSetCategoryForUpButton(pickedPhenoDataSetCategoryPersiting);
				if(pickedPhenoDataSetCategorySwap!=null){
					Long tempOrderNumber=pickedPhenoDataSetCategoryPersiting.getOrderNumber();
					pickedPhenoDataSetCategoryPersiting.setOrderNumber(pickedPhenoDataSetCategorySwap.getOrderNumber());
					pickedPhenoDataSetCategorySwap.setOrderNumber(tempOrderNumber);
					try {
						iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategoryPersiting);
						iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategorySwap);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
					}
				}
				cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
				target.add(phenoDataSetCategoryPickedListChoice);
				super.onSubmit(target, form);
			}
		};
		upButtonCat.setDefaultFormProcessing(false);
	}
	/**
	 * Down button.
	 * @param firstLevelSelectedCategories
	 */
	private void downButtonCat(PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
		downButtonCat = new AjaxButton("downButtonCat") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PickedPhenoDataSetCategory pickedPhenoDataSetCategory =pickedSelectedCategories.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PickedPhenoDataSetCategory pickedPhenoDataSetCategoryPersiting=iPhenotypicService.
        				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, pickedPhenoDataSetCategory.getPhenoDataSetCategory());
				PickedPhenoDataSetCategory pickedPhenoDataSetCategorySwap=iPhenotypicService
							.getSwapOverPickedPhenoDataSetCategoryForDownButton(pickedPhenoDataSetCategoryPersiting);
				if(pickedPhenoDataSetCategorySwap!=null){
					Long tempOrderNumber=pickedPhenoDataSetCategoryPersiting.getOrderNumber();
					pickedPhenoDataSetCategoryPersiting.setOrderNumber(pickedPhenoDataSetCategorySwap.getOrderNumber());
					pickedPhenoDataSetCategorySwap.setOrderNumber(tempOrderNumber);
					try {
						iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategoryPersiting);
						iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategorySwap);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
					}
				}
				cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
				target.add(phenoDataSetCategoryPickedListChoice);
	            super.onSubmit(target, form);
			}
		};
		downButtonCat.setDefaultFormProcessing(false);
	}
	/**
	 * left button for make sub categories of above category.
	 * @param firstLevelSelectedCategories
	 */
	private void leftButton(PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
			leftButton = new AjaxButton("leftButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PickedPhenoDataSetCategory pickedSelectedCategory=pickedSelectedCategories.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PickedPhenoDataSetCategory pickedPhenoDataSetCategoryPersiting=iPhenotypicService.
        				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, pickedSelectedCategory.getPhenoDataSetCategory());
				if(pickedPhenoDataSetCategoryPersiting.getParentPickedPhenoDataSetCategory()!=null){
					PickedPhenoDataSetCategory parentOfMe=pickedPhenoDataSetCategoryPersiting.getParentPickedPhenoDataSetCategory();
					if(parentOfMe.getParentPickedPhenoDataSetCategory()!=null){
						pickedPhenoDataSetCategoryPersiting.setParentPickedPhenoDataSetCategory(parentOfMe.getParentPickedPhenoDataSetCategory());
					}else{// if parent of parent null
						pickedPhenoDataSetCategoryPersiting.setParentPickedPhenoDataSetCategory(null);
					}
				}else{//if parent is null
					pickedPhenoDataSetCategoryPersiting.setParentPickedPhenoDataSetCategory(null);
				}
				try {
					iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategoryPersiting);
				} catch (ArkSystemException | ArkRunTimeUniqueException
						| ArkRunTimeException e) {
				}
				cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
				target.add(phenoDataSetCategoryPickedListChoice);
				super.onSubmit(target, form);
			}
		};
		leftButton.setDefaultFormProcessing(false);
	}
	/**
	 * right button for make sub categories of above category.
	 * @param firstLevelSelectedCategories
	 */
	private void rightButton(PropertyModel<List<PickedPhenoDataSetCategory>> pickedSelectedCategories) {
			rightButton = new AjaxButton("rightButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PickedPhenoDataSetCategory pickedSelectedCategory=pickedSelectedCategories.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PickedPhenoDataSetCategory pickedPhenoDataSetCategoryPersiting=iPhenotypicService.
        				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, pickedSelectedCategory.getPhenoDataSetCategory());
				pickedPhenoDataSetCategoryPersiting.setParentPickedPhenoDataSetCategory(getImmediateAboveCategoryInThePickedCategoryList(pickedSelectedCategory, PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories())));
				try {
					iPhenotypicService.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategoryPersiting);
				} catch (ArkSystemException | ArkRunTimeUniqueException
						| ArkRunTimeException e) {
				}
				cpModel.getObject().setPickedAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAllPickedCategories()));
				target.add(phenoDataSetCategoryPickedListChoice);
				super.onSubmit(target, form);
			}
		};
		rightButton.setDefaultFormProcessing(false);
	}
	private void addButtonFieldsToCategory(PropertyModel<List<PhenoDataSetField>> selectedPhenoDataSetFieldLst) {
		addButtonFieldsToCategory = new AjaxButton("addButtonFieldsToCategory") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				boolean erroStatus=false;
				if(selectedPhenoDataSetFieldLst!=null && filterPickedAllSelectedCategories()!=null && selectedPhenoDataSetFieldLst.getObject()!=null){
					for(PickedPhenoDataSetCategory pickedPhenoDataSetCategorySelected:filterPickedAllSelectedCategories() ){
						for(PhenoDataSetField phenoDataSetFieldSelected: selectedPhenoDataSetFieldLst.getObject()){
								LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=new LinkPhenoDataSetCategoryField();
								linkPhenoDataSetCategoryField.setStudy(study);
								linkPhenoDataSetCategoryField.setArkFunction(arkFunction);
								linkPhenoDataSetCategoryField.setArkUser(arkUser);
								linkPhenoDataSetCategoryField.setPhenoDataSetCategory(pickedPhenoDataSetCategorySelected.getPhenoDataSetCategory());
								linkPhenoDataSetCategoryField.setPhenoDataSetField(phenoDataSetFieldSelected);
								linkPhenoDataSetCategoryField.setOrderNumber(iPhenotypicService.
										getNextAvailbleNumberForAssignedField(study, arkFunction, arkUser, pickedPhenoDataSetCategorySelected.getPhenoDataSetCategory()));
								try {
									iPhenotypicService.createLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
								} catch (ArkSystemException | ArkRunTimeUniqueException
										| ArkRunTimeException | EntityExistsException | ConstraintViolationException e  ) {
									erroStatus=true;
								}
						}
					}
				}else{
					this.error("Please select the category first and then select the field list from available list.");
				}
				if(erroStatus){
					this.error("Please avoid entering duplicate entries.");
					erroStatus=false;
				}
				List<PhenoDataSetField> phenoDataSetFields=getLinkedPhenoDataSetFieldsForSelectedCategories(filterPickedAllSelectedCategories());
				cpModel.getObject().setLinkedAvailablePhenoDataSetFields(phenoDataSetFields);
				cpModel.getObject().setAvailablePhenoDataSetFields(getAvailablePhenoFieldListNotInLinked());
				addButtonFieldsToCategoryStatusWithAvailableFields(getAvailablePhenoFieldListNotInLinked(), target);
				removeButtonFieldsFromCategoryStatusWithAssignedFields(phenoDataSetFields, target);
				updateCategoryFieldSummaryPanel();
				target.add(categoryFieldSummarypanel);
				target.add(linkedFieldsOfACategoryChoice);
				target.add(phenoDataSetFieldAvailableListChoice);
				target.add(feedBackPanel);
				target.add(phenoDataSetListchoiceCategoryDetailWMC);
		        super.onSubmit(target, form);
			}
		};
		addButtonFieldsToCategory.setDefaultFormProcessing(false);
		
	}
	/**
	 * Remove the link between category and field at the category level display.
	 * @param selectedCategories
	 */
	private void removeButtonCategoryFromFields(PropertyModel<List<PhenoDataSetField>> linkedSelectedFieldsOfCategories ) {
		removeButtonFieldsFromCategory = new AjaxButton("removeButtonFieldsFromCategory") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				if(linkedSelectedFieldsOfCategories!=null && filterPickedAllSelectedCategories()!=null && linkedSelectedFieldsOfCategories.getObject()!=null){
					for(PickedPhenoDataSetCategory pickedPhenoDataSetCategory:filterPickedAllSelectedCategories() ){
						for(PhenoDataSetField phenoDataSetFieldSelected: linkedSelectedFieldsOfCategories.getObject()){
							LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=iPhenotypicService.getLinkPhenoDataSetCategoryField(study, arkFunction,arkUser, pickedPhenoDataSetCategory.getPhenoDataSetCategory(), phenoDataSetFieldSelected);
								if(linkPhenoDataSetCategoryField!=null){	
									try {
										iPhenotypicService.deleteLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
									} catch (ArkSystemException | ArkRunTimeUniqueException
											| ArkRunTimeException | EntityCannotBeRemoved  e) {
										e.printStackTrace();
									}
								}
						}
					}
				}else{
					this.info("Please select the category first and then select the field list from assigned field list.");
				}
				List<PhenoDataSetField> phenoDataSetFields=getLinkedPhenoDataSetFieldsForSelectedCategories(filterPickedAllSelectedCategories());
					cpModel.getObject().setLinkedAvailablePhenoDataSetFields(phenoDataSetFields);
					cpModel.getObject().setAvailablePhenoDataSetFields(getAvailablePhenoFieldListNotInLinked());
					addButtonFieldsToCategoryStatusWithAvailableFields(getAvailablePhenoFieldListNotInLinked(), target);
					removeButtonFieldsFromCategoryStatusWithAssignedFields(phenoDataSetFields, target);
					updateCategoryFieldSummaryPanel();
					target.add(categoryFieldSummarypanel);
					target.add(linkedFieldsOfACategoryChoice);
					target.add(phenoDataSetFieldAvailableListChoice);
					target.add(feedBackPanel);
					target.add(phenoDataSetListchoiceCategoryDetailWMC);
			        super.onSubmit(target, form);
				}
				
		};
		removeButtonFieldsFromCategory.setDefaultFormProcessing(false);
	}
	/**
	 * Up button.
	 * @param firstLevelSelectedCategories
	 */
	private void upButtonField(PropertyModel<List<PhenoDataSetField>> selectedFields) {
		upButtonField = new AjaxButton("upButtonField") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PhenoDataSetField phenoDataSetField = null;
				phenoDataSetField=selectedFields.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PhenoDataSetCategory phenoDataSetCategory=iPhenotypicService.getPhenoDataSetCategoryForAssignedPhenoDataSetField(study, arkFunction, arkUser, phenoDataSetField);
				LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=iPhenotypicService
							.getLinkPhenoDataSetCategoryField(study, arkFunction, arkUser,phenoDataSetCategory, phenoDataSetField);
				LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryFieldSwap=iPhenotypicService
							.getSwapOverPhenoDataSetFieldForUpButton(linkPhenoDataSetCategoryField);
				if(linkPhenoDataSetCategoryFieldSwap!=null){
					Long tempOrderNumber=linkPhenoDataSetCategoryField.getOrderNumber();
					linkPhenoDataSetCategoryField.setOrderNumber(linkPhenoDataSetCategoryFieldSwap.getOrderNumber());
					linkPhenoDataSetCategoryFieldSwap.setOrderNumber(tempOrderNumber);
					try {
						iPhenotypicService.updateLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
						iPhenotypicService.updateLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryFieldSwap);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
					}
				}
				cpModel.getObject().setLinkedAvailablePhenoDataSetFields(getLinkedPhenoDataSetFieldsForSelectedCategories(filterPickedAllSelectedCategories()));
				target.add(linkedFieldsOfACategoryChoice);
				super.onSubmit(target, form);
				
			}
		};
		upButtonField.setDefaultFormProcessing(false);
	}
	/**
	 * Down button.
	 * @param firstLevelSelectedCategories
	 */
	private void downButtonField(PropertyModel<List<PhenoDataSetField>> selectedFields) {
		downButtonField = new AjaxButton("downButtonField") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PhenoDataSetField phenoDataSetField = null;
				phenoDataSetField=selectedFields.getObject().get(0);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				PhenoDataSetCategory phenoDataSetCategory=iPhenotypicService.getPhenoDataSetCategoryForAssignedPhenoDataSetField(study, arkFunction, arkUser, phenoDataSetField);
				LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=iPhenotypicService
							.getLinkPhenoDataSetCategoryField(study, arkFunction, arkUser,phenoDataSetCategory, phenoDataSetField);
				LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryFieldSwap=iPhenotypicService
							.getSwapOverPhenoDataSetFieldForDownButton(linkPhenoDataSetCategoryField);
				if(linkPhenoDataSetCategoryFieldSwap!=null){
					Long tempOrderNumber=linkPhenoDataSetCategoryField.getOrderNumber();
					linkPhenoDataSetCategoryField.setOrderNumber(linkPhenoDataSetCategoryFieldSwap.getOrderNumber());
					linkPhenoDataSetCategoryFieldSwap.setOrderNumber(tempOrderNumber);
					try {
						iPhenotypicService.updateLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
						iPhenotypicService.updateLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryFieldSwap);
					} catch (ArkSystemException | ArkRunTimeUniqueException
							| ArkRunTimeException e) {
					}
				}
				cpModel.getObject().setLinkedAvailablePhenoDataSetFields(getLinkedPhenoDataSetFieldsForSelectedCategories(filterPickedAllSelectedCategories()));
				target.add(linkedFieldsOfACategoryChoice);
				super.onSubmit(target, form);
			}
		};
		downButtonField.setDefaultFormProcessing(false);
	}
	
	
	private List<PickedPhenoDataSetCategory> filterPickedAllSelectedCategories(){
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<PickedPhenoDataSetCategory> onlySelectedCategories=new ArrayList<PickedPhenoDataSetCategory>();
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=iPhenotypicService.getPickedPhenoDataSetCategories(study, arkFunction, arkUser);
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
			if(pickedPhenoDataSetCategory.getSelected()){
				onlySelectedCategories.add(pickedPhenoDataSetCategory);
			}
		}
		return onlySelectedCategories;
	}
	private List<PickedPhenoDataSetCategory> getAllPickedCategories(){
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=iPhenotypicService.getPickedPhenoDataSetCategories(study, arkFunction, arkUser);
		return pickedPhenoDataSetCategories;
	}
	
	private List<PhenoDataSetCategory> getAvailablePhenoCategoryListNotPicked(){
		ArkFunction arkFunctionPhenoCat=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_CATEGORY);
		ArkFunction arkFunctionPhenoCollection=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<PhenoDataSetCategory> allAvailablephenoDataSetFieldCategories = null;
		try {
			allAvailablephenoDataSetFieldCategories = iPhenotypicService.getAvailablePhenoCategoryListNotPicked(study, arkFunctionPhenoCat, arkFunctionPhenoCollection, arkUser);
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}
		return allAvailablephenoDataSetFieldCategories;
	}
	private List<PhenoDataSetField> getAvailablePhenoFieldListNotInLinked(){
		ArkFunction arkFunctionPhenoDataDictionary=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		ArkFunction arkFunctionPhenoCollection=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<PhenoDataSetField> allAvailablephenoDataSetFields = null;
		try {
			allAvailablephenoDataSetFields = iPhenotypicService.getAvailablePhenoFieldListNotInLinked(study, arkFunctionPhenoDataDictionary, arkFunctionPhenoCollection, arkUser); 
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}
		return allAvailablephenoDataSetFields;
	}
	private List<PhenoDataSetField> getAllLinkPhenoDataSetFieldLst(){
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields=iPhenotypicService.getLinkPhenoDataSetCategoryFieldLst(study, arkFunction, arkUser);
		return filterPhenoDataSetFieldsFromLinked(linkPhenoDataSetCategoryFields);
	} 
	private List<PhenoDataSetField> filterPhenoDataSetFieldsFromLinked(List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields){
		List<PhenoDataSetField> phenoDataSetFieldLst=new ArrayList<PhenoDataSetField>();
		for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
			phenoDataSetFieldLst.add(linkPhenoDataSetCategoryField.getPhenoDataSetField());
		}
		return phenoDataSetFieldLst;
	}
	
	private List<PhenoDataSetCategory> filterPhenoDataSetCategoriesFromLinked(List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields){
		List<PhenoDataSetCategory> phenoDataSetCategories=new ArrayList<PhenoDataSetCategory>();
		for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
			phenoDataSetCategories.add(linkPhenoDataSetCategoryField.getPhenoDataSetCategory());
		}
		return phenoDataSetCategories;
	}
	private void linkedFieldsOfCategories(IChoiceRenderer<String> renderer,PropertyModel<List<PhenoDataSetField>> availableFields,PropertyModel<List<PhenoDataSetField>> selectedFields) {
		linkedFieldsOfACategoryChoice=new ListMultipleChoice("linkedAvailablePhenoDataSetFields", selectedFields,availableFields,renderer);
		linkedFieldsOfACategoryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	buttonStatusForMultiSelectOfAssignedFields(selectedFields.getObject(), target);
            	target.add(linkedFieldsOfACategoryChoice);
            }
        });
	}
	private List<PhenoDataSetField> getPhenoDataSetFieldsForSelectedCategory(List<PhenoDataSetCategory> selectedPhenoDataSetCategories){
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		return iPhenotypicService.getLinkedPhenoDataSetFieldsForSelectedCategories(study, arkFunction, arkUser, selectedPhenoDataSetCategories);
	} 
	
	/**
	 * 
	 * @param pickedList
	 * @param target
	 */
	private void buttonStatusForMultiSelectOfPickedCategories(List<PickedPhenoDataSetCategory> pickedList,AjaxRequestTarget target){
		boolean status = false;
		if(pickedList.size() == 1){
			status=true;
			upButtonCat.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter upMe")));
			downButtonCat.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter downMe")));
			rightButton.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter addMe")));
			leftButton.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter removeMe")));
			//check add button with available fields.
			addButtonFieldsToCategoryStatusWithAvailableFields(getAvailablePhenoFieldListNotInLinked(), target);
			//check remove button with assigned fields.
			removeButtonFieldsFromCategoryStatusWithAssignedFields(getLinkedPhenoDataSetFieldsForSelectedCategories(pickedList), target);
		}else if(pickedList.size() > 1){
			status=false;
			upButtonCat.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			downButtonCat.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			rightButton.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			leftButton.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			addButtonFieldsToCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonFieldsFromCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
		}
		upButtonCat.setEnabled(status);
		downButtonCat.setEnabled(status);
		rightButton.setEnabled(status);
		leftButton.setEnabled(status);
		addButtonFieldsToCategory.setEnabled(status);
		removeButtonFieldsFromCategory.setEnabled(status);
		
		target.add(upButtonCat);
		target.add(downButtonCat);
		target.add(rightButton);
		target.add(leftButton);
		target.add(addButtonFieldsToCategory);
		target.add(removeButtonFieldsFromCategory);
		
	}
	/**
	 * 
	 * @param phenoDataSetFields
	 * @param target
	 */
	private void removeButtonCategoryStatusForPickedSelectedCategory(List<PhenoDataSetField> phenoDataSetFields,AjaxRequestTarget target){
		if(phenoDataSetFields.size() >0){
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonCategory.setEnabled(false);
		}else{
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter removeMe")));
			removeButtonCategory.setEnabled(true);
		}
		target.add(removeButtonCategory);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////Handling the part of Adding category and the removing category ///////////////////////////////// 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param phenoDataSetCategories
	 * @param target
	 */
	private void addButtonCategoryStatusWithAvailableCategory(List<PhenoDataSetCategory> phenoDataSetCategories,AjaxRequestTarget target){
		if(phenoDataSetCategories.size() == 0){
			addButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			addButtonCategory.setEnabled(false);
		}else{
			addButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter addMe")));
			addButtonCategory.setEnabled(true);
		}
		target.add(addButtonCategory);
		
	}
	/**
	 * 
	 * @param phenoDataSetCategories
	 */
	private void addButtonCategoryStatusAtInitialiseStage(List<PhenoDataSetCategory> phenoDataSetCategories){
		if(phenoDataSetCategories.size() == 0){
			addButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			addButtonCategory.setEnabled(false);
		}else{
			addButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter addMe")));
			addButtonCategory.setEnabled(true);
		}
	}
	
	/**
	 * 
	 * @param phenoDataSetCategories
	 * @param target
	 */
	private void removeButtonCategoryStatusWithPickedCategory(List<PickedPhenoDataSetCategory> phenoDataSetCategories,AjaxRequestTarget target){
		if(phenoDataSetCategories.size() == 0){
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonCategory.setEnabled(false);
		}else{
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter removeMe")));
			removeButtonCategory.setEnabled(true);
		}
		target.add(removeButtonCategory);
		
	}
	/**
	 * 
	 * @param phenoDataSetCategories
	 */
	private void removeButtonCategoryStatusAtInitialiseStage(List<PickedPhenoDataSetCategory> phenoDataSetCategories){
		if(phenoDataSetCategories==null || phenoDataSetCategories.size() == 0){
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonCategory.setEnabled(false);
		}else{
			removeButtonCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter removeMe")));
			removeButtonCategory.setEnabled(true);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////End of it.////////////////////////////////////////////////////// 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////Handling the part of Adding fields and removing // /////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param phenoDataSetCategories
	 * @param target
	 */
	private void addButtonFieldsToCategoryStatusWithAvailableFields(List<PhenoDataSetField> phenoDataSetFields,AjaxRequestTarget target) {
		if (phenoDataSetFields.size() == 0) {
			addButtonFieldsToCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			addButtonFieldsToCategory.setEnabled(false);
		} else {
			addButtonFieldsToCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter sumMe")));
			addButtonFieldsToCategory.setEnabled(true);
		}
		target.add(addButtonFieldsToCategory);

	}

	/**
	 * 
	 * @param phenoDataSetCategories
	 */
	private void addButtonFieldsToCategoryStatusAtInitialiseStage(List<PhenoDataSetField> phenoDataSetFields) {
		if (phenoDataSetFields.size() == 0) {
			addButtonFieldsToCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			addButtonFieldsToCategory.setEnabled(false);
		} else {
			addButtonFieldsToCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter sumMe")));
			addButtonFieldsToCategory.setEnabled(true);
		}
	}

	/**
	 * 
	 * @param phenoDataSetCategories
	 * @param target
	 */
	private void removeButtonFieldsFromCategoryStatusWithAssignedFields(List<PhenoDataSetField> phenoDataSetFields,AjaxRequestTarget target) {
		if (phenoDataSetFields.size() == 0) {
			removeButtonFieldsFromCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonFieldsFromCategory.setEnabled(false);
		} else {
			removeButtonFieldsFromCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter minusMe")));
			removeButtonFieldsFromCategory.setEnabled(true);
		}
		target.add(removeButtonFieldsFromCategory);
	}

	/**
	 * 
	 * @param phenoDataSetCategories
	 */
	private void removeButtonFieldsFromCategoryStatusAtInitialiseStage(List<PhenoDataSetField> phenoDataSetFields) {
		if (phenoDataSetFields == null|| phenoDataSetFields.size() == 0) {
			removeButtonFieldsFromCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			removeButtonFieldsFromCategory.setEnabled(false);
		} else {
			removeButtonFieldsFromCategory.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter minusMe")));
			removeButtonFieldsFromCategory.setEnabled(true);
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////End of it.////////////////////////////////////////////////////// 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param pickedList
	 * @return
	 */
	private List<PhenoDataSetField> getLinkedPhenoDataSetFieldsForSelectedCategories(List<PickedPhenoDataSetCategory> pickedList){
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		return iPhenotypicService.getLinkedPhenoDataSetFieldsForSelectedCategories(study, arkFunction, arkUser, filterPhenoDataSetCategoryLstFromPickedPhenoDataSetCategory(pickedList));
	}
	
	/**
	 * 
	 * @param pickedList
	 * @param target
	 */
	private void buttonStatusForMultiSelectOfAssignedFields(List<PhenoDataSetField> assignedList,AjaxRequestTarget target){
		boolean status = false;
		if(assignedList.size() == 1){
			status=true;
			upButtonField.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter upMe")));
			downButtonField.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter downMe")));
		}else if(assignedList.size() > 1){
			status=false;
			upButtonField.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
			downButtonField.add(new AttributeModifier("class", new Model("buttonPhenoCatFilter stop")));
		
		}
		upButtonField.setEnabled(status);
		downButtonField.setEnabled(status);
		target.add(upButtonField);
		target.add(downButtonField);
		
	}
	/**
	 * Filter PhenoDataSetCategory from PickedPhenoDatasetCategory.
	 * @param pickedPhenoDataSetCategories
	 * @return
	 */
	private List<PhenoDataSetCategory> filterPhenoDataSetCategoryLstFromPickedPhenoDataSetCategory(List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories){
		List<PhenoDataSetCategory> filteredLst=new ArrayList<PhenoDataSetCategory>();
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
			filteredLst.add(pickedPhenoDataSetCategory.getPhenoDataSetCategory());
		}
		return filteredLst;
	}
	
	private PickedPhenoDataSetCategory getImmediateAboveCategoryInThePickedCategoryList(PickedPhenoDataSetCategory currentPickedPhenoDataSetCategory ,List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories){
		if(pickedPhenoDataSetCategories.indexOf(currentPickedPhenoDataSetCategory)> 0 ){
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory=pickedPhenoDataSetCategories.get(pickedPhenoDataSetCategories.indexOf(currentPickedPhenoDataSetCategory)-1);
		return pickedPhenoDataSetCategory;
		}else{
			return null;
		}
	}
	private List<Object> createCategoryAndFieldList(List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories){
			List<Object> categoryList=new ArrayList<Object>();
			List<PhenoDataSetField> phenoDataSetFields;
			ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
			for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
				categoryList.add(pickedPhenoDataSetCategory.getPhenoDataSetCategory().getName());
				List<PhenoDataSetCategory> tempCategories=new ArrayList<PhenoDataSetCategory>();
				tempCategories.add(pickedPhenoDataSetCategory.getPhenoDataSetCategory());
				phenoDataSetFields=iPhenotypicService.getLinkedPhenoDataSetFieldsForSelectedCategories(study, arkFunction, arkUser, tempCategories);
				tempCategories.clear();
				if(phenoDataSetFields.size()>0){
					List<Object> fieldList=new ArrayList<Object>();
					for (PhenoDataSetField phenoDataSetField : phenoDataSetFields) {
						fieldList.add(phenoDataSetField.getName());
					}
					categoryList.add(fieldList);
					phenoDataSetFields.clear();
				}
			}
			return categoryList;
		}
	
	
	
}

