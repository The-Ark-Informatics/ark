package au.org.theark.genomics.web.component.datacenter;

import java.util.List;

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

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;

public class DataSourceResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ContainerForm containerForm;

	private ArkCrudContainerVO arkCrudContainerVO;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private PageableListView<DataSourceVo> sitePageableListView;
	
	public DataSourceResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}
	
	public PageableListView<DataSourceVo> buildPageableListView(IModel iModel) {
		this.sitePageableListView = new PageableListView<DataSourceVo>("dataSourceList", iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<DataSourceVo> item) {
				DataSourceVo dataSource = item.getModelObject();
			
				item.add(buildNameLink(dataSource));
				
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

//				item.add(buildSourceLink(dataSource));
				
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
	private AjaxLink buildNameLink(final DataSourceVo dataSourceVo) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.DATA_SOURCE_VO_FILE_NAME) {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub

				DataCenterVo dataCenterVo = containerForm.getModelObject();
				
				
				
			}
			
			@Override
			public boolean isEnabled() {
				return "Yes".equalsIgnoreCase(dataSourceVo.getDirectory());
			}
		};

		Label nameLinkLabel = new Label("nameLbl", dataSourceVo.getFileName());
		link.add(nameLinkLabel);
		return link;
	}	

}
