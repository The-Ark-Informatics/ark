package au.org.theark.core.web.component.customfield;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);

	private CompoundPropertyModel<CustomFieldVO>			cpModel;

	private FeedbackPanel			feedbackPanel;
	private ArkCrudContainerVO		arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	public SearchResultListPanel(String id, CompoundPropertyModel<CustomFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);

		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<CustomField> buildDataView(ArkDataProvider<CustomField, IArkCommonService> subjectProvider) {

		DataView<CustomField> studyCompDataView = new DataView<CustomField>("customFieldList", subjectProvider) {

			@Override
			protected void populateItem(final Item<CustomField> item) {
				CustomField field = item.getModelObject();

				/* The Field ID */
				if (field.getId() != null) {
					// Add the id component here
					item.add(new Label(Constants.CUSTOMFIELD_ID, field.getId().toString()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_ID, ""));
				}

				// Component Name Link
				item.add(buildLinkWMC(item));

				// TODO when displaying text escape any special characters
				// Field Type
				if (field.getFieldType() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, field.getFieldType().getName()));// the ID here
					// must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (field.getDescription() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_DESCRIPTION, field.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Units
				if (field.getUnitType() != null && field.getUnitType().getName() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_UNIT_TYPE, field.getUnitType().getName()));// the ID here must match the ones in
					// mark-up
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_UNIT_TYPE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Min
				if (field.getMinValue() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_MIN_VALUE, field.getMinValue()));// the ID here must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_MIN_VALUE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Max
				if (field.getMinValue() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_MAX_VALUE, field.getMaxValue()));// the ID here must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_MAX_VALUE, ""));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return studyCompDataView;
	}

	private WebMarkupContainer buildLinkWMC(final Item<CustomField> item) {

		WebMarkupContainer customfieldLinkWMC = new WebMarkupContainer("customfieldLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.CUSTOMFIELD_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				CustomField cf = (CustomField) (getParent().getDefaultModelObject());
//				cpModel.getObject().setCustomField(cf);
				CompoundPropertyModel<CustomFieldVO> newModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
				newModel.getObject().setCustomField(cf);
				newModel.getObject().setUseCustomFieldDisplay(cpModel.getObject().isUseCustomFieldDisplay());

				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);

				// Button containers
				// View Field, thus view container visible
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);

				DetailPanel detailsPanel = new DetailPanel("detailsPanel", feedbackPanel, newModel, arkCrudContainerVO);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailsPanel);
				target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
				target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
				target.addComponent(arkCrudContainerVO.getViewButtonContainer());
				target.addComponent(arkCrudContainerVO.getEditButtonContainer());
				target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
				
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		CustomField field = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		customfieldLinkWMC.add(link);
		return customfieldLinkWMC;
	}
}