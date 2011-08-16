package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * IViewModeEventHandler is an interface for use with the ViewModeButtonsPanel
 * 
 * @author elam
 */
public interface IViewModeEventHandler {

	void onViewEdit(AjaxRequestTarget target, Form<?> form);		//Edit button's onSubmit
	void onViewCancel(AjaxRequestTarget target, Form<?> form);	//Cancel button's onSubmit
	
	void onViewEditError(AjaxRequestTarget target, Form<?> form);		//Edit button's onError
	void onViewCancelError(AjaxRequestTarget target, Form<?> form);	//Cancel button's onError

}
