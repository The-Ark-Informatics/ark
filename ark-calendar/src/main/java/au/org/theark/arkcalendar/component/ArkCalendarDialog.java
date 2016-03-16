package au.org.theark.arkcalendar.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.threeten.bp.LocalDateTime;

import au.org.theark.arkcalendar.dao.ArkCalendarDao;
import au.org.theark.arkcalendar.data.ArkCalendarEvent;
import au.org.theark.arkcalendar.data.ArkCalendarEvent.Category;
import au.org.theark.arkcalendar.util.SignIn2Session;

import com.googlecode.wicket.jquery.ui.form.RadioChoice;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.kendo.ui.form.datetime.local.DateTimePicker;

public abstract class ArkCalendarDialog extends AbstractFormDialog<ArkCalendarEvent>
{
	private static final long serialVersionUID = 1L;
	protected final DialogButton btnSubmit = new DialogButton(SUBMIT, Model.of("Save"));
	
	protected final DialogButton btnDelete = new DialogButton("DELETE", Model.of("Delete"));
	
	protected final DialogButton btnClose = new DialogButton(CLOSE, Model.of("Close"));
	
		
	static IModel<ArkCalendarEvent> emptyModel()
	{
		return Model.of(new ArkCalendarEvent(0, "", Category.PUBLIC, LocalDateTime.now()));
	}

	private Form<?> form;
	private FeedbackPanel feedback;

	public ArkCalendarDialog(String id, String title)
	{
		super(id, title, emptyModel(), true);
		
		SignIn2Session session =(SignIn2Session)getSession();
		
		int calendarId = (int)getSession().getAttribute("calendarId");
		String user = session.getUser();
		
	
		

		this.form = new Form<ArkCalendarEvent>("form", new CompoundPropertyModel<ArkCalendarEvent>(this.getModel()));
		this.add(this.form);

		this.form.add(new RequiredTextField<String>("title"));
		this.form.add(new RequiredTextField<String>("subjectUID"));
		this.form.add(new RadioChoice<Category>("category", Arrays.asList(Category.values())));

		// DateTimePickers //
		final DateTimePicker startDateTimePicker = new DateTimePicker("start");
		final DateTimePicker endDateTimePicker = new DateTimePicker("end");

		this.form.add(startDateTimePicker.setRequired(true));
		this.form.add(endDateTimePicker);

		// All-day checkbox //
		CheckBox checkAllDay = new AjaxCheckBox("allDay") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure()
			{
				super.onConfigure();

				Boolean allday = this.getModelObject();
				startDateTimePicker.setTimePickerEnabled(!allday);
				endDateTimePicker.setTimePickerEnabled(!allday);
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				Boolean allday = this.getModelObject();
				startDateTimePicker.setTimePickerEnabled(target, !allday);
				endDateTimePicker.setTimePickerEnabled(target, !allday);
			}
		};

		this.form.add(checkAllDay.setOutputMarkupId(true));
		this.form.add(new Label("label", "All day?").add(AttributeModifier.append("for", checkAllDay.getMarkupId())));
		
		// FeedbackPanel //
		this.feedback = new JQueryFeedbackPanel("feedback");
		this.form.add(this.feedback);
		
		if("arksuperuser@ark.org.au".equals(user)){
			btnSubmit.setEnabled(true);
			btnDelete.setEnabled(true);
			btnClose.setEnabled(true);
			form.setEnabled(true);
		}
		else{
			String role= ArkCalendarDao.getUserRole(user, calendarId);
			if("Calendar Administrator".equals(role)){
				btnSubmit.setEnabled(true);
				btnDelete.setEnabled(true);
				form.setEnabled(true);
			}else if("Calendar Data Manager".equals(role)){
				btnSubmit.setEnabled(true);
				btnDelete.setEnabled(false);
				form.setEnabled(true);
			}else if("Calendar Read-Only User".equals(role)){
				btnSubmit.setEnabled(false);
				btnDelete.setEnabled(false);
				form.setEnabled(false);
			}else{
				btnSubmit.setEnabled(false);
				btnDelete.setEnabled(false);
				form.setEnabled(false);
			}
		}
	}
	
	

	// AbstractFormDialog //
	@Override
	protected List<DialogButton> getButtons()
	{
		return Arrays.asList(this.btnSubmit, this.btnDelete, this.btnClose);
	}

	@Override
	protected DialogButton getSubmitButton()
	{
		return this.btnSubmit;
	}

	@Override
	public Form<?> getForm()
	{
		return this.form;
	}

	// Events //
	@Override
	protected void onOpen(IPartialPageRequestHandler handler)
	{
		handler.add(this.form);
	}

	@Override
	public void onError(AjaxRequestTarget target)
	{
		target.add(this.feedback);
	}
}
