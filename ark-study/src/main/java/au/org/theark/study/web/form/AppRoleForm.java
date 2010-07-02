package au.org.theark.study.web.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.odlabs.wiquery.ui.accordion.Accordion;

import au.org.theark.core.vo.ArkUserVO;

@SuppressWarnings("serial")
public class AppRoleForm extends Form<ArkUserVO>{

	protected Accordion accordion;
	
	public Accordion getAccordion() {
		return accordion;
	}

	public void setAccordion(Accordion accordion) {
		this.accordion = accordion;
	}

	public AppRoleForm(String id, ArkUserVO arkUserVO) {
		super(id, new CompoundPropertyModel<ArkUserVO>(arkUserVO));
	
	}

}
