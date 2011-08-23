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
package au.org.theark.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * A class to get a handle on the hibernate session object. This class must be extended by classes that require access to a hibernate session.
 * 
 * @author nivedann
 * 
 */
public abstract class HibernateSessionDao {

	private SessionFactory	sessionFactory;
	private Dialect			dialect;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		dialect = ((SessionFactoryImpl) sessionFactory).getDialect();
	}

	/**
	 * Return a Hibernate Session instance through the sessionFactory
	 * 
	 * @return
	 */
	public Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory, true);
	}

	public Dialect getDialect() {
		if (dialect == null) {
			dialect = ((SessionFactoryImpl) sessionFactory).getDialect();
		}
		return dialect;
	}

	public StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}
}
