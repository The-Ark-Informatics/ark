/**
 * 
 */
package au.org.theark.geno.web.component.genoCollection.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.geno.model.vo.GenoCollectionVO;

/**
 * @author elam
 *
 */
public class ContainerForm extends AbstractContainerForm<GenoCollectionVO> {

	public ContainerForm(String id,
			CompoundPropertyModel<GenoCollectionVO> cpmModel) {
		super(id, cpmModel);
	}

}
