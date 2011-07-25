package au.org.theark.study.web.component.mydetails;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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

/**
 * A panel that allows the loggeg in user to update his personal details.
 * 
 * @author nivedann
 * 
 */
public class MyDetails extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8278822398202036799L;
	private transient Logger						log	= LoggerFactory.getLogger(MyDetails.class);
	@SpringBean(name = "userService")
	private IUserService								userService;
	private CompoundPropertyModel<ArkUserVO>	arkUserModelCpm;

	public MyDetails(String id, ArkUserVO arkUserVO, final FeedbackPanel feedBackPanel, ModalWindow modalWindow) {
		super(id);
		/* Initialise the CPM */
		arkUserModelCpm = new CompoundPropertyModel<ArkUserVO>(arkUserVO);
		MyDetailsForm myDetailForm = new MyDetailsForm(Constants.USER_DETAILS_FORM, arkUserModelCpm, feedBackPanel, modalWindow) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8858977824290273852L;

			protected void onSave(AjaxRequestTarget target) {
				ArkUserVO arkUser = getModelObject();
				
				if((arkUser.getPassword() != null && arkUser.getConfirmPassword() != null) &&
						(!arkUser.getPassword().isEmpty() && !arkUser.getConfirmPassword().isEmpty())	){
					// Temporary allow the user to select if he wants to change it
					arkUser.setChangePassword(true);
				}

				try {
					userService.updateLdapUser(arkUser);
					this.info("Details for user: " + arkUser.getUserName() + " updated");
					processFeedback(target, feedBackPanel);
				}
				catch (ArkSystemException arkSystemException) {
					log.error("Exception occured while performing an update on the user details in LDAP " + arkSystemException.getMessage());
					this.error("An error has occured, cannot update user details. Please contact support.");
					processFeedback(target, feedBackPanel);
					// add custom error message to feedback panel.
				}
				catch (Exception ex) {
					// Handle all other type of exceptions
					this.error("An error has occured while saving user details. Please contact support.");
					processFeedback(target, feedBackPanel);
					log.error("Exception occured when saving user details " + ex.getMessage());
				}
			}

			protected void processFeedback(AjaxRequestTarget target, FeedbackPanel feedbackPanel) {
				target.addComponent(feedbackPanel);
			}

			protected void onCancel(AjaxRequestTarget target) {
				this.setVisible(false);
			}
		};
		myDetailForm.initialiseForm();
		if (arkUserVO.getMode() == Constants.MODE_EDIT) {
			myDetailForm.getUserNameTxtField().setEnabled(false);
		}
		add(myDetailForm);
	}

}