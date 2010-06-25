package au.org.theark.study.web.component.user;

import java.util.List;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
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

	private UserDetailsPanel userDetailsPanel;
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
			List<EtaUserVO> userList = userService.searchUser(userVO);
			if(userList.size() > 0){
				userVO = userList.get(0);
			}
		}catch(ArkSystemException ine){
			log.error("Exception occured :" + ine.getMessage());
		}
		
		MyDetailsForm form = new MyDetailsForm("myDetailsContainerForm",userVO);
		add(form);
	}
	
	
	/**
	 * A Form class that this container will use to add child components.
	 */
	private class MyDetailsForm extends Form {

		/**
		 * Construct the Form object and set the current user details 
		 * @param id
		 * @param userVO
		 */
		public MyDetailsForm(String id, EtaUserVO userVO) {
			super(id, new CompoundPropertyModel<EtaUserVO>(userVO));
			userVO.setMode(Constants.MODE_EDIT);
			add( new UserDetailsPanel("userDetailsPanel", userVO));
			
		}
	}

}
