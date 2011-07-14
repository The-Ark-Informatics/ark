package au.org.theark.core.web.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class AbstractWizardHeaderPanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -626989967032928525L;

	/**
	 * A summary of this step, or some usage advice.
	 */
	private String summary;

	/**
	 * The title of this step.
	 */
	private String title;
	
	
	public AbstractWizardHeaderPanel(String id) {
		super(id);
		
		add(new Label("title", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return getTitle();
					}
				}).setEscapeModelStrings(false));
				add(new Label("summary", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return getSummary();
					}
				}).setEscapeModelStrings(false));
	}
	
	public AbstractWizardHeaderPanel(String id, String summary, String title) {
		super(id);
		
		this.title = title;
		this.summary = summary;
		
		add(new Label("title", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return getTitle();
					}
				}).setEscapeModelStrings(false));
				add(new Label("summary", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return getSummary();
					}
				}).setEscapeModelStrings(false));
	}
	
	/**
	 * Gets the summary of this step. This will be displayed in the title of the wizard while this
	 * step is active. The summary is typically an overview of the step or some usage guidelines for
	 * the user.
	 * 
	 * @return the summary of this step.
	 */
	public String getSummary()
	{
		return (summary != null) ? summary : null;
	}

	/**
	 * Gets the title of this step.
	 * 
	 * @return the title of this step.
	 */
	public String getTitle()
	{
		return (title != null) ? title : null;
	}

}
