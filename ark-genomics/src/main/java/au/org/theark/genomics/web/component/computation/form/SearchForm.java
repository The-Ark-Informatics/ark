package au.org.theark.genomics.web.component.computation.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.genomics.model.vo.ComputationVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class SearchForm extends AbstractSearchForm<ComputationVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	
	private TextField<String>				computationIdTxtFld;
	private TextField<String>				computationNameTxtFld;

	
	private PageableListView<Computation>	listView;
	
	private CompoundPropertyModel<Computation>	cpmModel;


	public SearchForm(String id, CompoundPropertyModel<ComputationVo> computation, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Computation> listView) {

		super(id, computation, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		
		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study selected. Please select a study.");
	}
	
	protected void initialiseSearchForm() {
		computationIdTxtFld = new TextField<String>(Constants.COMPUTATION_ID);
		computationNameTxtFld = new TextField<String>(Constants.COMPUTATION_NAME);
	}
	
	protected void addSearchComponentsToForm() {
		add(computationIdTxtFld);
		add(computationNameTxtFld);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
//		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		getModelObject().getMicroService().setStudyId(studyId);
		List<Computation> resultList = iGenomicService.searchComputations(getModelObject().getComputation());;
//		if (resultList != null && resultList.size() == 0) {
//			this.info("Micro service with the specified criteria does not exist in the system.");
//			target.add(feedbackPanel);
//		}
		getModelObject().setComputationList(resultList);
		listView.removeAll();
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
//		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getComputation().setId(null);
		preProcessDetailPanel(target);
	}
}
