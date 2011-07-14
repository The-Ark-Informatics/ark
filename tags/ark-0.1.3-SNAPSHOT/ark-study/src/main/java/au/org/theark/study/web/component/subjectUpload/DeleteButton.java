package au.org.theark.study.web.component.subjectUpload;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.web.component.AjaxDeleteButton;

public class DeleteButton extends AjaxDeleteButton {
		private static final long serialVersionUID = 4966354164332401574L;
		private transient Logger log = LoggerFactory.getLogger(DeleteButton.class);

		DeleteButton(final StudyUpload upload, Component component) {
			// Properties contains:
			// confirmDelete=Are you sure you want to delete?
			// delete=Delete
			super(au.org.theark.study.web.Constants.DELETE_FILE,
					new StringResourceModel("confirmDelete", component, null),
					new StringResourceModel(au.org.theark.study.web.Constants.DELETE,
							component, null));
		}
		
		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		}
	}
