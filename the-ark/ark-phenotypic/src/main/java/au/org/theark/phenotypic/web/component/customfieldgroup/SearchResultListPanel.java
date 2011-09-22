package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider2;

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
				CompoundPropertyModel<CustomFieldGroup> newModel = new CompoundPropertyModel<CustomFieldGroup>( new CustomFieldGroup());
				//Get the CustomFieldGroup from backend along with the Custom Fields for the study and arkfunction and then instantiate the details page
				
			}
			
		};
		
		CustomFieldGroup customFieldGroup = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", customFieldGroup.getName());
		link.add(nameLinkLabel);
		linkWmc.add(link);
		return linkWmc;
	}
}
