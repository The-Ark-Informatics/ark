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
import edu.columbia.ais.filesystem.FileSystem;
import edu.columbia.ais.filesystem.IFileSystemManager;
import edu.columbia.ais.portal.cms.CMSPermissionsPolicy;
import edu.columbia.ais.portal.cms.IEditorServantFactory;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.RequestTracker;
import edu.columbia.ais.portal.cms.contentmanager.*;
import org.jasig.portal.AuthorizationException;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IServant;
import org.w3c.dom.Document;
import org.jasig.portal.security.IPerson;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Hashtable;

import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;
import neuragenix.security.*;


/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.8 $
 */

public class CMSessionData {
  public Document model;
  public ChannelStaticData staticData;
  public ChannelRuntimeData runtimeData;
  public boolean isAdminUser = false;
  public IServant editorChannel;
  public IEditorServantFactory[] editorFactories;
  public String feedback;
  public FileSystem promptForArg;
  public RequestTracker publicationReport;
  public IFileSystemManager repository;
  public Project project;
  public String userLockKey;
  public File file;
  public ICommand queuedCommand;
  public ChannelRuntimeData queuedRuntime;
  public String screen;
  public String screenParm;
  public String highlightPath;
  public IServant permissionsManager;
  public String lastReport;
  public int activeEditor = 0;
  public String sortOrder;
  public ResourceBundle localization;
  public Locale locale;
  public boolean previewLink=false;
  public boolean previewReferencesLink=false;
  public Map transformerParms;
  public java.io.File zipfile;
  public String nextDownloadPath;
  public IPerson ip;
  public boolean reset = true;

    public CMSessionData() {
  }

  public boolean hasPermission(String activity, String path) throws AuthorizationException
  {
//      System.err.println("------------checking for permissions------------");
    return hasPermission(project.getPermissionsOwnerToken(),activity,path);
  }
  public boolean hasPermission(String[] activities, String path) throws AuthorizationException{
     for(int i = 0; i < activities.length; i++) {
          String activity = activities[i];
          if(!hasPermission(project.getPermissionsOwnerToken(),activity,path)){
              return false;
          }

      }
    return true;
  }
  public boolean hasPermission(String owner, String activity, String path) throws AuthorizationException
  {
    if (isAdminUser)
    {
      return true;
    }
    ip = staticData.getPerson();
    if(reset)
    {
        ip.setAttribute("permissioncheck",new Hashtable());
        reset = false;
    }
//    System.err.println("-----------ip----in CMSessionData----" + ip.getAttribute("permissioncheck"));
    try
    {
        if(((Hashtable)ip.getAttribute("permissioncheck")).isEmpty())
        {
//            ip.setAttribute("permissioncheck",hshtblowner);
            Hashtable hshtblowner = (Hashtable)ip.getAttribute("permissioncheck");
            DALSecurityQuery query = new DALSecurityQuery();     
            AuthToken authtkn = (AuthToken)staticData.getPerson().getAttribute("AuthToken");
            String struser = authtkn.getUserIdentifier();   
            Vector vtCheckPermissionsFormFields = DatabaseSchema.getFormFields("edit_permissions");    
            String membserv = null; 
            boolean rs = false;
            do
            {
                query.clearDomains();
                query.clearFields();
                query.clearWhere();
                query.setDomain("PERMISSIONS", null, null, null);
                query.setFields(vtCheckPermissionsFormFields, null);
                if(membserv != null)
                {
                    query.setWhere(null, 0, "PERMISSIONS_strPrincipalKey", "=", membserv+struser, 0, DALQuery.WHERE_HAS_VALUE);
                }
                else
                {
                     query.setWhere(null, 0, "PERMISSIONS_strPrincipalKey", "=", struser, 0, DALQuery.WHERE_HAS_VALUE);
                }
                query.setWhere("AND", 0, "PERMISSIONS_strOwner", "LIKE", "CMS_PROJECT::", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultSet = query.executeSelect(); 
               while(rsResultSet.next())
               {
                   String rsowner = rsResultSet.getString("PERMISSIONS_strOwner");
                   String rspath = rsResultSet.getString("PERMISSIONS_strTarget");
                   String rsactivity = rsResultSet.getString("PERMISSIONS_strActivity");
                   String rstype = rsResultSet.getString("PERMISSIONS_strPermissionType");
                   String key = rspath + "_" +rsactivity;
                   if(!hshtblowner.containsKey(rsowner))
                   {
                        hshtblowner.put(rsowner, new Hashtable());
//                        System.err.println("----------owner in hashtable---------" + rsowner);
                   }
                   Hashtable hshtblperm = (Hashtable)hshtblowner.get(rsowner);
                   if(!hshtblperm.containsKey(key))
                   {
                        hshtblperm.put(key, rstype);
//                        System.err.println("----------key and value in hashtable--------" + key + " " + rstype);
                   }
               }
                query.clearDomains();
                query.clearFields();
                query.clearWhere(); 
                query.setDomain("GROUPMEMBERSHIP", null, null, null);
                query.setFields(DatabaseSchema.getFormFields("group_membership"), null);
                query.setWhere(null, 0, "GROUPMEMBERSHIP_strMemberKey", "=", struser, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsmember = query.executeSelect();
                rs = rsmember.next();
                if(rs)
                {
                    struser = new String(rsmember.getString("GROUPMEMBERSHIP_intGroupID"));
//                    System.err.println("------------user in group------" + struser);
                    membserv = rsmember.getString("GROUPMEMBERSHIP_strMemberService")+".";
                }
            }while(rs);
//            ip.setAttribute("permissioncheck",hshtblowner);
         }
         Hashtable hshchkowner = (Hashtable)ip.getAttribute("permissioncheck");
         if(hshchkowner.containsKey(owner))
         {
            String key = path + "_"+activity;
            
            Hashtable hshchkperm = (Hashtable)hshchkowner.get(owner);
            if(hshchkperm.containsKey(key))
            {
                String perm = (String)hshchkperm.get(key);
                if(perm.equals("GRANT"))
                {
                    return true;
                }
                else if(perm.equals("DENY"))
                {
                    return false;
                }
            }
         }
         else if(path.equals("/"))
        {
            return false;
        }
        return true;         
     }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    return true;
  }

}
