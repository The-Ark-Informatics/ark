package au.org.theark.study.web.component.studycomponent.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.study.model.vo.StudyCompVo;



public class ContainerForm extends AbstractContainerForm<StudyCompVo>{
	
	public ContainerForm(String id, CompoundPropertyModel<StudyCompVo> model){
		super(id,model);
	}

}
