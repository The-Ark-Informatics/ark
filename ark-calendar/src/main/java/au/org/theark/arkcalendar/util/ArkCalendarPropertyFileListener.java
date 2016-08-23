package au.org.theark.arkcalendar.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ArkCalendarPropertyFileListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		   final Properties propsFromFile = new Properties();
		      try
		      {
		         propsFromFile.load(getClass().getResourceAsStream("/ark-calendar.properties"));
		      }
		      catch (final IOException e)
		      {
		          e.printStackTrace();
		      }
		      for (String prop : propsFromFile.stringPropertyNames())
		      {
		         if (System.getProperty(prop) == null)
		         {
		             System.setProperty(prop, propsFromFile.getProperty(prop));
		         }
		      }
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
