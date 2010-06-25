package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.study.model.entity.EtaUser;
import au.org.theark.study.model.entity.Person;

/**
 * 
 * @author nivedann
 *
 */
@Repository("userDao")
public class UserDao extends HibernateSessionDao implements IUserDao{

	public void createUser(EtaUser user) {
		
		getSession().save(user);
		
	}

	public EtaUser getUser(Long userId){
		return (EtaUser) getSession().get(EtaUser.class, userId);
	}
	
	
	public EtaUser getUser(String userName) {
		String query = "from EtaUser eu where eu.userName = :userName";
		Session session = getSession();
		EtaUser user = null;
		user = (EtaUser) session.createQuery(query).setString("userName",userName).uniqueResult();	
		return user;
	}
	
	public Person createPerson(Person personEntity){
		
		getSession().save(personEntity);
		return personEntity;
	}
	
	public List<Person> searchPerson(Person personVO) throws PersonNotFoundException{
		//Return all persons
		String queryString = "from Person";
		Query query = getSession().createQuery(queryString);
		List<Person> personList = query.list();
		return personList;
	}

}
