package au.org.theark.core.web.component.menu;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;

/**
 * @author nivedann
 *
 */
public class AbstractArkTabPanel extends Panel{
	
	@SpringBean( name="arkLdapRealm")
	private ArkLdapRealm realm;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	private ArkModule arkModule;
	
	/**
	 * @param id
	 */
	public AbstractArkTabPanel(String id) {
		super(id);
	}


	/**
	 * Clear the current user's session principals
	 * @param arkModuleName
	 * @param arkFunction
	 */
	public void processAuthorizationCache(String arkModuleName , ArkFunction arkFunction){
		arkModule = iArkCommonService.getArkModuleByName(arkModuleName); //Place a default module into session
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
		Subject currentUser = SecurityUtils.getSubject();	
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
	}

}
