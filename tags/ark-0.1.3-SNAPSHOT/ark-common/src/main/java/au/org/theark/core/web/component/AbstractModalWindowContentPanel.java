package au.org.theark.core.web.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.form.AbstractModalDetailForm;

public abstract class AbstractModalWindowContentPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4604115589812210436L;
	protected Label label;
	protected AbstractModalDetailForm<?> form;
	protected AbstractDetailModalWindow modalWindow;
	protected FeedbackPanel feedbackPanel;
	
	public AbstractModalWindowContentPanel(String id)
	{
		super(id);
		initialise();
	}
	
	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form)
	{
		super(id);
		this.form = form;
		initialise();
	}
	
	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form, AbstractDetailModalWindow modalWindow)
	{
		super(id);
		this.form = form;
		this.modalWindow = modalWindow;
		initialise();
	}
	
	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form, AbstractDetailModalWindow modalWindow, FeedbackPanel feedbackPanel)
	{
		super(id);
		this.form = form;
		this.modalWindow = modalWindow;
		this.feedbackPanel = feedbackPanel;
		initialise();
	}

	protected void initialise()
	{
		setOutputMarkupId(true);
		if(form == null)
		{
			Form form = new Form("detailForm");
			add(form);
		}
		else
		{
			form.getId();
			addOrReplace(form);
		}
	}
}