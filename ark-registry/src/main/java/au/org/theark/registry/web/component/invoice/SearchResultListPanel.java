package au.org.theark.registry.web.component.invoice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {


	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private CompoundPropertyModel<Pipeline>					cpmModel;
	private FeedbackPanel												feedbackPanel;
	private ArkCrudContainerVO											arkCrudContainerVO;
//	private DataView<CustomFieldDisplay>								cfdDataView;
	private ArkDataProvider2<Pipeline, Pipeline>	cfdArkDataProvider;

	/**
	 * Service references
	 */
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService														iArkCommonService;

	/**
	 * 
	 * @param id
	 * @param cpModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 */
	public SearchResultListPanel(String id, CompoundPropertyModel<Pipeline> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpmModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<Pipeline> buildDataView(ArkDataProvider2<Pipeline, Pipeline> provider) {

		DataView<Pipeline> dataView = new DataView<Pipeline>("pipelineList", provider) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Pipeline> item) {

				Pipeline pipeline = item.getModelObject();

				// ID has to be NOT null if it is coming from the backend
				item.add(new Label("id", pipeline.getId().toString()));

				item.add(buildLink(item));
				// TODO Escape any special characters
				if (pipeline.getDescription() != null) {
					item.add(new Label("description", pipeline.getDescription().trim()));
				}
				else {
					item.add(new Label("description", ""));
				}
				
				// For the alternative stripes
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

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
	public WebMarkupContainer buildLink(final Item<Pipeline> item) {

		WebMarkupContainer linkWmc = new WebMarkupContainer("pipelineLinkWMC", item.getModel());

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("name") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				final Pipeline itemSelected = item.getModelObject();

				DetailPanel detailPanel = new DetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO );
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);

				TextField<String> questionnaireName = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get("pipeline.name");
				questionnaireName.setEnabled(false);

				// The list of CFD must be displayed on the Detail form
				// Create a CFD List Panel here and add it to the detailForm.
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}

		};

	//Pipeline pipeline = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", item.getModelObject().getName()); //customFieldGroup.getName());
		link.add(nameLinkLabel);
		linkWmc.add(link);
		return linkWmc;
	}

}
