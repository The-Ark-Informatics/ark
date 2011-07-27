package au.org.theark.admin.web.component.rolePolicy;

import java.util.Collection;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 5237384531161620862L;
	protected transient Logger			log					= LoggerFactory.getLogger(SearchResultsPanel.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private ContainerForm				containerForm;

	private ArkCrudContainerVO			arkCrudContainerVo;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	@SuppressWarnings("unchecked")
	public DataView<ArkRoleModuleFunctionVO> buildDataView(ArkDataProvider<ArkRoleModuleFunctionVO, IAdminService> dataProvider) {
		DataView<ArkRoleModuleFunctionVO> dataView = new DataView<ArkRoleModuleFunctionVO>("arkRoleModuleFunctionVoList", dataProvider) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7977497161071264676L;

			@Override
			protected void populateItem(final Item<ArkRoleModuleFunctionVO> item) {

				ArkRoleModuleFunctionVO arkRoleModuleFunctionVo = item.getModelObject();

				// Role name is the link
				item.add(buildLink(arkRoleModuleFunctionVo));

				if (arkRoleModuleFunctionVo.getArkModule() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRoleModuleFunctionVo.arkModule", arkRoleModuleFunctionVo.getArkModule().getName()));
				}
				else {
					item.add(new Label("arkRoleModuleFunctionVo.arkModule", ""));
				}

				if (arkRoleModuleFunctionVo.getArkFunction() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRoleModuleFunctionVo.arkFunction", arkRoleModuleFunctionVo.getArkFunction().getName()));
				}
				else {
					item.add(new Label("arkRoleModuleFunctionVo.arkFunction", ""));
				}

				try {
					Collection<String> roleFunctionPermissions = iArkCommonService.getArkRolePermission(arkRoleModuleFunctionVo.getArkFunction(), arkRoleModuleFunctionVo.getArkRole().getName(),
							arkRoleModuleFunctionVo.getArkModule());
					if (roleFunctionPermissions.contains("CREATE")) {
						item.addOrReplace(new ContextImage("arkCreatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkCreatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (roleFunctionPermissions.contains("READ")) {
						item.addOrReplace(new ContextImage("arkReadPermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkReadPermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (roleFunctionPermissions.contains("UPDATE")) {
						item.addOrReplace(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (roleFunctionPermissions.contains("DELETE")) {
						item.addOrReplace(new ContextImage("arkDeletePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkDeletePermission", new Model<String>("images/icons/cross.png")));
					}
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return dataView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final ArkRoleModuleFunctionVO arkRoleModuleFunctionVo) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				containerForm.getModelObject().setArkRole(arkRoleModuleFunctionVo.getArkRole());
				containerForm.getModelObject().setArkModule(arkRoleModuleFunctionVo.getArkModule());
				containerForm.getModelObject().setArkFunction(arkRoleModuleFunctionVo.getArkFunction());

				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVo.getViewButtonContainer().setVisible(true);
				arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVo.getEditButtonContainer().setVisible(false);

				// Get all permission rows, and assign to VO accordingly
				try {
					Collection<String> arkPermissions = iArkCommonService.getArkRolePermission(arkRoleModuleFunctionVo.getArkFunction(), arkRoleModuleFunctionVo.getArkRole().getName(),
							arkRoleModuleFunctionVo.getArkModule());
					// Set up boolean references in VO
					containerForm.getModelObject().setArkCreatePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.CREATE));
					containerForm.getModelObject().setArkReadPermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.READ));
					containerForm.getModelObject().setArkUpdatePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.UPDATE));
					containerForm.getModelObject().setArkDeletePermission(arkPermissions.contains(au.org.theark.core.security.PermissionConstants.DELETE));
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}

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
		};

		// Add the label for the link
		Label linkLabel;
		if(arkRoleModuleFunctionVo.getArkRole() != null) {
			linkLabel = new Label("arkRoleModuleFunctionVo.arkRole", arkRoleModuleFunctionVo.getArkRole().getName().toString());
		}
		else {
			linkLabel = new Label("arkRoleModuleFunctionVo.arkRole", "-");
		}
		link.add(linkLabel);
		return link;
	}
}