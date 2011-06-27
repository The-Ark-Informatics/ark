package au.org.theark.core.web.component;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import au.org.theark.core.web.form.AbstractModalDetailForm;

public abstract class AbstractDetailModalWindow extends ModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -794586150493541168L;
	protected String title;
	protected AbstractModalWindowContentPanel contentPanel;
	protected AjaxButton cancelButton;

	public AbstractDetailModalWindow(String id)
	{
		super(id);
		this.title = "New Modal Window";
		initialise();
	}
	
	public AbstractDetailModalWindow(String id, String title)
	{
		super(id);
		this.title = title;
		this.contentPanel = new AbstractModalWindowContentPanel(this.getContentId())
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -3377138517694684452L;	
		};
		initialise();
	}
	
	public AbstractDetailModalWindow(String id, String title, AbstractModalDetailForm<?> form)
	{
		super(id);
		this.title = title;
		this.contentPanel = new AbstractModalWindowContentPanel(this.getContentId(), form, this)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -3377138517694684452L;	
		};
		initialise();
	}
	
	protected void initialise()
	{
		setTitle(this.title);
		setResizable(false);
		setWidthUnit("%");
		setInitialWidth(90);
		
		// Set the content panel, implementing the abstract methods
		setContent(contentPanel);		
		setCssClassName("");
	}
}
