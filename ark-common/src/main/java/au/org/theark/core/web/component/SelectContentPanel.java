package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.Constants;

@SuppressWarnings( { "serial", "unchecked" })
public abstract class SelectContentPanel extends Panel
{
	Label deleteMessage;
	
	public SelectContentPanel(String id)
	{
		super(id);

		// Create the form, to use later for the buttons
		Form form = new Form("confirmForm");
		add(form);
		
		deleteMessage =  new Label("deleteMessage", Constants.DELETE_CONFIRM_MESSAGE);
		form.add(deleteMessage);

		form.add(new AjaxButton(Constants.OK)
		{
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				onSelect(target, new String("OK pressed"));
			}
		});

		// Add a cancel / close button.
		form.add(new AjaxButton(Constants.CANCEL)
		{
			public void onSubmit(AjaxRequestTarget target, Form form)
			{
				onCancel(target);
			}
		});

	}

	abstract void onCancel(AjaxRequestTarget target);
	abstract void onSelect(AjaxRequestTarget target, String selection);
}