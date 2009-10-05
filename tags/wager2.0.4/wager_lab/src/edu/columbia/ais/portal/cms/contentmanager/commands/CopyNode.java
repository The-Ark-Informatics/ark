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

import edu.columbia.ais.portal.cms.CMSException;
import edu.columbia.ais.portal.cms.contentmanager.CMModel;
import edu.columbia.ais.portal.cms.contentmanager.CMSessionData;
import edu.columbia.ais.portal.cms.contentmanager.ICommand;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.ProjectFactory;
import edu.columbia.ais.portal.cms.contentmanager.commands.OpenProject;
import edu.columbia.ais.portal.cms.ContentTypes;

import edu.columbia.ais.filesystem.FileSystemException;
import edu.columbia.ais.portal.cms.CMSException;

import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;


import neuragenix.security.AuthToken;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Date;

import com.hp.hpl.jena.vocabulary.DC_10;
import com.hp.hpl.jena.rdf.model.Resource;
/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Sep 15, 2003
 * Time: 1:21:16 PM
 * To change this template use Options | File Templates.
 */
public class CopyNode extends NewFile implements ICommand
{
    String status = null;
    String newPath = null;            
    public String copyFile(ChannelStaticData staticData,ChannelRuntimeData runtimeData,String project,File file,String toPath,String newName)
    {
        InputStream data = null;   
        CMSessionData session = new CMSessionData();    
        String path = null;
        status = null;
        newPath = null;
        session.userLockKey = staticData.getPerson().getEntityIdentifier().getKey();
        try
        {
            AuthToken atkn = (AuthToken)(staticData.getPerson().getAttribute("AuthToken"));
              if(atkn.hasActivity("cucms_edit"))
              {
                  session.isAdminUser = true;
              }
                   Project p;
                OpenProject op = new OpenProject();            
                File f = null;
                String extension = (file.getName()).substring(file.getName().lastIndexOf(".")+1);
                newPath = constructPath(toPath,newName,extension);
                session.staticData = staticData;                
                 if(project != null)
                {
                    p = ProjectFactory.getProject(project); 
                    session = op.setProjectRoot(session,p);
                }
                 else
                 {
                    p = ProjectFactory.getProject(project);
                    session.repository = p.getRepositoryManager();
                 }
                if(session.hasPermission(CREATE_PERMISSION,toPath))
                {
                    String toPattern =session.project.getPattern(newPath);
                    data = new FileInputStream(file);
                    session.file = session.repository.newFile(newPath, session.userLockKey);                            
    //                session.repository.lock(fromPath,session.userLockKey,session.repository.EXCLUSIVE_LOCK);
    //                session.repository.copyFile(file,newPath,session.userLockKey);
    //                session.repository.unlock(fromPath,session.userLockKey);
    //                session.repository.unlock(newPath,session.userLockKey);
                    Resource resource = session.file.getMetaData();
                    String uName = staticData.getPerson().getFullName();
                    if(uName != null) 
                    {
                        resource.addProperty(DC_10.creator, uName);
                    }
                    if(runtimeData.getParameter("title") != null)
                    {
                        resource.addProperty(DC_10.title,runtimeData.getParameter("title"));
                    }
                    if(runtimeData.getParameter("keywords") != null)
                    {
                        resource.addProperty(DC_10.subject,runtimeData.getParameter("keywords"));
                    }
                    if(runtimeData.getParameter("description") != null)
                    {
                        resource.addProperty(DC_10.description,runtimeData.getParameter("description"));
                    }
                    String cType = ContentTypes.getContentType(newPath);
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
                    resource.addProperty(DC_10.identifier, newPath);
                    resource.addProperty(DC_10.date, formatter.format(new Date()));
                    session.repository.lock(newPath, session.userLockKey, session.repository.EXCLUSIVE_LOCK);                        
                    session.file = session.repository.storeFile(newPath, data, resource, session.userLockKey, true);
                    status = "The file has been added successfully"; 
                }
                    return status;
//            }
        }
        catch(FileSystemException fse)
        {
            return fse.getMessage();
        }
        catch(CMSException cmse)
        {
            return cmse.getMessage();
        }
        catch(NullPointerException npe)
        {
            return npe.getMessage();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
        finally
        {
            try
            {
                session.repository.unlock(newPath, session.userLockKey);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
//        return status;
    }
    public void execute(CMSessionData session) throws Exception {
        String fromPath = session.runtimeData.getParameter("fromPath");
        String toPath = session.runtimeData.getParameter("toPath");
        String fromPattern =session.project.getPattern(fromPath);
        if (session.hasPermission(READ_PERMISSION,fromPath) && session.hasPermission(CREATE_PERMISSION,toPath)){
            String newName = session.runtimeData.getParameter("newName");
            String newPath;
            if (session.repository.isFile(fromPath))
            {
                String extension = null;
                if(toPath.lastIndexOf("*")>toPath.lastIndexOf(SEPARATOR)){
                    // we need to copy the file extension from the fromPath
                    extension = fromPath.substring(fromPath.lastIndexOf(".")+1);
                }
                newPath = constructPath(toPath,newName,extension);
                String toPattern =session.project.getPattern(newPath);
                if(fromPattern!=null && fromPattern.equals(toPattern)){
                    session.repository.lock(fromPath,session.userLockKey,session.repository.EXCLUSIVE_LOCK);
                    session.repository.copyFile(session.repository.getFile(fromPath),newPath,session.userLockKey);
                    session.repository.unlock(fromPath,session.userLockKey);
                    session.repository.unlock(newPath,session.userLockKey);
                }
                else{
                    throw new CMSException(CMSException.NOT_PERMITTED);
                }
            }
            else 
            {
                newPath = constructPath(toPath,newName,null);
                String toPattern =session.project.getPattern(newPath);
                if(fromPattern!=null && fromPattern.equals(toPattern)){
                    session.repository.lock(fromPath,session.userLockKey,session.repository.EXCLUSIVE_LOCK);
                    session.repository.copyDirectory(fromPath,newPath,session.userLockKey);
                    session.repository.unlock(toPath,session.userLockKey);
                }
                else{
                    throw new CMSException(CMSException.NOT_PERMITTED);
                }
            }
            setCreatorPermissions(session,newPath);
            session.highlightPath = newPath;
            CMModel.trimNodes(session);
            CMModel.expandRepositoryNodeRecursive(session.highlightPath,session);
            CMModel.highlightNode(session.highlightPath,session);
        }
        else{
          throw new CMSException(CMSException.NOT_PERMITTED);
        }
    }

}
