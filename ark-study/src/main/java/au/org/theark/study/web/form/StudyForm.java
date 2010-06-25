package au.org.theark.study.web.form;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.web.Constants;

public class StudyForm extends Form<Study>{
	
	TextField<String> studyIdTxtField =new TextField<String>(Constants.STUDY_KEY);
	TextField<String> studyNameTxtField = new TextField<String>(Constants.STUDY_NAME);
	TextField<String> studyDescriptionTxtField = new TextField<String>(Constants.STUDY_DESCRIPTION);
	TextField<String> yearOfCompletionTxtField = new TextField<String>(Constants.STUDY_ESTIMATED_YEAR_OF_COMPLETION);
	TextField<String> studyStatusTxtField = new TextField<String>(Constants.STUDY_STATUS);//change this to drop down
	TextField<String> dateOfApplicationTxtField = new TextField<String>(Constants.STUDY_DATE_OF_APPLICATION);//Change this to a calendar control
	TextField<String> principalContactTxtField = new TextField<String>(Constants.STUDY_CONTACT);
	TextField<String> principalContactPhone = new TextField<String>(Constants.STUDY_CONTACT_PHONE);
	
	protected  void onSave(Study study){}
	protected  void onCancel(){}
	protected void  onDelete(Study study){}
	
	private void initFormFields(){
		
	}
	
	public StudyForm(String id, Study study) {
		
		super(id, new CompoundPropertyModel<Study>(study));
		initFormFields();
		
		
		Button saveButton = new Button(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit()
			{
				onSave((Study) getForm().getModelObject());
			}
		}; 
		
	
		Button cancelButton = new Button(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onCancel();
			}
			
		};
		
		Button deleteButton = new Button(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onDelete((Study) getForm().getModelObject());
			}
			
		};
		
		
	}

}
