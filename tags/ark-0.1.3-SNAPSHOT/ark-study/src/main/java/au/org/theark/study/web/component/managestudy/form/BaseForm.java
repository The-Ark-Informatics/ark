package au.org.theark.study.web.component.managestudy.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;

public  class BaseForm extends Form<StudyModelVO>{
	

	
	public BaseForm(String id, CompoundPropertyModel<StudyModelVO> model){
		super(id, model);
	}

}
