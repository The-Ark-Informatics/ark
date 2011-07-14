package au.org.theark.study.web.component.site.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.Details;

public class SiteForm extends Form<SiteModelVO>{
	
	@SpringBean( name = "userService")
	private IUserService userService;
	private WebMarkupContainer  resultListContainer;
	
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	private TextField<String> siteNameTxtFld;
	private TextArea<String> siteDescription;
	private ContainerForm containerForm;
	private Details detailsPanel;
	
	public SiteForm(String id, Details details, WebMarkupContainer listContainer, final WebMarkupContainer detailsContainer, ContainerForm siteContainerForm){
		
		super(id);
		
		this.resultListContainer = listContainer;
		this.detailsPanel = details;
		this.containerForm = siteContainerForm;
		
		/* Action buttons */
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				resultListContainer.setVisible(false);
				detailsContainer.setVisible(false);
				target.addComponent(detailsContainer);
				target.addComponent(resultListContainer);
				containerForm.getModelObject().setSiteVo(new SiteVO());
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//CompoundPropertyModel<SiteModelVO> cpm  = detailsPanel.getCpm();
				
				//SiteModelVO siteModel = cpm.getObject();
				target.addComponent(detailsContainer);
				onSave(containerForm.getModelObject(), target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processFeedback(target);
			}
		};
	}
	
	public void initialiseForm(){
		
		siteNameTxtFld = new TextField<String>("siteVo.siteName");
		siteNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		siteDescription = new TextArea<String>("siteVo.siteDescription");
		attachValidators();
		decorateComponents();
		addComponents();
	}
	
	private void addComponents(){
		add(siteNameTxtFld);
		add(siteDescription);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(siteNameTxtFld);
		ThemeUiHelper.componentRounded(siteDescription);
	}
	
	private void attachValidators(){
		
		siteNameTxtFld.setRequired(true);
		siteDescription.add(StringValidator.lengthBetween(1, 255));
	}
	
	
	protected void onSave(SiteModelVO siteModel, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void  onArchive(StudyModelVO studyModel,AjaxRequestTarget target){
		
	}
	
	protected void processFeedback(AjaxRequestTarget target){
		
	}

}
