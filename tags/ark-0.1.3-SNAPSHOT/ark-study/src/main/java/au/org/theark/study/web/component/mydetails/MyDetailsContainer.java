package au.org.theark.study.web.component.mydetails;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

/**
 * A class that will contain Details component.
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
	private FeedbackPanel feedBackPanel;
	
	
	/**
	 * Construct the panel that will contain the User Details
	 * @param id
	 * @param userVO
	 * @param subject
	 */
	public MyDetailsContainer(String id, ArkUserVO userVO, Subject subject) {
		super(id);
		//Create a Form instance and send in the currently logged in  user details
		userVO.setUserName(subject.getPrincipal().toString());
		try{
			userVO = userService.getCurrentUser(userVO.getUserName());
		}catch(ArkSystemException ine){
			log.error("Exception occured :" + ine.getMessage());
		}
		
		// Add feedbackpanel
		add(initialiseFeedBackPanel());
		
		//Add the details panel into the container
		userVO.setMode(Constants.MODE_EDIT);
		add(new MyDetails(Constants.MY_DETAILS_PANEL, userVO, feedBackPanel));
	}
	
	private FeedbackPanel initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
}