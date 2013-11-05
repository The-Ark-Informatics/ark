package au.org.theark.registry.web.component.invoice.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.*;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.registry.web.component.invoice.DetailPanel;//This may be wrong

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<Pipeline>{

	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private ArkCrudContainerVO	arkCrudContainerVO;
	private TextField<String> groupNameTxtFld;
	private CheckBox publishedStatusCb;	
	private ArkDataProvider2<Pipeline,Pipeline> cfdArkDataProvider;
	private Label generalTextLbl; 
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id,CompoundPropertyModel<Pipeline> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		resetButton.setVisible(false);
		searchButton.setVisible(false);
		generalTextLbl = new Label("generalLbl", new StringResourceModel("search.panel.text", new Model() ));
		add(generalTextLbl);
		//initialiseSearchForm();
		//addSearchComponentsToForm();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		/*
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		getModelObject().setStudy(study);*/
		
		//CompoundPropertyModel<Pipeline> newModel = new CompoundPropertyModel<Pipeline>(new Pipeline());
		
/*		//Copy over any details user may have typed in the search form and carry it to the Detail Form
		newModel.getObject().getCustomFieldGroup().setName(getModelObject().getCustomFieldGroup().getName());
		newModel.getObject().getCustomFieldGroup().setDescription(getModelObject().getCustomFieldGroup().getDescription());
		newModel.getObject().setAvailableCustomFields(availableListOfFields);
	*/	

		//TODO: is this needed?
		cfdArkDataProvider = new ArkDataProvider2<Pipeline, Pipeline>() {
			private static final long serialVersionUID = 1L;
			public Iterator<? extends Pipeline> iterator(int first, int count) {
				// TODO Auto-generated method stub
				return null;
			}
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}		
		};
		
		//DetailPanel detailPanel = new DetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO,containerForm);
		//arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);  TODO do we need tyhese two lines
		preProcessDetailPanel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().setStudy(study);
		
		long count = iArkCommonService.getPipelineCount(study);

		if (count <= 0L) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
	protected void initialiseSearchForm(){
		groupNameTxtFld = new TextField<String>("customFieldGroup.name");
		publishedStatusCb = new CheckBox("customFieldGroup.published");
	}
	
	protected void addSearchComponentsToForm() {
		add(groupNameTxtFld);
		add(publishedStatusCb);
	}

}
