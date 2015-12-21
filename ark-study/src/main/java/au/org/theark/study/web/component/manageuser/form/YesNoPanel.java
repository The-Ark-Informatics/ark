package au.org.theark.study.web.component.manageuser.form;



import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;



public class YesNoPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YesNoPanel(String id, String message, final ModalWindow modalWindow, final ConfirmationAnswer confirmationAnswer) {
		super(id);

		Form yesNoForm = new Form("yesNoForm");
		yesNoForm.setMultiPart(true);

		MultiLineLabel messageLabel = new MultiLineLabel("message", message);
		messageLabel.setEscapeModelStrings(false);

		yesNoForm.add(messageLabel);
		modalWindow.setTitle("Add user for this study?");

		modalWindow.setResizable(false);
		modalWindow.setUseInitialHeight(true);
		modalWindow.setInitialWidth(30);
		modalWindow.setInitialHeight(15);
		modalWindow.setWidthUnit("em");
		modalWindow.setHeightUnit("em");

		AjaxButton yesButton = new AjaxButton("yesButton", yesNoForm) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				if (target != null) {
					confirmationAnswer.setAnswer(true);
					modalWindow.close(target);
				}
			}
			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {

			}
		};

		AjaxButton noButton = new AjaxButton("noButton", yesNoForm) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				if (target != null) {
					confirmationAnswer.setAnswer(false);
					modalWindow.close(target);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};

		yesNoForm.add(yesButton);
		yesNoForm.add(noButton);
		add(yesNoForm);
	}

}