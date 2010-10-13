/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.studycomponent.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.study.model.entity.StudyComp;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.Details;

/**
 * @author nivedann
 *
 */
public class DetailsForm extends Form<StudyCompVo>{

	private WebMarkupContainer  resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	private ContainerForm studyCompContainerForm;
	
	private AjaxButton deleteButton;
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	private TextField<String> componentIdTxtFld;
	private TextField<String> componentNameTxtFld;
	private TextArea<String> componentDescription;
	private TextArea<String> keywordTxtArea;
	
	public void initialiseForm(){
		
		componentIdTxtFld = new TextField<String>("studyComponent.studyCompKey");
		componentNameTxtFld = new TextField<String>("studyComponent.name");
		componentDescription = new TextArea<String>("studyComponent.description");
		keywordTxtArea = new TextArea<String>("studyComponent.keyword");
		attachValidators();
		decorateComponents();
		addComponents();
		
	}
	
	private void addComponents(){
		add(componentIdTxtFld);
		add(componentNameTxtFld);
		add(componentDescription);
		add(keywordTxtArea);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(componentIdTxtFld);
		ThemeUiHelper.componentRounded(componentNameTxtFld);
		ThemeUiHelper.componentRounded(componentDescription);
		ThemeUiHelper.componentRounded(keywordTxtArea);
	}
	
	private void attachValidators(){
		
		componentNameTxtFld.setRequired(true);
		componentNameTxtFld.add(StringValidator.lengthBetween(3, 100));
		componentDescription.add(StringValidator.lengthBetween(5, 500));
		keywordTxtArea.add(StringValidator.lengthBetween(1,255));
	}
	
	
	
	/**
	 * Add other required input components like File upload
	 * controls and list view
	 */
	
	
	/**
	 * @param id
	 */
	public DetailsForm(String id, Details detailsPanel, WebMarkupContainer listContainer,WebMarkupContainer detailsContainer, ContainerForm containerForm) {
		super(id);
		this.studyCompContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				resultListContainer.setVisible(false);
				detailPanelContainer.setVisible(false);
				target.addComponent(detailPanelContainer);
				target.addComponent(resultListContainer);
				studyCompContainerForm.getModelObject().setStudyComponent(new StudyComp());
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(detailPanelContainer);
				onSave(studyCompContainerForm.getModelObject(), target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processFeedback(target);
			}
		};
			
	}	
	
	
	protected void onSave(StudyCompVo studyCompVo, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void  onArchive(StudyCompVo studyCompVo,AjaxRequestTarget target){
		
	}
	
	protected void processFeedback(AjaxRequestTarget target){
		
	}

	public TextField<String> getComponentIdTxtFld() {
		return componentIdTxtFld;
	}

	public void setComponentIdTxtFld(TextField<String> componentIdTxtFld) {
		this.componentIdTxtFld = componentIdTxtFld;
	}
	

}
