package au.org.theark.study.web.component.managestudy.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.form.AbstractContainerForm;



public class Container extends AbstractContainerForm<StudyModelVO>{

	public Container(String id, CompoundPropertyModel<StudyModelVO> model) {
		super(id, model);
	}

}
