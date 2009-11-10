/*
 * SessionManagerServlet.java
 *
 * Created on 14 December 2005, 12:18
 */

package neuragenix.security;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

/**
 *
 * @author ltran
 * @version
 */
public class SessionManagerServlet extends HttpServlet {
    
    private ServletContext context;
    private static Hashtable hsSession = new Hashtable();
 
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();        
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
     
    public void doGet(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {
        
        org.jasig.portal.security.IPerson person = null;
        try
        {
            person = org.jasig.portal.security.PersonManagerFactory.getPersonManagerInstance().getPerson(request);
            if( !(person != null && person.getSecurityContext().isAuthenticated())){
                response.getWriter().write( "false" );
                return;
            }
        }catch(Exception e){ e.printStackTrace();} // do nothing.

        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        String message = "";
        
        // if the client is interested in the interval only
        if( request.getParameter("getPollingInterval") != null){
            message = NGXSession.POLLING_INTERVAL + "";
            response.getWriter().write( message );
            return;
        }
        
        
        // if the client is interested in the session tolerance only
        if( request.getParameter("getSessionTolerance") != null){
            message = NGXSession.SESSION_TOLERANCE + "";
            response.getWriter().write( message );
            return;
        }
            
        HttpSession session = request.getSession();
        if(session == null) return;
        
        // getting the parameters
        String strRenewTime = request.getParameter("renew");
        String strStartTime = request.getParameter("reset");
        if( strRenewTime == null && strStartTime == null) return;
                
        // add new session
        if( !hsSession.containsKey(session) ){
            if( strRenewTime != null ){
                // session has already been removed from the hashtable due to expiration.
                response.getWriter().write( "false" );
                return;
            }
            NGXSession ngxSession = new NGXSession(this);
            ngxSession.start();
            hsSession.put(session, ngxSession);
            response.getWriter().write( "true" );            
        }else{
            
            //renew session
            NGXSession ngxSession = (NGXSession) hsSession.get(session);
            if(strStartTime != null){                
                ngxSession.setStartTime(System.currentTimeMillis());
                response.getWriter().write( "true" );
            }
            
            //reseting session, i.e: new page.
            if(strRenewTime != null){
                if( !ngxSession.renew() ){
                    // session has already been removed from the hashtable due to expiration.
                    response.getWriter().write( "false" );
                }else{
                    // renew OK.
                    response.getWriter().write( "true" );
                }
                
            }
        }
        
    
    }
   
    /** 
     * Method to remove session from the hastable and invalidate it.
     * 
     * @session the expired NGX Session
     **/
    public static void removeSession(NGXSession session){
        if( hsSession.containsValue(session)){
            // go through the whole list
            for (Enumeration e = hsSession.keys() ; e.hasMoreElements() ;) {
                 HttpSession httpSession = (HttpSession)e.nextElement();
                 NGXSession currentSession = (NGXSession)hsSession.get( httpSession );
                 
                 //check if the current session is the one to remove.
                 if( session == currentSession ){
                    hsSession.remove(httpSession);
                    try{
                        httpSession.invalidate();
                    }catch(Exception ex){} // this is when the session has already been invalidated, we don't care, do we?
                    return;
                 }
                 
            }
            
        }
    }
}
/**
 * Class to manage session with the last renew period
 **/
class NGXSession extends Thread{

    public static final int SESSION_TIMEOUT=
            PropertiesManager.getPropertyAsInt("neuragenix.genix.security.DefaultLockTimeOut");
    public static final int SESSION_TOLERANCE = 
            PropertiesManager.getPropertyAsInt("neuragenix.genix.security.SessionTolerance");
    public static final int POLLING_INTERVAL =
            PropertiesManager.getPropertyAsInt("neuragenix.genix.security.SessionPollingInterval");
    
    private long lgStartTime=-1;
    private long lgLastRenew=-1;
    
    private SessionManagerServlet sessionManager;
    
    // the only consutrutor
    public NGXSession(SessionManagerServlet sessionManager){
        this.sessionManager = sessionManager;
        setStartTime( System.currentTimeMillis() );
    }
    
    /**
     * Method to renew the session
     **/
    public boolean renew(){
        // only renew when NOT expired.
        if( isExpired() ) return false;
        // renew now.
        lgLastRenew = System.currentTimeMillis();
        return true;
    }
    
    /**
     * Method to reset the start time
     *
     * @param lgStartTime the starting time
     **/
    public void setStartTime( long lgStartTime ){
        // only set it if it's new.
        if( this.lgStartTime != lgStartTime )
            this.lgStartTime = lgLastRenew = lgStartTime;
    }
    
    /**
     * Method to check if the session has not been renewed "recently"
     *
     * @return true if the system has not been renewed "recently"
     **/
    public boolean isExpired(){
        
        // check if it's timed out.
        long sessionLife = (System.currentTimeMillis() - lgStartTime) / 1000;
        if( sessionLife > SESSION_TIMEOUT ) return true;
        
        // check if the user closed the browser
        long idlePeriod = (System.currentTimeMillis() - lgLastRenew) / 1000;
        if( idlePeriod > (SESSION_TOLERANCE * POLLING_INTERVAL)) return true;
        
        return false;
    }
    
    /**
     * Run method
     **/
    public void run(){
        
        try{
            
            while( true ){
                //check if expired.
                if( isExpired() ){
                    sessionManager.removeSession( this );                    
                    return;
                }
                sleep( (long) (POLLING_INTERVAL * 1000) );
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
   

}
