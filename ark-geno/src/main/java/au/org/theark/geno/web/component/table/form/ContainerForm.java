package au.org.theark.geno.web.component.table.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.geno.model.vo.RowListVO;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<RowListVO> {

	public ContainerForm(String id, CompoundPropertyModel<RowListVO> model) {
		super(id, model);
	}

}
