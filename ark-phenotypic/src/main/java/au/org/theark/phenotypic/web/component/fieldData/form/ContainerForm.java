package au.org.theark.phenotypic.web.component.fieldData.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<PhenoCollectionVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<PhenoCollectionVO> model){
		super(id,model);
	}

}
