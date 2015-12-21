package au.org.theark.study.web.component.manageuser.form;



import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;



public class SuccessFullySaved extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SuccessFullySaved(String id, String message, final ModalWindow modalWindow) {
		super(id);

		Form success = new Form("success");
		success.setMultiPart(true);

		MultiLineLabel messageLabel = new MultiLineLabel("message", message);
		messageLabel.setEscapeModelStrings(false);

		success.add(messageLabel);
		modalWindow.setTitle("Successfully Saved.");

		modalWindow.setResizable(false);
		modalWindow.setUseInitialHeight(true);
		modalWindow.setInitialWidth(30);
		modalWindow.setInitialHeight(15);
		modalWindow.setWidthUnit("em");
		modalWindow.setHeightUnit("em");

		AjaxButton okButton = new AjaxButton("okButton", success) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				if (target != null) {
					modalWindow.close(target);
				}
			}
			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {

			}
		};

		success.add(okButton);
		add(success);
	}

}