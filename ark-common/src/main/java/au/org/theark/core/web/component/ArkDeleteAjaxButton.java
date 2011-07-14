package au.org.theark.core.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArkDeleteAjaxButton extends AjaxDeleteButton
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2210428932835290880L;

	ArkDeleteAjaxButton(String id, Component component)
	{
		// Properties contains:
		// confirmDelete=Are you sure you want to delete?
		// delete=Delete
		super(id, new StringResourceModel("confirmDelete", component, null), new StringResourceModel(id, component, null));
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form)
	{
	}
}
