package au.org.theark.study.web.component.site.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.study.web.component.site.SiteModel;

public class ContainerForm extends Form<SiteModel>{

	public ContainerForm(String id, CompoundPropertyModel<SiteModel> siteModelCpm) {
		super(id,siteModelCpm );
	}

}
