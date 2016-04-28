package au.org.theark.arkbooking.pages.calendar;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import au.org.theark.arkbooking.component.DemoCalendarDialog;
import au.org.theark.arkbooking.dao.CalendarDAO;
import au.org.theark.arkbooking.data.DemoCalendarEvent;
import au.org.theark.arkbooking.data.DemoCalendarModel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.calendar.Calendar;
import com.googlecode.wicket.jquery.ui.calendar.CalendarView;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class ArkCalendarPage extends AbstractCalendarPage
{
	private static final long serialVersionUID = 1L;

	private Calendar calendar;

	public ExtendedCalendarPage()
	{
		// Form //
		final Form<Date> form = new Form<Date>("form");
		this.add(form);

		// FeedbackPanel //
		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		form.add(feedback.setOutputMarkupId(true));

		// Dialog //
		final DemoCalendarDialog dialog = new DemoCalendarDialog("dialog", "Event details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target)
			{
				DemoCalendarEvent event = this.getModelObject();

				// new event //
				if (CalendarDAO.isNew(event))
				{
					CalendarDAO.addEvent(event);
				}

				calendar.refresh(target); //use calendar.refresh(target) instead of target.add(calendar)
			}
		};

		this.add(dialog);

		// Calendar //
		Options options = new Options();
		options.set("theme", true);
		options.set("header", "{ left: 'title', right: 'month,agendaWeek,agendaDay, today, prev,next' }");

		this.calendar = new Calendar("calendar", new DemoCalendarModel(), options) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isSelectable()
			{
				return true;
			}

			@Override
			public boolean isDayClickEnabled()
			{
				return true;
			}

			@Override
			public boolean isEventClickEnabled()
			{
				return true;
			}

			@Override
			public boolean isEventDropEnabled()
			{
				return true;
			}

			@Override
			public boolean isEventResizeEnabled()
			{
				return true;
			}

			@Override
			public void onDayClick(AjaxRequestTarget target, CalendarView view, LocalDateTime date, boolean allDay)
			{
				DemoCalendarEvent event = CalendarDAO.newEvent(date);

				dialog.setModelObject(event);
				dialog.open(target);
			}

			@Override
			public void onSelect(AjaxRequestTarget target, CalendarView view, LocalDateTime start, LocalDateTime end, boolean allDay)
			{
				DemoCalendarEvent event = CalendarDAO.newEvent(start, end);
				event.setAllDay(allDay);

				dialog.setModelObject(event);
				dialog.open(target);
			}

			@Override
			public void onEventClick(AjaxRequestTarget target, CalendarView view, int eventId)
			{
				DemoCalendarEvent event = CalendarDAO.getEvent(eventId);

				if (event != null)
				{
					dialog.setModelObject(event);
					dialog.open(target);
				}
			}

			@Override
			public void onEventDrop(AjaxRequestTarget target, int eventId, long delta, boolean allDay)
			{
				DemoCalendarEvent event = CalendarDAO.getEvent(eventId);

				if (event != null)
				{
					event.setStart(event.getStart() != null ? event.getStart().plus(delta, ChronoUnit.MILLIS) : null);	//recompute start date
					event.setEnd(event.getEnd() != null ? event.getEnd().plus(delta, ChronoUnit.MILLIS) : null);	// recompute end date
					event.setAllDay(allDay);

					this.info(String.format("%s changed to %s", event.getTitle(), event.getStart()));
					target.add(feedback);
				}
			}

			@Override
			public void onEventResize(AjaxRequestTarget target, int eventId, long delta)
			{
				DemoCalendarEvent event = CalendarDAO.getEvent(eventId);

				if (event != null)
				{
					LocalDateTime date = event.getEnd() == null ? event.getStart() : event.getEnd();
					event.setEnd(date.plus(delta, ChronoUnit.MILLIS));

					this.info(String.format("%s now ends the %s", event.getTitle(), event.getEnd()));
					target.add(feedback);
				}
			}
		};

		form.add(this.calendar);
	}
}
