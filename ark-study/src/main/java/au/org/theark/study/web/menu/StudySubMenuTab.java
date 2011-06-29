package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.study.web.component.managestudy.StudyContainer;
import au.org.theark.study.web.component.manageuser.UserContainerPanel;
import au.org.theark.study.web.component.mydetails.MyDetailsContainer;
import au.org.theark.study.web.component.studycomponent.StudyComponentContainerPanel;

@SuppressWarnings("serial")
public class StudySubMenuTab extends AbstractArkTabPanel
{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	List<ITab> tabList;
	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
	//private ArkModule arkModule;
	
	public StudySubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public StudySubMenuTab(String id, WebMarkupContainer studyLogoMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.studyLogoMarkup = studyLogoMarkup;
		buildTabs();
	}

	public StudySubMenuTab(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	public void buildTabs()
	{

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_STUDY);
		List<ArkFunction>   arkFunctionList = iArkCommonService.getModuleFunction(arkModule);//Gets a list of ArkFunctions for the given Module
		
		//Iterate each ArkFunction render the Tabs.When something is clicked it uses the arkFunction and calls processAuthorixationCache to clear principals of the user
		//and loads the new set of principals.(permissions)
		for (final ArkFunction menuArkFunction : arkFunctionList) {
			
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(),this, null))
			{
				
				@Override
				public Panel getPanel(String panelId)
				{
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users
					
					if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_USER)){
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY,menuArkFunction);
						panelToReturn = new UserContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY))
					{
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY,menuArkFunction);
						panelToReturn = new StudyContainer(panelId, studyNameMarkup, studyLogoMarkup, arkContextMarkup);						
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY_COMPONENT))
					{
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY,menuArkFunction);
						panelToReturn = new StudyComponentContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_MY_DETAIL))
					{
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY,menuArkFunction);
						Subject currentUser = SecurityUtils.getSubject();	
						panelToReturn = new MyDetailsContainer(panelId, new ArkUserVO(), currentUser);
					}
					return panelToReturn;
				}
			});
			
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_STUDY_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

}
