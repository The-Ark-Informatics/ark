package au.org.theark.core.web.form;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxButton;

/**
 * <p>
 * An Abstract Form class for Search. This class contains common behaviour that the sub-classes can inherit. The sub-classes themselves can override
 * the behaviour of the abstract but can also add more specific implementation if needed. As part of this class we have defined the New,Search and
 * Reset button and their behaviour which will be common for all search functions.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractSearchForm<T> extends Form<T>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -408051334961302312L;
	protected AjaxButton				searchButton;
	protected AjaxButton				newButton;
	protected Button					resetButton;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	detailPanelContainer;
	protected WebMarkupContainer	searchMarkupContainer;
	protected WebMarkupContainer	listContainer;
	protected WebMarkupContainer	detailFormCompContainer;
	protected FeedbackPanel			feedbackPanel;
	
	

	/**
	 * @param id
	 * @param model
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel)
	{

		super(id, cpmModel);
		initialiseForm();

	}

	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailFormCompContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer listContainer, FeedbackPanel feedBackPanel)
	{

		super(id, cpmModel);
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.searchMarkupContainer = searchMarkupContainer;
		this.listContainer = listContainer;
		this.detailFormCompContainer = detailFormCompContainer;
		this.feedbackPanel = feedBackPanel;
		initialiseForm();

	}
	

	/**
	 * Nivedan working
	 * @param id
	 * @param cpmModel
	 */
	public AbstractSearchForm(	String id, 
								IModel<T> cpmModel, 
								FeedbackPanel feedBackPanel,
								ArkCrudContainerVO arkCrudContainerVO){
		super(id,cpmModel);
		this.feedbackPanel = feedBackPanel;
		initialiseForm(arkCrudContainerVO);
	}

	abstract protected void onSearch(AjaxRequestTarget target);

	abstract protected void onNew(AjaxRequestTarget target);

	/* This method should be implemented by sub-classes to secure a control(New button etc..) */
	abstract protected boolean isSecure(String actionType);
	
	
	protected boolean isActionPermitted(String actionType){
		boolean flag = false;
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		Long useCaseId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		Long module = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		if(actionType.equalsIgnoreCase(Constants.NEW)){
			if( securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.CREATE)){
				flag = true;
			}else{
				flag = false;
			}
		}else if (actionType.equalsIgnoreCase(Constants.SEARCH)){
			
			if( securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.READ)){
				flag = true;	
			}else{
				flag = false;
			}
			
			flag = true;
		}else{
			flag = true;
		}
		return flag;
	}
	

	protected void onReset()
	{
		clearInput();
		updateFormComponentModels();
	}

	protected void initialiseForm()
	{
		searchButton = new AjaxButton(Constants.SEARCH)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			public boolean isVisible()
			{
				 //return isActionPermitted(Constants.SEARCH);
				return  isSecure(Constants.SEARCH);
			}
			
			@Override
			 protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			} 
		};

		resetButton = new Button(Constants.RESET)
		{
			public void onSubmit()
			{
				onReset();
			}

			@Override
			public boolean isVisible()
			{
				return isSecure(Constants.RESET);
			}
		};

		newButton = new ArkBusyAjaxButton(Constants.NEW)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible, disabling delete button (if found)
				AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.addComponent(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible()
			{
				//return  isActionPermitted(Constants.NEW);
				return isSecure(Constants.NEW);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
		};

		addComponentsToForm();
	}

	
	protected void initialiseForm(final ArkCrudContainerVO arkCrudContainerVO)
	{
		searchButton = new AjaxButton(Constants.SEARCH)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			public boolean isVisible()
			{	
				return isActionPermitted(Constants.SEARCH);
				//return isSecure(Constants.SEARCH);
			}
			
			@Override
			 protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			} 
		};

		resetButton = new Button(Constants.RESET)
		{
			public void onSubmit()
			{
				onReset();
			}

			@Override
			public boolean isVisible()
			{	
				
				return isActionPermitted(Constants.RESET);
				//return isSecure(Constants.RESET);
			}
		};

		newButton = new AjaxButton(Constants.NEW)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible, disabling delete button (if found)
				//AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				AjaxButton ajaxButton = (AjaxButton)arkCrudContainerVO.getEditButtonContainer().get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.addComponent(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible()
			{	
				return isActionPermitted(Constants.NEW);
				//return isSecure(Constants.NEW);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
		};

		addComponentsToForm();
	}
	protected void addComponentsToForm()
	{
		add(searchButton);
		add(resetButton.setDefaultFormProcessing(false));
		add(newButton);
	}

	protected void preProcessDetailPanel(AjaxRequestTarget target)
	{

		detailPanelContainer.setVisible(true);
		listContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);
		detailFormCompContainer.setEnabled(true);

		target.addComponent(detailPanelContainer);
		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailFormCompContainer);
	}
	
	/**
	 * Overloaded Method that uses the VO to set the WMC's
	 * @param target
	 * @param flag
	 */
	protected void preProcessDetailPanel(AjaxRequestTarget target, ArkCrudContainerVO arkCrudContainerVO)
	{
		
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
	
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVO.getViewButtonContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
		
	}

	

	protected void disableSearchForm(Long sessionId, String errorMessage){	
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if (sessionId == null)
		{
			searchMarkupContainer.setEnabled(false);			
			this.error(errorMessage);
		}
		else
		{
			searchMarkupContainer.setEnabled(true);
		}

	}
	
	protected void disableSearchForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO){	
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if(	!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.CREATE) &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE) &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.READ)  &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE)){
			
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			this.error("You do not have the required security privileges to work with this function.Please see your Administrator.");
			
		}else{

			if (sessionId == null){
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}else{	
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
	}
	
	
	
	protected void disableSearchButtons(Long sessionId, String errorMessage)
	{	
		if (sessionId == null)
		{
			searchButton.setEnabled(false);
			newButton.setEnabled(false);
			resetButton.setEnabled(false);
			this.error(errorMessage);
		}
		else
		{
			newButton.setEnabled(true);
			searchButton.setEnabled(true);
			resetButton.setEnabled(true);
		}
	}
}