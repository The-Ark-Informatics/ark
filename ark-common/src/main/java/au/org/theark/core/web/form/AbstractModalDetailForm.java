package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AjaxDeleteButton;

/**
 * @author cellis
 * @param <T>
 * 
 */
public abstract class AbstractModalDetailForm<T> extends AbstractDetailForm<T>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4135522738458228329L;

	/**
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrdContainerVo
	 * @param form
	 */
	public AbstractModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, Form<T> form)
	{
		super(id, feedBackPanel, arkCrudContainerVo, form);
	}

	protected void initialiseForm()
	{

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (isNew())
				{
					editCancelProcess(target, true);
				}
				else
				{
					editCancelProcessForUpdate(target);
				}
			}

		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{

				saveOnErrorProcess(target);
			}
		};

		deleteButton = new AjaxDeleteButton(Constants.DELETE, new StringResourceModel("confirmDelete", this, null), new StringResourceModel(Constants.DELETE, this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4929802987078142352L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onDeleteConfirmed(target, null, selectModalWindow);
			}

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.DELETE);

			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editCancelProcess(target, true);
				// Add the sub-class functionality
				onCancelButtonClick(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}

		};

		selectModalWindow = initialiseModalWindow();
		
		addComponentsToForm();

	}
	
	/**
	 * Abstract method that allows sub-classes to implement specific functionality for onCancelButtonClick event.
	 */
	abstract protected void onCancelButtonClick(AjaxRequestTarget target);

	abstract protected void attachValidators();

	abstract protected boolean isNew();

	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);
}
