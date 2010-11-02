package au.org.theark.study.web.component.subject.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.SubjectVO;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends Form<SubjectVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<SubjectVO> model){
		super(id,model);
	}

}
