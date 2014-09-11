package au.org.theark.disease.web.component.affection.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.disease.vo.AffectionVO;

public class ContainerForm extends AbstractContainerForm<AffectionVO> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<AffectionVO> model) {
		super(id, model);
	}
	
}
