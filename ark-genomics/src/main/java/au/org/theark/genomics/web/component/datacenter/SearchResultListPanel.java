package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.button.ArkAjaxButton;
import au.org.theark.genomics.jobs.DataSourceUploadExecutor;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ContainerForm containerForm;

	private ArkCrudContainerVO arkCrudContainerVO;

	private AbstractDetailModalWindow modalWindow;

	private DataSourceContainerPanel dataSourceContainerPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private AjaxButton plinkUploadBtn;

	private AjaxButton plinkDeleteBtn;

	private Label statusLabel;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
		initializeComponents();
		addComponents();
	}

	protected void initializeComponents() {

		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {

			}
		};

		plinkUploadBtn = new AjaxButton("plinkUpload") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				try {
					DataCenterVo dataCenter = containerForm.getModelObject();
					dataCenter.setStatus("Uploading");

					String processUID = iGenomicService.executeDataSourceUpload(dataCenter);

					DataSourceVo dataSourceVo = new DataSourceVo();
					dataSourceVo.setDataCenter(dataCenter.getName());
					dataSourceVo.setMicroService(dataCenter.getMicroService());

					String dir = dataCenter.getDirectory();
					dataSourceVo.setPath(dir == null ? "/" : dir.charAt(0) == '/' ? dir : ("/" + dir));

					String[] dirList = dir.split("/");
					if (dirList.length > 0) {
						dataSourceVo.setFileName(dirList[dirList.length - 1]);
					}

					DataSource dataSource = iGenomicService.getDataSource(dataSourceVo);

					if (dataSource == null) {
						dataSourceVo.pupulateDataSource();
						dataSource = dataSourceVo.getDataSource();
					}
					
					dataSource.setStatus("Uploading");

					iGenomicService.saveOrUpdate(dataSource);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService);
					executor.run();
					
					this.setEnabled(false);
					plinkDeleteBtn.setEnabled(false);

					target.add(SearchResultListPanel.this);
				} catch (Exception e) {

				}

			}

		};

		plinkUploadBtn.setEnabled(false);

		plinkDeleteBtn = new AjaxButton("plinkDelete") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try{
					DataCenterVo dataCenter = containerForm.getModelObject();
					dataCenter.setStatus("Deleting");

					String processUID = iGenomicService.executeDataSourceUpload(dataCenter);

					DataSourceVo dataSourceVo = new DataSourceVo();
					dataSourceVo.setDataCenter(dataCenter.getName());
					dataSourceVo.setMicroService(dataCenter.getMicroService());
					// dataSourceVo.setFileName(cpmModel.getObject().getName());

					String dir = dataCenter.getDirectory();
					dataSourceVo.setPath(dir == null ? "/" : dir.charAt(0) == '/' ? dir : ("/" + dir));

					String[] dirList = dir.split("/");
					if (dirList.length > 0) {
						dataSourceVo.setFileName(dirList[dirList.length - 1]);
					}

					DataSource dataSource = iGenomicService.getDataSource(dataSourceVo);
					dataSource.setStatus("Deleting");
			

					iGenomicService.saveOrUpdate(dataSource);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService);
					executor.run();
					
					this.setEnabled(false);

					target.add(SearchResultListPanel.this);
					
				}catch(Exception e){
					
				}
			}
		};

		plinkDeleteBtn.setEnabled(false);

		statusLabel = new Label("status");

	}

	protected void addComponents() {
		add(modalWindow);
		add(plinkUploadBtn);
		add(plinkDeleteBtn);
		add(statusLabel);
	}

	public PageableListView<DataSourceVo> buildPageableListView(IModel iModel) {
		PageableListView<DataSourceVo> sitePageableListView = new PageableListView<DataSourceVo>("dataSourceList", iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<DataSourceVo> item) {
				DataSourceVo dataSource = item.getModelObject();

				if (dataSource.getFileName() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_FILE_NAME, dataSource.getFileName()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_FILE_NAME, ""));
				}
				if (dataSource.getDirectory() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, dataSource.getDirectory()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, ""));
				}
				if (dataSource.getPath() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, dataSource.getPath()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, ""));
				}
				if (dataSource.getDataSource() != null && dataSource.getDataSource().getStatus() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, dataSource.getDataSource().getStatus()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, ""));
				}

				item.add(buildSourceLink(dataSource));
				// item.add(buildUploadLink(dataSource));
				// item.add(buildDeleteLink(dataSource));

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		return sitePageableListView;
	}

	@SuppressWarnings({ "serial" })
	private ArkAjaxButton buildSourceLink(final DataSourceVo dataSourceVo) {

		ArkAjaxButton link = new ArkAjaxButton("source") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onSubmit(target, form);
				DataSource dataSource = dataSourceVo.getDataSource();

				dataSourceContainerPanel = new DataSourceContainerPanel("content", modalWindow);

				if (dataSource.getId() != null) {
					dataSourceContainerPanel.enableDeleteButton(true);
				} else {
					dataSourceContainerPanel.enableDeleteButton(false);
				}

				modalWindow.setTitle("Source");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				dataSourceContainerPanel.setPropertyModelObject(dataSourceVo);
				modalWindow.setContent(dataSourceContainerPanel);
				modalWindow.show(target);

			}
		};

		return link;
	}

	@SuppressWarnings({ "serial" })
	private ArkAjaxButton buildUploadLink(final DataSourceVo dataSourceVo) {

		final DataSource dataSource = dataSourceVo.getDataSource();

		ArkAjaxButton link = new ArkAjaxButton("upload") {

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {

					dataSource.setStatus("Uploading");
					iGenomicService.saveOrUpdate(dataSource);

					dataSourceVo.setStatus("Uploading");

					String processUID = iGenomicService.executeDataSourceUpload(dataSourceVo);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService);
					executor.run();
					target.add(SearchResultListPanel.this);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		if (!(dataSource.getStatus() == null || dataSource.getStatus().trim().length() == 0 || "Deleted".equalsIgnoreCase(dataSource.getStatus()))) {
			link.setVisible(false);
		}

		// Label nameLinkLabel = new Label("uploadLbl", "Upload");
		// link.add(nameLinkLabel);
		return link;
	}

	@SuppressWarnings({ "serial" })
	private ArkAjaxButton buildDeleteLink(final DataSourceVo dataSourceVo) {

		final DataSource dataSource = dataSourceVo.getDataSource();

		ArkAjaxButton link = new ArkAjaxButton("delete") {

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {

				try {
					dataSource.setStatus("Deleting");
					iGenomicService.saveOrUpdate(dataSource);

					dataSourceVo.setStatus("Deleting");

					String processUID = iGenomicService.executeDataSourceUpload(dataSourceVo);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService);
					executor.run();
					target.add(SearchResultListPanel.this);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		if (dataSource == null || !"Completed".equalsIgnoreCase(dataSource.getStatus())) {
			link.setVisible(false);
		}

		return link;
	}

}
