package au.org.theark.worktracking.web.component.workrequest.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.worktracking.model.vo.WorkRequestVo;

public class ContainerForm extends AbstractContainerForm<WorkRequestVo> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<WorkRequestVo> model) {
		super(id, model);
	}

}
