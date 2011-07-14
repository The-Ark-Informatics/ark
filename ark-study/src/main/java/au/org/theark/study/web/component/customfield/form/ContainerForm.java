/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 *
 */
public class ContainerForm extends AbstractContainerForm<CustomFieldVO>{

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id,	CompoundPropertyModel<CustomFieldVO> cpmModel) {
		super(id, cpmModel);
	}
	

}
