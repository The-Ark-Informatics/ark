package au.org.theark.study.web.component.managestudy.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.study.web.component.study.StudyModel;

public  class BaseForm extends Form<StudyModel>{
	
	public BaseForm(String id, CompoundPropertyModel<StudyModel> model){
		super(id, model);
	}

}
