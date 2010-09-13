package au.org.theark.study.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.Details;
import au.org.theark.study.web.component.site.SiteModel;
import au.org.theark.study.web.component.study.StudyModel;

public class SiteForm extends Form<SiteModel>{
	
	@SpringBean( name = "userService")
	private IUserService userService;
	private WebMarkupContainer  listContainer;
	
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	private TextField<String> siteNameTxtFld;
	private TextArea<String> siteDescription;
	
	private Details detailsPanel;
	
	public SiteForm(String id, Details details, WebMarkupContainer listContainer, final WebMarkupContainer detailsContainer){
		
		super(id);
		
		this.listContainer = listContainer;
		this.detailsPanel = details;
		
		/* Action buttons */
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				CompoundPropertyModel<SiteModel> cpm  = detailsPanel.getCpm();
				SiteModel siteModel = cpm.getObject();
				target.addComponent(detailsContainer);
				onSave(siteModel, target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
	}
	
	public void initialiseForm(){
		
		siteNameTxtFld = new TextField<String>("siteName");
		siteDescription = new TextArea<String>("siteDescription");
		attachValidators();
		decorateComponents();
		addComponents();
	}
	
	private void addComponents(){
		add(siteNameTxtFld);
		add(siteDescription);
		add(saveButton);
		add(cancelButton);
		
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(siteNameTxtFld);
		ThemeUiHelper.componentRounded(siteDescription);
	}
	
	private void attachValidators(){
		
		siteNameTxtFld.setRequired(true);
		siteDescription.add(StringValidator.lengthBetween(1, 255));
	}
	
	
	protected void onSave(SiteModel siteModel, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void  onArchive(StudyModel studyModel,AjaxRequestTarget target){
		
	}
	
	protected void processErrors(AjaxRequestTarget target){
		
	}

}
