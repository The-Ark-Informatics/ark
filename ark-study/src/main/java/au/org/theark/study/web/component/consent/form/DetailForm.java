/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.consent.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * @author nivedann
 *
 */
public class DetailForm  extends AbstractDetailForm<ConsentVO>{

	/**
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			Form<ConsentVO> containerForm) {
		super(id, feedBackPanel, resultListContainer, detailPanelContainer,
				detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);
		// TODO Auto-generated constructor stub
	}
	
	public void initialiseDetailForm(){
		
	}
	
	public void addDetailFormComponents(){
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,
			String selection, ModalWindow selectModalWindow) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<ConsentVO> containerForm,
			AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

}
