package au.org.theark.study.web.component.subject.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractContainerForm;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<SubjectVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<SubjectVO> model){
		super(id,model);
	}

}
