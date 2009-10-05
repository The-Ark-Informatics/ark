/**
 * Copyright (c) 2003 Columbia University in the City of New York.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package edu.columbia.ais.portal.cms.contentmanager;

import edu.columbia.ais.filesystem.File;
import edu.columbia.ais.filesystem.FileSystemMountArgument;
import edu.columbia.ais.filesystem.FileSystemException;
import edu.columbia.ais.portal.cms.*;
import org.jasig.portal.*;
import org.jasig.portal.groups.IEntityGroup;
import org.jasig.portal.groups.IGroupMember;
import org.jasig.portal.services.AuthorizationService;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.jasig.portal.security.IUpdatingPermissionManager;
import org.jasig.portal.security.IPermission;
import org.jasig.portal.services.GroupService;
import org.jasig.portal.services.LogService;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.utils.XSLT;
import org.xml.sax.ContentHandler;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import java.io.*;
import java.util.*;

import neuragenix.dao.DatabaseSchema;

/*
 This import is done since AuthToken is the implementation to check 
 the permissions of the user in the Neuragenix uPortal
 
 */
// @author rennypv

import neuragenix.security.AuthToken;


//import edu.columbia.ais.portal.cms.tests.TimeKeeper;
/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.7 $
 */

public class CContentManager implements IMultithreadedChannel, IMultithreadedCacheable, IMultithreadedMimeResponse, ICMSConstants{
  protected static final HashMap sessions = new HashMap();
  protected static final HashMap headers = new HashMap();
  protected static final Random random = new Random();

    // Initialize StylesheetSet
  private static final String sslLocation = CContentManager.class.getResource("CContentManager.ssl").toString();
  private static final String spacerImg = "media/org/jasig/portal/channels/CUserPreferences/tab-column/transparent.gif";
  private static final String skinLoc = "media/edu/columbia/ais/portal/cu.css";
  private IPerson ip;

  public CContentManager() {
    headers.put("Cache-Control","no-cache");
    headers.put("Pragma","no-cache");
    headers.put("Expires","0");
  }

  protected static synchronized CMSessionData getSessionData (String uid) {
        if (sessions.get(uid) == null) {
            CMSessionData csd = new CMSessionData();
            //csd.uid = uid;
            sessions.put(uid, csd);
        }
        return  (CMSessionData)sessions.get(uid);
    }

