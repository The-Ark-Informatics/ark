package au.org.theark.arkcalendar.data;

import java.util.List;

import org.threeten.bp.LocalDate;

import au.org.theark.arkcalendar.dao.ArkCalendarDao;

import com.googlecode.wicket.jquery.ui.calendar.CalendarEvent;
import com.googlecode.wicket.jquery.ui.calendar.CalendarModel;
import com.googlecode.wicket.jquery.ui.calendar.ICalendarVisitor;
//import com.googlecode.wicket.jquery.ui.samples.data.dao.CalendarDAO;

public class ArkCalendarModel extends CalendarModel implements ICalendarVisitor
{
	private static final long serialVersionUID = 1L;
	
	private int calendarId;
	
	public ArkCalendarModel(int calendarId) {
		super();
		this.calendarId = calendarId;
	}

	@Override
	protected List<ArkCalendarEvent> load()
	{
		 LocalDate start = this.getStart();
		 LocalDate end = this.getEnd();
		 
		 List<ArkCalendarEvent> events = ArkCalendarDao.getEvents(start, end, calendarId);
		
		return events;
	}

	// ICalendarVisitor //
	@Override
	public void visit(CalendarEvent event)
	{
		//you can set additional properties to each event retrieved by #load() here
	}
}
