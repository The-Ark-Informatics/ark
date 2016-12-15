package au.org.theark.genomics.web.component.datacenter.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class SourceForm extends AbstractDetailForm<DataSourceVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private TextField<String> dataSourceIdTxtFld;
	private TextField<String> dataSourceServiceTxtFld;
	private TextField<String> dataSourceNameTxtFld;
	private TextArea<String> dataSourceDescTxtFld;
	private TextField<String> dataSourcePathTxtFld;
	private TextField<String> dataSourceDataCenterTxtFld;
	private DropDownChoice<DataSourceType> dataSourceTypesDDC;
	private TextField<String> dataSourceStatusTxtFld;

	private AbstractDetailModalWindow modalWindow;

	private List<DataSourceType> dataSourceTypeList;

	private CompoundPropertyModel<DataSourceVo> cpmModel;

	public SourceForm(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<DataSourceVo> cpModel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		setOutputMarkupId(true);
		this.modalWindow = modalWindow;
		this.cpmModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		// TODO Auto-generated constructor stub
	}

	public void initialiseSourceForm() {

		PropertyModel<DataSourceType> pm = new PropertyModel<DataSourceType>(cpmModel, "dataSource.type");

		dataSourceIdTxtFld = new TextField<String>(Constants.DATA_SOURCE_ID);
		dataSourceIdTxtFld.setEnabled(false);

		dataSourceServiceTxtFld = new TextField<String>(Constants.DATA_SOURCE_MICRO_SERVICE_NAME);
		dataSourceServiceTxtFld.setEnabled(false);

		dataSourceNameTxtFld = new TextField<String>(Constants.DATA_SOURCE_NAME);
		dataSourceNameTxtFld.setEnabled(false);

		dataSourceDescTxtFld = new TextArea<String>(Constants.DATA_SOURCE_DESCRIPTION);

		dataSourcePathTxtFld = new TextField<String>(Constants.DATA_SOURCE_PATH);
		dataSourcePathTxtFld.setEnabled(false);

		dataSourceDataCenterTxtFld = new TextField<String>(Constants.DATA_SOURCE_DATA_CENTER);
		dataSourceDataCenterTxtFld.setEnabled(false);

		dataSourceTypeList = iGenomicService.listDataSourceTypes();
		initDataSourceTypesDDC(pm);

		dataSourceStatusTxtFld = new TextField<String>(Constants.DATA_SOURCE_STATUS);
		dataSourceStatusTxtFld.setEnabled(false);

		addDetailFormComponents();
	}

	private void initDataSourceTypesDDC(PropertyModel<DataSourceType> dataSourceType) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.dataSourceTypesDDC = new DropDownChoice(Constants.DATA_SOURCE_TYPE, dataSourceType, this.dataSourceTypeList, defaultChoiceRenderer);
	}

	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		modalWindow.close(target);
	}

	@Override
	protected void onSave(Form<DataSourceVo> containerForm, AjaxRequestTarget target) {
		if (cpModel.getObject().getDataSource().getId() == null) {
			iGenomicService.saveOrUpdate(cpModel.getObject().getDataSource());
			this.info("Data source was created successfully");
		} else {
			iGenomicService.saveOrUpdate(cpModel.getObject().getDataSource());
			this.info("Data source was updated successfully");
		}

		processErrors(target);

		AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
		deleteButton.setEnabled(true);

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
		target.add(feedBackPanel);

	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iGenomicService.refreshDataSource(cpModel.getObject().getDataSource());
		String status = cpModel.getObject().getDataSource().getStatus();

		if (status != null && !(status.toLowerCase().startsWith("pro") || status.toLowerCase().startsWith("ready"))) {
			int count = iGenomicService.getDataSourceCount(cpModel.getObject().getDataSource().getId());
			if (count == 0) {
				iGenomicService.delete(cpModel.getObject().getDataSource());
				modalWindow.close(target);
			} else {
				this.error("Data Source is already attached to an analysis");
				target.add(feedBackPanel);
			}
		} else {
			this.error("Cannot delete online data source");
			target.add(feedBackPanel);
		}
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getDataSource().getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceServiceTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceDescTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourcePathTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceDataCenterTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceTypesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourceStatusTxtFld);
	}

}
