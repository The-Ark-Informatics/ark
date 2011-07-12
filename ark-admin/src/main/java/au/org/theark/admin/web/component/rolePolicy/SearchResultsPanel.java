package au.org.theark.admin.web.component.rolePolicy;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;

public class SearchResultsPanel extends Panel
{	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5237384531161620862L;

	@SuppressWarnings("unchecked")
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SuppressWarnings("unused")
	@SpringBean( name="arkLdapRealm")
	private ArkLdapRealm realm;
	
	private ContainerForm containerForm;
	
	private ArkCrudContainerVO arkCrudContainerVo;
	
	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo)
	{
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<ArkRolePolicyTemplate> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer)
	{	
		PageableListView<ArkRolePolicyTemplate> pageableListView = new PageableListView<ArkRolePolicyTemplate>("arkRolePolicyTemplateList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) 
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3350183112731574263L;

			@Override
			protected void populateItem(final ListItem<ArkRolePolicyTemplate> item) {
				
				ArkRolePolicyTemplate arkRolePolicyTemplate = item.getModelObject();
				
				item.add(buildLink(arkRolePolicyTemplate,searchResultsContainer));
				
				if(arkRolePolicyTemplate.getArkRole() != null)
				{
					//the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkRole", arkRolePolicyTemplate.getArkRole().getName()));	
				}
				else
				{
					item.add(new Label("arkRolePolicyTemplate.arkRole", ""));
				}
				
				if(arkRolePolicyTemplate.getArkModule() != null)
				{
					//the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkModule", arkRolePolicyTemplate.getArkModule().getName()));	
				}
				else
				{
					item.add(new Label("arkRolePolicyTemplate.arkModule", ""));
				}
				
				if(arkRolePolicyTemplate.getArkFunction() != null)
				{
					//the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkFunction", arkRolePolicyTemplate.getArkFunction().getName()));	
				}
				else
				{
					item.add(new Label("arkRolePolicyTemplate.arkFunction", ""));
				}
				
				if(arkRolePolicyTemplate.getArkPermission() != null)
				{
					String permissionString = arkRolePolicyTemplate.getArkPermission().getName();
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("CREATE"))
					{
						item.add(new Label("arkCreatePermission", permissionString));
					}
					else
					{
						item.add(new Label("arkCreatePermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("READ"))
					{
						item.add(new Label("arkReadPermission", permissionString));
					}
					else
					{
						item.add(new Label("arkReadPermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("UPDATE"))
					{
						item.add(new Label("arkUpdatePermission", permissionString));
					}
					else
					{
						item.add(new Label("arkUpdatePermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("DELETE"))
					{
						item.add(new Label("arkDeletePermission", permissionString));
					}
					else
					{
						item.add(new Label("arkDeletePermission", ""));
					}
				}
				
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return pageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final ArkRolePolicyTemplate arkRolePolicyTemplate, final WebMarkupContainer searchResultsContainer)
	{	
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") 
		{
			@Override
			public void onClick(AjaxRequestTarget target) 
			{				
				Long id = arkRolePolicyTemplate.getId();
				ArkRolePolicyTemplate arkRolePolicyTemplate = iArkCommonService.getArkRolePolicyTemplate(id); 
				containerForm.getModelObject().setArkRolePolicyTemplate(arkRolePolicyTemplate);
				
				List<ArkModule> arkModuleList =  iArkCommonService.getArkModuleList();
				containerForm.getModelObject().setArkModuleList(arkModuleList);
				
				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVo.getViewButtonContainer().setVisible(true);
				arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVo.getEditButtonContainer().setVisible(false);
				
				// Refresh the markup containers
				target.addComponent(arkCrudContainerVo.getSearchResultPanelContainer());
				target.addComponent(arkCrudContainerVo.getDetailPanelContainer());
				target.addComponent(arkCrudContainerVo.getDetailPanelFormContainer());
				target.addComponent(arkCrudContainerVo.getSearchPanelContainer());
				target.addComponent(arkCrudContainerVo.getViewButtonContainer());
				target.addComponent(arkCrudContainerVo.getEditButtonContainer());
				
				// Refresh base container form to remove any feedBack messages
				target.addComponent(containerForm);
			}
		};
		
		//Add the label for the link
		Label linkLabel = new Label("arkRolePolicyTemplate.id", arkRolePolicyTemplate.getId().toString());
		link.add(linkLabel);
		return link;
	}
}