  public void setStaticData(ChannelStaticData sD, String uid) throws org.jasig.portal.PortalException {
    try {
//        System.err.println("-------------uid-----------" + uid);
      CMSessionData session = getSessionData(uid);
      session.staticData = sD;
      session.reset = true;

/*      
      IEntityGroup admin = GroupService.getDistinguishedGroup(GroupService.PORTAL_ADMINISTRATORS);
      IGroupMember me = AuthorizationService.instance().getGroupMember(sD.getAuthorizationPrincipal());
        if (admin.deepContains(me)) {
 
  */    
/*
    @author rennypv
    This bit of code is required to consider the admin rights of the neuragenix portal. This indicates that a person who has "cucms_edit"
 *rights has the admin rights and has the rights to create and edit all projects and properties
  */      
     ip = sD.getPerson();
     AuthToken atkn = (AuthToken)(ip.getAttribute("AuthToken"));
      if(ip.getAttribute("permissioncheck") == null)
        {
            ip.setAttribute("permissioncheck",new Hashtable());
        }
      
      if(atkn.hasActivity("cucms_edit"))
      {
//           System.err.println("in setStaticdata() with Authorization");
//           System.err.println("The person is :" + ((IPerson)sD.getPerson()).getFullName());*/
          session.isAdminUser = true;

      }
     
         InputStream file = CContentManager.class.getResourceAsStream("ContentManagerDBSchema.xml");
         DatabaseSchema.loadDomains(file, "ContentManagerDBSchema.xml");

/* End of validation*/     
//         System.err.println("in setStaticdata() outside isAdmin setting");
      CMModel.getStartingModel(session);
      CMModel.getProjectListings(session);
      session.locale = Locale.getDefault();
      session.localization = ResourceBundle.getBundle("edu.columbia.ais.portal.cms.locale.ContentManager",session.locale);
      session.userLockKey = sD.getPerson().getEntityIdentifier().getKey();


    } catch (Exception e) {
      throw new PortalException(e);
    }
  }
  public void setRuntimeData(ChannelRuntimeData rD, String uid) throws org.jasig.portal.PortalException 
  {
    //System.err.println("in RuntimeData" + uid);
     
    CMSessionData session = getSessionData(uid);
 //   System.err.println("in RuntimeData session attributes " +  session.isAdminUser);    
    synchronized(session){
        session.runtimeData = rD;
//        System.err.println("in RuntimeData CMServantFinish " + rD.getParameter("CMServantFinish"));
        String servantFinish = rD.getParameter("CMServantFinish");
        boolean servantError = false;
//        System.err.println("in RuntimeData cmsdownloadPath " + rD.getParameter("cms_downloadPath"));
        if(hasValue(rD.getParameter("cms_downloadPath")))
        {
            int count=0;
            try{
                while(session.nextDownloadPath!=null && count<5)
                {
                        Thread.sleep(200);
                        count++;
                }
            }
            catch(Exception e){}
//            System.err.println("in RuntimeData cms_downloadPath " + rD.getParameter("cms_downloadPath"));
            session.nextDownloadPath=rD.getParameter("cms_downloadPath");
        }
        if (hasValue(rD.getParameter("CMScreen"))){
           session.screen=rD.getParameter("CMScreen");
           if (hasValue(rD.getParameter("CMScreenParm")) && !hasValue(rD.getParameter("CMScreenParm"),"false")){
             session.screenParm = rD.getParameter("CMScreenParm");
            }
            else {
              session.screenParm = null;
            }
        }

        if (hasValue(rD.getParameter("CMSortOrder"))){
           session.sortOrder=rD.getParameter("CMSortOrder");
        }

        if (hasValue(rD.getParameter("CMCommand"))){
          try {
               //long time1 = System.currentTimeMillis();
/*              if(rD.getParameter("CMCommand").equals("assignPermissions"))
              {
                System.err.println("------------in assign permissions--------");
                System.err.println("---project--" + session.project.getTitle() + " " + session.project.getKey());
                
              }*/
            CommandFactory.getCommand(rD.getParameter("CMCommand")).execute(session);
               //long time2 = System.currentTimeMillis();
              //System.out.println("Executing CMCommand "+rD.getValue("CMCommand")+" took "+(time2-time1));
          }
          catch (Exception e){
            LogService.instance().log(LogService.ERROR,e);
            String[] args = new String[] {rD.getParameter("CMCommand"),e.getMessage(),e.getClass().getName()};
            session.feedback = L10n.getLocalizedString(session,"COMMAND_ERROR",args);
            /*
            if (session.file !=null){
              try{
                session.repository.unlock(session.file.getPath(),session.userLockKey);
                CMModel.expandRepositoryNodeRecursive(session.repository.getParentDirectory(session.file.getPath()),session);
              }
              catch(Exception ew){}
              session.file = null;
            }
            */
          }
        }
        if(hasValue(rD.getParameter(PublicationService.REQUEST))){
          // bypass servant processing as this request is intended to produce
          // a publication status report
        }
        else{

            if (session.editorChannel!=null){
              File file = null;
              try{
                session.editorChannel.setRuntimeData(rD);
                file = (File) session.editorChannel.getResults()[0];
                file.getPath();
                session.file = file;
              }
              catch(Exception e){
                session.feedback = session.localization.getString("ERROR")+" "+e.getMessage();
                servantError=true;
                LogService.instance().log(LogService.ERROR,e);
              }
                if(file!=null){
                    try{
                        session.repository.storeTempFile(file.getPath(),file.getInputStream(),file.getMetaData(),session.userLockKey);
                    }
                    catch(FileSystemException e){
                        session.feedback = session.localization.getString("ERROR")+" "+e.getMessage();
                    }
                }
            }
            else if (session.permissionsManager != null){
              session.permissionsManager.setRuntimeData(rD);
              if (session.permissionsManager.isFinished()){
                session.permissionsManager = null;
              }
            }

            if ((servantFinish != null) || servantError){
                // throw out servant - this should trigger file handling GUI
                if (session.editorChannel!=null){
                  session.editorChannel.receiveEvent(new PortalEvent(PortalEvent.SESSION_DONE));
                  session.editorChannel = null;
                }
                if (servantFinish != null){
                  session.editorFactories=null;
                }
                //System.gc();
            }
             // setup transformation parameters now, as runtimedata may be inconsistent
            // if user is acting during a publication request
            session.transformerParms = new HashMap();
            session.transformerParms.put("baseActionURL", rD.getBaseActionURL());
            session.transformerParms.put("baseDownloadURL", rD.getBaseWorkerURL("download",true));
            session.transformerParms.put("random",String.valueOf(random.nextLong()));
            if (hasValue(session.feedback)){
              session.transformerParms.put("feedback",session.feedback);
              session.feedback = null;
            }
        }

        // for debugging
        try{
         // LogService.instance().log(LogService.INFO,new String(DocumentFactory.getByteArrayFromDocument(session.model)));
        }
        catch(Exception e){LogService.instance().log(LogService.INFO,e);}
    }
  }
  public void receiveEvent (PortalEvent ev, String uid) {
      if (ev.getEventNumber() == ev.SESSION_DONE) {
          CMSessionData session = getSessionData(uid);
          sessions.remove(uid);
          if (session.project !=null){
            if (session.file != null){
              try{
                 session.project.getRepositoryManager().unlock(session.file.getPath(),session.userLockKey);
              } catch (Exception e){}
            }
            session.project.close();
          }
      }
  }
  public ChannelRuntimeProperties getRuntimeProperties(String parm1) {
     return  new ChannelRuntimeProperties();
  }

