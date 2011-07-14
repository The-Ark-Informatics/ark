/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author nivedann
 *
 */
public class ArkContextPanel extends Panel{

	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5419195208290241665L;
	private Label studySummaryLabel;
	
	/**
	 * @param id
	 */
	public ArkContextPanel(String id) {
		super(id);
		studySummaryLabel = new Label("studySummaryLabel","You have selected the Study ");
		add(studySummaryLabel);
	}
	
	

}
