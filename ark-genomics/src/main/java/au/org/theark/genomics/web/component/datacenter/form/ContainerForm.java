package au.org.theark.genomics.web.component.datacenter.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.genomics.model.vo.DataCenterVo;

public class ContainerForm extends AbstractContainerForm<DataCenterVo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContainerForm(String id, CompoundPropertyModel<DataCenterVo> cpmModel) {
		super(id, cpmModel);
	}
}
