package au.org.theark.study.web.component.user;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

/**
 * A class that will contain UserDetailsPanel component.
 * @author nivedann
 *
 */
public class MyDetailsContainer extends Panel{

	private transient Logger log = LoggerFactory.getLogger(MyDetailsContainer.class);
	
	/**
	 * The spring injected reference to a UserService implementation
	 */
	@SpringBean( name = "userService")
	private IUserService userService;
	
	/**
	 * Construct the panel that will contain the User Details
	 * @param id
	 * @param userVO
	 * @param subject
	 */
	public MyDetailsContainer(String id, EtaUserVO userVO, Subject subject) {
		super(id);
		//Create a Form instance and send in the currently logged in  user details
		userVO.setUserName(subject.getPrincipal().toString());
		try{
			userVO = userService.getCurrentUser(userVO.getUserName());
		}catch(ArkSystemException ine){
			log.error("Exception occured :" + ine.getMessage());
		}
		//Add the details panel into the container
		add(new MyDetailsPanel(Constants.MY_DETAILS_PANEL, userVO));
	}

}
