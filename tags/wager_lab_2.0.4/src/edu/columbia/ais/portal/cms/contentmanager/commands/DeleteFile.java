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
import org.jasig.portal.security.IUpdatingPermissionManager;
import org.jasig.portal.services.AuthorizationService;


import edu.columbia.ais.portal.cms.contentmanager.commands.OpenProject;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.ProjectFactory;
import edu.columbia.ais.portal.cms.contentmanager.commands.OpenProject;
import edu.columbia.ais.filesystem.IFileSystemManager;

import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.security.IPerson;

import neuragenix.security.AuthToken;

import java.io.File;
import java.util.Hashtable;
/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.1 $
 */

public class DeleteFile implements ICommand{
  public DeleteFile() 
  {
  }
  
  public static String deleteFiles(ChannelStaticData staticData,String key,String Directory)
  {
      IPerson ip;
      String error=null;
      try
      {
        CMSessionData session = new CMSessionData();    
        IFileSystemManager repository;
        String pathOfFile = null;
        Project p;
        ip = staticData.getPerson();
        if(ip.getAttribute("permissioncheck") == null)
        {
            ip.setAttribute("permissioncheck",new Hashtable());
        }
        session.staticData = staticData;
      AuthToken atkn = (AuthToken)(staticData.getPerson().getAttribute("AuthToken"));
      if(atkn.hasActivity("cucms_edit") || atkn.hasActivity("delete_case"))
      {
          session.isAdminUser = true;
      }
        OpenProject op = new OpenProject();            
        File f = null;
         if(key != null)
        {
            p = ProjectFactory.getProject(key); 
            session = op.setProjectRoot(session,p);
        }
        p = ProjectFactory.getProject(key); 
        session.repository = p.getRepositoryManager();
        //pathOfFile = NewFile.constructPath(Directory,"*",null);
        java.io.File dir = new File(Directory);
        if(session.repository.isDirectory(Directory))
        {
                session.repository.lock(Directory,atkn.getUserIdentifier(),session.repository.EXCLUSIVE_LOCK);
                session.repository.deleteDirectory(Directory,atkn.getUserIdentifier());
                session.repository.unlock(Directory,atkn.getUserIdentifier());
                IUpdatingPermissionManager upm = AuthorizationService.instance().newUpdatingPermissionManager(session.project.getPermissionsOwnerToken());
                upm.removePermissions(upm.getPermissions(null,Directory));
        }
        else
        {
            error = "There are no directories";
        }
        return error;
      }
      catch(Exception e)
      {
        e.printStackTrace();
        error = "exception has occured";
        return error;
      }
  }
  public void execute(CMSessionData session) throws Exception {
    String path = session.runtimeData.getParameter("path");
    if(session.hasPermission(DELETE_PERMISSION,path))
    {
        session.repository.lock(path,session.userLockKey,session.repository.EXCLUSIVE_LOCK);
        session.repository.deleteFile(path,session.userLockKey);
        session.repository.unlock(path,session.userLockKey);
        IUpdatingPermissionManager upm = AuthorizationService.instance().newUpdatingPermissionManager(session.project.getPermissionsOwnerToken());
        upm.removePermissions(upm.getPermissions(null,path));
        // collapse and expand parent node to refresh file definition
        String dirpath = session.repository.getParentDirectory(path);
        CMModel.expandRepositoryNode(dirpath,session);
    }
    else{
          throw new CMSException(CMSException.NOT_PERMITTED);
      }
  }
}
