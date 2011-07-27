package au.org.theark.admin.web.component.rolePolicy.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<AdminVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -204010204180506704L;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>				iAdminService;

	private CompoundPropertyModel<AdminVO>	cpmModel;
	private ArkCrudContainerVO					arkCrudContainerVo;
	private ContainerForm						containerForm;
	private FeedbackPanel						feedbackPanel;
	private DropDownChoice<ArkRole>			arkRoleDropDown;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param ArkCrudContainerVO
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVo, FeedbackPanel feedbackPanel, ContainerForm containerForm) {
		super(id, cpmModel, feedbackPanel, arkCrudContainerVo);

		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);

		this.setCpmModel(cpmModel);

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected void initialiseSearchForm() {
		// Role selection
		initArkRoleDropDown();
	}

	@SuppressWarnings("unchecked")
	private void initArkRoleDropDown() {
		List<ArkRole> arkRoleList = iAdminService.getArkRoleList();
		ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>("name", "id");
		arkRoleDropDown = new DropDownChoice("arkRoleModuleFunctionVo.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		int count = iAdminService.getArkRoleModuleFunctionVOCount(containerForm.getModelObject().getArkRoleModuleFunctionVo());
		if (count == 0) {
			this.info("There are no records that matched your query. Please modify your filter");
			target.addComponent(feedbackPanel);
		}

		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(true);
		target.addComponent(arkCrudContainerVo.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm() {
		add(arkRoleDropDown);
	}

	protected void onNew(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		containerForm.setModelObject(new AdminVO());
		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVo.getViewButtonContainer().setVisible(true);
		arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
		arkCrudContainerVo.getEditButtonContainer().setVisible(false);

		// Refresh the markup containers
		target.addComponent(arkCrudContainerVo.getSearchResultPanelContainer());
		target.addComponent(arkCrudContainerVo.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVo.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVo.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVo.getViewButtonContainer());
		target.addComponent(arkCrudContainerVo.getEditButtonContainer());

		// Refresh base container form to remove any feedBack messages
		target.addComponent(containerForm);
	}

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<AdminVO> cpmModel) {
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<AdminVO> getCpmModel() {
		return cpmModel;
	}
}