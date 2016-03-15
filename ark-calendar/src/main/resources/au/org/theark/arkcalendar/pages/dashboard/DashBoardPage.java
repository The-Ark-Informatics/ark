package au.org.theark.arkbooking.pages.dashboard;

import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import au.org.theark.arkbooking.SamplePage;
import au.org.theark.arkbooking.pages.calendar.ExtendedCalendarPage;

import com.googlecode.wicket.jquery.ui.markup.html.link.SubmitLink;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class DashBoardPage extends SamplePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DashBoardPage() {
		final Form<Date> form = new Form<Date>("form");
		this.add(form);

		// FeedbackPanel //
		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(new SubmitLink("link", Model.of("Calendar")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
//				DefaultButtonPage.this.info(this);
				 setResponsePage(ExtendedCalendarPage.class);
			}
		});
	}

}
