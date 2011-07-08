package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;
import au.org.theark.study.web.Constants;

/**
 * The main class that implements the common service IMainTabProvider.This contributes the Tab menu which forms the entry point into Study module. As
 * part of the main Tab that it contributes it will also contain the sub-menu tabs.This more like a plugin class.
 * 
 * @author nivedann
 * 
 */
public class MainTabProviderImpl extends Panel implements IMainTabProvider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1507516631450757896L;
	private List<ITab>			moduleTabsList;
	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
	private TabbedPanel			moduleTabbedPanel;

	/**
	 * Default constructor. Constructs a new instance of MainTabProviderImpl. Also instantiates the list of sub-men tabs contained within it.
	 * 
	 * @param panelId
	 *           The panel identifer (passed to the Panel super class)
	 */
	public MainTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * @param studyLogoMarkup The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyLogoMarkup)
	{
		this.studyLogoMarkup = studyLogoMarkup;
		
		// Main Top level Tabs
		ITab studyTab = createStudyTab(Constants.STUDY_MAIN_TAB);
		ITab subjectTab = createSubjectTab(Constants.SUBJECT_MAIN_TAB);
		moduleTabsList.add(studyTab);
		moduleTabsList.add(subjectTab);
		return moduleTabsList;
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * @param studyNameMarkup The reference to the WebMarkupContainer that contains the study name reference (refreshed on Study selection)
	 * @param studyLogoMarkup The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @param arkContextMarkup The reference to the WebMarkupContainer that contains the context items reference (refreshed on Study selection)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
	{
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;

		// Main Top level Tabs
		ITab studyTab = createStudyTab(Constants.STUDY_MAIN_TAB);
		ITab subjectTab = createSubjectTab(Constants.SUBJECT_MAIN_TAB);
		moduleTabsList.add(studyTab);
		moduleTabsList.add(subjectTab);
		return moduleTabsList;
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * @param studyNameMarkup The reference to the WebMarkupContainer that contains the study name reference (refreshed on Study selection)
	 * @param studyLogoMarkup The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @param arkContextMarkup The reference to the WebMarkupContainer that contains the context items reference (refreshed on Study selection)
	 * @param moduleTabbedPanel The reference to the main tab panel (Allows repainting on Study selection to show particular tabs)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup, TabbedPanel moduleTabbedPanel)
	{
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		this.setModuleTabbedPanel(moduleTabbedPanel);

		ITab tab1 = createStudyTab(Constants.STUDY_MAIN_TAB);// Forms the Main Top level Tab
		ITab tab2 = createSubjectTab(Constants.SUBJECT_MAIN_TAB);
		moduleTabsList.add(tab1);
		moduleTabsList.add(tab2);
		return moduleTabsList;
	}

	/**
	 * Create the main Study tab. This tab is always visible/accessible within the application
	 * 
	 * @param tabName
	 *           The id/name of the tab
	 * @return the main Study tab
	 */
	public ITab createStudyTab(final String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8671910074409249398L;

			@Override
			public Panel getPanel(String pid)
			{
				return panelToReturn(pid, tabName);
			}

			public boolean isAccessible()
			{
				// Study tab is always accessible
				return true;
			}

			public boolean isVisible()
			{
				// Study tab is always visible
				return true;
			}
		};
	}

	/**
	 * Creates/returns the panel of sub-menus for the parent main tab
	 * 
	 * @param pid
	 *           The panel identifier
	 * @param tabName
	 *           The name of the main tab
	 * @return The panel of sub-menu tabs for the passed in main tabName
	 */
	public Panel panelToReturn(String pid, String tabName)
	{
		Panel panelToReturn = null;// Set up a common tab that will be accessible for all users
		if (tabName.equals(Constants.STUDY_MAIN_TAB))
		{
			panelToReturn = new StudySubMenuTab(pid, studyNameMarkup, studyLogoMarkup, arkContextMarkup, this);// The sub menus for Study
		}
		else if (tabName.equalsIgnoreCase(Constants.SUBJECT_MAIN_TAB))
		{
			panelToReturn = new SubjectSubMenuTab(pid, arkContextMarkup);
		}
		return panelToReturn;
	}

	/**
	 * Create the main Subject tab. This tab is visible/accessible within the application when a Study is set into context
	 * 
	 * @param tabName
	 *           The id/name of the tab
	 * @return the main Subject tab
	 */
	public ITab createSubjectTab(final String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6838973454398478802L;

			@Override
			public Panel getPanel(String pid)
			{
				Panel panelToReturn = null;// Set up a common tab that will be accessible for all users
				if (tabName.equals(Constants.STUDY_MAIN_TAB))
				{
					panelToReturn = new StudySubMenuTab(pid, studyNameMarkup, studyLogoMarkup, arkContextMarkup);// The sub menus for Study
				}
				else if (tabName.equalsIgnoreCase(Constants.SUBJECT_MAIN_TAB))
				{
					panelToReturn = new SubjectSubMenuTab(pid, arkContextMarkup);
				}
				return panelToReturn;
			}

			public boolean isAccessible()
			{
				// Only accessible when study in session (repainted on Study selection)
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null)
				{
					this.getPanel(Constants.SUBJECT_MAIN_TAB).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}

			public boolean isVisible()
			{
				// Only visible when study in session (repainted on Study selection)
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null)
				{
					return false;
				}
				else
					return true;
			}
		};
	}

	/**
	 * @param moduleTabbedPanel
	 *           the moduleTabbedPanel to set
	 */
	public void setModuleTabbedPanel(TabbedPanel moduleTabbedPanel)
	{
		this.moduleTabbedPanel = moduleTabbedPanel;
	}

	/**
	 * @return the moduleTabbedPanel
	 */
	public TabbedPanel getModuleTabbedPanel()
	{
		return moduleTabbedPanel;
	}
}