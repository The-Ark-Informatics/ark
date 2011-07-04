package au.org.theark.lims.web.component.subjectSub.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;

public class ContainerForm extends AbstractContainerForm<LimsVO>{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5675898405239856515L;

	public ContainerForm(String id, CompoundPropertyModel<LimsVO> model){
		super(id,model);
	}
}
