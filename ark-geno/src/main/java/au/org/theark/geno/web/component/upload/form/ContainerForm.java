/**
 * 
 */
package au.org.theark.geno.web.component.upload.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.geno.model.vo.UploadCollectionVO;

/**
 * @author elam
 *
 */
public class ContainerForm extends AbstractContainerForm<UploadCollectionVO> {

	public ContainerForm(String id,
			CompoundPropertyModel<UploadCollectionVO> cpmModel) {
		super(id, cpmModel);
	}

}
