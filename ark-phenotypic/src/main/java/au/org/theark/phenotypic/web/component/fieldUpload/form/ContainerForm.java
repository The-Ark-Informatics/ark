package au.org.theark.phenotypic.web.component.fieldUpload.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.phenotypic.model.vo.UploadVO;

/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<UploadVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<UploadVO> model){
		super(id,model);
	}

}
