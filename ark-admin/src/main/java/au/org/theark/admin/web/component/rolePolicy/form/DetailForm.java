package au.org.theark.admin.web.component.rolePolicy.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO>
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 5096967681735723818L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>		iArkCommonService;

	private int									mode;
	private TextField<String>				idTxtFld;
	private DropDownChoice<ArkRole>		arkRoleDropDown;
	private DropDownChoice<ArkModule>	arkModuleDropDown;
	private DropDownChoice<ArkFunction>	arkFunctionDropDown;
	private CheckBox							arkCreatePermissionChkBox;
	private CheckBox							arkReadPermissionChkBox;
	private CheckBox							arkUpdatePermissionChkBox;
	private CheckBox							arkDeletePermissionChkBox;

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
		detailPanelContainer = arkCrudContainerVo.getDetailPanelContainer();
		detailPanelFormContainer = arkCrudContainerVo.getDetailPanelFormContainer();
		viewButtonContainer = arkCrudContainerVo.getViewButtonContainer();
		editButtonContainer = arkCrudContainerVo.getEditButtonContainer();
		setMultiPart(true);
	}

	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>("arkRolePolicyTemplate.id");
		idTxtFld.setEnabled(false);

		// Role selection
		initArkRoleDropDown();

		// Module selection
		initArkModuleDropDown();

		// Function selection
		initArkFunctionDropDown();

		arkCreatePermissionChkBox = new CheckBox("arkCreatePermission");
		arkCreatePermissionChkBox.setVisible(true);
		arkCreatePermissionChkBox.setOutputMarkupId(true);

		arkReadPermissionChkBox = new CheckBox("arkReadPermission");
		arkReadPermissionChkBox.setVisible(true);
		arkReadPermissionChkBox.setOutputMarkupId(true);

		arkUpdatePermissionChkBox = new CheckBox("arkUpdatePermission");
		arkUpdatePermissionChkBox.setVisible(true);
		arkUpdatePermissionChkBox.setOutputMarkupId(true);

		arkDeletePermissionChkBox = new CheckBox("arkDeletePermission");
		arkDeletePermissionChkBox.setVisible(true);
		arkDeletePermissionChkBox.setOutputMarkupId(true);

		attachValidators();
		addDetailFormComponents();
	}

	@SuppressWarnings("unchecked")
	private void initArkRoleDropDown()
	{
		List<ArkRole> arkRoleList = iArkCommonService.getArkRoleList();
		ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>("name", "id");
		arkRoleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initArkModuleDropDown()
	{
		List<ArkModule> arkModuleList = iArkCommonService.getArkModuleList();
		ChoiceRenderer<ArkModule> defaultChoiceRenderer = new ChoiceRenderer<ArkModule>("name", "id");
		arkModuleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1917750577626157879L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initArkFunctionDropDown()
	{
		List<ArkFunction> arkFunctionList = iArkCommonService.getArkFunctionList();
		ChoiceRenderer<ArkFunction> defaultChoiceRenderer = new ChoiceRenderer<ArkFunction>("name", "id");
		arkFunctionDropDown = new DropDownChoice("arkRolePolicyTemplate.arkFunction", arkFunctionList, defaultChoiceRenderer);
		arkFunctionDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
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

	private void addDetailFormComponents()
	{
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkRoleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkModuleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkFunctionDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkCreatePermissionChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkReadPermissionChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkUpdatePermissionChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkDeletePermissionChkBox);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	@Override
	protected void attachValidators()
	{
		// Set required field here
	}
	
	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target)
	{
		if (containerForm.getModelObject().getArkRolePolicyTemplate().getId() == null)
		{
			// Save
			this.info("Ark Role Policy Template " + containerForm.getModelObject().getArkRolePolicyTemplate().getId() + " was created successfully.");
		}
		else
		{
			// Update
			this.info("Ark Role Policy Template " + containerForm.getModelObject().getArkRolePolicyTemplate().getId() + " was updated successfully.");
		}

		target.addComponent(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		// Delete
		this.info("Ark Role Policy Template " + containerForm.getModelObject().getArkRolePolicyTemplate().getId() + "was deleted successfully.");
	}

	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	protected boolean isNew()
	{
		if (containerForm.getModelObject().getArkRolePolicyTemplate().getId() == null)
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
