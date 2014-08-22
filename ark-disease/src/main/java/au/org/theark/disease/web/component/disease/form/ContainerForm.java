package au.org.theark.disease.web.component.disease.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.DiseaseVO;
import au.org.theark.core.web.form.AbstractContainerForm;

public class ContainerForm extends AbstractContainerForm<DiseaseVO> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<DiseaseVO> model) {
		super(id, model);
	}
	
}
