package au.org.theark.disease.web.component.gene.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.disease.vo.GeneVO;

public class SearchForm extends AbstractSearchForm<GeneVO> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(SearchForm.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private CompoundPropertyModel<GeneVO> cpModel;
	
	private WebMarkupContainer arkContextMarkup;
	
	private TextField<String> nameTextFld;
	
	public SearchForm(String id, CompoundPropertyModel<GeneVO> cpModel, PageableListView<GeneVO> listView, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, WebMarkupContainer arkContextMarkup) {
		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		this.cpModel = cpModel;
		this.arkContextMarkup = arkContextMarkup;
		
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study selected. Please select a study.");
	}
	
	
	protected void initialiseSearchForm() {
		nameTextFld = new TextField<String>("gene.name");
	}
	
	protected void addSearchComponentsToForm() {
		add(nameTextFld);
	}
	
	protected void onNew(AjaxRequestTarget target) {
		preProcessDetailPanel(target);
	}
	
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
}
