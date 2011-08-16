package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * IEditModeEventHandler is an interface for use with the EditModeButtonsPanel
 * 
 * @author elam
 */
public interface IEditModeEventHandler {

	void onEditSave(AjaxRequestTarget target, Form<?> form);		//Save button's onSubmit
	void onEditCancel(AjaxRequestTarget target, Form<?> form);	//Cancel button's onSubmit
	void onEditDelete(AjaxRequestTarget target, Form<?> form);	//Delete button's onSubmit
	
	void onEditSaveError(AjaxRequestTarget target, Form<?> form);		//Save button's onError
	void onEditCancelError(AjaxRequestTarget target, Form<?> form);	//Cancel button's onError
	void onEditDeleteError(AjaxRequestTarget target, Form<?> form);	//Delete button's onError

}
