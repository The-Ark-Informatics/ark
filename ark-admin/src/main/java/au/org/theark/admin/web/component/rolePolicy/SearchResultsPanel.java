package au.org.theark.admin.web.component.rolePolicy;

import java.util.Collection;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
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

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>		iAdminService;

	private ContainerForm				containerForm;

	private ArkCrudContainerVO			arkCrudContainerVo;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	@SuppressWarnings("unchecked")
	public DataView<ArkRolePolicyTemplate> buildDataView(ArkDataProvider<ArkRolePolicyTemplate, IAdminService> dataProvider) {
		DataView<ArkRolePolicyTemplate> dataView = new DataView<ArkRolePolicyTemplate>("arkRolePolicyTemplateList", dataProvider) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7977497161071264676L;

			@Override
			protected void populateItem(final Item<ArkRolePolicyTemplate> item) {

				ArkRolePolicyTemplate arkRolePolicyTemplate = item.getModelObject();

				// Role name is the link
				item.add(buildLink(arkRolePolicyTemplate));

				if (arkRolePolicyTemplate.getArkModule() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkModule", arkRolePolicyTemplate.getArkModule().getName()));
				}
				else {
					item.add(new Label("arkRolePolicyTemplate.arkModule", ""));
				}

				if (arkRolePolicyTemplate.getArkFunction() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkFunction", arkRolePolicyTemplate.getArkFunction().getName()));
				}
				else {
					item.add(new Label("arkRolePolicyTemplate.arkFunction", ""));
				}

				if (arkRolePolicyTemplate.getArkPermission() != null) {
					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("CREATE")) {
						item.add(new ContextImage("arkCreatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkCreatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("READ")) {
						item.add(new ContextImage("arkReadPermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkReadPermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("UPDATE")) {
						item.add(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("DELETE")) {
						item.add(new ContextImage("arkDeletePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkDeletePermission", new Model<String>("images/icons/cross.png")));
					}
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

	@SuppressWarnings("unchecked")
	public PageableListView<ArkRolePolicyTemplate> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		PageableListView<ArkRolePolicyTemplate> pageableListView = new PageableListView<ArkRolePolicyTemplate>("arkRolePolicyTemplateList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3350183112731574263L;

			@Override
			protected void populateItem(final ListItem<ArkRolePolicyTemplate> item) {

				ArkRolePolicyTemplate arkRolePolicyTemplate = item.getModelObject();

				// Role name is the link
				item.add(buildLink(arkRolePolicyTemplate));

				if (arkRolePolicyTemplate.getArkModule() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkModule", arkRolePolicyTemplate.getArkModule().getName()));
				}
				else {
					item.add(new Label("arkRolePolicyTemplate.arkModule", ""));
				}

				if (arkRolePolicyTemplate.getArkFunction() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkRolePolicyTemplate.arkFunction", arkRolePolicyTemplate.getArkFunction().getName()));
				}
				else {
					item.add(new Label("arkRolePolicyTemplate.arkFunction", ""));
				}

				if (arkRolePolicyTemplate.getArkPermission() != null) {
					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("CREATE")) {
						item.add(new ContextImage("arkCreatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkCreatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("READ")) {
						item.add(new ContextImage("arkReadPermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkReadPermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("UPDATE")) {
						item.add(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (arkRolePolicyTemplate.getArkPermission().getName().equalsIgnoreCase("DELETE")) {
						item.add(new ContextImage("arkDeletePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.add(new ContextImage("arkDeletePermission", new Model<String>("images/icons/cross.png")));
					}
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
		return pageableListView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final ArkRolePolicyTemplate arkRolePolicyTemplate) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = arkRolePolicyTemplate.getId();
				ArkRolePolicyTemplate arkRolePolicyTemplate = iAdminService.getArkRolePolicyTemplate(id);
				containerForm.getModelObject().setArkRolePolicyTemplate(arkRolePolicyTemplate);
				containerForm.getModelObject().setArkRole(arkRolePolicyTemplate.getArkRole());

				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVo.getViewButtonContainer().setVisible(true);
				arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVo.getEditButtonContainer().setVisible(false);

				// Get all permission rows, and assign to VO accordingly
				try {
					Collection<String> arkPermissions = iArkCommonService.getArkRolePermission(arkRolePolicyTemplate.getArkFunction(), arkRolePolicyTemplate.getArkRole().getName(), arkRolePolicyTemplate
							.getArkModule());
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
		Label linkLabel = new Label("arkRolePolicyTemplate.arkRole", arkRolePolicyTemplate.getArkRole().getName().toString());
		link.add(linkLabel);
		return link;
	}
}