    protected void setCommonParms(Transformer trans, CMSessionData session) throws PortalException {
        Iterator keys = session.transformerParms.keySet().iterator();
        while(keys.hasNext()){
            String key = (String)keys.next();
            trans.setParameter(key, session.transformerParms.get(key));
        }

        /*trans.setParameter("baseActionURL", session.runtimeData.getBaseActionURL());
        trans.setParameter("baseDownloadURL", session.runtimeData.getBaseWorkerURL("download",true));
        trans.setParameter("random",String.valueOf(random.nextLong()));
        if (hasValue(session.feedback)){
          trans.setParameter("feedback",session.feedback);
          session.feedback = null;
        }
        */
    }

  public void renderXML(ContentHandler out, String uid) throws org.jasig.portal.PortalException {
    try {
        CMSessionData session = getSessionData(uid);
        if (session.permissionsManager !=null){
          session.permissionsManager.renderXML(out);
        }
        else{


            // start servant code
//            System.err.println("This is in the renderXML()");
            if ((session.editorFactories != null) &&(session.file !=null)) 
            {
//                System.err.println("This is in the renderXML() in editorfactories!=null");
                String xslURI  = this.getClass().getResource(XSLT.getStylesheetURI(sslLocation,"Editor",session.runtimeData.getBrowserInfo())).toString();
                Transformer trans = DocumentFactory.getTransformer(xslURI,session.localization,null);
                setCommonParms(trans,session);
                trans.setParameter("filename",session.file.getName());
                trans.setParameter("filepath",session.file.getPath());
                trans.setParameter("activeEditor",String.valueOf(session.activeEditor));
                if(session.previewLink){
                  trans.setParameter("previewlink","true");
                }

                if(session.previewReferencesLink){
                  trans.setParameter("previewReferencesLink","true");
                }
                try {
                     trans.transform(new DOMSource(session.model),new SAXResult(out));
                } catch (Exception e) {
                    LogService.instance().log(LogService.ERROR, e);
                }
                if (session.editorChannel != null) {
                  session.editorChannel.renderXML(out);
                }
                LogService.instance().log(LogService.DEBUG, "ContentManager.renderXML(): Defering to servant render");
            }
            else {
//                System.err.println("This is in the renderXML() in editorfactories=null");
                String xslURI  = this.getClass().getResource(XSLT.getStylesheetURI(sslLocation,"Main",session.runtimeData.getBrowserInfo())).toString();
//                System.err.println("This is in the renderXML() in editorfactories =null and xslURI is" + xslURI);
                Transformer trans = DocumentFactory.getTransformer(xslURI,session.localization,null);
                setCommonParms(trans,session);
                LogService.instance().log(LogService.DEBUG, "ContentManager.renderXML(): rendering announcements");

                if (hasValue(session.highlightPath)){
                  trans.setParameter("highlightPath",session.highlightPath);
                }
                if (hasValue(session.screen)){
                   trans.setParameter("screen",session.screen);
                }
                if (hasValue(session.screenParm)){
                   trans.setParameter("screenParm",session.screenParm);
                }
                if (hasValue(session.sortOrder)){
                   trans.setParameter("sortOrder",session.sortOrder);
                }
                /*
                if (session.file != null){
                  trans.setParameter("handleFile","true");
                }
                */
                if(session.project!=null){
                    trans.setParameter("projectTitle",session.project.getTitle());
                }
                trans.setParameter("userKey",session.userLockKey);
                try {
                    trans.transform(new DOMSource(session.model),new SAXResult(out));
                } catch (Exception e) {
                    LogService.instance().log(LogService.ERROR, e);
                }
            }
        }
    } catch (Exception e) {
        LogService.instance().log(LogService.ERROR, e);
    }

  }
  protected boolean hasValue (Object o) {
        boolean rval = false;
        if (o != null && !o.toString().trim().equals("")) {
            rval = true;
        }
        return  rval;
    }

