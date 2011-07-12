package au.org.theark.admin.web.component.rolePolicy.form;

import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Hibernate;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.web.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.web.form.AbstractDetailForm;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class DetailForm extends AbstractDetailForm<AdminVO>
{
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private int						mode;
	private TextField<String>	idTxtFld;
	private DropDownChoice		arkRoleDropDown;
	private DropDownChoice		arkModuleDropDown;
	private DropDownChoice		arkFunctionDropDown;
	private CheckBox				arkCreatePermissionChkBox;
	private CheckBox				arkReadPermissionChkBox;
	private CheckBox				arkUpdatePermissionChkBox;
	private CheckBox				arkDeletePermissionChkBox;

	/*
	 * ID int(11) PK ARK_ROLE_ID int(11) ARK_MODULE_ID int(11) ARK_FUNCTION_ID int(11) ARK_PERMISSION_ID int(11)
	 */

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO)
	{
		super(id, feedbackPanel, containerForm, arkCrudContainerVO);
		this.containerForm = containerForm;
		setMultiPart(true);
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
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
		addComponents();
	}

	private void initArkRoleDropDown()
	{
		List<ArkRole> arkRoleList = iArkCommonService.getArkRoleList();
		ChoiceRenderer<StudyStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyStatus>("name", "id");
		arkRoleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	private void initArkModuleDropDown()
	{
		List<ArkModule> arkModuleList = iArkCommonService.getArkModuleList();
		ChoiceRenderer<SubjectUidToken> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidToken>("name", "id");
		arkModuleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	private void initArkFunctionDropDown()
	{
		List<ArkFunction> arkFunctionList = iArkCommonService.getArkFunctionList();
		ChoiceRenderer<SubjectUidPadChar> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidPadChar>("name", "id");
		arkFunctionDropDown = new DropDownChoice("arkRolePolicyTemplate.arkFunction", arkFunctionList, defaultChoiceRenderer);
		arkFunctionDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{

			}
		});
	}

	private void addComponents()
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
		
	}

	@Override
	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target)
	{
		if (containerForm.getModelObject().getArkRolePolicyTemplate().getId() == null)
		{
			// Save
			this.info("Ark Role Policy Template " + containerForm.getModelObject().getArkRolePolicyTemplate().getId() + "created successfully.");
		}
		else
		{
			// Update
			this.info("Ark Role Policy Template " + containerForm.getModelObject().getArkRolePolicyTemplate().getId() + "updated successfully.");
		}
		
		target.addComponent(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		containerForm.setModelObject(new AdminVO());
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractArchiveDetailForm#isNew()
	 */
	@Override
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

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		// TODO Auto-generated method stub
	}
}
