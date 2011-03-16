package au.org.theark.study.web.component.subjectFile;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.web.component.AjaxDeleteButton;

public class DeleteButton extends AjaxDeleteButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4536757533116035270L;

	DeleteButton(final SubjectFile subjectFile, Component component) {
		// Properties contains:
		// confirmDelete=Are you sure you want to delete?
		// delete=Delete
		super(au.org.theark.study.web.Constants.DELETE_FILE,
				new StringResourceModel("confirmDelete", component, null),
				new StringResourceModel(
						au.org.theark.study.web.Constants.DELETE, component,
						null));
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	}
}
