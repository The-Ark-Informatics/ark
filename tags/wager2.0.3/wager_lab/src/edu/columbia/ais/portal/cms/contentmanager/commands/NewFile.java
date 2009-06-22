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
package edu.columbia.ais.portal.cms.contentmanager.commands;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_10;
import edu.columbia.ais.filesystem.File;
import edu.columbia.ais.filesystem.FileSystemException;
import edu.columbia.ais.portal.cms.CMSException;
import edu.columbia.ais.portal.cms.ContentTypes;
import edu.columbia.ais.portal.cms.IEditorServantFactory;
import edu.columbia.ais.portal.cms.contentmanager.CMModel;
import edu.columbia.ais.portal.cms.contentmanager.CMSessionData;
import edu.columbia.ais.portal.cms.contentmanager.ICommand;
import edu.columbia.ais.portal.cms.contentmanager.commands.OpenProject;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.ProjectFactory;
import org.jasig.portal.services.LogService;
import org.jasig.portal.services.AuthorizationService;
import org.jasig.portal.security.IUpdatingPermissionManager;
import org.jasig.portal.security.Permission;
import org.jasig.portal.security.IPermission;
import org.jasig.portal.ChannelStaticData;
import edu.columbia.ais.filesystem.IFileSystemManager;
import edu.columbia.ais.portal.cms.FileHolder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import org.jasig.portal.security.IPerson;
import java.util.Hashtable;

import neuragenix.security.AuthToken;
import neuragenix.dao.DatabaseSchema;
/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.7 $
 */

public class NewFile implements ICommand 
{
    IPerson ip;
    final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    final static char[] badChars = new char[]{'&', '*', '?', '\'', '"', '/', '\\', ' ', '.', '+', '=', '#', '<', '>', '|', ':', ';', ','};
        final static char[] badChars = new char[]{'&', '*', '?', '\'', '"', '/', '\\', '.', '+', '=', '#', '<', '>', '|', ':', ';', ','};
        IFileSystemManager repository;
        static
        {
             InputStream file = NewFile.class.getResourceAsStream("ContentManagerDBSchema.xml");
             DatabaseSchema.loadDomains(file, "ContentManagerDBSchema.xml");
        }

