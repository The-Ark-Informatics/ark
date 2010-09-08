package au.org.theark.study.web.component.site;

import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteContainerPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(SiteContainerPanel.class);
	
	//Search Site Panel
	Search searchSitesPanel;
	
	public SiteContainerPanel(String id) {
		super(id);
		log.info("SiteContainerPanel initialised. Creating the Search Panel for Sites..");
		searchSitesPanel = new Search("searchSitePanel");
		searchSitesPanel.initialise();
		add(searchSitesPanel);
	}
	

}
