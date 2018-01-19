package au.org.theark.genomics.web.component.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.jobs.CompilationExecutor;
import au.org.theark.genomics.jobs.QueueExecutor;
import au.org.theark.genomics.model.vo.AnalysisVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.analysis.form.ContainerForm;

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

	public PageableListView<Analysis> buildPageableListView(IModel iModel) {

		PageableListView<Analysis> sitePageableListView = new PageableListView<Analysis>("analysisList", iModel, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Analysis> item) {

				Analysis analysis = item.getModelObject();
				
				if (analysis.getId() != null) {
					item.add(new Label(Constants.ANALYIS_ID, analysis.getId().toString()));
				}
				else {
					item.add(new Label(Constants.ANALYIS_ID, ""));
				}
				
				item.add(buildLink(analysis));
				
				if (analysis.getMicroService()!= null) {
					item.add(new Label(Constants.ANALYIS_MICRO_SERVICE, analysis.getMicroService().getName()));
				}
				else {
					item.add(new Label(Constants.ANALYIS_MICRO_SERVICE, ""));
				}
				if (analysis.getDataSource()!= null) {
					item.add(new Label(Constants.ANALYIS_DATA_SOURCE, analysis.getDataSource().getName()));
				}
				else {
					item.add(new Label(Constants.ANALYIS_DATA_SOURCE, ""));
				}
				if (analysis.getComputation()!= null) {
					item.add(new Label(Constants.ANALYIS_COMPUTAION, analysis.getComputation().getName()));
				}
				else {
					item.add(new Label(Constants.ANALYIS_COMPUTAION, ""));
				}
				
				if (analysis.getStatus()!= null) {
					item.add(new Label(Constants.ANALYIS_STATUS, analysis.getStatus()));
				}
				else {
					item.add(new Label(Constants.ANALYIS_STATUS, ""));
				}
				
				item.add(buildRunButton(analysis));
				item.add(buildDownloadButton(analysis));
				
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
	private AjaxLink buildLink(final Analysis analysis) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.ANALYIS_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				DropDownChoice<Computation> microserviceDDC= (DropDownChoice)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_MICRO_SERVICE);
				DropDownChoice<DataSource> dataSourceDDC= (DropDownChoice)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_DATA_SOURCE);
				DropDownChoice<Computation> computationDDC= (DropDownChoice)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_COMPUTAION);
				
				TextField resultsTxtFld = (TextField)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_RESULT);
				
				if(dataSourceDDC !=null){
					List<DataSource> dataSources = iGenomicService.searchDataSources(analysis.getMicroService());
					dataSourceDDC.setChoices(dataSources);
				}
				
				if(computationDDC !=null){
					List<Computation> computations = iGenomicService.searchComputation(analysis.getMicroService());
					computationDDC.setChoices(computations);
				}
				
				
				if(!Constants.STATUS_UNDEFINED.equals(analysis.getStatus())){
					microserviceDDC.setEnabled(false);
					dataSourceDDC.setEnabled(false);
					computationDDC.setEnabled(false);
					resultsTxtFld.setEnabled(false);
				}
				
				AnalysisVo analysisVo = containerForm.getModelObject();
//				computationVo.setMode(Constants.MODE_EDIT);
				analysisVo.setAnalysis(analysis);
						
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		
		
		
		Label nameLinkLabel = new Label("nameLbl", analysis.getName());
		link.add(nameLinkLabel);
		return link;
	}
	
	private AjaxButton buildRunButton(final Analysis analysis) {
		AjaxButton ajaxButton = new AjaxButton("runAnalysis") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {


					iGenomicService.submitToQueue(analysis);

					QueueExecutor executor = new QueueExecutor(analysis, iGenomicService);

					executor.run();

				} catch (Exception e) {
					this.error("An unexpected error occurred. Analysis execution failed.");
					e.printStackTrace();
				}

//				target.add(analysisStatusLbl);
//				target.add(analysisJobIdLbl);
//				target.add(feedBackPanel);
//				target.add(queueButton);
//				target.add(resultButton);
				target.add(this);
			}

			@Override
			public boolean isEnabled() {
				boolean enabled = false;
				if (analysis.getId() != null && Constants.STATUS_UNDEFINED.equalsIgnoreCase(analysis.getStatus())) {
					enabled = true;
				}
				return enabled;
			}
			
			
		};
		
		ajaxButton.setOutputMarkupId(true);
		
		return ajaxButton;
	}
	
	private AjaxButton buildDownloadButton(final Analysis analysis) {
		AjaxButton ajaxButton = new AjaxButton("downloadResults") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					byte[] data = iGenomicService.getAnalysisResult(analysis);
					String output = null;
					if (analysis.getResult() == null) {
						output = "" + analysis.getId() + "results.txt";
					} else {
						output = analysis.getResult();
					}
					getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, output));
				} catch (Exception e) {
					this.error("An unexpected error occurred. Results download failed.");
					e.printStackTrace();
				}
//				target.add(feedBackPanel);
				target.add(this);
			}

			@Override
			public boolean isEnabled() {
				boolean enabled = false;
//				Analysis analysis = getFormModelObject().getAnalysis();
				if (Constants.STATUS_COMPLETED.equalsIgnoreCase(analysis.getStatus())) {
					enabled = true;
				}
				return enabled;
			}
			
		};
		
		ajaxButton.setOutputMarkupId(true);
		
		return ajaxButton;
	}
}
