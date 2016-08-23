package au.org.theark.genomics.web.component.analysis.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.genomics.model.vo.AnalysisVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class SearchForm extends AbstractSearchForm<AnalysisVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	
	private TextField<String>				analysisIdTxtFld;
	private TextField<String>				analysisNameTxtFld;

	
	private PageableListView<Analysis>	listView;
	
	private CompoundPropertyModel<Analysis>	cpmModel;

	public SearchForm(String id, CompoundPropertyModel<AnalysisVo> computation, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Analysis> listView) {

		super(id, computation, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
	}
	
	protected void initialiseSearchForm() {
		analysisIdTxtFld = new TextField<String>(Constants.ANALYIS_ID);
		analysisNameTxtFld = new TextField<String>(Constants.ANALYIS_NAME);
	}
	
	protected void addSearchComponentsToForm() {
		add(analysisIdTxtFld);
		add(analysisNameTxtFld);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<Analysis> resultList = iGenomicService.searchAnalysis(getModelObject().getAnalysis(), studyId);
		if (resultList != null && resultList.size() == 0) {
			this.info("Analysis with the specified criteria does not exist in the system.");
			target.add(feedbackPanel);
		}
		getModelObject().setAnalysisList(resultList);
		listView.removeAll();
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
//		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getAnalysis().setId(null);
		getModelObject().getAnalysis().setStatus(Constants.STATUS_UNDEFINED);
		
		DropDownChoice<DataSource> dataSourceDDC= (DropDownChoice)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_DATA_SOURCE);
		DropDownChoice<Computation> computationDDC= (DropDownChoice)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.ANALYIS_COMPUTAION);
		if(dataSourceDDC !=null){
			dataSourceDDC.setChoices(new ArrayList<DataSource>());
		}
		
		if(computationDDC !=null){
			computationDDC.setChoices(new ArrayList<Computation>());
		}
		
		preProcessDetailPanel(target);
	}
}
