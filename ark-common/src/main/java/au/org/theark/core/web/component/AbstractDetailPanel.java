package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.web.form.IDetailEventHandler;

/**
 * <p>
 * Abstract class for the Detail panel. Historically speaking, the 
 * functionality of Detail Forms have been moved here given it has
 * direct access to a majority of the containers, etc (courtesy of 
 * AbstractCRUDPanel).  
 * </p>
 * @author elam
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDetailPanel extends AbstractCRUDPanel implements IDetailEventHandler {

	protected ModalWindow selectModalWindow;

	/**
	 * @param id
	 */
	public AbstractDetailPanel(String id) {
		super(id);

		selectModalWindow = initialiseModalWindow();
		detailPanelFormContainer.add(selectModalWindow);
	}
	
	abstract protected void attachValidators();
	
	public void onDelete(AjaxRequestTarget target){
		selectModalWindow.show(target);
		target.addComponent(selectModalWindow);
	}

	protected void onCancelPostProcess(AjaxRequestTarget target){
		
		searchResultPanelContainer.setVisible(false);
		detailPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(searchResultPanelContainer);
	}
	
	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by
	 * sub-classes as part of the onSave() implementation.Once the user has pressed Save either to
	 * create a new entity or update, invoking this method will place the new/edited record panel in 
	 * View/Read only mode.
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target){
		
		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		searchResultPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		
		target.addComponent(searchResultPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		
	}

	public void onEdit(AjaxRequestTarget target) {
		viewButtonContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		detailPanelFormContainer.setEnabled(true);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailPanelFormContainer);
	}
	
	/*
	 * The following is for the selectModalWindow 
	 */
	
	/**
	 * 
	 * @param target
	 * @param selectModalWindow
	 */
	protected  void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow){
		selectModalWindow.close(target);
	}
	
	protected ModalWindow initialiseModalWindow(){
	
		System.out.println("called new initialModalWin");
		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow"){

			protected void onSelect(AjaxRequestTarget target, String selection){
				onDeleteConfirmed(target,selection, selectModalWindow);
		    }
	
		    protected void onCancel(AjaxRequestTarget target){
				onDeleteCancel(target,selectModalWindow);
		    }
		};
		
		return selectModalWindow;

	}
}
