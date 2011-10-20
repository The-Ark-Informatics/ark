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

import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

public class CustomFieldDisplayListPanel extends Panel {

	protected AbstractDetailModalWindow	modalWindow;
	private Panel modalContentPanel;
	private FeedbackPanel feedbackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	
	/**
	 * Constructor
	 * @param id
	 * @param feedbackPanel
	 * @param arkCrudContainerVO
	 */
	public CustomFieldDisplayListPanel(String id, FeedbackPanel feedbackPanel,ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO; 
		setOutputMarkupPlaceholderTag(true);
	}
	
	public void initialisePanel(){
	
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				
				target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
			}

		};
		
		add(modalWindow);
	}
	
	public DataView<CustomFieldDisplay> buildDataView(ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> provider){
		
		
		DataView<CustomFieldDisplay> dataView = new DataView<CustomFieldDisplay>("customFieldDisplayList", provider){

			@Override
			protected void populateItem(Item<CustomFieldDisplay> item) {
				
				CustomFieldDisplay cfd  = item.getModelObject();
				
				item.add(buildLink(item));
				if(cfd.getSequence() != null){
					item.add( new Label("sequence",cfd.getSequence().toString()));
				}else{
					item.add(new Label("sequence",""));
				}
				
				if(cfd.getRequired() != null && cfd.getRequired()){
					item.add( new Label("required", "Yes"));
				}else{
					item.add( new Label("required", "No"));
				}
				
				
				if(cfd.getRequiredMessage() != null){
					item.add( new Label("requiredMessage",cfd.getRequiredMessage()));
				}else{
					item.add(new Label("requiredMessage",""));
				}
			}
		};
		
		return dataView;
	}
	
	
	public WebMarkupContainer buildLink(final Item<CustomFieldDisplay> item){
		
		WebMarkupContainer linkWmc = new WebMarkupContainer("cfdLinkWmc", item.getModel());
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("id") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				CustomFieldDisplay customFieldDisplayToEdit = iArkCommonService.getCustomFieldDisplay(item.getModelObject().getId());
				CustomFieldGroupVO cfgVO = new CustomFieldGroupVO();
				cfgVO.setCustomFieldDisplay(customFieldDisplayToEdit);
				CompoundPropertyModel<CustomFieldGroupVO> newCpmModel = new CompoundPropertyModel<CustomFieldGroupVO>(cfgVO);
				showModalWindow(target, newCpmModel);
				//refresh the list
				
			}
		};
		
		
		CustomFieldDisplay customFieldDisplay = item.getModelObject();
		Label idLink = new Label("idLabel", customFieldDisplay.getId().toString());
		link.add(idLink);
		linkWmc.add(link);
		return linkWmc;
	}
	
	/**
	 * 
	 * @param target
	 */
	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<CustomFieldGroupVO> cpmModel){
		
		modalContentPanel = new CustomFieldDisplayModalPanel("content",modalWindow,cpmModel,feedbackPanel);
		modalWindow.setTitle("Custom Field Display Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}
	

}
