package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.pheno.entity.*;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {


	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private CompoundPropertyModel<PhenoDataSetFieldGroupVO>					cpmModel;
	private FeedbackPanel												feedbackPanel;
	private ArkCrudContainerVO											arkCrudContainerVO;
//	private DataView<CustomFieldDisplay>								cfdDataView;
	private ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay>	cfdArkDataProvider;
	/**
	 * Service references
	 */
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService														iArkCommonService;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService														iPhenotypicService;
	
	private Study study;
	private ArkFunction arkFunction;
	private ArkUser arkUser;

	/**
	 * 
	 * @param id
	 * @param cpModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 */
	public SearchResultListPanel(String id, CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpmModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<PhenoDataSetGroup> buildDataView(ArkDataProvider2<PhenoDataSetGroup, PhenoDataSetGroup> provider) {

		DataView<PhenoDataSetGroup> dataView = new DataView<PhenoDataSetGroup>("phenoDataSetGroupList", provider) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<PhenoDataSetGroup> item) {

				PhenoDataSetGroup phenoDataSetGroup = item.getModelObject();

				// ID has to be NOT null if it is coming from the backend
				item.add(new Label("id", phenoDataSetGroup.getId().toString()));

				item.add(buildLink(item));
				// TODO Escape any special characters
				if (phenoDataSetGroup.getDescription() != null) {
					item.add(new Label("description", phenoDataSetGroup.getDescription().trim()));
				}
				else {
					item.add(new Label("description", ""));
				}

				if (phenoDataSetGroup.getPublished()) {
					item.addOrReplace(new ContextImage("published", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.addOrReplace(new ContextImage("published", new Model<String>("images/icons/cross.png")));
				}

				// For the alternative stripes
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		return dataView;
	}

	@SuppressWarnings("unchecked")
	public WebMarkupContainer buildLink(final Item<PhenoDataSetGroup> item) {
		WebMarkupContainer linkWmc = new WebMarkupContainer("phenoDataSetGroupLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("name") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				final PhenoDataSetGroup itemSelected = item.getModelObject();
				//PhenoDataSetGroup phenoDataSetGroup = (PhenoDataSetGroup) (getParent().getDefaultModelObject());
				CompoundPropertyModel<PhenoDataSetFieldGroupVO> newModel = new CompoundPropertyModel<PhenoDataSetFieldGroupVO>(new PhenoDataSetFieldGroupVO());
				study=cpmModel.getObject().getPhenoDataSetGroup().getStudy();
				arkFunction=cpmModel.getObject().getPhenoDataSetGroup().getArkFunction();
				arkUser=cpmModel.getObject().getArkUser();
				//initialize the table before proceed.
				iPhenotypicService.deletePickedCategoriesAndAllTheirChildren(study, arkFunction, arkUser);
				newModel.getObject().setPhenoDataSetGroup(itemSelected);
				newModel.getObject().setPhenoDataSetFieldCategoryLst(cpmModel.getObject().getPhenoDataSetFieldCategoryLst());
				newModel.getObject().setAvailablePhenoDataSetFields(cpmModel.getObject().getAvailablePhenoDataSetFields());
				List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays=iPhenotypicService.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(itemSelected);
				for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplays) {
					if(phenoDataSetFieldDisplay.getPhenoDataSetField()!=null){
						createPickedPhenoDataSetCategory(phenoDataSetFieldDisplay,target);
						createLinkPhenoDataSetCategoryField(phenoDataSetFieldDisplay,target);
					}else{
						createPickedPhenoDataSetCategory(phenoDataSetFieldDisplay,target);
					}
				}
				// Data provider to paginate a list of CustomFieldDisplays linked to the CustomFieldGroup
				cfdArkDataProvider = new ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay>() {
					private static final long serialVersionUID = 1L;
					public long size() {
						return iPhenotypicService.getCFDLinkedToQuestionnaireCount(itemSelected);
					}
					public Iterator<PhenoDataSetFieldDisplay> iterator(long first, long count) {

						Collection<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplayList = new ArrayList<PhenoDataSetFieldDisplay>();
						phenoDataSetFieldDisplayList = iPhenotypicService.getCFDLinkedToQuestionnaire(itemSelected, first, count);
						return phenoDataSetFieldDisplayList.iterator();
					}
				};

				DataDictionaryGroupDetailPanel detailPanel = new DataDictionaryGroupDetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO, newModel, cfdArkDataProvider, true);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
				TextField<String> questionnaireName = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get("phenoDataSetGroup.name");
				questionnaireName.setEnabled(false);

				// The list of CFD must be displayed on the Detail form
				// Create a CFD List Panel here and add it to the detailForm.
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};

		PhenoDataSetGroup phenoDataSetGroup = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", phenoDataSetGroup.getName());
		link.add(nameLinkLabel);
		linkWmc.add(link);
		return linkWmc;
	}
	private void createPickedPhenoDataSetCategory(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay,AjaxRequestTarget target) {
		PickedPhenoDataSetCategory pickedPhenoDataSetCategory=new PickedPhenoDataSetCategory();
		pickedPhenoDataSetCategory.setStudy(study);
		pickedPhenoDataSetCategory.setArkFunction(arkFunction);
		pickedPhenoDataSetCategory.setArkUser(arkUser);
		pickedPhenoDataSetCategory.setPhenoDataSetCategory(phenoDataSetFieldDisplay.getPhenoDataSetCategory()); 
		//Set parent.
		pickedPhenoDataSetCategory.
		setParentPickedPhenoDataSetCategory(iPhenotypicService.
				getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, phenoDataSetFieldDisplay.getParentPhenoDataSetCategory()));
		pickedPhenoDataSetCategory.setSelected(false);
		pickedPhenoDataSetCategory.setOrderNumber(phenoDataSetFieldDisplay.getPhenoDataSetCategoryOrderNumber());
		//Check for exist before create again.
		if(!isPickedPhenoDataSetCategoryAlreadyExsists(phenoDataSetFieldDisplay.getPhenoDataSetCategory())){
			try {
				iPhenotypicService.createPickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException| ArkRunTimeException | EntityExistsException |LazyInitializationException e) {
				error("Problem occurs during the dataset initialisation(save category)please try again.");
				target.add(feedbackPanel);
			}
		}
	}

	private void createLinkPhenoDataSetCategoryField(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay,AjaxRequestTarget target) {
		LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=new LinkPhenoDataSetCategoryField();
			linkPhenoDataSetCategoryField.setStudy(study);
			linkPhenoDataSetCategoryField.setArkFunction(arkFunction);
			linkPhenoDataSetCategoryField.setArkUser(arkUser);
			linkPhenoDataSetCategoryField.setPhenoDataSetCategory(phenoDataSetFieldDisplay.getPhenoDataSetCategory());
			linkPhenoDataSetCategoryField.setPhenoDataSetField(phenoDataSetFieldDisplay.getPhenoDataSetField());
			linkPhenoDataSetCategoryField.setOrderNumber(phenoDataSetFieldDisplay.getPhenoDataSetFiledOrderNumber());
			try {
				iPhenotypicService.createLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
			} catch (ArkSystemException| ArkRunTimeUniqueException| ArkRunTimeException| EntityExistsException |LazyInitializationException e ) {
				error("Problem occurs during the dataset initialisation(save field)please try again.");
				target.add(feedbackPanel);
			}
	}
	private boolean isPickedPhenoDataSetCategoryAlreadyExsists(PhenoDataSetCategory phenoDataSetCategory){
		PickedPhenoDataSetCategory pickedPhenoDataSetCategory=iPhenotypicService.getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, phenoDataSetCategory);
		if(pickedPhenoDataSetCategory!=null)
			return true;
		else
			return 	false;
	}

}
