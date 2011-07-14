package au.org.theark.lims.web.component.subjectLims.subject.form;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractContainerForm;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class ContainerForm extends AbstractContainerForm<SubjectVO>{
	
	protected WebMarkupContainer contextUpdateLimsWMC = null;

	public ContainerForm(String id, CompoundPropertyModel<SubjectVO> model){
		super(id,model);
	}

	public WebMarkupContainer getContextUpdateLimsWMC() {
		return contextUpdateLimsWMC;
	}

	public void setContextUpdateLimnsWMC(WebMarkupContainer contextUpdateTarget) {
		this.contextUpdateLimsWMC = contextUpdateTarget;
	}
	

}
