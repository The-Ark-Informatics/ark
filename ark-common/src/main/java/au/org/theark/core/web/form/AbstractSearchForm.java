/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;


/**
 * <p>
 * An Abstract Form class for Search. This class contains common behaviour that the  sub-classes can inherit. 
 * The sub-classes themselves can override the behaviour of the abstract but can also add more specific implementation if needed. 
 * As part of this class we  have defined the New,Search and Reset button and their behaviour which will be common for all search functions.
 * @author nivedann
 * @param <T>
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractSearchForm<T>  extends Form<T>{

	protected AjaxButton searchButton;
	protected AjaxButton newButton;
	protected Button resetButton;
	
	/**
	 * @param id
	 * @param model
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel) {
		
		super(id, cpmModel);
		
		initialiseForm();
		
	}
	
	
	abstract protected void onSearch(AjaxRequestTarget target);
	
	abstract protected void onNew(AjaxRequestTarget target);
	
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
	}
	
	protected void initialiseForm(){
		
		newButton = new AjaxButton(Constants.NEW){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onNew(target);
			}
			
			@Override
			public boolean isVisible(){
				return true;
			}
		};
		
		
		searchButton = new AjaxButton(Constants.SEARCH){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onSearch(target);
			}
		};
		
		resetButton = new Button(Constants.RESET){
			public void onSubmit(){
				onReset();
			}
		};
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId == null){
			searchButton.setEnabled(false);
			newButton.setEnabled(false);
			resetButton.setEnabled(false);
			this.error("There is no study in context.Please select a study");
			
		}else{
			newButton.setEnabled(true);
			searchButton.setEnabled(true);
			resetButton.setEnabled(true);
		}
		
		addComponentsToForm();
	}
	
	protected void addComponentsToForm(){
		add(searchButton);
		add(resetButton);
		add(newButton);
	}
	


}
