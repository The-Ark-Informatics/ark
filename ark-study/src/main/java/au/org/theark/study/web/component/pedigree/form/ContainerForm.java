package au.org.theark.study.web.component.pedigree.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.study.model.vo.PedigreeVo;

public class ContainerForm extends AbstractContainerForm<PedigreeVo> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<PedigreeVo> model) {
		super(id, model);
		this.setMultiPart(true);
	}

}
