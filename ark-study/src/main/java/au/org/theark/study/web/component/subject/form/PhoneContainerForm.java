/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 *
 */
public class PhoneContainerForm extends Form<SubjectVO>{

	
	AjaxButton addPhoneButton;
	/**
	 * @param id
	 * @param model
	 */
	public PhoneContainerForm(String id, IModel<SubjectVO> model) {
		super(id, model);
	
	}
	

}
