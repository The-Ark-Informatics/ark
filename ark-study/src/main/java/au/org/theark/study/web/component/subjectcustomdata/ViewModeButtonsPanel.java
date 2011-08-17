package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;

public class ViewModeButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private static final Logger log = LoggerFactory.getLogger(ViewModeButtonsPanel.class);

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
			public boolean isVisible() {
				// Ark-Security implemented
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.EDIT);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (editButton.isVisible() && editButton.isEnabled()) {
					eventHandler.onViewEdit(target, form);
				}
				else {
					log.error("Illegal Edit button submit: button is not enabled and/or not visible.");
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!editButton.isVisible() || !editButton.isEnabled()) {
					log.error("Illegal onError for Edit button submit: button is not enabled and/or not visible.");	
				}
				eventHandler.onViewEditError(target, form);
			}
		};
		this.add(editButton);
		
		cancelButton = new AjaxButton("cancel") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (cancelButton.isVisible() && cancelButton.isEnabled()) {
					eventHandler.onViewCancel(target, form);
				}
				else {
					log.error("Illegal Cancel button submit: button is not enabled and/or not visible.");
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!cancelButton.isVisible() || !cancelButton.isEnabled()) {
					log.error("Illegal onError for Cancel button submit: button is not enabled and/or not visible.");	
				}
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
