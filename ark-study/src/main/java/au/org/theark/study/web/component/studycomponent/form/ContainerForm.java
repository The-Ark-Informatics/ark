package au.org.theark.study.web.component.studycomponent.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.study.model.vo.StudyCompVo;

public class ContainerForm extends Form<StudyCompVo>{
	
	public ContainerForm(String id, CompoundPropertyModel<StudyCompVo> model){
		super(id,model);
	}

}
