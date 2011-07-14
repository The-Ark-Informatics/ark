package au.org.theark.study.web.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public interface IArkComponent {
	
	public void initialiseMarkupContainers();
	
	public WebMarkupContainer initialiseDetailPanel();
	
	//public void initialisePanel();
	
	public FeedbackPanel initialiseFeedBackPanel();
	
	public WebMarkupContainer initialiseSearchResults();
	
	public WebMarkupContainer initialiseSearchPanel();

}
