package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractDetailModalWindow extends ModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -794586150493541168L;
	protected String title;
	protected FeedbackPanel feedbackPanel; 
	protected Panel panel;
	
	
	public AbstractDetailModalWindow(String id)
	{
		super(id);
		this.title = "";
		this.panel = new EmptyPanel("content");
		initialise();
		initialiseContentPanel(panel);
	}

	protected void initialise()
	{
		setTitle(this.title);
		setResizable(false);
		setWidthUnit("%");
		setInitialWidth(90);		
		setCssClassName(CSS_CLASS_GRAY);
		add(initialiseFeedBackPanel());
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedbackPanel= new FeedbackPanel("modalWindowFeedback");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	protected void initialiseContentPanel(Panel panel)
	{
		// Set the content panel, implementing the abstract methods
		setContent(panel);
	}
	
	public void close(final AjaxRequestTarget target)
	{
		super.close(target);
		onCloseModalWindow(target);
	}
	
	abstract protected void onCloseModalWindow(AjaxRequestTarget target);

	/**
	 * @return the feedbackPanel
	 */
	public FeedbackPanel getModalFeedbackPanel()
	{
		return feedbackPanel;
	}

	/**
	 * @param feedbackPanel the feedbackPanel to set
	 */
	public void setModalFeedbackPanel(FeedbackPanel feedbackPanel)
	{
		this.feedbackPanel = feedbackPanel;
	}
}
