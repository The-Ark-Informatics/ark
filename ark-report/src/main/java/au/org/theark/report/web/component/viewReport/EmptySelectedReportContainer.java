package au.org.theark.report.web.component.viewReport;


public class EmptySelectedReportContainer extends AbstractSelectedReportContainer<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptySelectedReportContainer(String id) {
		super(id);
	}

	@Override
	protected void initialiseCPModel() {
		cpModel = null;
	}

}
