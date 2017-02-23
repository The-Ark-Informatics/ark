package au.org.theark.core.web.component.audit.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.audit.modal.AuditModalPanel;

public class HistoryButtonPanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	private ModalWindow modalWindow;
	private AjaxButton historyButton;
	
	public HistoryButtonPanel(Form<?> containerForm, WebMarkupContainer parentContainer, WebMarkupContainer auditContainer) {
		super("history");
		modalWindow = new ModalWindow("historyModalWindow");
	
		historyButton = new AjaxButton("historyButton") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AuditModalPanel historyPanel = new AuditModalPanel("content", containerForm.getModelObject(), auditContainer);
				modalWindow.setTitle("Entity History");
				modalWindow.setAutoSize(true);
				modalWindow.setMinimalWidth(950);
				modalWindow.setContent(historyPanel);
				target.add(modalWindow);
				modalWindow.show(target);
				target.add(historyPanel.getFeedbackPanel());
				super.onSubmit(target, form);
			}
		};
		
		this.add(historyButton);
		this.add(modalWindow);
		
		parentContainer.addOrReplace(this);
	}
}
