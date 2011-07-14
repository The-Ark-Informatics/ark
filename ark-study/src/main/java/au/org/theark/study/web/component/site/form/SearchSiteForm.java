package au.org.theark.study.web.component.site.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.study.web.Constants;


public class SearchSiteForm extends Form<SiteModelVO>{

	private TextField<String> siteNameTxtFld;
	private DropDownChoice<Person> siteContactDDC;
	
	private List<Person> availableContactList;
	
	private AjaxButton searchButton;
	private AjaxButton newButton;
	private Button resetButton;

	public SearchSiteForm(String id,CompoundPropertyModel<SiteModelVO> model, List<Person> availablePersons) {
		
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
	

}
