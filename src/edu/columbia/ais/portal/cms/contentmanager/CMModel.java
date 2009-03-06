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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_10;
import edu.columbia.ais.filesystem.File;
import edu.columbia.ais.filesystem.FileSystemException;
import edu.columbia.ais.filesystem.vocabulary.CUFS;
import edu.columbia.ais.portal.cms.*;
import org.apache.lucene.search.Hits;
import org.jasig.portal.AuthorizationException;
import org.jasig.portal.services.LogService;
import org.w3c.dom.*;

import java.text.DateFormat;
import java.util.Stack;

/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.1 $
 */

public class CMModel implements ICMSConstants{
  protected static final DateFormat dateformat;

  static{
    dateformat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.FULL);
    dateformat.setLenient(true);
  }

// --Recycle Bin START (8/20/03 2:52 PM):
//  public CMModel() {
//  }
// --Recycle Bin STOP (8/20/03 2:52 PM)
  public static void updateSessionPreviewBooleans(CMSessionData session) throws FileSystemException {
        if((session.project.getPreviewFileSystem() != null) && (session.project.getOutputPaths(session.file.getPath()).length>0)){
          session.previewLink=true;
        }
        else{
          session.previewLink=false;
        }

        if((session.project.getPreviewFileSystem() != null) && (ContentTypes.areEquivalent("application/xml+xslt",ContentTypes.getContentType(session.file.getPath()))) && (session.project.getDependentFilePaths(session.file.getPath()).length>0)){
          session.previewReferencesLink=true;
        }
         else{
          session.previewReferencesLink=false;
        }
  }
  public static void getProjectListings(CMSessionData session) throws AuthorizationException{
    NodeList nl = session.model.getElementsByTagName("project-listing");
    Node root = session.model.getDocumentElement();
    for (int i = (nl.getLength()-1); i>=0; i--){
      root.removeChild(nl.item(i));
    }
    String[] keys = ProjectFactory.getProjectKeys();
    for(int j=0; j<keys.length; j++){
      Project pr = null;
      try{
        pr = ProjectFactory.getProject(keys[j]);
      }
      catch(Exception e){
        LogService.instance().log(LogService.ERROR,"contentmanager.CMModel.getProjectListings(): Project could not be loaded from "+keys[j]);
        LogService.instance().log(LogService.ERROR,e);
      }
      if ((pr!=null) && session.hasPermission(pr.getPermissionsOwnerToken(),READ_PERMISSION,SEPARATOR)){
        Element p = session.model.createElement("project-listing");
        p.setAttribute("key",keys[j]);
        p.setAttribute("title",String.valueOf(pr.getTitle()));
        p.setAttribute("description",String.valueOf(pr.getDescription()));
        root.insertBefore(p,root.getFirstChild());
      }
    }
  }

  public static void getStartingModel(CMSessionData session){
    try{
    session.model = DocumentFactory.newDocument(null);
    Element root = session.model.createElement("content-manager");
    session.model.appendChild(root);
    Node ct = ContentTypes.getTypesDefinition().getDocumentElement();
    Node ct2 = session.model.importNode(ct,true);
    root.appendChild(ct2);
    Element rnodes = session.model.createElement("repository-nodes");
    root.appendChild(rnodes);
    }
    catch(Exception e){
      LogService.instance().log(LogService.ERROR,e);
    }
  }

  public static void toggleRepositoryNode(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
    Element e =  getRepositoryNode(path,session);
    if("true".equals(e.getAttribute("expanded"))){
      collapseRepositoryNode(path,session);
    }
    else{
      expandRepositoryNode(path,session);
    }
  }

  public static void toggleFileHistory(String path, CMSessionData session){
    Element node =  getRepositoryNode(path,session);
    if (node.getAttribute("showHistory").equals("false")){
      node.setAttribute("showHistory","true");
    }
    else{
      node.setAttribute("showHistory","false");
    }
  }

  public static void listEditors(CMSessionData session){
    clearEditors(session);
    if (session.editorFactories != null){
      for(int i=0; i<session.editorFactories.length;i++){
          try{
              Element e = session.model.createElement("active-editor");
              e.setAttribute("index",String.valueOf(i));
              e.setAttribute("name",session.editorFactories[i].getServantLabel(session.locale));
              session.model.getDocumentElement().appendChild(e);
          }
          catch(CMSException e){
              LogService.log(LogService.ERROR,e);
          }
      }
    }
  }

  public static void clearEditors(CMSessionData session){
     NodeList nl = session.model.getDocumentElement().getElementsByTagName("active-editor");
     for (int i=nl.getLength()-1;i>=0;i--){
        session.model.getDocumentElement().removeChild(nl.item(i));
     }
  }

  public static Element getRepositoryNode(String path, CMSessionData session){
    Element n = null;
    Element fs = getRepositoryNodeRoot(session);
    NodeList nl = fs.getElementsByTagName("node");
    for (int i=0; i< nl.getLength(); i++){
      Element tn = (Element) nl.item(i);
      LogService.instance().log(LogService.DEBUG,"comparing "+tn.getAttribute("path")+" to "+path);
      if (tn.getAttribute("path").equals(path)){
        n = tn;
        break;
      }
    }
    return n;
  }

  protected static void setNodePermissions(Element node, CMSessionData session) throws FileSystemException, AuthorizationException{
    //TimeKeeper.startTask("setNodePermissions");
    node.setAttribute("isRequired",String.valueOf(session.project.isRequired(node.getAttribute("path"))));
    String path = node.getAttribute("path");
    node.setAttribute("canRead",String.valueOf(session.hasPermission(READ_PERMISSION,path)));
    node.setAttribute("canWrite",String.valueOf(session.hasPermission(WRITE_PERMISSION,path)));
    node.setAttribute("canDelete",String.valueOf(session.hasPermission(DELETE_PERMISSION,path)));
    node.setAttribute("canCreate",String.valueOf(session.hasPermission(CREATE_PERMISSION,path)));
    node.setAttribute("canAssign",String.valueOf(session.hasPermission(GRANT_PERMISSION,path)));
    node.setAttribute("canBuild",String.valueOf(session.hasPermission(BUILD_PERMISSION,path)));
    node.setAttribute("canPublish",String.valueOf(session.hasPermission(PUBLISH_PERMISSION,path)));
    //TimeKeeper.stopTask("setNodePermissions");
  }

  public static void expandRepositoryNodeRecursive(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
    Stack stack = new Stack();
    stack.push(path);
    Element n = getRepositoryNode(path,session);
    while (n == null){
      path = session.repository.getParentDirectory(path);
      n = getRepositoryNode(path,session);
      stack.push(path);
    }
    while (stack.size() > 0){
      String p = (String) stack.pop();
      expandRepositoryNode(p,session);
    }
  }

  public static void highlightNode(String path, CMSessionData session) throws FileSystemException, CMSException, AuthorizationException{
    Element n = getRepositoryNode(path,session);
    if (n!=null){
      n.setAttribute("lastModified",session.repository.getLastModified(path).toString());
      boolean hasOutput = session.project.hasOutput(path);
      n.setAttribute("hasOutput",String.valueOf(hasOutput));
      NodeList cl = n.getChildNodes();
        boolean addWarnings=true;
      for(int i=0; i<cl.getLength();i++){
          Node c = cl.item(i);
          if(c.getNodeName()!=null && c.getNodeName().equals("warnings")){
              addWarnings=false;
              break;
          }
      }
        if(addWarnings){
          Element warnings = session.model.createElement("warnings");
            n.appendChild(warnings);
          Element compact = session.model.createElement("compact");
          compact.appendChild(session.model.createTextNode(L10n.getLocalizedString(session,"CONFIRM_COMPACT",path)));
          warnings.appendChild(compact);
          Element delete = session.model.createElement("delete");
          delete.appendChild(session.model.createTextNode(L10n.getLocalizedString(session,"CONFIRM_DELETE",path)));
          warnings.appendChild(delete);
          Element deleteEdition = session.model.createElement("deleteEdition");
          deleteEdition.appendChild(session.model.createTextNode(L10n.getLocalizedString(session,"CONFIRM_DELETE_EDITION",path)));
          warnings.appendChild(deleteEdition);
        }
      if(session.repository.isFile(path)){
        NodeList nl = n.getElementsByTagName("file-def");
        for(int i=(nl.getLength()-1);i>=0;i--){
          n.removeChild(nl.item(i));
        }
        //long time1 = System.currentTimeMillis();
        Element def = generateFileDef(path,session);
        n.appendChild(def);
        //  long time2 = System.currentTimeMillis();
        IEditorServantFactory[] factories = session.project.getServantFactories(path);
        if (factories.length>0){
          n.setAttribute("hasEditorFactory","true");
        }
        //  long time3 = System.currentTimeMillis();
         getReferences(path,session);
        //  long time4 = System.currentTimeMillis();
         addNodeNewFileOptions(session.repository.getParentDirectory(path),session);
          addNodeMoveOptions(path,session);
        //  long time5 = System.currentTimeMillis();
         // System.out.println("Highligh node times: "+(time2-time1)+", "
         //   +(time3-time2)+", "+(time4-time3)+", "+(time5-time4));
      }
      else{
        if(hasOutput){
            n.setAttribute("build-url",joinUrlToPath(session.project.getBaseBuildURL(),path));
            n.setAttribute("publish-url",joinUrlToPath(session.project.getBasePublishURL(),path));
        }
        addNodeNewFileOptions(path,session);
          addNodeMoveOptions(path,session);
      }
    }

  }

  public static void refreshFile(String path, CMSessionData session) throws FileSystemException,CMSException,AuthorizationException{
    String dirpath = session.repository.getParentDirectory(path);
    Node n = getRepositoryNode(path,session);
    boolean history=false;
    if(n!=null){
      if ("true".equals(((Element)n).getAttribute("showHistory"))){
        history=true;
      }
    }
      CMModel.collapseRepositoryNode(dirpath,session);
      CMModel.expandRepositoryNodeRecursive(dirpath,session);
      highlightNode(path,session);
      if(history){
        toggleFileHistory(path,session);
      }
  }

  public static void expandRepositoryNode(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
    //TimeKeeper.startTask("expandRepositoryNode");
    Element n = getRepositoryNode(path,session);
    if (n!=null){
      if (session.repository.isDirectory(path)){
        collapseRepositoryNode(path,session);
        //TimeKeeper.startTask("listDirs");
        String[] dirs = session.repository.listDirectories(path);
        //TimeKeeper.stopTask("listDirs");

        for(int i=0; i<dirs.length; i++){
          //TimeKeeper.startTask("genDirElement");
          Element f = session.model.createElement("node");
          f.setAttribute("path",dirs[i]);
          /*int x = dirs[i].lastIndexOf(SEPARATOR)+1;
          if (x == dirs[i].length()){
             x = (dirs[i].substring(0,dirs[i].lastIndexOf(SEPARATOR))).lastIndexOf(SEPARATOR)+1;
          }
          String name = dirs[i].substring(x);
          */
          f.setAttribute("name",session.project.getName(dirs[i]));
          f.setAttribute("label",session.project.getLabel(dirs[i]));

          f.setAttribute("expanded","false");
          //f.setAttribute("hasOutput","true");
          f.setAttribute("isFile","false");
          //f.setAttribute("lastModified",session.repository.getLastModified(dirs[i]).toRFC2426String());
          setNodePermissions(f,session);
         // LogService.instance().log(LogService.DEBUG,"Set directory path to "+f.getAttribute("path")+" and name to "+f.getAttribute("name")+" with x of "+x);
          n.appendChild(f);
          //TimeKeeper.stopTask("genDirElement");
        }

        //TimeKeeper.startTask("listFiles");
        String[] files = session.repository.listFiles(path);
        //TimeKeeper.stopTask("listFiles");
        for(int i=0; i<files.length; i++){
          //TimeKeeper.startTask("genFileElement");
          Element f = session.model.createElement("node");
          f.setAttribute("path",files[i]);
          String name = files[i].substring(files[i].lastIndexOf(SEPARATOR)+1);
          f.setAttribute("name",name);
            f.setAttribute("label",name);

          f.setAttribute("isFile","true");
          //TimeKeeper.startTask("getlastMod");
          //String lastmod = session.repository.getLastModified(files[i]).toRFC2426String();
          //TimeKeeper.stopTask("getlastMod");
          //f.setAttribute("lastModified",lastmod);
          setNodePermissions(f,session);
          //f.appendChild(generateFileDef(files[i],session));
          f.setAttribute("showHistory","false");
          n.appendChild(f);
          //TimeKeeper.stopTask("genFileElement");
        }

      }

      n.setAttribute("expanded","true");
      //n.setAttribute("hasOutput","true");
      n.setAttribute("lastModified",session.repository.getLastModified(path).toString());
      setNodePermissions(n,session);
      }
      //TimeKeeper.stopTask("expandRepositoryNode");
      //TimeKeeper.printTimes();
      //TimeKeeper.reset();
  }

  protected static String joinUrlToPath(String url, String path){
    String r = url;
    if (r.endsWith(SEPARATOR)){
      r = r.substring(0,r.length()-1) ;
    }
    return r+path;
  }

  protected static void populateHit(Element hit, FileHolder holder, CMSessionData session, float score) throws FileSystemException, AuthorizationException{
    hit.setAttribute("path",holder.getFile().getPath());
      if (session.repository.exists(holder.getFile().getPath())){
           hit.setAttribute("exists","true");
      }
      else{
           hit.setAttribute("exists","false");
      }
    hit.setAttribute("title",holder.getFile().getName());
    hit.setAttribute("score",String.valueOf(score));
    hit.setAttribute("extension",holder.getFile().getExtension());
    hit.setAttribute("edition",String.valueOf(holder.getFile().getEdition()));
    hit.setAttribute("typeID",session.project.getTypeElementID(holder.getFile().getPath()));
    hit.setAttribute("current","true");
    if(session.hasPermission(WRITE_PERMISSION,holder.getFile().getPath())){
         hit.setAttribute("canWrite","true");
    }
    else{
        hit.setAttribute("canWrite","false");
    }
    Resource r = holder.getMetaData();
    if(r.hasProperty(DC_10.title)){
       hit.setAttribute("title",r.getProperty(DC_10.title).getString());
    }
    if(r.hasProperty(DC_10.description)){
      Text desc = hit.getOwnerDocument().createTextNode(r.getProperty(DC_10.description).getString());
       hit.appendChild(desc);
    }
    hit.setAttribute("hasPreview","false");
      if((session.project.getPreviewFileSystem()!=null) && (holder.getOutputPaths().length > 0)){
        hit.setAttribute("hasPreview","true");
      }
  }
  public static void toggleDependencies(String path, String outputPath, CMSessionData session) throws FileSystemException, AuthorizationException{
    String id = session.project.getOutputElementID(path,outputPath);
    Element e =  getFileOutputElement(id,session);
    NodeList old = e.getElementsByTagName("hit");
    if(old.getLength()>0){
      for(int i=old.getLength()-1;i>=0;i--){
        e.removeChild(old.item(i));
      }
    }
    else{
      String[] deps = session.project.getDependencies(session.project.getFileHolder(path,null),outputPath);
      for(int i=0; i<deps.length;i++){
        if(session.hasPermission(READ_PERMISSION,deps[i])){
            Element d = session.model.createElement("hit");
            FileHolder holder = session.project.getFileHolder(deps[i],null);
            populateHit(d,holder,session,0);

            e.appendChild(d);
        }
      }
    }
  }

  protected static Element getFileOutputElement(String id, CMSessionData session){
    Element r= null;
    NodeList li = session.model.getElementsByTagName("file-output");
    for(int i=0;i<li.getLength();i++){
      Element e = (Element)li.item(i);
      if(e.getAttribute("output-id").equals(id)){
        r=e;
        break;
      }
    }
    return r;
  }

  public static void highlightOutput(String id, CMSessionData session){
    Element fe = getFileOutputElement(id,session);
    if (fe !=null){
        NodeList li = fe.getParentNode().getChildNodes();
        for(int i=0;i<li.getLength();i++){
          if(li.item(i).getNodeType()==li.item(i).ELEMENT_NODE){
              Element e = (Element)li.item(i);
              if(e.getAttribute("output-id").equals(id)){
                 e.setAttribute("highlighted","true");
              }
              else{
                e.setAttribute("highlighted","false");
              }
          }
        }
    }
  }

  protected static Element generateFileDef(String path, CMSessionData session) throws FileSystemException, CMSException{
      //TimeKeeper.startTask("generateFileDef");
      File current = session.repository.getFile(path);
      //TimeKeeper.startTask("layXML");
      Element def = session.model.createElement("file-def");
      def.setAttribute("currentEdition",String.valueOf(current.getEdition()));
      def.setAttribute("content-type",ContentTypes.getContentType(path));
      def.setAttribute("lastModified",dateformat.format(session.repository.getLastModified(path)));
      String[] ops = session.project.getOutputPaths(path);
      for(int i=0;i<ops.length;i++){
        Element fo = session.model.createElement("file-output");
        fo.setAttribute("label",ContentTypes.getTypeLabel(ContentTypes.getContentType(ops[i])));
        fo.setAttribute("output-id",session.project.getOutputElementID(path,ops[i]));
        fo.setAttribute("build-url",joinUrlToPath(session.project.getBaseBuildURL(),ops[i]));
        fo.setAttribute("publish-url",joinUrlToPath(session.project.getBasePublishURL(),ops[i]));
        fo.setAttribute("output-path",ops[i]);
        fo.setAttribute("highlighted","false");
        def.appendChild(fo);
      }
      //TimeKeeper.stopTask("layXML");
      //TimeKeeper.startTask("getMetaData");
      //Resource metadata = session.repository.getMetaData(path);
      //TimeKeeper.stopTask("getMetaData");
      //addMetaData(metadata,def);
      //TimeKeeper.startTask("getEditions");
      File[] editions = session.repository.getFileEditions(path);
      //TimeKeeper.stopTask("getEditions");
      for (int i=0; i<editions.length;i++){
        Element edition = session.model.createElement("edition");
        edition.setAttribute("index",String.valueOf(editions[i].getEdition()));
        populateEdition(edition,editions[i]);
        def.appendChild(edition);
      }
      //TimeKeeper.startTask("tempFiles");
      try{
        File temp = session.repository.getTempFile(path,session.userLockKey);
        if (temp!=null){
          Element t = session.model.createElement("temp-file");
          populateEdition(t,temp);
          def.appendChild(t);
        }
      }
      catch(Exception e){}
      //TimeKeeper.stopTask("tempFiles");
      //TimeKeeper.stopTask("generateFileDef");
      return def;
  }

  protected static void populateEdition(Element edition, File file) throws FileSystemException, CMSException{
    //TimeKeeper.startTask("populateEdition");
    edition.setAttribute("date",dateformat.format(file.getDate()));
    edition.setAttribute("editor",file.getCreator());
    //TimeKeeper.startTask("getMetaData");
    Resource metadata = file.getMetaData();
    //TimeKeeper.stopTask("getMetaData");
    addMetaData(metadata,edition);
    //TimeKeeper.stopTask("populateEdition");
  }

  protected static void addMetaData(Resource metadata, Element parent) throws CMSException{
    //TimeKeeper.startTask("addMetaData");
    Document doc = DocumentFactory.getRDFDocument(metadata,null);
    Element meta = doc.getDocumentElement();
    parent.appendChild(parent.getOwnerDocument().importNode(meta,true));
    //TimeKeeper.stopTask("addMetaData");
  }

  public static String getContentType(String path,CMSessionData session){
     String r = null;
     Element n = getRepositoryNode(path,session);
     NodeList nl = n.getElementsByTagName("file-def");
     if (nl.getLength()==1){
        Element e = (Element) nl.item(0);
        r= e.getAttribute("content-type");
     }
     return r;
  }

  public static void collapseRepositoryNode(String path, CMSessionData session){
    Element n = getRepositoryNode(path,session);
    if (n!=null){
      //if (n.getFirstChild()!=null){
      //  if (n.getFirstChild().getNodeName().equals("node")){
          NodeList nl = n.getChildNodes();
          for (int i = (nl.getLength() -1); i >=0; i--){
            n.removeChild(nl.item(i));
          }
      //  }
      //}
      n.setAttribute("expanded","false");
    }
  }

  public static Element getRepositoryNodeRoot(CMSessionData session){
    Element fs = null;
    if(session.model != null)
    {
        NodeList rep = session.model.getElementsByTagName("repository-nodes");
        if(rep.getLength()==1)
        {
          fs = (Element) rep.item(0);
        }
    }
    return fs;
  }

  public static void removeProject(CMSessionData session){
    NodeList nl = session.model.getElementsByTagName("cms-project");
    for(int i=(nl.getLength()-1); i >= 0; i--){
      nl.item(i).getParentNode().removeChild(nl.item(i));
    }
    Element fs = getRepositoryNodeRoot(session);
    NodeList fsn = fs.getChildNodes();
    for(int i=(fsn.getLength()-1); i >= 0; i--){
      if (fsn.item(i).getNodeName().equals("node")){
        fs.removeChild(fsn.item(i));
      }
    }
    NodeList sr = session.model.getElementsByTagName("search-results");
    for(int i=(sr.getLength()-1); i >= 0; i--){
      sr.item(i).getParentNode().removeChild(sr.item(i));
    }
    //fs.setAttribute("expanded","false");
  }

  public static void addNodeMoveOptions(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
      String pattern = session.project.getPattern(path);
      Element node = getRepositoryNode(path,session);
      NodeList nl = node.getChildNodes();
      for (int x=nl.getLength()-1;x>=0;x--){
          Node n = nl.item(x);
          String name = n.getNodeName();
          if(name!=null){
              if (name.equals("moveOptions")){
                  node.removeChild(n);
              }
              if (name.equals("copyOptions")){
                  node.removeChild(n);
              }
          }
      }
      if(pattern !=null){
      String[] matches = session.project.resolvePattern(pattern,SEPARATOR,-1);
          if(matches.length>0){
              String currentName = getCurrentName(matches,path);
              boolean toFile = pattern.endsWith("*");
              //int wildex = getLabelPosition(pattern);
              String[] patParts = session.project.splitPathElements(pattern);
             /* int wildex = -1;
              for(int z=0;z<patParts.length;z++){
                  if(patParts[z].startsWith("*")){
                      wildex=z;
                  }
              }   */
              if(!session.project.isRequired(path)){
                  if(patParts[patParts.length-1].startsWith("*")){
                      Element nfo = session.model.createElement("moveOptions");
                      nfo.setAttribute("currentName",currentName);
                      if (toFile){
                          nfo.setAttribute("promptString",L10n.getLocalizedString(session,"PROMPT_MOVE_FILE"));
                      }
                      else{
                          nfo.setAttribute("promptString",L10n.getLocalizedString(session,"PROMPT_MOVE_DIRECTORY"));
                      }
                      /*if (patParts[patParts.length-1].startsWith("*")){
                          nfo.setAttribute("namedNode","false");
                      }
                      else{
                          nfo.setAttribute("namedNode","true");
                      } */

                      for(int j=0; j<matches.length; j++){
                          //String matchPat = session.project.getPattern(matches[j]);
                          if(session.hasPermission(CREATE_PERMISSION,matches[j]) && session.hasPermission(DELETE_PERMISSION,path)){
                              Element nf =  session.model.createElement("moveOption");
                              nf.setAttribute("path",matches[j]);
                              if(session.project.fulfilsPattern(path,matches[j])){
                                 nf.setAttribute("current","true");
                              }
                              else{
                                 nf.setAttribute("current","false");
                              }
                              /*
                              if(matches[j].indexOf("*")>=0){
                                  nf.setAttribute("promptLabel",getPromptLabel(session,pattern,matches[j]));
                              }
                              */
                             //String[] matchParts = session.project.splitPathElements(matches[j]);
                              String label = getOptionLabel(session,pattern,matches[j]);//matchParts[wildex];

                              /*if(label.indexOf(SEPARATOR)>0){
                                  label = label.substring(0,label.indexOf(SEPARATOR));
                              }
                              */
                              nf.setAttribute("label",label);
                              nfo.appendChild(nf);
                          }
                      }
                      node.appendChild(nfo);
                  }
                  Element nfo = session.model.createElement("copyOptions");
                  nfo.setAttribute("currentName",currentName);
                  if (toFile){
                      nfo.setAttribute("promptString",L10n.getLocalizedString(session,"PROMPT_COPY_FILE"));
                  }
                  else{
                      nfo.setAttribute("promptString",L10n.getLocalizedString(session,"PROMPT_COPY_DIRECTORY"));
                  }
                  /*if (patParts[patParts.length-1].startsWith("*")){
                      nfo.setAttribute("namedNode","false");
                  }
                  else{
                      nfo.setAttribute("namedNode","true");
                  }   */

                  for(int j=0; j<matches.length; j++){
                      //String matchPat = session.project.getPattern(matches[j]);
                      if(session.hasPermission(CREATE_PERMISSION,matches[j]) && session.hasPermission(READ_PERMISSION,path)){
                          //String[] matchParts = session.project.splitPathElements(matches[j]);
                          //String label = matchParts[wildex];
                          String label = getOptionLabel(session,pattern,matches[j]);
                          //if((j==(matches.length-1)) || !label.startsWith("*")){
                          Element nf =  session.model.createElement("copyOption");
                          nf.setAttribute("path",matches[j]);
                          if(session.project.fulfilsPattern(path,matches[j])){
                             nf.setAttribute("current","true");
                          }
                          else{
                             nf.setAttribute("current","false");
                          }
                         /*
                          if(matches[j].indexOf("*")>=0){
                              nf.setAttribute("promptLabel",getPromptLabel(session,pattern,matches[j]));
                          }
                            */
                          //if(label.indexOf(SEPARATOR)>0){
                          //    label = label.substring(0,label.indexOf(SEPARATOR));
                          //}
                          nf.setAttribute("label",label);
                          nfo.appendChild(nf);
                      //w}
                      }
                  }
                  node.appendChild(nfo);
              }
          }
      }
  }

    protected static String getCurrentName(String[] patterns, String path){
         String r="";
        String[] pathParts = Project.splitPathElements(path);
        for(int i = 0; i < patterns.length; i++) {
            if(patterns[i].indexOf("*")>=0){
                String[] patternParts = Project.splitPathElements(patterns[i]);
                for(int j = 0; j < patternParts.length; j++) {
                    if(patternParts[j].startsWith("*")){
                        r = pathParts[j];
                        if (r.indexOf(".")>0){
                            r = r.substring(0,r.indexOf("."));
                        }
                        if (r.indexOf("/")>0){
                            r = r.substring(0,r.indexOf("/"));
                        }
                        return r;
                    }

                }
            }

        }
        return r;
    }

    protected static String getOptionLabel(CMSessionData session,String pattern, String path) throws FileSystemException {
        StringBuffer buffer = new StringBuffer();
        boolean first=true;
        String[] patternParts = Project.splitPathElements(pattern);
        String[] pathParts = Project.splitPathElements(path);
        if(patternParts.length==pathParts.length){
            for(int i = 0; i < pathParts.length; i++) {
                if(patternParts[i].startsWith("*")){
                    String sub = pathParts[i];
                    //if(sub.indexOf(SEPARATOR)>0){
                    //    sub = sub.substring(0,sub.indexOf(SEPARATOR));
                    //}
                    if(!sub.startsWith("*")){
                        if(!first){
                            buffer.append(" > ");
                        }
                        first=false;
                        String dir = Project.join(pathParts,0,"",i+1);
                        buffer.append(session.project.getLabel(dir));
                    }

                }

            }
        }
        return buffer.toString();
    }

  public static void addNodeNewFileOptions(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
      String[] patterns = session.project.getDefinedPatterns();
      Element node = getRepositoryNode(path,session);
      NodeList nl = node.getChildNodes();
      for (int x=nl.getLength()-1;x>=0;x--){
          if (nl.item(x).getNodeName().equals("newFileOptions")){
              node.removeChild(nl.item(x));
          }
      }
      String dir = path;
      if (session.repository.isFile(path)){
          dir = session.repository.getParentDirectory(path);
      }

      for(int i=0; i<patterns.length; i++){
         // System.out.println("Setting new file options for pattern "+patterns[i]);
          String[] matches = session.project.resolvePattern(patterns[i],dir,2);
          if(matches.length>0){
              Element nfo = session.model.createElement("newFileOptions");
              String l = session.project.getPatternLabel(patterns[i]);
              nfo.setAttribute("label",l);
              if(patterns[i].endsWith("*")){
                nfo.setAttribute("prompt",L10n.getLocalizedString(session,"PROMPT_CREATE_FILE",l));
              }
              else{
                nfo.setAttribute("prompt",L10n.getLocalizedString(session,"PROMPT_CREATE_DIRECTORY",new String[] {l,patterns[i].substring(patterns[i].lastIndexOf("*/")+2)}));
              }
              if (patterns[i].substring(patterns[i].lastIndexOf(SEPARATOR)+1).startsWith("*")){
                  nfo.setAttribute("namedFile","false");
              }
              else{
                  nfo.setAttribute("namedFile","true");
              }
              String[] types = session.project.getContentTypesForPattern(patterns[i]);
              for(int x=0; x<types.length; x++){
                  Element ct = session.model.createElement("content");
                  ct.setAttribute("type",types[x]);
                  nfo.appendChild(ct);
              }
              for(int j=0; j<matches.length; j++){
                  String matchPat = session.project.getPattern(matches[j]);
                  if(patterns[i].equals(matchPat) && session.hasPermission(CREATE_PERMISSION,matches[j])){
                      Element nf =  session.model.createElement("newFileOption");
                      nf.setAttribute("path",matches[j]);
                      String label = getOptionLabel(session,patterns[i],matches[j]);
                      nf.setAttribute("label",label);
                      nfo.appendChild(nf);
                  }
              }
              node.appendChild(nfo);
          }
      }

  }
  public static void addNewFileOptions(String[] paths, String label, String type, boolean namedFile,CMSessionData session) {
    NodeList nl = session.model.getElementsByTagName("newFileOptions");
    for(int i=(nl.getLength()-1); i >= 0; i--){
      nl.item(i).getParentNode().removeChild(nl.item(i));
    }
    Element nfo = session.model.createElement("newFileOptions");
    nfo.setAttribute("label",label);
    nfo.setAttribute("type",type);
    nfo.setAttribute("namedFile",String.valueOf(namedFile));
    for(int j=0; j<paths.length; j++){
          Element nf =  session.model.createElement("newFileOption");
          nf.setAttribute("path",paths[j]);
          nfo.appendChild(nf);
    }
    session.model.getDocumentElement().appendChild(nfo);
  }

  public static void setSearchResults(String field, String term, Hits hits, CMSessionData session){
    NodeList nl = session.model.getElementsByTagName("search-results");
    for(int i=(nl.getLength()-1); i >= 0; i--){
      nl.item(i).getParentNode().removeChild(nl.item(i));
    }
    Element results = session.model.createElement("search-results");
    results.setAttribute("field",field);
    results.setAttribute("term",term);
    for(int i=0; i<hits.length(); i++){
      try{
        org.apache.lucene.document.Document doc = hits.doc(i);
        String path = doc.get(CUFS.path.toString());
        String edition = doc.get(CUFS.edition.toString());
        if(session.repository.exists(path) && session.hasPermission(session.project.getPermissionsOwnerToken(),READ_PERMISSION,path)){
            FileHolder holder = session.project.getFileHolder(path,null);
            //only report current results
            if(edition.equals(String.valueOf(holder.getFile().getEdition()))){
                Element hit =  session.model.createElement("hit");
                populateHit(hit,holder,session,hits.score(i));
                results.appendChild(hit);
            }
            /*
            String name = path;
            boolean current = false;
            File file = session.repository.getFile(path);
            int ce = file.getEdition();
            if(edition.equals(String.valueOf(ce))){
                Element hit =  session.model.createElement("hit");
                current = true;
                hit.setAttribute("extension",file.getExtension());
                hit.setAttribute("hasPreview","false");
                  if((session.project.getPreviewFileSystem()!=null) && (session.project.getOutputPaths(path).length > 0)){
                    hit.setAttribute("hasPreview","true");
                  }

                name = file.getName();
                Resource resource = file.getMetaData();
                if(resource.hasProperty(DC_10.title)){
                  name = resource.getProperty(DC_10.title).getString();
                }
                if(resource.hasProperty(DC_10.description)){
                  Text ht = session.model.createTextNode(resource.getProperty(DC_10.description).getString());
                  hit.appendChild(ht);
                }
                hit.setAttribute("title",name);
                hit.setAttribute("exists","true");
                hit.setAttribute("current",String.valueOf(current));
                hit.setAttribute("score",String.valueOf(hits.score(i)));
                hit.setAttribute("path",path);
                hit.setAttribute("typeID",session.project.getTypeElementID(path));
                hit.setAttribute("edition",edition);
                results.appendChild(hit);
            }
            */
        }
      }
      catch(Exception e){
        LogService.log(LogService.ERROR,e);
      }
    }
    session.model.getDocumentElement().appendChild(results);
  }

  public static void getReferences(String path, CMSessionData session) throws FileSystemException, AuthorizationException{
    Element e =  getRepositoryNode(path,session);
    NodeList old = e.getElementsByTagName("references");

      for(int i=old.getLength()-1;i>=0;i--){
        e.removeChild(old.item(i));
      }
      Element ref = session.model.createElement("references");
      e.appendChild(ref);
      String[] deps = session.project.getDependentFilePaths(path);
      for(int i=0; i<deps.length;i++){
        if(session.hasPermission(READ_PERMISSION,deps[i])){
            Element d = session.model.createElement("hit");
            FileHolder holder = session.project.getFileHolder(deps[i],null);
            populateHit(d,holder,session,0);
            ref.appendChild(d);
        }
      }

  }

    public static void trimNodes(CMSessionData session) throws FileSystemException {
        Element fs = getRepositoryNodeRoot(session);
        NodeList nl = fs.getElementsByTagName("node");
        for (int i=nl.getLength()-1; i>=0; i--){
          Element tn = (Element) nl.item(i);
          if (!session.repository.exists(tn.getAttribute("path"))){
            tn.getParentNode().removeChild(tn);
          }
        }
    }

    public static void updateProjectDef(CMSessionData session){
        NodeList kids = session.model.getDocumentElement().getChildNodes();
        for(int i=kids.getLength()-1;i>=0;i--){
            Node k = kids.item(i);
            if(k!=null && "cms-project".equals(k.getNodeName())){
                session.model.getDocumentElement().removeChild(k);
            }
        }
        Element pdef = (Element)session.model.importNode(session.project.getDefinition().getDocumentElement(), true);
        session.model.getDocumentElement().appendChild(pdef);
    }

}
