package au.org.theark.arkcalendar.pages.calendar;

import java.util.Arrays;
import java.util.List;

import au.org.theark.arkcalendar.SamplePage;





abstract class AbstractCalendarPage extends SamplePage
{
	private static final long serialVersionUID = 1L;
	
	/** Wicket jQuery UI Google Calendar API Key */
	static final String GCAL_API_KEY = "AIzaSyD9QzHiSkjGAZ0ajGe-okoxWQ07H39jksU";

	public AbstractCalendarPage()
	{
		
	}

	@Override
	protected List<DemoLink> getDemoLinks()
	{
		return Arrays.asList(
//				new DemoLink(DefaultCalendarPage.class, "Calendar: event sources"),
//				new DemoLink(CustomCalendarPage.class, "Calendar: custom events"),
//				new DemoLink(ObjectCalendarPage.class, "Calendar: event objects"),
				new DemoLink(ArkCalendarPage.class, "Calendar: <b>full demo</b>")
			);
	}
}
