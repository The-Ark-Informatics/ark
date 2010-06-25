package au.org.theark.study.web.component.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InvalidNameException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.ui.accordion.Accordion;

import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.RoleVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.AppRoleForm;
import au.org.theark.core.util.UIHelper;

public class AppRoleAccordion extends Panel{

	private EtaUserVO etaUserVO;
	private static final long serialVersionUID = 1L;
	private List<ModuleVO> membershipModules;
	
	
	@SuppressWarnings("unchecked")
	public AppRoleAccordion(String id, EtaUserVO etaUserVO, List<ModuleVO> moduleList){
		super(id);
		this.etaUserVO = etaUserVO;//Set the private instance of etaUserVO
		membershipModules = etaUserVO.getModules();//List of Applications the user is a member of
		final AppRoleForm appRoleForm = new AppRoleForm(Constants.APP_ROLE_FORM, etaUserVO);
		
		//Create an instance of Wiquery Accordion widget
		Accordion moduleAccordion = new Accordion(Constants.ACCORDION);
		
		//Build the sections dynamically for the Accordion
		ListView sectionListView = new ListView(Constants.ACCORDION_SECTION, moduleList){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem listItem) {
				
				ModuleVO currentModule = ((ModuleVO)listItem.getDefaultModelObject());
				String moduleName = currentModule.getModule();
				
				Label sectionName = new Label(Constants.ACCORDION_SECTION_NAME_LBL, moduleName);
				listItem.setRenderBodyOnly(true);//Excludes excess tags on the markup
				listItem.add(sectionName);

				WebMarkupContainer groupSelectorContainer;
				String displayModule;
				ModuleVO selectedModule = new ModuleVO();

				for(ModuleVO mod :membershipModules){
					 displayModule = UIHelper.getDisplayModuleName(mod.getModule());
					if(displayModule.equals(moduleName)){
						selectedModule = mod;
						break;
					}
				}
				groupSelectorContainer  = initGroupSelectorContainer(appRoleForm, moduleName, selectedModule.getRole(),currentModule.getRole());
				listItem.add(groupSelectorContainer);
			}
		};
		
