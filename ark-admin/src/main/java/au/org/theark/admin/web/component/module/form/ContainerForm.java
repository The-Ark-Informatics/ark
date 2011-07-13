package au.org.theark.admin.web.component.module.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.web.form.AbstractContainerForm;



public class ContainerForm extends AbstractContainerForm<AdminVO>{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5677467747747142811L;

	public ContainerForm(String id, CompoundPropertyModel<AdminVO> model) {
		super(id, model);
	}

}
