/*

 * CHelp.java

 *

 * Created on 18 March 2003, 13:50

 */



package neuragenix.genix.help;



import org.jasig.portal.IChannel;

import org.jasig.portal.ChannelStaticData;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.utils.XSLT;

import org.xml.sax.ContentHandler;

import org.jasig.portal.security.*;

import org.jasig.portal.services.LogService;



import java.awt.Dimension;

import javax.swing.JPanel;



/** This channel deals with the data

 *  integratio channel

 *

 */

public class CHelp implements IChannel {



private static String HELP_PAGE = "help";



// At the very least, you'll need to keep track of the static data and the runtime data that the portal sends you. 

private ChannelStaticData staticData;

private ChannelRuntimeData runtimeData;



private String strCurrent; // This is used to know what the current page the user is on

private String strXML = ""; // used to create the xml document with the starting XML header

private String strStylesheet; // Used to specify the stylesheet to use for the portal



  /**

   *  Contructs the Help Channel

   */

  public CHelp() {

    this.strStylesheet = HELP_PAGE; // The default stylesheet for the help

  }

 

 /**

    *  Returns channel runtime properties.

    *  Satisfies implementation of Channel Interface.

    *

    *  @return handle to runtime properties

    */

   public ChannelRuntimeProperties getRuntimeProperties() {

     return new ChannelRuntimeProperties();

   }

    /** 

   *  Process layout-level events coming from the portal.

   *  Satisfies implementation of IChannel Interface.

   *

   *  @param PortalEvent ev a portal layout event

   */

  public void receiveEvent(PortalEvent ev) {   

  }

  /** 

   *  Receive static channel data from the portal.

   *  Satisfies implementation of IChannel Interface.

   *

   *  @param ChannelStaticData sd static channel data

   */

  public void setStaticData(ChannelStaticData sd) {

    this.staticData = sd;

  }

  /**

   *  Receive channel runtime data from the portal.

   *  Satisfies implementation of IChannel Interface.

   *

   *  @param ChannelRuntimeData rd handle to channel runtime data

   */

  public void setRuntimeData(ChannelRuntimeData rd) {



    try  {   

        this.runtimeData = rd;

        strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><help></help>";        

    }

    catch (Exception e)

    {

        e.printStackTrace();

        LogService.instance().log(LogService.ERROR, "Unknown error in Help Channel - " + e.toString());

    }

  }

  

  /** Output channel content to the portal

   *  @param out a sax document handler

   */

  public void renderXML(ContentHandler out) throws PortalException {

    



    // Create a new XSLT styling engine

    XSLT xslt = new XSLT(this);



    // pass the result xml to the styling engine.

    xslt.setXML(strXML);



    // specify the stylesheet selector

    xslt.setXSL("CHelp.ssl", strStylesheet, runtimeData.getBrowserInfo());



    // set parameters that the stylesheet needs.

    xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());

    

    // set the output Handler for the output.

    xslt.setTarget(out);



    // do the deed

    xslt.transform();

  } 

}
