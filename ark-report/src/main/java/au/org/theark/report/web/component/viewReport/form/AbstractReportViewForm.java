package au.org.theark.report.web.component.viewReport.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractReportViewForm<T> extends Form<T>{
	
	public AbstractReportViewForm(String id, CompoundPropertyModel<T> model) {
		super(id, model);
	}

}
