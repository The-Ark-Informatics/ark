package au.org.theark.worktracking.web.component.billableitem.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.worktracking.model.vo.BillableItemVo;

public class ContainerForm extends AbstractContainerForm<BillableItemVo> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<BillableItemVo> model) {
		super(id, model);
		this.setMultiPart(true);
	}

}
