package au.org.theark.admin.web.component.rolePolicy;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
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
	@SuppressWarnings("unused")
	private TabbedPanel moduleTabbedPanel;
	
	public SearchResultsPanel(String id, ContainerForm containerForm){
		super(id);
		this.containerForm = containerForm;
	}
	
	public SearchResultsPanel(String id, ContainerForm containerForm, TabbedPanel moduleTabbedPanel){
		super(id);
		this.containerForm = containerForm;
		this.moduleTabbedPanel = moduleTabbedPanel;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<ArkRolePolicyTemplate> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer)
	{	
		PageableListView<ArkRolePolicyTemplate> studyPageableListView = new PageableListView<ArkRolePolicyTemplate>("arkRoleTemplatePolicyList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) 
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
					item.add(new Label("arkRoleTemplatePolicy.arkRole", arkRolePolicyTemplate.getArkRole().getName()));	
				}
				else
				{
					item.add(new Label("arkRoleTemplatePolicy.arkRole", ""));
				}
				
				if(arkRolePolicyTemplate.getArkModule() != null)
				{
					//the ID here must match the ones in mark-up
					item.add(new Label("arkRoleTemplatePolicy.arkModule", arkRolePolicyTemplate.getArkModule().getName()));	
				}
				else
				{
					item.add(new Label("arkRoleTemplatePolicy.arkModule", ""));
				}
				
				if(arkRolePolicyTemplate.getArkFunction() != null)
				{
					//the ID here must match the ones in mark-up
					item.add(new Label("arkRoleTemplatePolicy.arkFunction", arkRolePolicyTemplate.getArkFunction().getName()));	
				}
				else
				{
					item.add(new Label("arkRoleTemplatePolicy.arkRole", ""));
				}
				
				if(arkRolePolicyTemplate.getArkPermission() != null)
				{
					String permissionString = arkRolePolicyTemplate.getArkPermission().getName();
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("CREATE"))
					{
						item.add(new Label("arkRoleTemplatePolicy.createPermission", permissionString));
					}
					else
					{
						item.add(new Label("arkRoleTemplatePolicy.createPermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("READ"))
					{
						item.add(new Label("arkRoleTemplatePolicy.readPermission", permissionString));
					}
					else
					{
						item.add(new Label("arkRoleTemplatePolicy.readPermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("UPDATE"))
					{
						item.add(new Label("arkRoleTemplatePolicy.updatePermission", permissionString));
					}
					else
					{
						item.add(new Label("arkRoleTemplatePolicy.updatePermission", ""));
					}
					
					//the ID here must match the ones in mark-up
					if(arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("DELETE"))
					{
						item.add(new Label("arkRoleTemplatePolicy.deletePermission", permissionString));
					}
					else
					{
						item.add(new Label("arkRoleTemplatePolicy.deletePermission", ""));
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
		return studyPageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final ArkRolePolicyTemplate arkRolePolicyTemplate, final WebMarkupContainer searchResultsContainer) {
		
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
				
				// Refresh base container form to remove any feedBack messages
				target.addComponent(containerForm);
			}
		};
		
		//Add the label for the link
		Label linkLabel = new Label("arkRoleTemplatePolicy.id", arkRolePolicyTemplate.getId().toString());
		link.add(linkLabel);
		return link;
	}
}
