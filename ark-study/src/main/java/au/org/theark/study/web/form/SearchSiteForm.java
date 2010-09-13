package au.org.theark.study.web.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.SiteModel;

public class SearchSiteForm extends Form<SiteModel>{

	private TextField<String> siteNameTxtFld;
	private DropDownChoice<Person> siteContactDDC;
	
	private List<Person> availableContactList;
	
	private AjaxButton searchButton;
	private AjaxButton newButton;
	private Button resetButton;

	public SearchSiteForm(String id,CompoundPropertyModel<SiteModel> model, List<Person> availablePersons) {
		
		super(id,model);
		
		//siteIdTxtFld =new TextField<String>(Constants.STUDY_SITE_KEY);
		siteNameTxtFld = new TextField<String>("siteVo.siteName");
		availableContactList = availablePersons;
		
		newButton = new AjaxButton(Constants.NEW){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onNew(target);
			}
			
			@Override
			public boolean isVisible(){
				
				SecurityManager securityManager =  ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();		
				boolean flag = false;
				if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
						securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
					flag = true;
				}
				//if it is a Super or Study admin then make the new available
				return flag;
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
		decorateComponents();
		addComponentsToForm();
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(siteNameTxtFld);
		ThemeUiHelper.componentRounded(searchButton);
		ThemeUiHelper.componentRounded(newButton);
		ThemeUiHelper.componentRounded(resetButton);
	}
	
	private void addComponentsToForm(){
		add(siteNameTxtFld);
		add(searchButton);
		add(resetButton);
		add(newButton);
	}
	
	protected void onSearch(AjaxRequestTarget target){}
	
	protected void onNew(AjaxRequestTarget target){}
	
	// A non-ajax function
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
		
	}
	
	private void initContactDropDown(PropertyModel<Person> personPropertyModel){
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.PERSON_LAST_NAME, Constants.PERSON_KEY);
		siteContactDDC = new DropDownChoice("personDDC",personPropertyModel,availableContactList, defaultChoiceRenderer);
	}
	

}
