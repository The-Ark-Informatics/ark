/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 *
 */
public class ContainerForm extends AbstractContainerForm<AddressVO>{

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id, CompoundPropertyModel<AddressVO> cpmModel) {
		super(id, cpmModel);
	}

}
