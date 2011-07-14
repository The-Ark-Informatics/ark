package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Person;

@SuppressWarnings("unchecked")
@Repository("bioPersonDao")
public class BioPersonDao extends HibernateSessionDao implements IBioPersonDao
{
	public Person getPerson(Long id) throws EntityNotFoundException, ArkSystemException
	{
		Criteria criteria = getSession().createCriteria(Person.class);
		criteria.add(Restrictions.eq("id", id));
		List<Person> listOfPerson = criteria.list();
		if (listOfPerson != null && listOfPerson.size() > 0)
		{
			return listOfPerson.get(0);
		}
		else
		{
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}
	}
}
