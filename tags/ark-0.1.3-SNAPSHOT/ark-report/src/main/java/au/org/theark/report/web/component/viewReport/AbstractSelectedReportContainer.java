package au.org.theark.report.web.component.viewReport;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public abstract class AbstractSelectedReportContainer<T> extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected CompoundPropertyModel<T> cpModel;
	
	public AbstractSelectedReportContainer(String id) {
		super(id);
		initialiseCPModel();
	}

	protected abstract void initialiseCPModel();
}
