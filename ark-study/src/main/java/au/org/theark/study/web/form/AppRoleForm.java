package au.org.theark.study.web.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.odlabs.wiquery.ui.accordion.Accordion;

import au.org.theark.core.vo.EtaUserVO;

public class AppRoleForm extends Form{

	protected Accordion accordion;
	
	
	
	public Accordion getAccordion() {
		return accordion;
	}

	public void setAccordion(Accordion accordion) {
		this.accordion = accordion;
	}

	public AppRoleForm(String id, EtaUserVO etaUserVO) {
		super(id, new CompoundPropertyModel(etaUserVO));
	
	}

}
