package au.org.theark.phenotypic.web.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkUserVO;

@SuppressWarnings("serial")
public class AppRoleForm extends Form<ArkUserVO>{

	public AppRoleForm(String id, ArkUserVO arkUserVO) {
		super(id, new CompoundPropertyModel<ArkUserVO>(arkUserVO));
	
	}
	
	public AppRoleForm(String id) {
		super(id);
	}

}
