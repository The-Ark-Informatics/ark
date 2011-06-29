package au.org.theark.core.web.component;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractDetailModalWindow extends ModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -794586150493541168L;
	protected String title;
	protected FeedbackPanel modalFeedbackPanel; 
	protected Panel contentPanel;
	
	public AbstractDetailModalWindow(String id, String title)
	{
		super(id);
		this.title = title;
		initialise();
	}

	public AbstractDetailModalWindow(String id, String title, Panel panel)
	{
		super(id);
		this.title = title;
		this.contentPanel = panel;
		initialise();
		initialiseContentPanel(contentPanel);
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
		modalFeedbackPanel= new FeedbackPanel("modalFeedback");
		modalFeedbackPanel.setOutputMarkupId(true);
		return modalFeedbackPanel;
	}
	
	protected void initialiseContentPanel(Panel panel)
	{
		// Set the content panel, implementing the abstract methods
		setContent(panel);
	}

	/**
	 * @return the feedbackPanel
	 */
	public FeedbackPanel getModalFeedbackPanel()
	{
		return modalFeedbackPanel;
	}

	/**
	 * @param feedbackPanel the feedbackPanel to set
	 */
	public void setModalFeedbackPanel(FeedbackPanel feedbackPanel)
	{
		this.modalFeedbackPanel = feedbackPanel;
	}
}
