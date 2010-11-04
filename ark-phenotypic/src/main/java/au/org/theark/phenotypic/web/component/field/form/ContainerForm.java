package au.org.theark.phenotypic.web.component.field.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.phenotypic.model.vo.FieldVO;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<FieldVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<FieldVO> model){
		super(id,model);
	}

}
