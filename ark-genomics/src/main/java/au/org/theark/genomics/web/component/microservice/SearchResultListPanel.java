package au.org.theark.genomics.web.component.microservice;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.MicroServiceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.microservice.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	
	
	
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	public PageableListView<MicroService> buildPageableListView(IModel iModel) {

		PageableListView<MicroService> sitePageableListView = new PageableListView<MicroService>("microServiceList", iModel, iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<MicroService> item) {

				MicroService microService = item.getModelObject();
				
				if (microService.getId() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_ID, microService.getId().toString()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_ID, ""));
				}
				
				item.add(buildLink(microService));
				
				if (microService.getDescription() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_DESCRIPTION, microService.getDescription()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_DESCRIPTION, ""));
				}			
				
				if (microService.getServiceUrl() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_URL, microService.getServiceUrl()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_URL, ""));
				}
				
				if (microService.getStatus()!= null) {
					item.add(new Label(Constants.MICRO_SERVICE_STATUS, microService.getStatus()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_STATUS, ""));
				}
				item.add(buildStatusBtn(microService));
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final MicroService microService) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.MICRO_SERVICE_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				MicroServiceVo microServiceVo = containerForm.getModelObject();
				microServiceVo.setMode(Constants.MODE_EDIT);
				microServiceVo.setMicroService(microService);
				
				TextField microserviceNameTxtFld = (TextField)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.MICRO_SERVICE_NAME);
				TextArea microserviceUrlTxtArea = (TextArea)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.MICRO_SERVICE_URL);
				AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				
				microserviceNameTxtFld.setEnabled(false);
				microserviceUrlTxtArea.setEnabled(false);
				deleteButton.setEnabled(false);
				
				target.add(microserviceNameTxtFld);
				target.add(microserviceUrlTxtArea);
				target.add(deleteButton);
				
				
			}
		};
		
		Label nameLinkLabel = new Label("nameLbl", microService.getName());
		link.add(nameLinkLabel);
		return link;
	}
	
	private AjaxButton buildStatusBtn(final MicroService microService){
		AjaxButton statusBtn = new AjaxButton("statusBtn") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				microService.setStatus(iGenomicService.checkServiceStatus(microService));
				target.add(form);
			}
			
		};
		
		Label nameLinkLabel = new Label("statusLbl", "TEST");
		statusBtn.add(nameLinkLabel);
		
		
		return statusBtn;
	}

}
