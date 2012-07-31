package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

/**
 * Panel for displaying the list of custom fields
 * 
 * @author cellis
 * 
 */
public class CustomFieldDisplayListPanel extends Panel {


	private static final long				serialVersionUID	= 1L;
	protected AbstractDetailModalWindow	modalWindow;
	private Panel								modalContentPanel;
	private FeedbackPanel					feedbackPanel;
	private ArkCrudContainerVO				arkCrudContainerVO;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	private Boolean							flag;

	public CustomFieldDisplayListPanel(String id, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO, Boolean flag) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.flag = flag;
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel() {

		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {

				target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
			}

		};

		add(modalWindow);
	}

	public DataView<CustomFieldDisplay> buildDataView(ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> provider) {

		DataView<CustomFieldDisplay> dataView = new DataView<CustomFieldDisplay>("customFieldDisplayList", provider) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(Item<CustomFieldDisplay> item) {

				CustomFieldDisplay cfd = item.getModelObject();

				if (cfd.getCustomField().getId() != null) {
					item.add(new Label("id", cfd.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				item.add(buildLink(item));
				

				if (cfd.getCustomField().getFieldLabel() != null) {
					item.add(new Label("fieldLabel", cfd.getCustomField().getFieldLabel()));
				}
				else {
					item.add(new Label("fieldLabel", ""));
				}
				
				if (cfd.getSequence() != null) {
					item.add(new Label("sequence", cfd.getSequence().toString()));
				}
				else {
					item.add(new Label("sequence", ""));
				}
				
				if (cfd.getRequired() != null && cfd.getRequired()) {
					item.addOrReplace(new ContextImage("required", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.addOrReplace(new ContextImage("required", new Model<String>("images/icons/cross.png")));
				}
				if(cfd.getAllowMultiselect() != null && cfd.getAllowMultiselect()){
					item.addOrReplace(new ContextImage("allowMultiselect", new Model<String>("images/icons/tick.png")));
				}
				else{
					item.addOrReplace(new ContextImage("allowMultiselect", new Model<String>("images/icons/cross.png")));
				}
			}
		};

		return dataView;
	}

	public AjaxLink<String> buildLink(final Item<CustomFieldDisplay> item) {
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("link") {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				CustomFieldDisplay customFieldDisplayToEdit = iArkCommonService.getCustomFieldDisplay(item.getModelObject().getId());
				CustomFieldGroupVO cfgVO = new CustomFieldGroupVO();
				cfgVO.setCustomFieldDisplay(customFieldDisplayToEdit);
				CompoundPropertyModel<CustomFieldGroupVO> newCpmModel = new CompoundPropertyModel<CustomFieldGroupVO>(cfgVO);
				showModalWindow(target, newCpmModel);
			}
		};

		Label linkLabel = new Label("name", item.getModelObject().getCustomField().getName());
		link.add(linkLabel);
		return link;
	}

	/**
	 * Show the modal window, with specified detail panel
	 * 
	 * @param target
	 * @param cpmModel
	 */
	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<CustomFieldGroupVO> cpmModel) {
		modalContentPanel = new CustomFieldDisplayModalPanel("content", modalWindow, cpmModel, feedbackPanel, flag);
		modalWindow.setTitle("Custom Field Display Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}
}
