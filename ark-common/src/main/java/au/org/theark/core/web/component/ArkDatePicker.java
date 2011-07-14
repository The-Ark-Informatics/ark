/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.component;

import org.apache.wicket.extensions.yui.calendar.DatePicker;

/**
 * @author nivedann
 *
 */
public class ArkDatePicker extends DatePicker{
	
	public ArkDatePicker(){
		super();
	}
	
	@Override
	protected boolean enableMonthYearSelection()
	{
		return true;
	}

}
