package au.org.theark.phenotypic.web.component.phenodatasetdefinition.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 *
 */
public class ContainerForm   extends AbstractContainerForm<PhenoDataSetFieldGroupVO>{


	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id,	CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpmModel) {
		super(id, cpmModel);
		
	}

}
