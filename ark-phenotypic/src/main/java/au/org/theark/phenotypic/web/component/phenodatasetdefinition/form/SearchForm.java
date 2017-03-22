package au.org.theark.phenotypic.web.component.phenodatasetdefinition.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatasetdefinition.DataDictionaryGroupDetailPanel;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<PhenoDataSetFieldGroupVO>{


	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;
	
	private ArkCrudContainerVO	arkCrudContainerVO;
	private TextField<String> groupNameTxtFld;
	//private CheckBox publishedStatusCb;	
	private DropDownChoice<Boolean>  publishedStatusDdc;
	private ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay> cfdArkDataProvider;
	private Study study;
	//private Label generalTextLbl; 
	/**
	 * @param id1
	 * @param cpmModel
	 */
	public SearchForm(String id,CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		CompoundPropertyModel<PhenoDataSetFieldGroupVO> newModel = new CompoundPropertyModel<PhenoDataSetFieldGroupVO>(new PhenoDataSetFieldGroupVO());
		final PhenoDataSetGroup cfg = newModel.getObject().getPhenoDataSetGroup();
		cfdArkDataProvider = new ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay>() {
			private static final long serialVersionUID = 1L;
			public long size() {
				return iPhenotypicService.getCFDLinkedToQuestionnaireCount(cfg);
			}
			public Iterator<PhenoDataSetFieldDisplay> iterator(long first, long count) {
				
				Collection<PhenoDataSetFieldDisplay> phenoFieldDisplayList = new ArrayList<PhenoDataSetFieldDisplay>();
				phenoFieldDisplayList = iPhenotypicService.getCFDLinkedToQuestionnaire(cfg, first, count);
				return phenoFieldDisplayList.iterator();
			}
		};
		
		
		DataDictionaryGroupDetailPanel detailPanel = new DataDictionaryGroupDetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO,newModel,cfdArkDataProvider,false);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
		preProcessDetailPanel(target);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		getModelObject().getPhenoDataSetGroup().setStudy(study);
		//long count = iArkCommonService.getCustomFieldGroupCount(getModelObject().getPhenoDataSetGroup());
		long count = iPhenotypicService.getPhenoDataSetFieldGroupCount(getModelObject().getPhenoDataSetGroup());
		if (count <= 0L) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
	protected void initialiseSearchForm(){
		groupNameTxtFld = new TextField<String>("phenoDataSetGroup.name");
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		List<Boolean> publishedStatusLst = iPhenotypicService.getPublishedSatusLst(study, arkFunction);
		ChoiceRenderer publishedStatusLstRenderer = new ChoiceRenderer("published"){
			@Override
			public Object getDisplayValue(Object object) {
				Boolean value=(Boolean)object;
				if(value.equals(true)){
					return "Yes";
				}else{
					return "No";
				}
			}
		};
		publishedStatusDdc = new DropDownChoice<Boolean>("phenoDataSetGroup.published", (List) publishedStatusLst, publishedStatusLstRenderer);
		publishedStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	getModelObject().getPhenoDataSetGroup().setPublished(publishedStatusDdc.getModelObject());
            }
            });
	}
	
	protected void addSearchComponentsToForm() {
		add(groupNameTxtFld);
		//add(publishedStatusCb);
		add(publishedStatusDdc);
		
	}

}
