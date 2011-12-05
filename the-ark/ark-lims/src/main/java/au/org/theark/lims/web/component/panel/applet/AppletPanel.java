package au.org.theark.lims.web.component.panel.applet;

import org.apache.wicket.markup.html.panel.Panel;

public class AppletPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2488908016769287046L;

	/**
	 * Construct a new applet, given the specified paramters
	 * @param id The Wicket identifier
	 * @param name The name of the Applet
	 * @param className The class that the applet refers to
	 * @param archive The archive resource (jar)
	 * @param paramaterName A parameter name to add
	 * @param parameterValue A parameter value to add
	 */
	public AppletPanel(String id, final String name, final String className, final String archive, final String paramaterName, final String parameterValue) {
		super(id);
		setOutputMarkupPlaceholderTag(true);

		final DeployJava applet = new DeployJava("applet");
		applet.setOutputMarkupPlaceholderTag(true);
		applet.setName(name);
		applet.setCode(className);
		applet.setArchive(archive);
		applet.addParameter(paramaterName, parameterValue);
		
		applet.setCodebase("../classes");
		applet.setMinimalVersion("1.6");
		applet.setWidth(100);
		applet.setHeight(200);
		add(applet);
	}
}