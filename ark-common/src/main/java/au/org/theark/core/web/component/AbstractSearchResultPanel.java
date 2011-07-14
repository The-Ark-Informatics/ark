/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.component;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author nivedann
 *
 */
public abstract class AbstractSearchResultPanel<T> extends Panel{

	/**
	 * @param id
	 */
	public AbstractSearchResultPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	
	public abstract PageableListView<T> buildPageableListView(IModel iModel);

	
	
}
