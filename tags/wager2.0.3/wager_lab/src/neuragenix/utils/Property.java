package neuragenix.utils;



import java.io.IOException;

import java.util.Properties;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.*;


import org.jasig.portal.services.LogService;


// neuragenix packages
import neuragenix.security.StringEncrypter;


public class Property {



  private static final String PROPERTIES_FILE_NAME = "/properties/neuragenix.properties";

  private static final Properties props = new Properties();



  static {

    loadProps();

  }



  /**

   * Load up the portal properties.  Right now the portal properties is a simple

   * .properties file with name value pairs.  It may evolve to become an XML file

   * later on.

   */

  protected static void loadProps () {

    try {

      props.load(Property.class.getResourceAsStream(PROPERTIES_FILE_NAME));

    } catch (IOException ioe) {

      LogService.log(LogService.ERROR, "Unable to read neuragenix.properties file.");

      LogService.log(LogService.ERROR, ioe);

    }

  }



  /**

   * Returns the value of a property for a given name. A runtime exception is

   * throws in the property cannot be found

   * @param name the name of the requested property

   * @return value the value of the property matching the requested name

   */

  public static String getProperty(String name) {

    String val = props.getProperty(name);

    if (val == null)

      throw new RuntimeException("Property " + name + " not found!");

    return val;

  }



  /**

   * Returns the value of a property for a given name.

   * This method can be used if the property is boolean in

   * nature and you want to make sure that <code>true</code> is

   * returned if the property is set to "true", "yes", "y", or "on"

   * (regardless of case),

   * and <code>false</code> is returned in all other cases.

   * @param name the name of the requested property

   * @return value <code>true</code> if property is set to "true", "yes", "y", or "on" regardless of case, otherwise <code>false</code>

   */

  public static boolean getPropertyAsBoolean(String name) {

    boolean retValue = false;

    String value = getProperty(name);

    if (value != null) {

      if (value.equalsIgnoreCase("true") ||

          value.equalsIgnoreCase("yes") ||

          value.equalsIgnoreCase("y") ||

          value.equalsIgnoreCase("on"))

        retValue = true;

    }

    return retValue;

  }



  /**

   * Returns the value of a property for a given name as a <code>byte</code>

   * @param name the name of the requested property

   * @return value the property's value as a <code>byte</code>

   */

  public static byte getPropertyAsByte(String name) {

    return Byte.parseByte(getProperty(name));

  }



  /**

   * Returns the value of a property for a given name as a <code>short</code>

   * @param name the name of the requested property

   * @return value the property's value as a <code>short</code>

   */

  public static short getPropertyAsShort(String name) {

    return Short.parseShort(getProperty(name));

  }



  /**

   * Returns the value of a property for a given name as an <code>int</code>

   * @param name the name of the requested property

   * @return value the property's value as an <code>int</code>

   */

  public static int getPropertyAsInt(String name) {

    return Integer.parseInt(getProperty(name));

  }



  /**

   * Returns the value of a property for a given name as a <code>long</code>

   * @param name the name of the requested property

   * @return value the property's value as a <code>long</code>

   */

  public static long getPropertyAsLong(String name) {

    return Long.parseLong(getProperty(name));

  }



  /**

   * Returns the value of a property for a given name as a <code>float</code>

   * @param name the name of the requested property

   * @return value the property's value as a <code>float</code>

   */

  public static float getPropertyAsFloat(String name) {

    return Float.parseFloat(getProperty(name));

  }



  /**

   * Returns the value of a property for a given name as a <code>long</code>

   * @param name the name of the requested property

   * @return value the property's value as a <code>long</code>

   */

  public static double getPropertyAsDouble(String name) {

    return Double.parseDouble(getProperty(name));

  }
  
    /** Return decrypted property
     *  @param name the name of the requested property
     *  @return decryption property value
     */
     public static String getDecryptedProperty(String name) {
        String propertyValue = getProperty(name);
        //String decryptedValue = propertyValue;
      
        if (propertyValue.indexOf(StringEncrypter.NGX_ENCRYPTION_PREFIX) >= 0) {
            try {
                StringEncrypter encrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
                propertyValue = encrypter.decrypt(propertyValue);
            }
            catch (Exception e) {
              
            }
        }
        else {
            BufferedReader inFile = null;
            BufferedWriter outFile = null;

            try {
                // encrypt the password
                StringEncrypter encrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
                String encryptedPassword = encrypter.encrypt(propertyValue);

                // write to the configuration file
                URL confURL = Property.class.getClassLoader().getResource(PROPERTIES_FILE_NAME);
                if (confURL == null) {
                    confURL = ClassLoader.getSystemResource(PROPERTIES_FILE_NAME);
                }

                inFile = new BufferedReader(new InputStreamReader(confURL.openStream()));
                String strLine = inFile.readLine();
                String content = "";

                while (strLine != null)
                {
                    content += strLine + "\n";
                    strLine = inFile.readLine();
                }

                String replacedContent = content.replaceAll(name + "=" + propertyValue, name + "=" + encryptedPassword);
                outFile = new BufferedWriter(new FileWriter(new File(confURL.getFile())));
                outFile.write(replacedContent);
            }
            catch (Exception xe) {
                System.out.println(xe.getMessage());
                System.out.println(xe);
                xe.printStackTrace();
                System.exit(0);
            }
            finally {
                try {
                    if (inFile != null) {
                        inFile.close();
                    }
                    if (outFile != null) {
                        outFile.close();
                    }
                }
                catch (Exception e) {

                }
            }
        }
      
        return propertyValue;
  }

}







