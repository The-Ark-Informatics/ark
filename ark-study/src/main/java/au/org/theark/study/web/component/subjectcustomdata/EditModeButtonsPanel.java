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

public class EditModeButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private static final Logger log = LoggerFactory.getLogger(EditModeButtonsPanel.class);

	protected Button saveButton;
	protected Button cancelButton;
	protected Button deleteButton;
	protected IEditModeEventHandler eventHandler;
	
	public EditModeButtonsPanel(String id, IEditModeEventHandler eventHandler) {
		super(id);
		this.eventHandler = eventHandler;
		
		setOutputMarkupPlaceholderTag(true);
		initialisePanel();
	}

	protected void initialisePanel() {
		saveButton = new AjaxButton("save") {
			
			@Override
			public boolean isVisible() {
				// Ark-Security implemented
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (saveButton.isVisible() && saveButton.isEnabled()) {
					eventHandler.onEditSave(target, form);
				}
				else {
					log.error("Illegal Save button submit: button is not enabled and/or not visible.");
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!saveButton.isVisible() || !saveButton.isEnabled()) {
					log.error("Illegal onError for Save button submit: button is not enabled and/or not visible.");	
				}
				eventHandler.onEditSaveError(target, form);
			}
		};
		this.add(saveButton);
		
		cancelButton = new AjaxButton("cancel") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (cancelButton.isVisible() && cancelButton.isEnabled()) {
					eventHandler.onEditCancel(target, form);
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
				eventHandler.onEditCancelError(target, form);
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		this.add(cancelButton);
		
		deleteButton  = new AjaxButton("delete") {
			
			@Override
			public boolean isVisible() {
				// Ark-Security implemented
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (deleteButton.isVisible() && deleteButton.isEnabled()) {
					eventHandler.onEditDelete(target, form);
				}
				else {
					log.error("Illegal Delete button submit: button is not enabled and/or not visible.");
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!deleteButton.isVisible() || !deleteButton.isEnabled()) {
					log.error("Illegal onError for Delete button submit: button is not enabled and/or not visible.");	
				}
				eventHandler.onEditDeleteError(target, form);
			}
		};
		deleteButton.setDefaultFormProcessing(false);
		this.add(deleteButton);
	}

	public boolean isSaveButtonVisible() {
		return saveButton.isVisible();
	}

	public void setSaveButtonVisible(boolean visible) {
		saveButton.setVisible(visible);
	}

	public boolean isSaveButtonEnabled() {
		return saveButton.isEnabled();
	}

	public void setSaveButtonEnabled(boolean enabled) {
		saveButton.setEnabled(enabled);
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

	public boolean isDeleteButtonVisible() {
		return deleteButton.isVisible();
	}

	public void setDeleteButtonVisible(boolean visible) {
		deleteButton.setVisible(visible);
	}
	
	public boolean isDeleteEnabled() {
		return deleteButton.isEnabled();
	}
	
	public void setDeleteButtonEnabled(boolean enabled) {
		deleteButton.setEnabled(enabled);
	}
	
}
