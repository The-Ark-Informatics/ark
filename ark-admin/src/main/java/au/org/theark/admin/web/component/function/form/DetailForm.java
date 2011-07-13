package au.org.theark.admin.web.component.function.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4764831185215105696L;

	protected transient Logger log = LoggerFactory.getLogger(DetailForm.class);
	
	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>		iAdminService;

	private int									mode;
	private TextField<String>				idTxtFld;
	private TextField<String>				nameTxtFld;
	private TextArea<String>				descriptionTxtAreaFld;
	private DropDownChoice<ArkFunction>	arkFunctionTypeDropDown;
	private TextField<String>				resourceKeyTxtFld;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo)
	{
		super(id, feedbackPanel, containerForm, arkCrudContainerVo);
		this.containerForm = containerForm;
		arkCrudContainerVO = arkCrudContainerVo;
		setMultiPart(true);
	}

	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>("arkFunction.id");
		idTxtFld.setEnabled(false);
		
		nameTxtFld = new TextField<String>("arkFunction.name");
		descriptionTxtAreaFld = new TextArea<String>("arkFunction.description");

		// FunctionType selection
		initArkFunctionTypeDropDown();
		
		resourceKeyTxtFld = new TextField<String>("arkFunction.resourceKey");

		attachValidators();
		addDetailFormComponents();
	}

	@SuppressWarnings("unchecked")
	private void initArkFunctionTypeDropDown()
	{
		List<ArkFunctionType> arkFunctionTypeList = iAdminService.getArkFunctionTypeList();
		ChoiceRenderer<ArkFunction> defaultChoiceRenderer = new ChoiceRenderer<ArkFunction>("name", "id");
		arkFunctionTypeDropDown = new DropDownChoice("arkFunction.arkFunctionType", arkFunctionTypeList, defaultChoiceRenderer);
		arkFunctionTypeDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1007263623823525412L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	@Override
	protected void attachValidators()
	{
		// Set required field here
	}
	
	private void addDetailFormComponents()
	{
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtAreaFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkFunctionTypeDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resourceKeyTxtFld);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target)
	{
		// Save or update
		iAdminService.creatOrUpdateArkFunction(containerForm.getModelObject());
		
		this.info("Ark Function: " + containerForm.getModelObject().getArkFunction().getName() + " was created/updated successfully.");
		target.addComponent(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		// Delete
		iAdminService.deleteArkFunction(containerForm.getModelObject());
		
		this.info("Ark Function: " + containerForm.getModelObject().getArkFunction().getName() + " was deleted successfully.");
		editCancelProcess(target, true);
	}

	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	protected boolean isNew()
	{
		if (containerForm.getModelObject().getArkFunction().getId() == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}
}
