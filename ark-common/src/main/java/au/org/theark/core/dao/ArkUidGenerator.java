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

/*** 
 * @author elam
 * Based on: http://www.pointyspoon.com/categories/java/hibernate/
 */

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.StatelessSession;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentifierGeneratorFactory;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.impl.StatelessSessionImpl;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

@Repository("arkUidGenerator")
public class ArkUidGenerator extends HibernateSessionDao {

	private IdentifierGenerator	generator;
	private Properties				configuration;
	private String						studyNameKy;

	// No-Arg constructor
	public ArkUidGenerator() {
		initProperties();
	}

	private void initProperties() {
		configuration = new Properties();
		configuration.setProperty(TableGenerator.TABLE_PARAM, "study.subjectuid_sequence");
		configuration.setProperty(TableGenerator.SEGMENT_COLUMN_PARAM, "STUDY_NAME_ID");
		configuration.setProperty(TableGenerator.VALUE_COLUMN_PARAM, "UID_SEQUENCE");
		configuration.setProperty(TableGenerator.INCREMENT_PARAM, "1");
	}

	public Serializable getId(String studyNameKy) {
		if (!studyNameKy.equals(this.studyNameKy)) {
			this.studyNameKy = studyNameKy;
			configuration.setProperty(TableGenerator.SEGMENT_VALUE_PARAM, studyNameKy);
			generator = IdentifierGeneratorFactory.create("org.hibernate.id.enhanced.TableGenerator", new IntegerType(), configuration, getDialect());
		}

		StatelessSession session = getStatelessSession();
		Serializable id = generator.generate((StatelessSessionImpl) session, new Id());
		session.close();
		return id;
	}

	/**
	 * Target object for ID generation
	 */
	private static class Id {
		private Integer	id;

		@SuppressWarnings("unused")
		@javax.persistence.Id
		public Integer getId() {
			return id;
		}

		@SuppressWarnings("unused")
		public void setId(Integer id) {
			this.id = id;
		}
	}
}
