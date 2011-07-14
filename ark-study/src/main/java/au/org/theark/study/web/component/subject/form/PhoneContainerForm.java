/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 *
 */
public class PhoneContainerForm extends Form<SubjectVO>{

	
	AjaxButton addPhoneButton;
	
	public PhoneContainerForm(String id) {
		super(id);
	}	

}
