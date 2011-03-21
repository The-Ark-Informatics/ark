package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * @author nivedann
 *
 */
public abstract class  AbstractArchiveDetailForm <T> extends Form<T>{
	
	private static final long		serialVersionUID	= 1L;
	protected FeedbackPanel	feedBackPanel;
	protected Form<T>	containerForm;
	protected AjaxButton saveButton;
	protected AjaxButton cancelButton;
	protected AjaxButton editButton;
	protected AjaxButton editCancelButton;
	protected ArkCrudContainerVO crudVO;

	
	/**
	 * @param id
	 */
	public AbstractArchiveDetailForm(String id) {
		super(id);
		initialiseForm();
	}

	abstract protected void attachValidators();


	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);

	protected void onCancelPostProcess(AjaxRequestTarget target){
		
		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getViewButtonContainer().setEnabled(true);
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);
		
		target.addComponent(feedBackPanel);
		target.addComponent(crudVO.getSearchPanelContainer());
		target.addComponent(crudVO.getSearchResultPanelContainer());
		target.addComponent(crudVO.getDetailPanelContainer());
		target.addComponent(crudVO.getDetailPanelFormContainer());

		target.addComponent(crudVO.getViewButtonContainer());
		target.addComponent(crudVO.getEditButtonContainer());
	}


	/**
	 * Constructor
	 * @param id
	 * @param feedBackPanel
	 * @param crudVO
	 * @param containerForm
	 */
	public  AbstractArchiveDetailForm(	String id,	FeedbackPanel feedBackPanel, ArkCrudContainerVO crudVO,Form<T> containerForm){
		
		super(id);
		this.crudVO = crudVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		initialiseForm();
	}
	
	@SuppressWarnings("serial")
	protected void initialiseForm(){

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)){

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				
				crudVO.getSearchResultPanelContainer().setVisible(false);// Hide the Search Result List Panel via the WebMarkupContainer
				crudVO.getDetailPanelContainer().setVisible(false);// Hide the Detail Panle via the WebMarkupContainer
				target.addComponent(crudVO.getDetailPanelContainer());// Attach the Detail WebMarkupContainer to be re-rendered using Ajax
				target.addComponent(crudVO.getSearchResultPanelContainer());// Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
				//onCancel(target);// Invoke a onCancel() that the sub-class can use to build anything more specific
				onCancelPostProcess(target);
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))	{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(crudVO.getDetailPanelContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null))	{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				crudVO.getViewButtonContainer().setVisible(false);
				crudVO.getEditButtonContainer().setVisible(true);
				crudVO.getDetailPanelFormContainer().setEnabled(true);
				
				target.addComponent(crudVO.getViewButtonContainer());
				target.addComponent(crudVO.getEditButtonContainer());
				target.addComponent(crudVO.getDetailPanelFormContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null)){
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				crudVO.getSearchResultPanelContainer().setVisible(true);// Hide the Search Result List Panel via the WebMarkupContainer
				crudVO.getDetailPanelContainer().setVisible(false);// Hide the Detail Panle via the WebMarkupContainer
				crudVO.getSearchPanelContainer().setVisible(true);

				target.addComponent(feedBackPanel);
				target.addComponent(crudVO.getSearchPanelContainer());
				target.addComponent(crudVO.getDetailPanelContainer());
				target.addComponent(crudVO.getSearchResultPanelContainer());
				onCancel(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		addComponentsToForm();
	}

	protected void addComponentsToForm(){
		crudVO.getEditButtonContainer().add(saveButton);
		crudVO.getEditButtonContainer().add(cancelButton.setDefaultFormProcessing(false));
		crudVO.getViewButtonContainer().add(editButton);
		crudVO.getViewButtonContainer().add(editCancelButton.setDefaultFormProcessing(false));
		
		add(crudVO.getDetailPanelFormContainer());
		add(crudVO.getEditButtonContainer());
		add(crudVO.getViewButtonContainer());
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target,ArkCrudContainerVO crudVO){
		//Visibility
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);
		
		//Enable
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getViewButtonContainer().setEnabled(true);
	
		target.addComponent(crudVO.getSearchResultPanelContainer());
		target.addComponent(crudVO.getDetailPanelContainer());
		target.addComponent(crudVO.getDetailPanelFormContainer());
		target.addComponent(crudVO.getSearchPanelContainer());
		target.addComponent(crudVO.getViewButtonContainer());
		target.addComponent(crudVO.getEditButtonContainer());

	}


}
