package au.org.theark.core.web.component.audit.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.audit.modal.AuditModalPanel;

public class HistoryButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private ArkCrudContainerVO arkCrudContainerVO;
	private Object modelObject;
	private Form<?> containerForm;
	private ModalWindow modalWindow;
	private AjaxButton historyButton;
	
	public HistoryButtonPanel(Form<?> containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super("history");
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		modalWindow = new ModalWindow("historyModalWindow");
	
		historyButton = new AjaxButton("historyButton") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AuditModalPanel historyPanel = new AuditModalPanel("content", containerForm.getModelObject(), arkCrudContainerVO);
				modalWindow.setTitle("Entity History");
				modalWindow.setAutoSize(true);
				modalWindow.setContent(historyPanel);
				target.add(modalWindow);
				modalWindow.show(target);
				super.onSubmit(target, form);
			}
		};
		
		this.add(historyButton);
		this.add(modalWindow);
		
		
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(this);
//		arkCrudContainerVO.getEditButtonContainer().addOrReplace(modalWindow);
		
	}
	
	public void setObject(Object modelObject) {
		this.modelObject = modelObject;
	}	
}
