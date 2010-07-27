package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.util.UIHelper;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.study.StudyModel;

@SuppressWarnings("serial")
public class ListMultipleChoiceForm extends Form<StudyModel>{

	private WebMarkupContainer listMultipleChoiceContainer;//A container that will house the ListMultipleChoice controls
	private ListMultipleChoice<String> availableItemsLmc;
	private ListMultipleChoice selectedItemsLmc;
	private AjaxButton addButton;
	private AjaxButton addAllButton;
	private AjaxButton removeButton;
	private AjaxButton removeAllButton;
	private List<ModuleVO> modules;
	
	/**
	 * Constructor
	 * @param id
	 * @param model
	 * @param modules
	 */
	public ListMultipleChoiceForm(String id, IModel<StudyModel> model, List<ModuleVO> modules) {
		
		super(id, new CompoundPropertyModel<StudyModel>(model));
		this.modules = modules;
	}
	
	/**
	 * Invoke this method on the Form instance rather than invoking it as part of the Form's constructor
	 * @return
	 * @throws ArkSystemException
	 */
	public WebMarkupContainer initLMCContainer() throws ArkSystemException{
		
		listMultipleChoiceContainer = new WebMarkupContainer(Constants.LMC_AJAX_CONTAINER);
		listMultipleChoiceContainer.setOutputMarkupId(true);
		
		/*Initialise the selected application List first*/
		List<String> selectedApps = new ArrayList<String>();
		selectedItemsLmc = new ListMultipleChoice<String>(Constants.LMC_SELECTED_APPS, selectedApps);
		/*Initialise the available application list*/
		List<String> availableApps = new ArrayList<String>();
		UIHelper.getDisplayModuleNameList(modules,availableApps);
		
		availableItemsLmc = new ListMultipleChoice<String>(Constants.LMC_AVAILABLE_APPS,availableApps);
		
		//Attach a Ajax Behavior to update the Selected Applications control
		availableItemsLmc.add( new AjaxFormComponentUpdatingBehavior("ondblclick") {
			
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Set<String> selectedItems = (HashSet<String>) availableItemsLmc.getModelObject();
				UIHelper.addSelectedItems(selectedItems, selectedItemsLmc);
				List<String> choices = selectedItemsLmc.getChoices();

				Collection model = getModelObject().getLmcSelectedApps();
				Iterator it = choices.iterator();
				while (it.hasNext())
				{
					String selectedChoice = (String)it.next();
					model.add(selectedChoice);
				}
				modelChanged();
				((IModel<Object>) getDefaultModel()).setObject(model);
				
				target.addComponent(listMultipleChoiceContainer);
			}
		});
		
		addButton = initialiseAddButton(	listMultipleChoiceContainer, availableItemsLmc,
											selectedItemsLmc, Constants.ADD_SELECTED, 
											addButton,	Constants.ACTION_ADD_SELECTED);
		
		addAllButton = initialiseAddButton(	listMultipleChoiceContainer, availableItemsLmc,
											selectedItemsLmc,	Constants.ADD_ALL_BUTTON,
											addAllButton,	Constants.ACTION_ADD_ALL);
		
		removeButton = initialiseRemoveButton(	listMultipleChoiceContainer,selectedItemsLmc,
												Constants.REMOVE_SELECTED_BUTTON,removeButton,
												Constants.ACTION_REMOVE_SELECTED);
		
		removeAllButton = initialiseRemoveButton(	listMultipleChoiceContainer,	selectedItemsLmc,
													Constants.REMOVE_ALL_BUTTON,removeAllButton,
													Constants.ACTION_REMOVE_ALL);
		
		listMultipleChoiceContainer.add(selectedItemsLmc);
		listMultipleChoiceContainer.add(availableItemsLmc);
		listMultipleChoiceContainer.add(addButton);
		listMultipleChoiceContainer.add(addAllButton);
		listMultipleChoiceContainer.add(removeButton);
		listMultipleChoiceContainer.add(removeAllButton);
		ThemeUiHelper.componentRounded(availableItemsLmc);
		ThemeUiHelper.componentRounded(selectedItemsLmc);
		ThemeUiHelper.buttonRounded(addButton);
		ThemeUiHelper.buttonRounded(addAllButton);
		ThemeUiHelper.buttonRounded(removeButton);
		ThemeUiHelper.buttonRounded(removeAllButton);

		
		
		return listMultipleChoiceContainer;
	}
	
	private AjaxButton initialiseAddButton(	final WebMarkupContainer container, final ListMultipleChoice<String> availableAppsLMC, 
											final ListMultipleChoice<String> targetMLC, String buttonId, Button button, final String action){
		
		button =(AjaxButton) new AjaxButton(buttonId){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Set<String> selectedChoice = new HashSet<String>();
				//Get the items selected from the control's MODEL
				if(action.equalsIgnoreCase(Constants.ACTION_ADD_SELECTED)){
					selectedChoice = (Set<String>)availableAppsLMC.getModelObject();
					
				}else{
					selectedChoice = buildChoiceSet(availableAppsLMC.getChoices());
				}
				UIHelper.addSelectedItems(selectedChoice, targetMLC);
				targetMLC.modelChanged();
				targetMLC.setModelObject(selectedChoice);
				target.addComponent(container);
			}
			@Override	
			protected void onError(AjaxRequestTarget target, Form<?> form){
				System.out.println("onError called on Add Button");
			}
		};
		button.setModel(new StringResourceModel("addSelectedTxt",this,null));
		return (AjaxButton)button;
	}
	
	
	private Set<String> buildChoiceSet(List choices){
		Set<String> choiceSet = new HashSet<String>();
		for (Iterator iterator = choices.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			choiceSet.add((String)object);
		}
		return choiceSet;
	}
	
	private AjaxButton initialiseRemoveButton(	final WebMarkupContainer container,	final ListMultipleChoice<String> targetMLC, 
												String buttonId, Button button, final String action){
		
		button = (AjaxButton)new AjaxButton(buttonId){
			@Override
			protected void onSubmit(AjaxRequestTarget requestTarget, Form<?> arg1) {
				
				Set<String> selectedItems = new HashSet<String>(); 
				
				if(action.equalsIgnoreCase(Constants.ACTION_REMOVE_SELECTED)){

					selectedItems = (Set<String>) targetMLC.getModelObject();
					targetMLC.getChoices().removeAll(selectedItems);	
										
				}else{
					
					selectedItems = buildChoiceSet(targetMLC.getChoices());
					targetMLC.getChoices().removeAll(selectedItems);
				}
				
				
				Set<String> itemsRemaining = new HashSet<String>(); 
				for (Iterator iterator = targetMLC.getChoices().iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					itemsRemaining.add((String)object);
				}
				targetMLC.modelChanged();
				targetMLC.setModelObject(itemsRemaining);
				requestTarget.addComponent(container);
			}
		};
		button.setModel(new StringResourceModel("removeSelectedTxt",this,null));
		return(AjaxButton) button;
	}

}
