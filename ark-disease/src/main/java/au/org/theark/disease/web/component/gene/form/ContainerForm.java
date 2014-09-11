package au.org.theark.disease.web.component.gene.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.disease.vo.GeneVO;

public class ContainerForm extends AbstractContainerForm<GeneVO> {
	
	private static final long serialVersionUID = 1L;
	
	public ContainerForm(String id, CompoundPropertyModel<GeneVO> model) {
		super(id, model);
	}
	
}
