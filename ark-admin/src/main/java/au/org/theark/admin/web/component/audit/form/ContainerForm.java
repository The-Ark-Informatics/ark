package au.org.theark.admin.web.component.audit.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.AuditVO;
import au.org.theark.core.web.form.AbstractContainerForm;

public class ContainerForm extends AbstractContainerForm<AuditVO> {
 
	private static final long serialVersionUID = 1L;

	public ContainerForm(String id, CompoundPropertyModel<AuditVO> cpmModel) {
		super(id, cpmModel);
	}
}