		moduleAccordion.add(sectionListView);
		appRoleForm.add(moduleAccordion);//Add the accordion to the form
		add(appRoleForm);
	}
	
	/**
	 * This method will initialise the multi select controls and the buttons and then add it to the container.
	 * 
	 * @param form
	 * @param availableRoleChoices
	 * @param selectedRolesLMC
	 * @return
	 */
	private WebMarkupContainer initGroupSelectorContainer(Form form, String moduleName, List<RoleVO> currentRoles, List<RoleVO> availableRoles) {
		
		WebMarkupContainer container = new WebMarkupContainer(Constants.AJAX_CONTAINER);
		//Ensures that the html markup for components under this container are all refreshed along with their current state. 
		container.setOutputMarkupId(true);
		
		//Create and initialise the Available Roles Select Control and as a pre-requisite it needs a reference to Selected Roles MLC
		List<String> selectedChoices = new ArrayList<String>();
		if(etaUserVO.getMode() == Constants.MODE_EDIT){
			for(RoleVO roleVO: currentRoles){
				selectedChoices.add(UIHelper.getDisplayRoleName(roleVO.getRole()));
			}
		}
		
		ListMultipleChoice selectedRolesLMC = new ListMultipleChoice(Constants.SELECTED_ROLES_MLC, new Model(),selectedChoices);
		container.add(selectedRolesLMC);
		
		ListMultipleChoice availableRolesLMC = initAvailableRoleMLC(container,selectedRolesLMC, moduleName, availableRoles);
		container.add(availableRolesLMC);
		
		AjaxButton addAllButton = initialiseAddAllButton(form, availableRolesLMC.getChoices(), selectedRolesLMC, container);
		container.add(addAllButton);
		
		AjaxButton addSelectedButton = initialiseAddButton(container,form,availableRolesLMC,selectedRolesLMC);
		container.add(addSelectedButton);
		
		AjaxButton removeAllButton = initialiseRemoveAllButton(form,selectedRolesLMC,container);
		container.add(removeAllButton);
		
		AjaxButton removeButton = initialiseRemoveButton(container, form, selectedRolesLMC);
		container.add(removeButton);
		
		return container;
	}
	
	
	/**
	 * Method to populate the Multi Select control.
	 * @param WebMarkupContainer that will contain the  ListMultipleChoice controls
	 * @throws Exception 
	 * @throws InvalidNameException 
	 */
	private ListMultipleChoice initAvailableRoleMLC(final WebMarkupContainer container, final ListMultipleChoice selectedRolesLMC, String moduleName, List<RoleVO> availableRoles)  {
		List<String> roles = new ArrayList<String>();
		for(RoleVO role: availableRoles){
			roles.add(role.getRole());
		}

		final ListMultipleChoice availableRolesLMC = new ListMultipleChoice(Constants.AVAILABLE_ROLES_MLC, new Model(), roles);
		
		availableRolesLMC.add( new AjaxFormComponentUpdatingBehavior("ondblclick"){

			@Override
			protected void onUpdate(AjaxRequestTarget ajaxrequesttarget) {
				
				List<String> selectedItems = (List<String>) availableRolesLMC.getModelObject();
				addSelectedItems(selectedItems, selectedRolesLMC);
				ajaxrequesttarget.addComponent(container);
				
			}
		});
		return availableRolesLMC;
	}
	
	/**
	 * Adds the selected items into the target multiselect control if the item(s) is not in the
	 * target choice list.
	 * @param selectedItems
	 * @param targetMLC
	 */
	private void addSelectedItems(List<String> selectedItems, ListMultipleChoice targetMLC) {
		if (selectedItems == null) {
			return;
		}
		
		Iterator<String> it = selectedItems.iterator();
		while(it.hasNext()){
			String member = it.next();
			if(!targetMLC.getChoices().contains(member)){
				targetMLC.getChoices().add(member);
			}
		}
	}
	
	
	private AjaxButton initialiseAddButton(final WebMarkupContainer container, Form form, final ListMultipleChoice availableRolesLMC, final ListMultipleChoice targetMLC){
		
		AjaxButton ajaxButton = new AjaxButton(Constants.ADD_SELECTED){
			@Override
			protected void onSubmit(AjaxRequestTarget requestTarget, Form<?> arg1) {
				List<String> selectedChoice = new ArrayList<String>();
				//Get the items selected from the control's MODEL
				selectedChoice = (List<String>)availableRolesLMC.getModelObject();
				addSelectedItems(selectedChoice, targetMLC);
				requestTarget.addComponent(container);
			}
		};
		ajaxButton.setModel(new StringResourceModel("addSelectedTxt",form,null));
		return ajaxButton;
	}
	
	private AjaxButton initialiseRemoveButton(final WebMarkupContainer container, Form form,  final ListMultipleChoice targetMLC){
		
		AjaxButton ajaxButton = new AjaxButton(Constants.REMOVE_SELECTED_BUTTON){
			@Override
			protected void onSubmit(AjaxRequestTarget requestTarget, Form<?> arg1) {
				List<String> selectedItems = (List<String>)targetMLC.getChoices();
				targetMLC.getChoices().removeAll(selectedItems);
				requestTarget.addComponent(container);
			}
		};
		ajaxButton.setModel(new StringResourceModel("removeSelectedTxt",form,null));
		return ajaxButton;
	}

	
	/**
	 * Method that builds an Ajax Submit button
	 */
	private AjaxButton initialiseAddAllButton(Form form, final List<String> availableChoices, final ListMultipleChoice selectedRolesLMC, final WebMarkupContainer container ){
		
		AjaxButton addAllAjaxButton = new AjaxButton(Constants.ADD_ALL_BUTTON, form) {
			private static final long serialVersionUID = 663764401173690603L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				if(selectedRolesLMC.getChoices().size() > 0){
					List<String> selectedItems = (List<String>)selectedRolesLMC.getChoices();
					selectedRolesLMC.getChoices().removeAll(selectedItems);	
				}
				System.out.println("Submit called");
				
				selectedRolesLMC.getChoices().addAll(availableChoices);
				target.addComponent(container);
			}
			
			protected void onError(AjaxRequestTarget target, Form form){
				System.out.println("An error occured");
				
			}
		};
		
		addAllAjaxButton.setModel(new StringResourceModel(Constants.ADD_ALL_BUTTON,form, null));
		return addAllAjaxButton;
	}
	
	private AjaxButton initialiseRemoveAllButton(Form form, final ListMultipleChoice selectedRolesLMC, final WebMarkupContainer container){
		
		AjaxButton removeAllAjaxButton = new AjaxButton(Constants.REMOE_ALL_BUTTON, form){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if(selectedRolesLMC.getChoices().size() > 0){
					List<String> selectedItems = (List<String>)selectedRolesLMC.getChoices();
					selectedRolesLMC.getChoices().removeAll(selectedItems);	
				}
				target.addComponent(container);
			}
			
			protected void onError(AjaxRequestTarget target, Form form){
				System.out.println("An error occured when user clicked on Remove All");
				
			}
		};
		
		removeAllAjaxButton.setModel(new StringResourceModel(Constants.REMOE_ALL_BUTTON,form,null));
		return removeAllAjaxButton;
		
	}

	/**
	 * Gains access to the custom Accordion Component and the associated Form object.
	 * It uses the Form object to traverse the child components and determine the selected
	 * values of the Application Roles.
	 * @param form
	 * @return
	 */

	public static boolean validateRoles(Form form){
		
		boolean isValid = false;
		
		ListView sectionListView = getAccordionSections(form);
		
		for (Iterator iterator = sectionListView.iterator(); iterator.hasNext();) {
			ListItem listItem = (ListItem) iterator.next();
			WebMarkupContainer container = (WebMarkupContainer)listItem.get(Constants.AJAX_CONTAINER);
			ListMultipleChoice selectedRolesLMC = (ListMultipleChoice)container.get(Constants.SELECTED_ROLES_MLC);
			if (selectedRolesLMC.getChoices().size() > 0){
				isValid = true;
				return true;
			}
			List<String> selectedItems  = (List<String>)selectedRolesLMC.getChoices();
			for (Iterator iterator2 = selectedItems.iterator(); iterator2
					.hasNext();) {
				String role = (String) iterator2.next();
				System.out.println("Selected Roles: " + role);
				
			}
		}
		return isValid;
	}
	
	public static ListView getAccordionSections(Form parentForm){
		AppRoleAccordion ma  = (AppRoleAccordion)parentForm.get(Constants.APP_ROLE_ACCORDION);
		AppRoleForm aForm = (AppRoleForm)  ma.get(Constants.APP_ROLE_FORM);
		Accordion ac =  (Accordion)aForm.get(Constants.ACCORDION);
		return  (ListView)ac.get(Constants.ACCORDION_SECTION);	
	}
	
	/**
	 * A helper that returns the selected applications and roles in a class
	 */
	public static void getSelectedAppRoles(Form parentForm, EtaUserVO etaUserVO){
		
		List<ModuleVO> moduleVOlist = new ArrayList<ModuleVO>();
		etaUserVO.setModules(moduleVOlist);
		
		/* The following code is redundant and should be moved into a helper, that will return the ListView instance*/
		ListView sectionListView = getAccordionSections(parentForm);
		for (Iterator iterator = sectionListView.iterator(); iterator.hasNext();) {
			
			ListItem listItem = (ListItem) iterator.next();
			//Application Name
			Label sectionName  = (Label)listItem.get(Constants.ACCORDION_SECTION_NAME_LBL);
			String lblName = sectionName.getDefaultModelObjectAsString();
			
			WebMarkupContainer container = (WebMarkupContainer)listItem.get(Constants.AJAX_CONTAINER);
			
			ListMultipleChoice selectedRolesLMC = (ListMultipleChoice)container.get(Constants.SELECTED_ROLES_MLC);
			
			List<String> selectedItems  = (List<String>)selectedRolesLMC.getChoices();
			
			if(selectedItems.size()> 0){
				ModuleVO moduleVO = new ModuleVO();
				moduleVO.setModule(lblName);
				List<RoleVO> roleVOList = new ArrayList<RoleVO>();
				for (Iterator iterator2 = selectedItems.iterator(); iterator2.hasNext();) {
					RoleVO roleVO = new RoleVO();
					String role = (String) iterator2.next();
					roleVO.setRole(role);
					roleVOList.add(roleVO);
				}
				moduleVO.setRole(roleVOList);
				moduleVOlist.add(moduleVO);
				moduleVO = new ModuleVO();	
			}
		}
	}

}
