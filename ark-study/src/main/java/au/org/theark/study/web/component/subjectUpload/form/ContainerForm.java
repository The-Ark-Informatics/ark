package au.org.theark.study.web.component.subjectUpload.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractContainerForm;

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
