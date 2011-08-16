package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class ViewModeButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected Button editButton;
	protected Button cancelButton;
	protected IViewModeEventHandler eventHandler;
	
	public ViewModeButtonsPanel(String id, IViewModeEventHandler eventHandler) {
		super(id);
		this.eventHandler = eventHandler;
		
		setOutputMarkupPlaceholderTag(true);
		initialisePanel();
	}

	protected void initialisePanel() {
		editButton = new AjaxButton("edit") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onViewEdit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onViewEditError(target, form);
			}
		};
		this.add(editButton);
		
		cancelButton = new AjaxButton("cancel") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onViewCancel(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onViewCancelError(target, form);
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		this.add(cancelButton);
		
	}

	public boolean isEditButtonVisible() {
		return editButton.isVisible();
	}

	public void setEditButtonVisible(boolean visible) {
		editButton.setVisible(visible);
	}
	
	public boolean isEditButtonEnabled() {
		return editButton.isEnabled();
	}

	public void setEditButtonEnabled(boolean enabled) {
		editButton.setEnabled(enabled);
	}
	
	public boolean isCancelButtonVisible() {
		return cancelButton.isVisible();
	}

	public void setCancelButtonVisible(boolean visible) {
		cancelButton.setVisible(visible);
	}
	
	public boolean isCancelButtonEnabled() {
		return cancelButton.isEnabled();
	}

	public void setCancelButtonEnabled(boolean enabled) {
		cancelButton.setEnabled(enabled);
	}
}
