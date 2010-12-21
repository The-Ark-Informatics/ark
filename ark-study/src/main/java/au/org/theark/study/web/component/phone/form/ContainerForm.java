/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.phone.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 *
 */
public class ContainerForm extends AbstractContainerForm<PhoneVO> {

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id, CompoundPropertyModel<PhoneVO> cpmModel) {
		super(id, cpmModel);
	}

}
