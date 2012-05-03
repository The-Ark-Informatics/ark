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
package au.org.theark.lims.model.dao;

/*** 
 * @author cellis
 */

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.lims.entity.BiospecimenUidSequence;

@Repository("biospecimenUidGenerator")
public class BiospecimenUidGenerator extends HibernateSessionDao {
	private static Logger		log	= LoggerFactory.getLogger(BiospecimenUidGenerator.class);
	
	/**
	 * get Id and increment sequence table
	 * @param studyNameKey
	 * @return
	 */
	public Serializable getId(String studyNameKey) {
		return getUidAndIncrement(studyNameKey, 1);
	}


	/**
	 * TODO this should use a real fkey
	 * @param studyNameKy
	 * @return
	 */
	public Integer getUidAndIncrement(String studyNameKy, int numToInsert) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidSequence.class);
		criteria.add(Restrictions.eq("studyNameId", studyNameKy));
		BiospecimenUidSequence seqData = (BiospecimenUidSequence) criteria.uniqueResult();
		if(seqData==null){
			log.error("sequence does not exist...creating");
			BiospecimenUidSequence seq = new BiospecimenUidSequence();
			seq.setInsertLock(false);
			seq.setStudyNameId(studyNameKy);
			seq.setUidSequence(numToInsert);
			getSession().persist(seq);
			getSession().flush();
			return new Integer(0);
		}
		else{
			log.warn("so we hav a seq");
			int currentSeqNumber = seqData.getUidSequence();
			seqData.setUidSequence((currentSeqNumber + numToInsert));
			getSession().update(seqData);
			getSession().flush();
			return currentSeqNumber;//TODO asap...this should be handled transactionally in one class, and probably with generators...although this isnt really even a key
		}
	}
/*
	private IdentifierGenerator	generator;
	private Properties				configuration;
	private String						studyNameKey;

	// No-Arg constructor
	public BiospecimenUidGenerator() {
		initProperties();
	}

	private void initProperties() {
		configuration = new Properties();
		configuration.setProperty(TableGenerator.TABLE_PARAM, "lims.biospecimenuid_sequence");
		configuration.setProperty(TableGenerator.SEGMENT_COLUMN_PARAM, "STUDY_NAME_ID");
		configuration.setProperty(TableGenerator.VALUE_COLUMN_PARAM, "UID_SEQUENCE");
		configuration.setProperty(TableGenerator.INCREMENT_PARAM, "1");
	}

	public Serializable getId(String studyNameKey) {
		if (!studyNameKey.equals(this.studyNameKey)) {
			this.studyNameKey = studyNameKey;
			configuration.setProperty(TableGenerator.SEGMENT_VALUE_PARAM, studyNameKey);
//			generator = IdentifierGeneratorFactory.create("org.hibernate.id.enhanced.TableGenerator", new IntegerType(), configuration, getDialect());
			DefaultIdentifierGeneratorFactory factory = new DefaultIdentifierGeneratorFactory();			
			factory.createIdentifierGenerator("org.hibernate.id.enhanced.TableGenerator", new IntegerType(), configuration);//TODO analyze where dialect comes in
			factory.setDialect(getDialect());
			generator = factory.createIdentifierGenerator("org.hibernate.id.enhanced.TableGenerator", new IntegerType(), configuration);

		}

		StatelessSession session = getStatelessSession();
		Serializable id = generator.generate((StatelessSessionImpl) session, new Id());
		session.close();
		return id;
	}

	/**
	 * Target object for ID generation
	 * 
	 * TODO:  Evaluate is this working fine?  
	 *
	private static class Id {
		private Integer	id;

		@javax.persistence.Id
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
	}*/
}
