package au.org.theark.arkcalendar.pages.calendar;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import au.org.theark.arkcalendar.component.ArkCalendarDialog;
import au.org.theark.arkcalendar.dao.ArkCalendarDao;
import au.org.theark.arkcalendar.data.ArkCalendarEvent;
import au.org.theark.arkcalendar.data.ArkCalendarModel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.calendar.Calendar;
import com.googlecode.wicket.jquery.ui.calendar.CalendarView;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;

public class ArkCalendarPage extends AbstractCalendarPage
{
	private static final long serialVersionUID = 1L;

	private Calendar calendar;
	
	private int calendarId;

	public ArkCalendarPage()
	{
		
		
		calendarId = (int)getSession().getAttribute("calendarId");
		
		// Form //
		final Form<Date> form = new Form<Date>("form");
		this.add(form);
		
		String calendarLabel="Study: "+ getSession().getAttribute("study").toString()+" - "+getSession().getAttribute("calendarName").toString();
		
		form.add(new Label("calendarLabel",calendarLabel));

		// FeedbackPanel //
		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		form.add(feedback.setOutputMarkupId(true));

		// Dialog //
		final ArkCalendarDialog dialog = new ArkCalendarDialog("dialog", "Event details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target)
			{
				ArkCalendarEvent event = this.getModelObject();

				event.setCalenderId(getCalendarId());
				// new event //
				
				if (ArkCalendarDao.isNew(event))
				{
					ArkCalendarDao.addEvent(event);
				}else{
					ArkCalendarDao.updateEvent(event);
				}
				calendar.refresh(target); //use calendar.refresh(target) instead of target.add(calendar)
			}
			
			@Override
			public void onClick(AjaxRequestTarget target, DialogButton button) {
				String btnName= button.getName();
				if("DELETE".equals(btnName)){
					ArkCalendarEvent event = this.getModelObject();
					ArkCalendarDao.cancelEvent(event);
				}
				calendar.refresh(target); //
				super.onClick(target, button);
			}
			
		};

		this.add(dialog);

		// Calendar //
		Options options = new Options();
		options.set("theme", true);
		options.set("header", "{ left: 'title', right: 'month,agendaWeek,agendaDay, today, prev,next' }");

		this.calendar = new Calendar("calendar", new ArkCalendarModel(calendarId), options) {

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
					ArkCalendarEvent event = ArkCalendarDao.newEvent(date);
	
					event.setCustomFieldData(ArkCalendarDao.getCustomFieldList(event.getCalenderId(),event.getSubjectUID()));
					dialog.setModelObject(event);
					dialog.initCalendarDialog();
					dialog.open(target);
			}

			@Override
			public void onSelect(AjaxRequestTarget target, CalendarView view, LocalDateTime start, LocalDateTime end, boolean allDay)
			{
				
					ArkCalendarEvent event = ArkCalendarDao.newEvent(start, end);
					
					event.setCustomFieldData(ArkCalendarDao.getCustomFieldList(event.getCalenderId(), event.getSubjectUID()));
					event.setAllDay(allDay);
	
					dialog.setModelObject(event);
					dialog.initCalendarDialog();
					dialog.open(target);
			}

			@Override
			public void onEventClick(AjaxRequestTarget target, CalendarView view, int eventId)
			{
				ArkCalendarEvent event = ArkCalendarDao.getEvent(eventId);

				if (event != null)
				{
					event.setCustomFieldData(ArkCalendarDao.getCustomFieldList(event.getCalenderId(), event.getSubjectUID()));
					dialog.setModelObject(event);
					dialog.initCalendarDialog();
					dialog.open(target);
				}
			}

			@Override
			public void onEventDrop(AjaxRequestTarget target, int eventId, long delta, boolean allDay)
			{
				ArkCalendarEvent event = ArkCalendarDao.getEvent(eventId);

				if (event != null)
				{
					event.setStart(event.getStart() != null ? event.getStart().plus(delta, ChronoUnit.MILLIS) : null);	//recompute start date
					event.setEnd(event.getEnd() != null ? event.getEnd().plus(delta, ChronoUnit.MILLIS) : null);	// recompute end date
					event.setAllDay(allDay);
					this.info(String.format("%s changed to %s", event.getTitle(), event.getStart()));
					ArkCalendarDao.updateEvent(event);
					target.add(feedback);
				}
			}

			@Override
			public void onEventResize(AjaxRequestTarget target, int eventId, long delta)
			{
				ArkCalendarEvent event = ArkCalendarDao.getEvent(eventId);

				if (event != null)
				{
					LocalDateTime date = event.getEnd() == null ? event.getStart() : event.getEnd();
					event.setEnd(date.plus(delta, ChronoUnit.MILLIS));
					this.info(String.format("%s now ends the %s", event.getTitle(), event.getEnd()));
					ArkCalendarDao.updateEvent(event);
					target.add(feedback);
				}
			}
		};

		form.add(this.calendar);
		
		
	}
	
	public int getCalendarId() {
		return calendarId;
	}
}
