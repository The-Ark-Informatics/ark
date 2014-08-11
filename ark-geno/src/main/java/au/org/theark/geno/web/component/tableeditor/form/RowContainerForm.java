package au.org.theark.geno.web.component.tableeditor.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.geno.model.vo.RowListVO;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings("serial")
public class RowContainerForm extends AbstractContainerForm<RowListVO> {

	public RowContainerForm(String id, CompoundPropertyModel<RowListVO> model) {
		super(id, model);
	}

}
