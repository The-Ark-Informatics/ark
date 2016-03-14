package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

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

		DataView<PhenoDataSetGroup> dataView = new DataView<PhenoDataSetGroup>("customFieldGroupList", provider) {


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

		WebMarkupContainer linkWmc = new WebMarkupContainer("customfieldGroupLinkWMC", item.getModel());

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("name") {


			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				final PhenoDataSetGroup itemSelected = item.getModelObject();

				PhenoDataSetGroup phenoDataSetGroup = (PhenoDataSetGroup) (getParent().getDefaultModelObject());
				CompoundPropertyModel<PhenoDataSetFieldGroupVO> newModel = new CompoundPropertyModel<PhenoDataSetFieldGroupVO>(new PhenoDataSetFieldGroupVO());

				PhenoDataSetField phenoDataSetFieldCriteria = new PhenoDataSetField();
				//ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				phenoDataSetFieldCriteria.setStudy(phenoDataSetGroup.getStudy());
				phenoDataSetFieldCriteria.setArkFunction(arkFunction);

				//Collection<PhenoDataSetField> availableList = iArkCommonService.getCustomFieldList(phenoDataSetFieldCriteria);
				List<PhenoDataSetField> availableList =iPhenotypicService.getPhenoDataSetFieldList(phenoDataSetFieldCriteria);
				
				List<PhenoDataSetField> selectedList = (ArrayList) iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(itemSelected);

				newModel.getObject().setAvailablePhenoDataSetFields(availableList);
				newModel.getObject().setSelectedPhenoDataSetFields(selectedList);
				newModel.getObject().setPhenoDataSetGroup(phenoDataSetGroup);

				// Data provider to paginate a list of CustomFieldDisplays linked to the CustomFieldGroup
				cfdArkDataProvider = new ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay>() {

					private static final long serialVersionUID = 1L;

					public int size() {
						return (int)iPhenotypicService.getCFDLinkedToQuestionnaireCount(itemSelected);
					}

					public Iterator<PhenoDataSetFieldDisplay> iterator(int first, int count) {

						Collection<PhenoDataSetFieldDisplay> customFieldDisplayList = new ArrayList<PhenoDataSetFieldDisplay>();
						customFieldDisplayList = iPhenotypicService.getCFDLinkedToQuestionnaire(itemSelected, first, count);
						return customFieldDisplayList.iterator();
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

}