    protected boolean hasValue (Object o, String test) {
        boolean rval = false;
        if (hasValue(o)) {
            if (String.valueOf(o).equals(test)) {
                rval = true;
            }
        }
        return  rval;
    }
  public String getContentType(String uid) {
    CMSessionData session = getSessionData(uid);
    IServant is = session.editorChannel;
    if(hasValue(session.runtimeData.getParameter(PublicationService.REQUEST))){
      return "text/html";
    }
    else if(hasValue(session.nextDownloadPath)){
        try{
            get(session,session.nextDownloadPath);
        }
        catch(Exception e){
            return "text/plain";
        }
          String ct = ContentTypes.getContentType(session.runtimeData.getParameter("cms_downloadPath"));
          return ct;
      }
    else if ((is!=null) && (is instanceof IMimeResponse) ){
      return ((IMimeResponse) is).getContentType();
    }
      else if(session.runtimeData.getParameter("zip")!=null && session.zipfile!=null){
          return "application/zip";
      }

    throw new java.lang.UnsupportedOperationException("Method getContentType() not yet implemented.");
  }
  public InputStream getInputStream(String uid) throws java.io.IOException {
    CMSessionData session = getSessionData(uid);
    IServant is = session.editorChannel;
    if(hasValue(session.runtimeData.getParameter(PublicationService.REQUEST))){
      if(session.promptForArg == null){
        if ("true".equals(session.runtimeData.getParameter("killRequest"))){
            //System.out.println("killing request");

            session.publicationReport.killRequest();
        }
        return producePublicationReport(uid);
      }
      else{
        return producePromptScreen(uid);
      }

    }
    else if(hasValue(session.nextDownloadPath)){
        try{
            return new BufferedInputStream(get(session,session.nextDownloadPath).getInputStream());
        }
        catch(Exception e){
            byte[] bytes = e.getMessage().getBytes();
            return new ByteArrayInputStream(bytes);
        }
        finally{
            session.nextDownloadPath=null;
        }
    }
    else if ((is!=null) && (is instanceof IMimeResponse) ){
      return ((IMimeResponse) is).getInputStream();
    }
    else if(session.runtimeData.getParameter("zip")!=null && session.zipfile!=null){
        InputStream stream= new BufferedInputStream(new FileInputStream(session.zipfile));
        session.zipfile=null;
        return stream;
    }

    throw new java.lang.UnsupportedOperationException("Method getInputStream() not yet implemented.");
  }

