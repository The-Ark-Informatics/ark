package au.org.theark.study.web.component.user.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkUserVO;

@SuppressWarnings("serial")
public class ContainerForm extends Form<ArkUserVO>{
	
	public ContainerForm(String id, CompoundPropertyModel<ArkUserVO> model){
		super(id,model);
	}

}
