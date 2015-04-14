package au.org.theark.admin.web.component.audit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.AuditVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AuditVO> {

	private static final long serialVersionUID = 1L;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		setMultiPart(true);
	}

	public void initialiseDetailForm() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onSave(Form containerForm, AjaxRequestTarget target) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addDetailFormComponents() {
		// TODO Auto-generated method stub
	}
}