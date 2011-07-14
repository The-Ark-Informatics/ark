package au.org.theark.study.web.component.site.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.SiteModelVO;

public class ContainerForm extends Form<SiteModelVO>{

	public ContainerForm(String id, CompoundPropertyModel<SiteModelVO> siteModelCpm) {
		super(id,siteModelCpm );
	}

}
