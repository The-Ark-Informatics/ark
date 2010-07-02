package au.org.theark.security;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.service.ContainerService;


@Component
public class ArkLdapRealm extends AuthorizingRealm{
	
	
	static final Logger log = LoggerFactory.getLogger(ArkLdapRealm.class);

	/*Interface to Core*/

	
	protected ContainerService containerService;

	protected IArkCommonService iArkCommonService;
	
	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}
	
	
	@Autowired
	public void setContainerService(ContainerService containerService) {
		this.containerService = containerService;
	}


	public ArkLdapRealm() {
		log.info("In ArkLdapRealm constructor");
        setName("ArkLdapRealm"); //This name must match the name in the User class's getPrincipals() method
        setCredentialsMatcher(new Sha256CredentialsMatcher());
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    	//System.out.println("\n -----In ArkLdapRealm:doGetAuthenticationInfo");
    	log.info(" in  doGetAuthenticationInfo()");
    	SimpleAuthenticationInfo sai = null;
    	ArkUserVO userVO = null;
    	UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        try{
        	userVO = iArkCommonService.getUser(token.getUsername().trim());//Example to use core services to get user
        	if(userVO != null){
        		sai = new SimpleAuthenticationInfo(userVO.getUserName(), userVO.getPassword(),getName());
        	}
        
        }catch(ArkSystemException etaSystem){
        	log.error("Exception occured while the Realm invoked Ldap Interface to look up person" + etaSystem.getMessage());
        }
        return sai;
    }

    /**
     * This method will check for roles.Wicket calls this method.
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	
    	List<String> roles = null;
    	SimpleAuthorizationInfo info = null;
    	log.info("doGetAuthorizationInfo invoked");
    	//Since the credentials stored was username we cast it to String, if an ID was stored in Long format then use the user.getId()
    	String userName = (String) principals.fromRealm(getName()).iterator().next();
    	
    	try{
    		 roles = iArkCommonService.getUserRole(userName);
    		
    	}catch(ArkSystemException ine)
    	{
    		log.error("Exception occured while locating user in ldap from ArkLdapRealm.doGetAuthorizationInfo()" + ine.getMessage());
    	}    	
    		 
        if( roles != null && roles.size() > 0) {
        	 info = new SimpleAuthorizationInfo();
        	 info.addRoles(roles);
        }
         return info;
    }
	

}
