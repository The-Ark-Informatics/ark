/**
 * 
 */
package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

/**
 * @author elam
 *
 */
public interface IDetailEventHandler {

	void onCancel(AjaxRequestTarget target);
	
	void onSave(AjaxRequestTarget target);
	
	void processErrors(AjaxRequestTarget target);

	void onEdit(AjaxRequestTarget target);
	
	void onEditCancel(AjaxRequestTarget target);
	
	void onDelete(AjaxRequestTarget target);

	void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow);

}
