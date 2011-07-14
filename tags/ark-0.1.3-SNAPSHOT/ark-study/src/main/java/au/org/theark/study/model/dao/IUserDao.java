package au.org.theark.study.model.dao;

import java.util.List;

import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.model.study.entity.EtaUser;
import au.org.theark.core.model.study.entity.Person;
;
/*
 * Interface that works on the User object with Hibernate
 */
public interface IUserDao {
	
	public EtaUser getUser(String userName);
	
	public EtaUser getUser(Long userId);
	
	public void createUser(EtaUser user);
	
	public Person createPerson(Person person);
	
	public List<Person> searchPerson(Person person) throws PersonNotFoundException;
	
	public boolean isArkUserPresent(String userName);

}
