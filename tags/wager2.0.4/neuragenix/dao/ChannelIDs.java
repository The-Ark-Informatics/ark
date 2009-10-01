/*

 * ChannelIDs.java

 *

 * Created on January 13, 2003, 9:31 AM

 */



package neuragenix.dao;





import java.util.Hashtable;

import org.jasig.portal.IChannel;

/**

 *

 * @author  Administrator

 */

public class ChannelIDs {

    

    private static final String intSurveyChannelID = "59";

    private static final String intBiospecimenChannelID = "61";

    private static final String intPatientChannelID = "46";

    private static final String intDocumentChannelID = "56";

    private static final String intInventoryViewChannelID = "112";

    private static final String intInventoryChannelID = "110";

    

    

    private static final String intNominationChannelID = "106";

    private static final String intWelcomeChannelID = "102";



    private static Hashtable hashChannel = new Hashtable();



    

    /** Creates a new instance of ChannelIDs */

    public ChannelIDs() {

    }

    

    



    public static void putChannel(String cName, IChannel channel)

    {

        hashChannel.put(cName, channel);

    }

    

    public static IChannel getChannel(String cName)

    {

        return (IChannel) hashChannel.get(cName);

    }

    

    public static String getSurveyChannelID(){

        return intSurveyChannelID;

    }

    

    public static String getBiospecimenChannelID(){

        return intBiospecimenChannelID;

    }

    

    public static String getPatientChannelID(){

        return intPatientChannelID;

    }

    public static String getDocumentChannelID(){

        return intDocumentChannelID;

    }

    public static String getInventoryViewChannelID(){

        return intInventoryViewChannelID; 

    }

    public static String getInventoryChannelID(){

        return intInventoryChannelID;

    }

        

    

    

    public static String getNominationChannelID(){

        return intNominationChannelID;

    }

    

    public static String getWelcomeChannelID(){

        return intWelcomeChannelID;

    }



    

    

}

