package au.org.theark.security;

import java.lang.annotation.Annotation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;

import au.org.theark.core.security.SecurityConstraint;
import au.org.theark.core.security.ShiroAction;


public class CustomAuthorizationStrategy implements IAuthorizationStrategy{

	public boolean isActionAuthorized(final Component component, final Action action) {


		ShiroAction _action = (action.getName().equals( Action.RENDER ) )    ? ShiroAction.RENDER : ShiroAction.ENABLE;
	    
		Class<? extends Component> clazz = component.getClass();
	    
	    SecurityConstraint fail = checkInvalidInstantiation( clazz.getAnnotations(), _action );
	    if( fail == null ) {
	      fail = checkInvalidInstantiation( clazz.getPackage().getAnnotations(), _action );
	    }
	    return fail == null;
	}

	
	
	public <T extends Component> boolean isInstantiationAuthorized(Class<T> componentClass) {
		SecurityConstraint securityConstraint =  checkInvalidInstantiation(componentClass);
		 if(securityConstraint != null){
			 return false;
		 }
		return true;
	}

	
	private SecurityConstraint checkInvalidInstantiation(Annotation[] annotationList, ShiroAction action){
		
		
		
		for (Annotation annotation : annotationList) {
			
  			if(annotation instanceof SecurityConstraint ){
				
  				SecurityConstraint constraint = (SecurityConstraint) annotation;
				//ACTION
				if(constraint.action() == action){
					System.out.println("Constraints match");
					//The component in context has this action annotated.
					//Check if the current user has the rights/access by checking the subjects  
					SecurityManager securityManager =  ThreadContext.getSecurityManager();
			        Subject subject = SecurityUtils.getSubject();//The subject in session maintained by Shiro Security Manager
			        
			        //CONSTRAINT
			        //Checks if the subject has one of the below constraints, if the subject does not then return the constraint that 
			        //is violated
			        switch(constraint.constraint()) {
				        
			        	case HasRole:{
				        	//Check if the Shiro Security Manager if the user has role
				        	if(!securityManager.hasRole(subject.getPrincipals(), constraint.value())){
				        		System.out.println("Subject does not have role: " + constraint.value());
				        		return constraint;
				        	}
				        	break;
				        }
				        case HasPermission: {
				              
				        	if(!securityManager.isPermitted( subject.getPrincipals(), constraint.value() ) ) {
				        		System.out.println("Subject does not have Permission");
				                return constraint;
				            }
				            break;
				        }
				        case IsAuthenticated: {
				             
				        	 if(!subject.isAuthenticated() ) {
				        		System.out.println("Subject is Not Authenticated");
				                return constraint;
				             }
				             break;
				        }
				        case LoggedIn:{
				              
				        	if( subject.getPrincipal() == null ) {
				        		System.out.println("Subject is not LoggedIn");
				                return constraint;
				            }
				            break;
				       }
			        }
				
				}else{
					System.out.println("Actions do not match ");
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if the instance of the class contains any annotations and if it does checks if
	 * the component has any constraints associated with it by invoking a overloaded method
	 * passing in the list of annotations and an action of Instantiate(ShiroAction.INSTANTIATE)
	 * @param <T>
	 * @param componentClass
	 * @return
	 */
	  public <T extends Component> SecurityConstraint checkInvalidInstantiation( final Class<T> componentClass )
	  {
	  	SecurityConstraint securityConstraint = null;//combination of action and things like is user authenticated, etc...

	  	Annotation[] annotationList = componentClass.getAnnotations();
	  	//Check if the component in context can be created(the action is Instantiate)
	  	if(annotationList != null){
	  		securityConstraint = checkInvalidInstantiation(annotationList, ShiroAction.RENDER);
	  	}else
	  	{
	  		System.out.println("\n There are no annotations defined for this component/class ");
	  	}
		return securityConstraint;  
	  }
	
	

}
