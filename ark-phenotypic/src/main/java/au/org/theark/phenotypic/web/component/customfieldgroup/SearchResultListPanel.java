package au.org.theark.phenotypic.web.component.customfieldgroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
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
	private CompoundPropertyModel<CustomFieldGroupVO>					cpmModel;
	private FeedbackPanel												feedbackPanel;
	private ArkCrudContainerVO											arkCrudContainerVO;
//	private DataView<CustomFieldDisplay>								cfdDataView;
	private ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay>	cfdArkDataProvider;

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
	public SearchResultListPanel(String id, CompoundPropertyModel<CustomFieldGroupVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpmModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<CustomFieldGroup> buildDataView(ArkDataProvider2<CustomFieldGroup, CustomFieldGroup> provider) {

		DataView<CustomFieldGroup> dataView = new DataView<CustomFieldGroup>("customFieldGroupList", provider) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<CustomFieldGroup> item) {

				CustomFieldGroup customFieldGroup = item.getModelObject();

				// ID has to be NOT null if it is coming from the backend
				item.add(new Label("id", customFieldGroup.getId().toString()));

				item.add(buildLink(item));
				// TODO Escape any special characters
				if (customFieldGroup.getDescription() != null) {
					item.add(new Label("description", customFieldGroup.getDescription().trim()));
				}
				else {
					item.add(new Label("description", ""));
				}

				if (customFieldGroup.getPublished()) {
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
	public WebMarkupContainer buildLink(final Item<CustomFieldGroup> item) {

		WebMarkupContainer linkWmc = new WebMarkupContainer("customfieldGroupLinkWMC", item.getModel());

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("name") {


			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				final CustomFieldGroup itemSelected = item.getModelObject();

				CustomFieldGroup cfg = (CustomFieldGroup) (getParent().getDefaultModelObject());
				CompoundPropertyModel<CustomFieldGroupVO> newModel = new CompoundPropertyModel<CustomFieldGroupVO>(new CustomFieldGroupVO());

				CustomField customFieldCriteria = new CustomField();
				//ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
				customFieldCriteria.setStudy(cfg.getStudy());
				customFieldCriteria.setArkFunction(arkFunction);

				Collection<CustomField> availableList = iArkCommonService.getCustomFieldList(customFieldCriteria);

				ArrayList<CustomField> selectedList = (ArrayList) iPhenotypicService.getCustomFieldsLinkedToCustomFieldGroup(itemSelected);

				newModel.getObject().setAvailableCustomFields(availableList);
				newModel.getObject().setSelectedCustomFields(selectedList);
				newModel.getObject().setCustomFieldGroup(cfg);

				// Data provider to paginate a list of CustomFieldDisplays linked to the CustomFieldGroup
				cfdArkDataProvider = new ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay>() {

					private static final long serialVersionUID = 1L;

					public int size() {
						return (int)iPhenotypicService.getCFDLinkedToQuestionnaireCount(itemSelected);
					}

					public Iterator<CustomFieldDisplay> iterator(int first, int count) {

						Collection<CustomFieldDisplay> customFieldDisplayList = new ArrayList<CustomFieldDisplay>();
						customFieldDisplayList = iPhenotypicService.getCFDLinkedToQuestionnaire(itemSelected, first, count);
						return customFieldDisplayList.iterator();
					}
				};

				CustomFieldGroupDetailPanel detailPanel = new CustomFieldGroupDetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO, newModel, cfdArkDataProvider, true);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);

				TextField<String> questionnaireName = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get("customFieldGroup.name");
				questionnaireName.setEnabled(false);

				// The list of CFD must be displayed on the Detail form
				// Create a CFD List Panel here and add it to the detailForm.
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}

		};

		CustomFieldGroup customFieldGroup = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", customFieldGroup.getName());
		link.add(nameLinkLabel);
		linkWmc.add(link);
		return linkWmc;
	}

}
