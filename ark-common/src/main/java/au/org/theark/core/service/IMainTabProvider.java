package au.org.theark.core.service;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface IMainTabProvider {
	
	public ITab createTab(String tabName);
}
