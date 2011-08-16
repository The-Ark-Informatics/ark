package au.org.theark.core.web.component.customfield.form;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<CustomFieldVO> {

	public ContainerForm(String id, CompoundPropertyModel<CustomFieldVO> model) {
		super(id, model);
	}

}
