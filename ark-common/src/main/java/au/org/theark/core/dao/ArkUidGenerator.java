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

    private IdentifierGenerator generator;
    private Properties configuration;
    private String studyNameKy;
    
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
        if (!studyNameKy.equals(this.studyNameKy))
        {
        	this.studyNameKy = studyNameKy;
            configuration.setProperty(TableGenerator.SEGMENT_VALUE_PARAM, studyNameKy);
            generator = IdentifierGeneratorFactory.create("org.hibernate.id.enhanced.TableGenerator",
			                  new IntegerType(),
			                  configuration,
			                  getDialect());
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
        private Integer id;

        @javax.persistence.Id
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
