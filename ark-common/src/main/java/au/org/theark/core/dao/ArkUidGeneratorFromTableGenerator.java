package au.org.theark.core.dao;

import java.util.Properties;

import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;

public class ArkUidGeneratorFromTableGenerator extends TableGenerator {
	

	@Override
	protected String determineValueColumnName(Properties params, Dialect dialect) {
		System.out.println("What is params.get( IDENTIFIER_NORMALIZER ) ........ " +  params.get( IDENTIFIER_NORMALIZER ));
		System.out.println("What is VALUE_COLUMN_PARAM   ........ " +  VALUE_COLUMN_PARAM);
		System.out.println("What is  params    (.toString())  ........ " +  params.toString());
		System.out.println("What is DEF_VALUE_COLUMN  ........ " +  DEF_VALUE_COLUMN);
		ObjectNameNormalizer normalizer = ( ObjectNameNormalizer ) params.get( IDENTIFIER_NORMALIZER );
		String name = ConfigurationHelper.getString( VALUE_COLUMN_PARAM, params, DEF_VALUE_COLUMN );

		System.out.println("What isname      (ConfigurationHelper.getString( VALUE_COLUMN_PARAM, params, DEF_VALUE_COLUMN ))    ........ " +  DEF_VALUE_COLUMN);
		System.out.println("What isname      normalizer.normalizeIdentifierQuoting( name )    ........ " +  normalizer.normalizeIdentifierQuoting( name ));
		return dialect.quote( normalizer.normalizeIdentifierQuoting( name ) );
	}

	
}
