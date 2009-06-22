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
import edu.columbia.ais.portal.cms.CMSPermissible;
import edu.columbia.ais.portal.cms.contentmanager.CMSessionData;
import edu.columbia.ais.portal.cms.contentmanager.ICommand;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.ProjectFactory;

import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.channels.permissionsmanager.CPermissionsManagerServantFactory;
import org.jasig.portal.services.LogService;

import org.jasig.portal.security.provider.AuthorizationImpl;
import org.jasig.portal.security.IPermission;
import org.jasig.portal.security.IUpdatingPermissionManager;
import org.jasig.portal.services.AuthorizationService;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.jasig.portal.security.provider.PersonImpl;
import org.jasig.portal.ChannelRuntimeData;



import edu.columbia.ais.portal.cms.ICMSConstants;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Vector;
import java.sql.ResultSet;

import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;

import java.io.InputStream;
/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.5 $
 */

public class AssignPermissions implements ICommand 
{
    Hashtable users = null;
    Hashtable permissions = null;
    static
    {
         InputStream file = AssignPermissions.class.getResourceAsStream("ContentManagerDBSchema.xml");
         DatabaseSchema.loadDomains(file, "ContentManagerDBSchema.xml");
    }
  public AssignPermissions() 
  {
  }

  public static void setContentManagerPermissions(Hashtable users,String project,String path,Hashtable permissions)
  {
    try
    {
//        System.err.println("-----------path---------" + path);
//        System.err.println("-----------project---------" + project);
        ChannelRuntimeData rd = new ChannelRuntimeData();
        Vector vtCheckPermissionsFormFields = DatabaseSchema.getFormFields("edit_permissions");  
        Vector vtGroupFormFields = DatabaseSchema.getFormFields("check_group");  
        boolean isGroup = false;
        rd.setParameter("PERMISSIONS_strOwner",ICMSConstants.PROJECT_OWNER_PREFIX + project);
        rd.setParameter("PERMISSIONS_strTarget",path);
        for(Enumeration enumUser = users.keys();enumUser.hasMoreElements();)
        {
            String struser = (String)enumUser.nextElement();
            String tempuser = struser;
            isGroup = ((Boolean)users.get(struser)).booleanValue();
            DALSecurityQuery query = new DALSecurityQuery();
            query.clearDomains();
            query.clearFields();
            query.clearWhere();
             if(isGroup)
            {
                rd.setParameter("PERMISSIONS_intPrincipalType","3");
                query.setDomain("GROUPCHECK",null,null,null);
                query.setFields(vtGroupFormFields,null);
                query.setWhere(null,0,"GROUPCHECK_strGroupName","=",struser,0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsgroup = query.executeSelect();
                if(rsgroup.next())
                {
                    struser = "local."+rsgroup.getString("GROUPCHECK_intGroupID");
//                    System.err.println("--------groupID-------" + struser);
                    rd.setParameter("PERMISSIONS_strPrincipalKey",struser);
                }
            }
            else
            {
                rd.setParameter("PERMISSIONS_intPrincipalType","2");
                rd.setParameter("PERMISSIONS_strPrincipalKey",struser);
            }
//            System.err.println("------------before Enumeation----------" + permissions.size());
            for(Enumeration enumPerm=permissions.keys();enumPerm.hasMoreElements();)
            {
//                System.err.println("---------in Enumeration----------");
                String permActivity = (String)enumPerm.nextElement();
                if(permActivity.startsWith(tempuser))
                {
//                    System.err.println("---------permActivity---------" + permActivity);
                    String[] activity = permActivity.split("_");
//                    System.err.println("------activity--------" + activity[1]);
                    rd.setParameter("PERMISSIONS_strActivity",activity[1]);                    
                    query.clearDomains();
                    query.clearFields();
                    query.clearWhere(); 
                    query.setDomain("PERMISSIONS", null, null, null);
                    query.setFields(vtCheckPermissionsFormFields, null);
                    query.setWhere(null, 0, "PERMISSIONS_strPrincipalKey", "=", struser, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"PERMISSIONS_strOwner","=",ICMSConstants.PROJECT_OWNER_PREFIX + project,0,DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"PERMISSIONS_strTarget","=",path,0,DALQuery.WHERE_HAS_VALUE);            
                    query.setWhere("AND",0,"PERMISSIONS_strActivity","=", activity[1],0,DALQuery.WHERE_HAS_VALUE); 
                    ResultSet rsResultSet = query.executeSelect();
                    rd.setParameter("PERMISSIONS_strPermissionType",(String)permissions.get(permActivity));                    
//                    System.err.println("--------permission type---runtimeData--------" + rd.getParameter("PERMISSIONS_strPermissionType"));                    
                    if(rsResultSet.next())
                    {
//                        System.err.println("------------permission type--------" + rsResultSet.getString("PERMISSIONS_strPermissionType"));
                        if(!rsResultSet.getString("PERMISSIONS_strPermissionType").equals(rd.getParameter("PERMISSIONS_strPermissionType")))
                        {
//                            System.err.println("------------update required----------");
                            query.clearFields();
                            query.setField("PERMISSIONS_strPermissionType",(String)permissions.get(permActivity));
                            query.executeUpdate();
                        }
                    }
                    else
                    {
                        query.clearFields();
                        query.setFields(DatabaseSchema.getFormFields("add_permissions"), rd);
                        query.executeInsert();
                    }
                }
            }
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
  }
   public void execute(CMSessionData session) throws Exception
   {
      String path = session.runtimeData.getParameter("path");
//      System.err.println("----------path---------" + path);
//      System.err.println("------------project---------" + session.project.getPermissionsOwnerToken());
      if (session.hasPermission(GRANT_PERMISSION,path))
      {
          if(session.runtimeData.getParameter("project") != null)
          {
//            System.err.println("setting permissions for the project----------");
            users = new Hashtable();
//            users.put("Researchers", new Boolean("true"));
//            users.put("user1",new Boolean("false"));
//            users.put("neuragenix",new Boolean("false"));
            permissions = new Hashtable();
//            permissions.put("Researchers_Read","GRANT");
//            permissions.put("user1_Write","DENY");
//            permissions.put("user1_Read","DENY");            
//            permissions.put("neuragenix_Write","GRANT");
            setContentManagerPermissions(users,session.runtimeData.getParameter("project"),session.runtimeData.getParameter("path"),permissions);
          }
          else
          {
              LogService.instance().log(LogService.DEBUG,"Assigning Permissions on path "+path);
              CMSPermissible permissible = new CMSPermissible(session.project);
              LogService.instance().log(LogService.DEBUG,"Using "+String.valueOf(permissible.getActivityTokens().length)+" activities for "+permissible.getOwnerName());
              String [] temp = permissible.getActivityTokens();
              for(int i=0;i<temp.length;i++)
              {
//                System.err.println("------------permissions------------" + temp[i]);
              }
//              System.err.println("---iperson--------" + session.staticData.getPerson().getFullName());
              session.permissionsManager = CPermissionsManagerServantFactory.getPermissionsServant(permissible,
              session.staticData, null, permissible.getActivityTokens(), new String[] {path});
          }
      }
      else
      {
          throw new CMSException(CMSException.NOT_PERMITTED);
      }
   }
}