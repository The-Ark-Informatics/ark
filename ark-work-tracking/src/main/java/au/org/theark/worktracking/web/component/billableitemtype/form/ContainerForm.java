package au.org.theark.worktracking.web.component.billableitemtype.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;

public class ContainerForm extends AbstractContainerForm<BillableItemTypeVo> {

	private static final long	serialVersionUID	= 1L;

	public ContainerForm(String id, CompoundPropertyModel<BillableItemTypeVo> model) {
		super(id, model);
	}

}
