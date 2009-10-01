package neuragenix.utils;


import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;
import java.io.FileInputStream;

public class PropertyDetection{

		private static final Properties mfProps = new Properties();
		private static final Properties props = new Properties();

		public static void main(String args[]){
			try {
				mfProps.load( new FileInputStream( args[0] ));
				props.load( new FileInputStream( args[1] ));
			} catch (IOException ioe) {
				System.err.println("Cannot find properties files: " + args[0] + " - " + args[1]);
			}

			for (Enumeration e = mfProps.propertyNames() ; e.hasMoreElements() ;) {
					String strProp = (String) e.nextElement();

					if( props.getProperty( strProp ) == null ){
							System.err.println( "[WARNING] :: Property '" + strProp + "' not found. Suggested value: '" + mfProps.getProperty( strProp ) + "'" );
					}
		  	}
		}

}

