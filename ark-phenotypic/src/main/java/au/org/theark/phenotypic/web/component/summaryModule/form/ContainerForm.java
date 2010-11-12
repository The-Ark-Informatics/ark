package au.org.theark.phenotypic.web.component.summaryModule.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.phenotypic.model.vo.CollectionVO;

/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<CollectionVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<CollectionVO> model){
		super(id,model);
	}

}
