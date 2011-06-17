package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;

/**
 * Contains general navigation buttons, to allow user navigation between the corresponding results list in context
 * @author cellis
 *
 */
public class ArkNavigationButtonPanel extends Panel{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7754187448497965757L;
	protected WebMarkupContainer	navigationButtonContainer;
	
	protected AjaxButton				firstRecordButton;
	protected AjaxButton				previousRecordButton;
	protected AjaxButton				searchResultsButton;
	protected AjaxButton				nextRecordButton;
	protected AjaxButton				lastRecordButton;
	
	
	/**
	 * @param id
	 */
	public ArkNavigationButtonPanel(String id) {
		super(id);
		initialise();
	}
	
	protected void initialise(){
		
		firstRecordButton = new AjaxButton(Constants.FIRST, new StringResourceModel("firstKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1733110406791626819L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
			}
				
			@Override
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
			
			//TODO NN Uncomment after User Management UI is completed	
			@Override
			public boolean isVisible()
			{
				return true;
			}
		};
		
		previousRecordButton = new AjaxButton(Constants.PREVIOUS, new StringResourceModel("previousKey", this, null))
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -5240216794000921473L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
			}
				
			@Override
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
			
			//TODO NN Uncomment after User Management UI is completed	
			@Override
			public boolean isVisible()
			{
				return true;
			}
		};
		
		searchResultsButton = new AjaxButton(Constants.SEARCH_RESULTS, new StringResourceModel("searchResultsKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5582380497807575737L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				
			}
			
			//TODO NN Uncomment after User Management UI is completed	
			@Override
			public boolean isVisible()
			{
				return true;
			}
		};

		nextRecordButton = new AjaxButton(Constants.NEXT, new StringResourceModel("nextKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4112229943335225249L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
			}
				
			@Override
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
			
			//TODO NN Uncomment after User Management UI is completed	
			@Override
			public boolean isVisible()
			{
				return true;
			}
		};
		
		lastRecordButton = new AjaxButton(Constants.LAST, new StringResourceModel("lastKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 7221391521277297779L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
			
			//TODO NN Uncomment after User Management UI is completed	
			@Override
			public boolean isVisible()
			{
				return true;
			}
		};

		addComponents();
	}
	
	protected void addComponents()
	{
		navigationButtonContainer.add(firstRecordButton);
		navigationButtonContainer.add(previousRecordButton);
		navigationButtonContainer.add(nextRecordButton);
	}
}