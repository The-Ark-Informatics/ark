package au.org.theark.geno.web.component.tableeditor.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.geno.model.vo.BeamListVO;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings("serial")
public class BeamContainerForm extends AbstractContainerForm<BeamListVO> {

	public BeamContainerForm(String id, CompoundPropertyModel<BeamListVO> model) {
		super(id, model);
	}

}
