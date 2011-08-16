package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class EditModeButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
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
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onEditSave(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onEditSaveError(target, form);
			}
		};
		this.add(saveButton);
		
		cancelButton = new AjaxButton("cancel") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onEditCancel(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onEditCancelError(target, form);
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		this.add(cancelButton);
		
		deleteButton  = new AjaxButton("delete") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				eventHandler.onEditDelete(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
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
