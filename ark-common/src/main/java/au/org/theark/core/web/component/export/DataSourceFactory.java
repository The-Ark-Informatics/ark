package au.org.theark.core.web.component.export;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceFactory {
	private transient Logger			log			= LoggerFactory.getLogger(DataSourceFactory.class);
	private static String				dbms			= "mysql";
	private static String				serverName	= "ark-database";
	private static String				portNumber	= "3306";
	private static String				userName		= "arkadmin";
	private static String				password		= "v@Tym4np[]";
	private static BasicDataSource	bdSource		= new BasicDataSource();

	public static BasicDataSource createDataSource(String dbName) {
		bdSource.setDriverClassName("com.mysql.jdbc.Driver");
		bdSource.setUrl("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + dbName);
		bdSource.setUsername(userName);
		bdSource.setPassword(password);
		return bdSource;
	}

	public Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			if (connection == null) {
				connection = bdSource.getConnection();
				log.info("Connection Done successfully");
			}
		}
		catch (Exception e) {
			log.error("Error Occured " + e.toString());
		}

		return connection;
	}
}
