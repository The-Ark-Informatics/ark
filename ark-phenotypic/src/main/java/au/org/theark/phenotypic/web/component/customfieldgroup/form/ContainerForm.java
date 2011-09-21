package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 *
 */
public class ContainerForm   extends AbstractContainerForm<CustomFieldGroupVO>{

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id,	CompoundPropertyModel<CustomFieldGroupVO> cpmModel) {
		super(id, cpmModel);
		
	}

}
