package au.org.theark.core.web.component;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Abstract class for implementation of the main tab/modules at the "top" level of the application
 * 
 * @author cellis
 * 
 */
public abstract class ArkMainTab extends AbstractTab
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3346358837428172502L;
	private Panel					panel;

	/**
	 * Creates a new ArkMainTab object, extending AbstractTab
	 * 
	 * @param id
	 *           The id of the tab that Ajax will reference
	 */
	public ArkMainTab(IModel<String> id)
	{
		super(id);
	}

	/**
	 * Method that determines whether or not tab is clickable or not
	 */
	public abstract boolean isAccessible();

	/**
	 * Method that determines whether or not tab is visible or not
	 */
	public abstract boolean isVisible();

	@Override
	public Panel getPanel(String panelId)
	{
		if (panel == null)
		{
			// Lazily create the panel
			panel = createPanel();
			if (!TabbedPanel.TAB_PANEL_ID.equals(panel.getId()))
			{
				throw new IllegalArgumentException("Panel id must be TabbedPanel.TAB_PANEL_ID");
			}
			panel.setOutputMarkupId(true);
		}
		return panel;
	}

	protected Panel createPanel()
	{
		throw new IllegalArgumentException("Must provide a panel");
	}
}