/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Person;

/**
 * 
 * @author nivedann
 * 
 */
@Repository("userDao")
public class UserDao extends HibernateSessionDao implements IUserDao {


	public Person createPerson(Person personEntity) {

		getSession().save(personEntity);
		return personEntity;
	}

	public List<Person> searchPerson(Person personVO) throws PersonNotFoundException {
		// Return all persons
		String queryString = "from Person";
		Query query = getSession().createQuery(queryString);
		List<Person> personList = query.list();
		return personList;
	}

	/**
	 * Checks if the Ark User is present in the system. IT does no compare with a particular Study. If a person existed in the system this will return
	 * true. This must be used only during create operation. If you want to add a ArkUser to another study then another method with study must be
	 * passed in.
	 */
	public boolean isArkUserPresent(String userName) {
		boolean isPresent = false;
		Criteria criteria = getSession().createCriteria(ArkUser.class);
		if (userName != null) {
			criteria.add(Restrictions.eq("ldapUserName", userName));
		}

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.list().get(0);
		if (count > 0) {
			isPresent = true;
		}

		return isPresent;
	}

}
