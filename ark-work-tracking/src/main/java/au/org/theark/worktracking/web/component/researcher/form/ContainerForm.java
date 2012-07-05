package au.org.theark.worktracking.web.component.researcher.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.worktracking.model.vo.ResearcherVo;

public class ContainerForm extends AbstractContainerForm<ResearcherVo> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<ResearcherVo> model) {
		super(id, model);
	}

}
