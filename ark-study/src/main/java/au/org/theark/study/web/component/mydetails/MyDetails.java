package au.org.theark.study.web.component.mydetails;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.mydetails.form.MyDetailsForm;
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

	private CompoundPropertyModel<ArkUserVO> arkUserModelCpm;
	public MyDetails(String id, ArkUserVO arkUserVO){
		super(id);
		/*Initialise the CPM */
		arkUserModelCpm = new CompoundPropertyModel<ArkUserVO>(arkUserVO);
		MyDetailsForm myDetailForm = new MyDetailsForm(Constants.USER_DETAILS_FORM, arkUserModelCpm){
			
			protected void onSave(AjaxRequestTarget target){
				ArkUserVO arkUser  = getModelObject();
				arkUser.setChangePassword(true);//Temporary allow the user to select if he wants to change it
				try{
					userService.updateLdapUser(arkUser);	
				}
				catch (ArkSystemException arkSystemException) {
					log.error("Exception occured while performing an update on the user details in LDAP " + arkSystemException.getMessage());
					//add custom error message to feedback panel. 
				}catch(Exception ex){
					//Handle all other type of exceptions
					log.error("Exception occured when saving user details " + ex.getMessage());
				}
			}
			
			protected void onCancel(AjaxRequestTarget target){
				this.setVisible(false);
			}
		};
		myDetailForm.initialiseForm();
		if(arkUserVO.getMode() == Constants.MODE_EDIT){
			myDetailForm.getUserNameTxtField().setEnabled(false);
		}
		add(myDetailForm);
	}
	
//	public MyDetails(String id, ArkUserVO userVO) {
//		
//		super(id);
//		userForm  = new UserForm(Constants.USER_DETAILS_FORM,userVO){
//			
//			//Do an update
//			protected  void onSave(ArkUserVO userVO){
//				//Update the user details TODO
//				try {
//					//forcing update of password
//					userVO.setChangePassword(true);
//					userService.updateLdapUser(userVO);
//				} catch (ArkSystemException arkSystemException) {
//					log.error("Exception occured while performing an update on the user details in LDAP " + arkSystemException.getMessage());
//					//add custom error message to feedback panel. 
//				}catch(Exception ex){
//					//Handle all other type of exceptions
//					log.error("Exception occured when saving user details " + ex.getMessage());
//				}
//			}
//			
//			protected void onCancel(){
//				log.info("\n -----------------onCancel Clicked hide Details-----------------\n");
//				this.setVisible(false);
//			}
//			
//		};
//		
//		userForm.initialiseForm();
//		
//		if(userVO.getMode() == Constants.MODE_EDIT){
//			userForm.getUserNameTxtField().setEnabled(false);
//		}
//		
//		userForm.getDeleteBtn().setVisible(false);
//		//userForm.getDeleteButton().setVisible(false);
//		add(userForm);
//		
//	}

	

}
