package au.org.theark.study.web.component.manageuser.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractContainerForm;

@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<ArkUserVO>{

	public ContainerForm(String id, CompoundPropertyModel<ArkUserVO> cpmModel) {
		super(id, cpmModel);
	}

}
