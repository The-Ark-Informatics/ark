/**
 * 
 */
package au.org.theark.core.web.component;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.model.IModel;

/**
 * @author cellis
 *
 */
public abstract class ArkMainTab extends AbstractTab
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3346358837428172502L;

	/**
	 * @param id
	 */
	public ArkMainTab(IModel<String> id)
	{
		super(id);
	}
	
	/**
	 * Method that determines whether or not tab is clickable or not
	 */
	public abstract boolean isAccessible();
}
