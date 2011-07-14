package au.org.theark.lims.web.component.subjectLims.lims.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<LimsVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<LimsVO> model){
		super(id,model);
	}

}
