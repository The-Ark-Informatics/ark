package au.org.theark.study.web.component.user;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.UserForm;

/**
 * A panel that allows the loggeg in user to update his personal details.
 * @author nivedann
 *
 */
public class MyDetails extends Panel{
	
	private transient Logger log = LoggerFactory.getLogger(MyDetails.class);
	private UserForm userForm;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	public MyDetails(String id, EtaUserVO userVO) {
		
		super(id);
		userForm  = new UserForm(Constants.USER_DETAILS_FORM,userVO){
			private static final long serialVersionUID = 6077699021177330917L;
			//Do an update
			protected  void onSave(EtaUserVO userVO){
				//Update the user details TODO
				try {
					//forcing update of password
					userVO.setChangePassword(true);
					userService.updateLdapUser(userVO);
				} catch (ArkSystemException arkSystemException) {
					log.error("Exception occured while performing an update on the user details in LDAP " + arkSystemException.getMessage());
					//add custom error message to feedback panel. 
				}catch(Exception ex){
					//Handle all other type of exceptions
					log.error("Exception occured when saving user details " + ex.getMessage());
				}
			}
			
			protected void onCancel(){
				log.info("\n -----------------onCancel Clicked hide Details-----------------\n");
				this.setVisible(false);
			}
			
		};
		
		if(userVO.getMode() == Constants.MODE_EDIT){
			userForm.getUserNameTxtField().setEnabled(false);
		}
		userForm.getDeleteButton().setVisible(false);
		add(userForm);
		
	}

	

}
