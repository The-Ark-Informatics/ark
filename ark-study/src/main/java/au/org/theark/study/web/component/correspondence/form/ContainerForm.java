package au.org.theark.study.web.component.correspondence.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.form.AbstractContainerForm;

public class ContainerForm extends AbstractContainerForm<CorrespondenceVO> {

	public ContainerForm(String id, CompoundPropertyModel<CorrespondenceVO> cpmModel) {
		super(id, cpmModel);
	}

}
