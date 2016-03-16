package au.org.theark.genomics.web.component.computation;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.ComputationVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.computation.form.ContainerForm;

public class SearchResultListPanel extends Panel {
	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	public PageableListView<Computation> buildPageableListView(IModel iModel) {

		PageableListView<Computation> sitePageableListView = new PageableListView<Computation>("computationList", iModel, iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Computation> item) {

				Computation computation = item.getModelObject();
				
				if (computation.getId() != null) {
					item.add(new Label(Constants.COMPUTATION_ID, computation.getId().toString()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_ID, ""));
				}
				
				item.add(buildLink(computation));
				
				if (computation.getMicroService() != null) {
					item.add(new Label(Constants.COMPUTATION_MICROSERVICE, computation.getMicroService().getName()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_MICROSERVICE, ""));
				}			
				
				if (computation.getStatus()!= null) {
					item.add(new Label(Constants.COMPUTATION_STATUS, computation.getStatus()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_STATUS, ""));
				}
				
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
	private AjaxLink buildLink(final Computation computation) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.COMPUTATION_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				ComputationVo computationVo = containerForm.getModelObject();
//				computationVo.setMode(Constants.MODE_EDIT);
				computationVo.setComputation(computation);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		
		Label nameLinkLabel = new Label("nameLbl", computation.getName());
		link.add(nameLinkLabel);
		return link;
	}
}
