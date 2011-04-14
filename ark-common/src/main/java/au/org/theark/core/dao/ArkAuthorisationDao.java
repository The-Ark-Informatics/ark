package au.org.theark.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;

/**
 * @author nivedann
 *
 */
@Repository("arkAuthorisationDao")
public class ArkAuthorisationDao<T>  extends HibernateSessionDao implements IArkAuthorisation{


	/**
	 * Looks up a ArkUser based on a String that represents the user name in LDAP.
	 * If the user name provided is null or is invalid/does not exist in the database, the method will return NULL for a ArkUser
	 * instance.
	 * @param ldapUserName
	 * @return ArkUser
	 */
	protected ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException{
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUser.class);
		criteria.add(Restrictions.eq("ldapUserName", ldapUserName));
		ArkUser arkUser  = (ArkUser)criteria.uniqueResult();
		//Close the session
		session.close();
		if(arkUser != null){
			return arkUser;
		}else{
			throw new EntityNotFoundException("The given Ldap User does not exist in the database system");
		}
	}
	
	/**
	 * Returns a list of ArkRole objects from the backend. This does not use the Stateless session.
	 * Can be used by front-end client's.
	 * @return  List<ArkRole>
	 */
	public List<ArkRole> getArkRoles(){
		
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		List<ArkRole> arkRoleList = criteria.list();
		return arkRoleList;
	}
	
	/**<p>
	 * Given a Ark Role name like Super Administrator or Administrator the method will return the actual instance of
	 * ArkRole object.</p>
	 * @param roleName
	 * @return ArkRole
	 */
	protected ArkRole getArkRoleByName(String roleName){
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkRole.class);
		criteria.add(Restrictions.eq("name", roleName));
		ArkRole arkRole = (ArkRole)criteria.uniqueResult();
		session.close();
		return arkRole;
	}
	
	/**
	 * <p>
	 * The method takes in a LdapUserName and queries the Database using a Stateless session.
	 * The query is again Ark_User_Role table/ArkUserRole entity. The getArkRoleByName is invoked
	 * to get a object that represents Administrator. The method uses this to then check if the given user
	 * is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise
	 * false is returned.</p><br>
	 * @return boolean 
	 * @throws EntityNotFoundException 
	 */
	public boolean isAdministator(String ldapUserName) throws EntityNotFoundException {
		
		boolean isAdministrator = false;
		
		StatelessSession session = getStatelessSession();
		//Check or get user ark_user object based on ldapUserName
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole  = getArkRoleByName(Constants.ARK_ROLE_ADMINISTATOR);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if(arkUserRole != null){
			isAdministrator = true;
		}
		session.close();//Close the session here since it is stateless.  
		return isAdministrator;
	}

	/**
	 * <p>
	 * The method takes in a LdapUserName and queries the Database using a Stateless session.
	 * The query is again Ark_User_Role table/ArkUserRole entity. The getArkRoleByName is invoked
	 * to get a object that represents Administrator. The method uses this to then check if the given user
	 * is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise
	 * false is returned.</p><br>
	 * 
	 * @return boolean 
	 * @see au.org.theark.core.dao.IStudyDao#isAdministator(java.lang.String)
	 */
	public boolean isSuperAdministrator(String ldapUserName) throws EntityNotFoundException{
		
		boolean isSuperAdministrator = false;
		
		StatelessSession session = getStatelessSession();
		//Check or get user ark_user object based on ldapUserName
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole  = getArkRoleByName(Constants.ARK_ROLE_SUPER_ADMINISTATOR);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if(arkUserRole != null){
			isSuperAdministrator = true;
		}
		session.close();//Close the session here since it is stateless.  
		return isSuperAdministrator;
	}

}
