package au.org.theark.phenotypic.web.component.customfieldgroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author nivedann
 *
 */
public class SearchResultListPanel extends Panel{

	
	private CompoundPropertyModel<CustomFieldGroupVO> cpmModel;
	private FeedbackPanel	feedbackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private DataView<CustomFieldGroup> dataView;
	private ArkDataProvider2<CustomFieldGroup, CustomFieldGroup> arkDataProvider;
	
	/**
	 * Service references
	 */
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;
	
	/**
	 * 
	 * @param id
	 * @param cpModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 */
	public SearchResultListPanel(String id, CompoundPropertyModel<CustomFieldGroupVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel){
		
		super(id);
		this.cpmModel  = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		
	}
	
	public DataView<CustomFieldGroup> buildDataView(ArkDataProvider2<CustomFieldGroup, CustomFieldGroup> provider){
		
		DataView<CustomFieldGroup> dataView = new DataView<CustomFieldGroup>("customFieldGroupList", provider){

			@Override
			protected void populateItem(Item<CustomFieldGroup> item) {
				
				CustomFieldGroup customFieldGroup = item.getModelObject();
				
				//ID has to be NOT null if it is coming from the backend
				item.add(new Label("id", customFieldGroup.getId().toString()));
				
				item.add(buildLink(item));
				//TODO Escape any special characters
				if(customFieldGroup.getDescription() != null){
					item.add( new Label("description", customFieldGroup.getDescription().trim()));
				}else{
					item.add(new Label("description", ""));
				}
				
				if(customFieldGroup.getPublished()){
					item.add( new Label("published", "Yes"));
				}else{
					item.add( new Label("published", "No"));
				}
					
				
			}
			
		};
		
		return dataView;
	}
	
	public WebMarkupContainer buildLink(final Item<CustomFieldGroup> item){
		
		WebMarkupContainer linkWmc = new WebMarkupContainer("customfieldGroupLinkWMC", item.getModel());
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("name") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				//TODO
				CustomFieldGroup cfg =  (CustomFieldGroup) (getParent().getDefaultModelObject());
				
				
				CompoundPropertyModel<CustomFieldGroupVO> newModel = new CompoundPropertyModel<CustomFieldGroupVO>( new CustomFieldGroupVO());
				
						//details page
				CustomFieldGroup itemSelected = item.getModelObject();
				
				CustomField customFieldCriteria = new CustomField();
				ArkFunction arkFunction  =iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
				customFieldCriteria.setStudy(cfg.getStudy());
				customFieldCriteria.setArkFunction(arkFunction);
				Collection<CustomField> availableList = iArkCommonService.getCustomFieldList(customFieldCriteria);
				Collection<CustomField> selectedList  = iPhenotypicService.getCustomFieldsLinkedToCustomFieldGroup(itemSelected);
				newModel.getObject().setAvailableCustomFields(availableList);
				newModel.getObject().setSelectedCustomFields(selectedList);
				newModel.getObject().setCustomFieldGroup(cfg);
				CustomFieldGroupDetailPanel detailPanel = new CustomFieldGroupDetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO, newModel,true);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
				
				Collection list = iPhenotypicService.getCFDLinkedToQuestionnaire(cfg);
				
				TextField<String> questionnaireName = (TextField<String>)arkCrudContainerVO.getDetailPanelFormContainer().get("customFieldGroup.name");
				questionnaireName.setEnabled(false);
				
			

				//The list of CFD must be displayed on the Detail form
				//Create a CFD List Panel here and add it to the detailForm.
				
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
				// Button containers
				// View Field, thus view container visible
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);// saveBtn
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);
			
				target.add(arkCrudContainerVO.getSearchPanelContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getViewButtonContainer());
				target.add(arkCrudContainerVO.getEditButtonContainer());
				target.add(arkCrudContainerVO.getDetailPanelFormContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
		
			}
			
		};
		
		CustomFieldGroup customFieldGroup = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", customFieldGroup.getName());
		link.add(nameLinkLabel);
		linkWmc.add(link);
		return linkWmc;
	}
}
