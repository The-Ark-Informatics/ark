package au.org.theark.arkcalendar.component;

import java.util.List;

import org.apache.wicket.model.IModel;

import au.org.theark.arkcalendar.data.ArkCalendarVo;

import com.googlecode.wicket.jquery.ui.widget.menu.IMenuItem;
import com.googlecode.wicket.jquery.ui.widget.menu.MenuItem;

public class ArkMenuItem extends MenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArkCalendarVo calendarVo;
	
	public ArkMenuItem(IModel<String> title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(IModel<String> title, List<IMenuItem> items) {
		super(title, items);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(IModel<String> title, String icon, List<IMenuItem> items) {
		super(title, icon, items);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(IModel<String> title, String icon) {
		super(title, icon);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(String title, List<IMenuItem> items) {
		super(title, items);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(String title, String icon, List<IMenuItem> items) {
		super(title, icon, items);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(String title, String icon) {
		super(title, icon);
		// TODO Auto-generated constructor stub
	}

	public ArkMenuItem(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public ArkMenuItem(String title, ArkCalendarVo calendarVo) {
		super(title);
		this.calendarVo = calendarVo;
	}

	public ArkCalendarVo getCalendarVo() {
		return calendarVo;
	}

	public void setCalendarVo(ArkCalendarVo calendarVo) {
		this.calendarVo = calendarVo;
	}
	


}
