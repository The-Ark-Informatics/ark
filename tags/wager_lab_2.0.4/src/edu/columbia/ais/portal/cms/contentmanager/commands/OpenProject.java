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

import edu.columbia.ais.filesystem.FileSystemException;
import edu.columbia.ais.portal.cms.CMSException;
import edu.columbia.ais.portal.cms.Project;
import edu.columbia.ais.portal.cms.ProjectFactory;
import edu.columbia.ais.portal.cms.contentmanager.CMModel;
import edu.columbia.ais.portal.cms.contentmanager.CMSessionData;
import edu.columbia.ais.portal.cms.contentmanager.ICommand;
import edu.columbia.ais.portal.cms.contentmanager.commands.HighlightNode;
import org.w3c.dom.Element;

/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.5 $
 */

public class OpenProject implements ICommand {
    /** Constructor */    
    public OpenProject() 
    {
    }

    /** This method is used to open a project and set the root directory and build the
     * nodes under there
     * @param session all session variables with the parameters required
     * @throws Exception not permitted to open the porject
     */    
    public void execute(CMSessionData session) throws Exception 
    {
        Project p;
//        System.err.println("In execute of open project");
        if(session.project==null)
        {
//            System.err.println("In OpenProject" + session.runtimeData.getParameter("key"));
            p = ProjectFactory.getProject(session.runtimeData.getParameter("key"));
/*
    @author rennypv
 *changed from the original code to incorporate the possibility of opening a project and the path through a link
 *
 */            
            session = setProjectRoot(session,p);
            session = setProjectParams(session,p);
/*End of edit by rennypv */
         }
        else
        {
/*
 *@author rennypv
    This section was included also for the functionality of opening a project and a node with just a link from another channel
 *a remove project done here because before opening the next node the project has to be closed and then re-opened
 *all the relevant session variables have to be set to null
 *then the requested project is opened and the path is highlighted and expanded
 */            
 //           System.err.println("-------in the else where session.project exists------" + session.project.getTitle());
//            System.err.println("-------in the else where key is------" + session.runtimeData.getParameter("key"));
            String tmp_rntimedtkey = session.runtimeData.getParameter("key");
            String tmp_projkey  = session.project.getKey();
//            System.err.println(tmp_projkey);
                 CMModel.removeProject(session);
                 session.promptForArg = null;
                  if(session.repository!=null){
                     session.repository.unmount();
                     session.repository = null;
                  }
                  if(session.project!=null){
                     session.project.close();
                     session.project = null;
                  }
                 session.publicationReport=null;
                 session.highlightPath=null;
                 session.screen=null;
                 session.screenParm =null;
                 p = ProjectFactory.getProject(session.runtimeData.getParameter("key"));
                 session.reset=true;                 
                 session = setProjectRoot(session,p);
                 session = setProjectParams(session,p);
//            System.err.println("-------in the else where key in project is------" + p.getKey());            
//            session = setProjectParams(session,p);
/*End of edit by rennypv*/                 
        }
        
            if(p != null) {
//                System.err.println("--------------in if where project != null-------");
                CMModel.updateProjectDef(session);
                //Element pdef = (Element)session.model.importNode(p.getDefinition().getDocumentElement(), true);
                //session.model.getDocumentElement().appendChild(pdef);
            }

        else{
          throw new CMSException(CMSException.NOT_PERMITTED);
        }
        //LogService.instance().log(LogService.DEBUG,BaseTest.serializeDoc(session.model));
    }
    /** This bit of code checks for whether the session contains a parameter "path" in
     * which case it is to go straight to the defined root.
     * @param p This is the project to be opened
     * @param session the session variable
     * @throws Exception if not permitted to view the path
     * @return return the session variable
     */    
    protected CMSessionData setProjectParams(CMSessionData session,Project p) throws Exception
    {
//        System.err.println("In setProjectParams");
/*
 @author rennypv
 *this is to check for if the link contains a path variable in which case it is coming from another channel.
 *it is then checked whether the user has the right to view the requested path
 *the expandRepositoryNodeRecursive looks for the highlight path variable of the session and hence that has
 to be set before the method called
 */        
           if(session.runtimeData.getParameter("path") != null)
            {
/*                p = ProjectFactory.getProject(session.runtimeData.getParameter("key"));
                session = setProjectRoot(session,p);                */
                if(session.hasPermission(p.getPermissionsOwnerToken(), READ_PERMISSION, session.runtimeData.getParameter("path")))
                {
//                    System.err.println("This is the path is : "+session.runtimeData.getParameter("path"));
                    String path = session.runtimeData.getParameter("path");
                    session.highlightPath = path;
                    CMModel.expandRepositoryNodeRecursive(session.highlightPath,session);
//                    System.err.println("before highlightNode");
                    CMModel.highlightNode(session.highlightPath,session);
                }
                else
                {
                    throw new CMSException(CMSException.NOT_PERMITTED);
                }
            }
           return session;
/*End of edit by rennypv */   
    }
    /** This method checks for whether the person has the rights to read the projects
     * and if yes then creates the root of the project and sets properties to expand
     * the nodes of the root
     * @param session session variable
     *
     * @param p the project to be considered
     * @throws Exception if the person is not permitted to view the project
     * @return the session variable
     */    
    protected CMSessionData setProjectRoot(CMSessionData session,Project p) throws Exception
    {
        try
        {
            if(session.hasPermission(p.getPermissionsOwnerToken(), READ_PERMISSION, SEPARATOR)) 
            {
                session.project = p;
                    session.repository = p.getRepositoryManager();
                    //e.setAttribute("mounted","true");
//                    System.err.println("---------in set Project root--------");
                    if(CMModel.getRepositoryNodeRoot(session) == null)
                    {
                        return session;
                    }
                    else
                    {
                        Element e = CMModel.getRepositoryNodeRoot(session);
                        Element root = session.model.createElement("node");
                        root.setAttribute("path", SEPARATOR);
                        root.setAttribute("name", SEPARATOR);
                        root.setAttribute("label", SEPARATOR);
                        root.setAttribute("expanded", "true");
                        root.setAttribute("isFile", "false");
                        e.appendChild(root);
                        CMModel.expandRepositoryNode(SEPARATOR, session);
                        CMModel.highlightNode(SEPARATOR, session);
                    }
                }
                return session;
        }
        catch(FileSystemException fe) 
        {
            if((fe.getType() == fe.X_BAD_MOUNT_ARGUMENTS) && session.project.getRepositoryFileSystem().needsPrompt) 
            {
                session.queuedCommand = this;
                session.queuedRuntime = session.runtimeData;
                session.promptForArg = session.project.getRepositoryFileSystem();
            }
            else 
            {
                session.project=null;
                session.repository=null;
                throw(fe);
            }
            return session;
        }
    }
}