    public NewFile() {

    }
    public void newFolder(ChannelStaticData staticData,String key,String newDirectory,String filePath) throws CMSException
    {
        InputStream data = null;   
        CMSessionData session = new CMSessionData();    
        String path = null;
        session.userLockKey = staticData.getPerson().getEntityIdentifier().getKey();
        ip = staticData.getPerson();
        if(ip.getAttribute("permissioncheck") == null)
        {
            ip.setAttribute("permissioncheck",new Hashtable());
        }
        session.staticData = staticData;
        try
        {
            AuthToken atkn = (AuthToken)(staticData.getPerson().getAttribute("AuthToken"));
              if(atkn.hasActivity("cucms_edit"))
              {
        //           System.err.println("in setStaticdata() with Authorization");
        //           System.err.println("The person is :" + ((IPerson)sD.getPerson()).getFullName());*/
                  session.isAdminUser = true;

              }
               Project p;
            OpenProject op = new OpenProject();            
            File f = null;
//            System.err.println("--------key--------" + key);
             if(key != null)
            {
                p = ProjectFactory.getProject(key); 
//                System.err.println("----------projectOwnersToken---------" + p.getPermissionsOwnerToken());
                session = op.setProjectRoot(session,p);
            }
            p = ProjectFactory.getProject(key); 
            repository = p.getRepositoryManager();
            String basePath = filePath;
//            System.err.println("CMFilePath is -------" + basePath);
            String newDir = newDirectory;
//            System.err.println("CMNewDirectory is -------" + newDir);
            path = constructPath(basePath, newDir, null);  
//            System.err.println("---------------path-------------" + path);
            LogService.instance().log(LogService.DEBUG, "Trying to create file " + path);
            File template = null;
//            System.err.println("--------------valid path---------" + p.validXmlPath(path));
                if(session.project.validXmlPath(path)) 
                {
//                        System.err.println("---------valid XML Path-----------");
                        template = p.getXmlTemplateFile(path);
                        if(template == null) 
                        {
                            data = new ByteArrayInputStream(p.makeNewXmlDocument(path).getBytes());
                        }
                }
                else 
                {
                    data = new ByteArrayInputStream(new byte[0]);
                }
                        makeDirRecursive(path);
                        if(template == null) 
                        {
                            session.file = session.repository.newFile(path, session.userLockKey);
                        }
                        else
                        {
                              session.file = session.repository.copyFile(template, path, session.userLockKey);
                                data = session.file.getInputStream();
                        }
                        Resource resource = session.file.getMetaData();
                        String uName = staticData.getPerson().getFullName();
                        if(uName != null) {
                            resource.addProperty(DC_10.creator, uName);
                        }
                        String cType = ContentTypes.getContentType(path);
                        if(cType != null) {
                            resource.addProperty(DC_10.format, cType);
                            if(cType.startsWith("text")) {
                                resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Text");
                            }
                            else if(cType.startsWith("image")) {
                                resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Image");
                            }
                            else if(cType.startsWith("audio")) {
                                resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Audio");
                            }
                        }
                        resource.addProperty(DC_10.identifier, path);
                        resource.addProperty(DC_10.date, formatter.format(new Date()));
                        session.repository.lock(path, session.userLockKey, session.repository.EXCLUSIVE_LOCK);                        
                        session.file = session.repository.storeFile(path, data, resource, session.userLockKey, true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally 
                {
                    try 
                    {
                        if(data != null)
                        {
                            data.close();
                        }
                        session.repository.unlock(path, session.userLockKey);
                    }
                    catch(Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
   }

    protected static Character getBadChar(String test) {
        char[] chars = test.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            for(int j = 0; j < badChars.length; j++) {
                if(chars[i] == badChars[j]) {
                    return new Character(badChars[j]);
                }
            }
        }
        return null;
    }

    protected static String replaceWildCard(String search, String replace) {
        String r = search;
        int i = search.indexOf("*");
        if(i >= 0) {
            r = search.substring(0, i) + replace + search.substring(i + 1);
        }
        return r;
    }

    protected static void makeDirRecursive(String path, CMSessionData session) throws FileSystemException 
    {
        String dirtest = path;
        String baseDir = "";
        while(dirtest.indexOf(SEPARATOR) >= 0) {
            baseDir = baseDir + dirtest.substring(0, dirtest.indexOf(SEPARATOR) + 1);
            dirtest = dirtest.substring(dirtest.indexOf(SEPARATOR) + 1);
            if(!session.repository.exists(baseDir)) {
                session.repository.newDirectory(baseDir, session.userLockKey);
            }
        }
    }
    
    protected void makeDirRecursive(String path) throws FileSystemException {
        String dirtest = path;
        String baseDir = "";
//        System.err.println("------------making recursive directories--------------" + path);
        while(dirtest.indexOf(SEPARATOR) >= 0) {
            baseDir = baseDir + dirtest.substring(0, dirtest.indexOf(SEPARATOR) + 1);
            dirtest = dirtest.substring(dirtest.indexOf(SEPARATOR) + 1);
//            System.err.println("---------baseDir--------dirtest--------" + baseDir + " " + dirtest);
            if(!repository.exists(baseDir)) 
            {
                repository.newDirectory(baseDir,null);
            }
        }
    }

    protected static String constructPath(String pattern, String newName, String extension) throws CMSException {
        String path = pattern;
        if(pattern.indexOf("*") >= 0) {
            if(newName.trim().length() == 0) {
                throw new CMSException(CMSException.EMPTY_NAME);
            }
            Character c = getBadChar(newName);
            if(c != null) {
                throw new CMSException(CMSException.BAD_CHARACTERS, new String[]{c.toString()});
            }
            if(extension != null) {
                newName = newName + "." + extension;
            }
            path = replaceWildCard(pattern, newName);
        }
        return path;
    }

    protected void setCreatorPermissions(CMSessionData session, String path) throws Exception{
//        System.err.println("----------owner--------------" + session.project.getPermissionsOwnerToken());
        IUpdatingPermissionManager pman = AuthorizationService.instance().newUpdatingPermissionManager(session.project.getPermissionsOwnerToken());
        ArrayList perms = new ArrayList();
//            System.err.println("---------iprincipal----" + session.staticData.getAuthorizationPrincipal().getPrincipalString());        
        if(!session.hasPermission(READ_PERMISSION,path))
        {
//            System.err.println("---------iprincipal----" + session.staticData.getAuthorizationPrincipal().getPrincipalString());
             IPermission p = pman.newPermission(session.staticData.getAuthorizationPrincipal());
            p.setActivity(READ_PERMISSION);
            p.setTarget(path);
            p.setType(p.PERMISSION_TYPE_GRANT);
            perms.add(p);
        }
        if(!session.hasPermission(WRITE_PERMISSION,path)){
             IPermission p = pman.newPermission(session.staticData.getAuthorizationPrincipal());
            p.setActivity(WRITE_PERMISSION);
            p.setTarget(path);
            p.setType(p.PERMISSION_TYPE_GRANT);
            perms.add(p);
        }
        if(!session.hasPermission(DELETE_PERMISSION,path)){
             IPermission p = pman.newPermission(session.staticData.getAuthorizationPrincipal());
            p.setActivity(DELETE_PERMISSION);
            p.setTarget(path);
            p.setType(p.PERMISSION_TYPE_GRANT);
            perms.add(p);
        }

        IPermission[] ps = (IPermission[])perms.toArray(new IPermission[perms.size()]);
        if(ps.length>0){
            pman.addPermissions(ps);
        }
    }

    public void execute(CMSessionData session) throws Exception 
    {
        if(session.runtimeData.getParameter("create") != null)
        {
//            System.err.println("---------new Folder to be created----------");
            newFolder(session.staticData,session.runtimeData.getParameter("key"),session.runtimeData.getParameter("CMNewDirectory"),session.runtimeData.getParameter("CMFilePath"));
        }
        Project p;
        OpenProject op = new OpenProject();
/*
 @author rennypv
 *this is incorporated to include the option of creating a new node from another channel if provided with key,CMFilePath,CMNewDirectory
 *the key is required to specify in which project the new node is to be created
 *this bit opens the project based on the key and uses the remaining of the code to create the node
 */        
        if(session.runtimeData.getParameter("key") != null)
        {
            p = ProjectFactory.getProject(session.runtimeData.getParameter("key")); 
            session = op.setProjectRoot(session,p);
        }
/*End of edit by rennypv*/        
        String basePath = session.runtimeData.getParameter("CMFilePath");
//        System.err.println("CMFilePath is -------" + basePath);
        String path;
        String newDir = session.runtimeData.getParameter("CMNewDirectory");
//        System.err.println("CMNewDirectory is -------" + newDir);
        if(newDir != null) 
        {
            path = constructPath(basePath, newDir, null);
        }
        else {
            String fb = session.runtimeData.getParameter("CMFileName");
            path = constructPath(basePath, fb, session.runtimeData.getParameter("CMFileExtension"));
        }
        LogService.instance().log(LogService.DEBUG, "Trying to create file " + path);
        if(session.hasPermission(CREATE_PERMISSION,path)){
            File template = null;
            InputStream data = null;
            try {
                if(session.project.validXmlPath(path)) 
                {
                    template = session.project.getXmlTemplateFile(path);
                    if(template == null) {
                        data = new ByteArrayInputStream(session.project.makeNewXmlDocument(path).getBytes());
                    }
                }
                else {
                    data = new ByteArrayInputStream(new byte[0]);
                }
                makeDirRecursive(path, session);

                if(template == null) {
                    session.file = session.repository.newFile(path, session.userLockKey);
                }
                else {
                    try {
                        session.repository.lock(template.getPath(), session.userLockKey, session.repository.EXCLUSIVE_LOCK);
                        session.file = session.repository.copyFile(template, path, session.userLockKey);
                        data = session.file.getInputStream();
                    }
                    catch(Exception e) {
                        throw(e);
                    }
                    finally {
                        session.repository.unlock(template.getPath(), session.userLockKey);
                    }
                }
                try {
                    setCreatorPermissions(session,path);
                    Resource resource = session.file.getMetaData();
                    String uName = session.staticData.getPerson().getFullName();
                    if(uName != null) {
                        resource.addProperty(DC_10.creator, uName);
                    }
                    String cType = ContentTypes.getContentType(path);
                    if(cType != null) {
                        resource.addProperty(DC_10.format, cType);
                        if(cType.startsWith("text")) {
                            resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Text");
                        }
                        else if(cType.startsWith("image")) {
                            resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Image");
                        }
                        else if(cType.startsWith("audio")) {
                            resource.addProperty(DC_10.type, "http://purl.org/dc/dcmitype/Audio");
                        }
                    }
                    resource.addProperty(DC_10.identifier, path);
                    resource.addProperty(DC_10.date, formatter.format(new Date()));

                    session.repository.lock(path, session.userLockKey, session.repository.EXCLUSIVE_LOCK);
                    session.file = session.repository.storeFile(path, data, resource, session.userLockKey, true);
                }
                catch(Exception e) {
                    throw(e);
                }
                finally {
                    session.repository.unlock(path, session.userLockKey);
                }


                session.highlightPath = path;
                //session.screen = null;
                //session.screenParm = null;
                IEditorServantFactory[] factories = session.project.getServantFactories(session.file.getPath());
                if(factories.length > 0) {
                    session.repository.lock(path, session.userLockKey, session.repository.EXCLUSIVE_LOCK);
                    session.editorFactories = factories;
                    session.activeEditor = 0;
                    session.editorChannel = factories[0].getEditorServant(session.file, session.project, session.locale);
                    session.editorChannel.setStaticData(session.staticData);
                    CMModel.listEditors(session);
                    CMModel.updateSessionPreviewBooleans(session);
                }
            }
            finally {
                try {
                    data.close();
                }
                catch(Exception e) {
                }
            }
        }
        else{
          throw new CMSException(CMSException.NOT_PERMITTED);
        }
    }
}