  protected static File get(CMSessionData session, String path) throws Exception{
      if(session.hasPermission(READ_PERMISSION,path)){
            return session.repository.getFile(path);
        }
        else{
            throw new CMSException(CMSException.NOT_PERMITTED);
        }
  }

  public void downloadData(OutputStream parm1, String uid) throws java.io.IOException {
    IServant is =getSessionData(uid).editorChannel;
    if ((is!=null) && (is instanceof IMimeResponse) ){
      ((IMimeResponse) is).downloadData(parm1);
    }
    throw new java.lang.UnsupportedOperationException("Method downloadData() not yet implemented.");
  }
  public String getName(String uid) {
    String r=null;
    CMSessionData session = getSessionData(uid);
    IServant is = session.editorChannel;
    if(hasValue(session.runtimeData.getParameter(PublicationService.REQUEST))){
      r= "PublicationRequestReport";
    }
    else if ((is!=null) && (is instanceof IMimeResponse) ){
      r = ((IMimeResponse) is).getName();
    }
    else if(session.runtimeData.getParameter("zip")!=null){
        r= session.runtimeData.getParameter("zip")+".zip";
    }
    return r;
  }
 
  public Map getHeaders(String uid) {
    CMSessionData session = getSessionData(uid);
    IServant is = session.editorChannel;
    if(hasValue(session.runtimeData.getParameter(PublicationService.REQUEST)) || hasValue(session.nextDownloadPath)){
      return headers;
    }
    else if ((is!=null) && (is instanceof IMimeResponse) ){
      return ((IMimeResponse) is).getHeaders();
    }
    else if(session.runtimeData.getParameter("zip")!=null && session.zipfile!=null){
        Map h = new HashMap();
        h.putAll(headers);
        headers.put("Content-Disposition","attachment;filename=\""+session.runtimeData.getParameter("zip")+".zip\"");
        return headers;
      }
   throw new java.lang.UnsupportedOperationException("Method getHeaders() not yet implemented.");
  }

  public InputStream producePromptScreen(String uid){
    CMSessionData session = getSessionData(uid);
    StringBuffer buf = new StringBuffer("<html><head><title>").append(session.localization.getString("INPUT_REQUIRED"));
    buf.append("</title></head><body onLoad=\"this.window.focus();\"><h2>").append(session.localization.getString("SUPPLY_ARGUMENTS")).append(" ");
    buf.append(session.promptForArg.name);
    buf.append("</h2>");
    try{
      buf.append("<form action=\"").append(session.runtimeData.getBaseWorkerURL("download",true)).append("\" method=\"POST\">");
      buf.append("<input type=\"hidden\" name=\"CMCommand\" value=\"setArgs\">");
      for(int i=0 ; i<session.promptForArg.args.length; i++){
        FileSystemMountArgument a = session.promptForArg.args[i];
        if(a.prompt){
          buf.append("<br>").append(a.promptString).append("<input type=\"").append(a.formType).append("\" name=\"FsArg_").append(a.name).append("\" value=\"");
          buf.append(a.value).append("\">");
        }
      }
      buf.append("<input type=submit></form></body></html>");
    }
    catch (Exception e){
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      ps.print("<h1>"+session.feedback+"</h1>");
      ps.print("<br>");
      e.printStackTrace(ps);
      LogService.instance().log(LogService.ERROR,e);
      return new ByteArrayInputStream(os.toByteArray());
    }
    return new ByteArrayInputStream(buf.toString().getBytes());
  }

