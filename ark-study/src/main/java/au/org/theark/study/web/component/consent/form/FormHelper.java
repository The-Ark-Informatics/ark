/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.consent.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class FormHelper {

	public FormHelper(){
		
	}
	
	public void updateStudyCompStatusDates(AjaxRequestTarget target, String statusName,
											WebMarkupContainer wmcPlain, 
											WebMarkupContainer wmcRequested, 
											WebMarkupContainer wmcRecieved, WebMarkupContainer wmcCompleted){
		
		
		if( (statusName.equalsIgnoreCase(Constants.STUDY_STATUS_RECEIVED))){
			wmcRecieved.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRequested.setVisible(false);
			wmcCompleted.setVisible(false);
			target.addComponent(wmcPlain);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcCompleted);
		}else if( (statusName.equalsIgnoreCase(Constants.STUDY_STATUS_REQUESTED))){
		
			wmcRequested.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRecieved.setVisible(false);
			wmcCompleted.setVisible(false);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcPlain);
			target.addComponent(wmcCompleted);
		}else if ((statusName.equalsIgnoreCase(Constants.STUDY_STATUS_COMPLETED))){
			
			wmcCompleted.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRecieved.setVisible(false);
			wmcRequested.setVisible(false);
			target.addComponent(wmcCompleted);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcPlain);
		}else{
			setDatePickerDefaultMarkup(target,wmcPlain,wmcRequested,wmcRecieved,wmcCompleted);
		}
		
	}
	
	
	public  void setDatePickerDefaultMarkup(AjaxRequestTarget target,WebMarkupContainer wmcPlain, WebMarkupContainer wmcRequested, WebMarkupContainer wmcRecieved, WebMarkupContainer wmcCompleted){
		wmcPlain.setVisible(true);
		wmcRecieved.setVisible(false);
		wmcRequested.setVisible(false);
		wmcCompleted.setVisible(false);
		target.addComponent(wmcCompleted);
		target.addComponent(wmcRecieved);
		target.addComponent(wmcRequested);
		target.addComponent(wmcPlain);
	}

}
