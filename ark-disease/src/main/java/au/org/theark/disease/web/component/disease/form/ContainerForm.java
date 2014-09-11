package au.org.theark.disease.web.component.disease.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.disease.vo.DiseaseVO;

public class ContainerForm extends AbstractContainerForm<DiseaseVO> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<DiseaseVO> model) {
		super(id, model);
	}
	
}
