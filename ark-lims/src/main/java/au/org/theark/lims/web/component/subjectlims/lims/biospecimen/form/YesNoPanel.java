package au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;


public class YesNoPanel extends Panel {
	 
    public YesNoPanel(String id, String message, final ModalWindow modalWindow, final ConfirmationAnswer answer) {
        super(id);
 
        Form yesNoForm = new Form("yesNoForm");
        MultiLineLabel messageLabel = new MultiLineLabel("message", message);
        messageLabel.setEscapeModelStrings(false);
        yesNoForm.add(messageLabel);
        yesNoForm.setMultiPart(true);
        modalWindow.setTitle("Warning");
        modalWindow.setResizable(false);
        modalWindow.setUseInitialHeight(true);
        modalWindow.setInitialWidth(30);
        modalWindow.setInitialHeight(15);
        modalWindow.setWidthUnit("em");
        modalWindow.setHeightUnit("em");
         
        AjaxButton yesButton = new AjaxButton("yesButton", yesNoForm) {
 
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                if (target != null) {
                    answer.setAnswer(true);
                    modalWindow.close(target);
                }
            }

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub
				
			}
        };
 
        AjaxButton noButton = new AjaxButton("noButton", yesNoForm) {
 
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                if (target != null) {
                    answer.setAnswer(false);
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