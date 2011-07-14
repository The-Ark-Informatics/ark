package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import au.org.theark.core.Constants;

public abstract class SelectModalWindow extends ModalWindow
{
	public SelectModalWindow(String id)
	{
		super(id);

		// Set sizes of this ModalWindow. You can also do this from the HomePage
		// but its not a bad idea to set some good default values.
		setInitialWidth(300);
		setInitialHeight(150);

		setTitle(Constants.DELETE_CONFIRM_TITLE);

		// Set the content panel, implementing the abstract methods
		setContent(new SelectContentPanel(this.getContentId())
		{
			void onCancel(AjaxRequestTarget target)
			{
				SelectModalWindow.this.onCancel(target);
			}

			void onSelect(AjaxRequestTarget target, String selection)
			{
				SelectModalWindow.this.onSelect(target, selection);
			}
		});
	}

	protected abstract void onCancel(AjaxRequestTarget target);

	protected abstract void onSelect(AjaxRequestTarget target, String selection);

}
