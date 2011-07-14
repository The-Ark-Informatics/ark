package au.org.theark.phenotypic.web.component.phenoCollection;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.web.component.AjaxDeleteButton;

public class DeleteButton extends AjaxDeleteButton {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6715945954180081192L;

		DeleteButton(final PhenoCollection phenoCollection, Component component) {
			// Properties contains:
			// confirmDelete=Are you sure you want to delete?
			// delete=Delete
			super(au.org.theark.phenotypic.web.Constants.DELETE_FILE,
					new StringResourceModel("confirmDelete", component, null),
					new StringResourceModel(au.org.theark.phenotypic.web.Constants.DELETE,
							component, null));
		}
		
		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		}
	}