  public InputStream producePublicationReport(String uid){
    //TimeKeeper.startTask("producePublicationReport");
    CMSessionData session = getSessionData(uid);
    StringBuffer def = new StringBuffer(session.localization.getString("ERROR_SEE_LOG"));
    if (session.feedback!=null){
        def.append(":<br/>").append(session.feedback);
        session.feedback = null;
    }
    String r = def.toString();
    RequestTracker report = session.publicationReport;
    if (report == null){
      if (session.lastReport!=null){
        r = session.lastReport;
      }
    }
    else{
      StringBuffer buf = new StringBuffer("<html><head><title>");
      try{
        buf.append(report.getTitle());
        buf.append("</title>");
        boolean finished = report.isFinished();
        buf.append("<link href=\"").append(skinLoc).append("\" rel=\"stylesheet\" type=\"text/css\">");
        if(!finished){
          try {
            buf.append("<META HTTP-EQUIV=Refresh CONTENT=\"2; URL=") ;
            buf.append(session.runtimeData.getBaseWorkerURL("download",true));
            buf.append("?");
            buf.append(PublicationService.REQUEST);
            buf.append("=true&random=").append(String.valueOf(random.nextLong())).append("\">");
          }
          catch (Exception e){
             LogService.instance().log(LogService.ERROR,e);
          }
          buf.append("</head><body>");
        }
        else {
          session.publicationReport=null;
          if(((report.getExceptions().length==0)
            && (report.getOutputFiles().length==1)
            && (report.getOutputURLs().length==1)
            && (report.getRequestType() == PREVIEW)
            && (!(report.getBaseUrl()+SEPARATOR).equals(report.getOutputURLs()[0]))
            && (!report.getBaseUrl().equals(report.getOutputURLs()[0])))
           || (session.zipfile!=null)
            ){
            try {
              // cut to the chase
              buf.append("<META HTTP-EQUIV=Refresh CONTENT=\"0; URL=") ;
              buf.append(report.getOutputURLs()[0]).append("\">");

            }
            catch (Exception e){
               LogService.instance().log(LogService.ERROR,e);
            }
          }
          buf.append("</head><body onLoad=\"this.window.focus();\">");
        }

        if(session.feedback !=null){
          buf.append("<span class=\"uportal-channel-warning\">").append(session.feedback).append("</span><br/>");
        }
        //if(!finished){
          String remaining = "--";
          if (report.isPrepared()){
            remaining = String.valueOf(report.getEstimatedSecondsRemaining());
          }
          buf.append("<span class=\"uportal-channel-strong\">").append(report.getTitle()).append("</span>");
          buf.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td class=\"uportal-background-highlight\" colspan=5><img src=\"").append(spacerImg).append("\" height=2 width=400></td>");
          if(!report.isFinished()){
              buf.append("<td rowspan=\"3\">");
              if (session.publicationReport.status==session.publicationReport.RUNNING){
                    buf.append("<a title=\"").append(session.localization.getString("STOP_REQUEST")).append("\" href=\"").append(session.runtimeData.getBaseWorkerURL("download",true));

                    buf.append("?");
                    buf.append(PublicationService.REQUEST);
                    buf.append("=true&random=").append(String.valueOf(random.nextLong())).append("&killRequest=true\">");
                    buf.append("<img alt=\"").append(session.localization.getString("STOP_REQUEST")).append("\" ");
                    buf.append("src=\"media/edu/columbia/ais/portal/stop_sign.gif\" hspace=\"2\" height=\"19\" width=\"19\" border=\"0\">");

                    buf.append("</a><br/>");
                }
                  else{
                    buf.append("<img src=\"media/edu/columbia/ais/portal/yield_sign.gif\" hspace=\"2\" height=\"19\" width=\"19\" border=\"0\">");
                }
              buf.append("</td>");
          }
          buf.append("</tr>");
          buf.append("<tr><td class=\"uportal-background-highlight\"><img src=\"").append(spacerImg).append("\" height=15 width=2></td>");
          buf.append("<td class=\"uportal-background-dark\"><img src=\"").append(spacerImg).append("\" width=").append((int)(400*report.getPercentageSuccessful())).append(" height=15></td>");
          buf.append("<td class=\"uportal-background-med\"><img src=\"").append(spacerImg).append("\" width=").append((int)(400*(report.getPercentageComplete()-report.getPercentageSuccessful()))).append(" height=15></td>");
          buf.append("<td class=\"uportal-background-light\"><img src=\"").append(spacerImg).append("\" width=").append((int)(400*(1-report.getPercentageComplete()))).append(" height=15></td>");

          buf.append("<td class=\"uportal-background-highlight\"><img src=\"").append(spacerImg).append("\" height=15 width=2></td></tr>");
          buf.append("<tr><td class=\"uportal-background-highlight\" colspan=5><img src=\"").append(spacerImg).append("\" height=2 width=400></td></tr>");
          buf.append("<tr><td colspan=5><table width=\"400\"><tr><td class=\"uportal-channel-table-row-even\">").append(session.localization.getString("SECONDS_ELAPSED")).append(" ").append(report.getElapsedSeconds()).append("</td>");
          buf.append("<td align=\"right\" class=\"uportal-channel-table-row-even\">").append(session.localization.getString("SECONDS_REMAINING")).append(" ").append(remaining).append("</td></tr></table></td></tr>");
          buf.append("</table>");
          //buf.append("<br> Percent Successful: ").append(String.valueOf(report.getPercentageSuccessful()*100));
          //buf.append("<br> Percent Complete: ").append(String.valueOf(report.getPercentageComplete()*100));
          //buf.append("<br>");
        //}
        if (report.isPrepared()){
          /*
          int complete = report.getCompletedTaskCount();
          int success = report.getSuccessfulTaskCount();
          buf.append("# of tasks: ").append(String.valueOf(report.getTaskCount()));
          buf.append("<br>Completed: ").append(String.valueOf(complete));
          buf.append("<br>Successful: ").append(String.valueOf(success));
          */

          if(finished){
            Exception[] es = report.getExceptions();
            if(es.length > 0){
              buf.append("<p><a href=\"#errors\"><span class=\"uportal-channel-warning\">").append(es.length).append(" ").append(session.localization.getString("ERRORS")).append("</span></a></p>");
            }
            String[] ws = report.getWarnings();
            if(ws.length > 0){
              buf.append("<p><a href=\"#warnings\"><span class=\"uportal-channel-warning\">").append(ws.length).append(" ").append(session.localization.getString("WARNINGS")).append("</span></a></p>");
            }
            String[] urls = report.getOutputURLs();
                if(urls.length>0){
                buf.append("<p><span class=\"uportal-channel-strong\">").append(session.localization.getString("OUTPUT_LOCATION")).append("</span><span class=\"uportal-channel-table-row-even\"><ul>");
                for(int i=0; i<urls.length; i++){
                  buf.append("<li><a href=\"").append(urls[i]).append("\">").append(urls[i]).append("</a>");
                }
                buf.append("</ul></span></p>");
            }
          //if (report.getRequestType() != PREVIEW){
            String baseurl = report.getBaseUrl();
            if((baseurl.lastIndexOf(SEPARATOR) == (baseurl.length()-1)) && (baseurl.length() > 0)){
              baseurl = baseurl.substring(0,baseurl.length()-1);
            }
            String[] changedFiles = report.getOutputFiles();
            buf.append("<p><span class=\"uportal-channel-strong\">").append(String.valueOf(changedFiles.length)).append(" ").append(session.localization.getString("UPDATED_FILES")).append("</span><span class=\"uportal-channel-table-row-even\"><ul>");
            if(baseurl.length()>0){
                for(int cf =0; cf < changedFiles.length; cf++){
                  buf.append("<li><a href=\"").append(baseurl).append(changedFiles[cf]).append("\">").append(changedFiles[cf]).append("</a>");
                }
            }
            else{
                for(int cf =0; cf < changedFiles.length; cf++){
                  buf.append("<li>").append(changedFiles[cf]);
                }
            }
            buf.append("</ul></span></p>");

            String[] deletedPaths = report.getDeletedPaths();
            if(deletedPaths.length > 0){
              buf.append("<p><span class=\"uportal-channel-strong\">").append(String.valueOf(deletedPaths.length)).append(" ").append(session.localization.getString("DELETED_FILES")).append("</span><span class=\"uportal-channel-table-row-even\"><ul>");
              for(int cf =0; cf < deletedPaths.length; cf++){
                buf.append("<li>").append(deletedPaths[cf]);
              }
              buf.append("</ul></span></p>");
            }
          //}

          //es = report.getExceptions();
          if(es.length > 0){
            buf.append("<p><a name=\"errors\"></a><span class=\"uportal-channel-strong\">").append(String.valueOf(es.length)).append(" ").append(session.localization.getString("ERRORS")).append("</span><span class=\"uportal-channel-table-row-even\"> <ul>");
            for(int i=0; i<es.length; i++){
               buf.append("<li>");
               buf.append(es[i].getMessage());
               buf.append("<pre>");
               ByteArrayOutputStream bs = new ByteArrayOutputStream();
               PrintStream ps = new PrintStream(bs);
               es[i].printStackTrace(ps);
               ps.flush();
               ps.close();
               buf.append(bs.toString());
               buf.append("</pre>");
            }
            buf.append("</ul></span></p>");

          }
          if(ws.length > 0){
            buf.append("<p><a name=\"warnings\"></a><span class=\"uportal-channel-strong\">").append(String.valueOf(ws.length)).append(" ").append(session.localization.getString("WARNINGS")).append("</span><span class=\"uportal-channel-table-row-even\"> <ul>");
            for(int i=0; i<ws.length; i++){
               buf.append("<li>");
               buf.append(ws[i]);
            }
            buf.append("</ul></span></p>");

          }
          if(report.getMetrics()!=null){
            buf.append("<p><pre>").append(report.getMetrics().toString(session.locale)).append("</pre></p>");
          }
          session.publicationReport = null;
          }
        }

        buf.append("</body></html>");
      }
      catch (Exception e){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.print("<h1>"+session.feedback+"</h1>");
        ps.print("<h2>"+e.getMessage()+"</h2>");
        ps.print("<br>");
        e.printStackTrace(ps);
        LogService.instance().log(LogService.ERROR,e);
        //e.printStackTrace();
        LogService.instance().log(LogService.ERROR,e);
        return new ByteArrayInputStream(os.toByteArray());
      }
      r = buf.toString();
      session.lastReport = r;
    }
      //TimeKeeper.stopTask("producePublicationReport");
    return new ByteArrayInputStream(r.getBytes());
  }

    public ChannelCacheKey generateKey(String s) {
        CMSessionData session = getSessionData(s);
        if ((session.editorChannel != null) && (session.editorChannel instanceof ICacheable)){
            return ((ICacheable)session.editorChannel).generateKey();
        }
        return null;
    }

    public boolean isCacheValid(Object o, String s) {
        CMSessionData session = getSessionData(s);
        if ((session.editorChannel != null) && (session.editorChannel instanceof ICacheable)){
            return ((ICacheable)session.editorChannel).isCacheValid(o);
        }
        return false;
    }

}
