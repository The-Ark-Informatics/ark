package au.org.theark.study.web.component.pedigree.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.PedigreeDisplayPanel;
import au.org.theark.study.web.component.pedigree.PedigreeParentContainerPanel;
import au.org.theark.study.web.component.pedigree.PedigreeTwinContainerPanel;

public class SearchForm extends Form<PedigreeVo> {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private WebMarkupContainer	 					arkContextMarkup;
	protected WebMarkupContainer 				studyNameMarkup;
	protected WebMarkupContainer 				studyLogoMarkup;
	
	protected FeedbackPanel			feedbackPanel;
	
	protected ArkCrudContainerVO arkCrudContainerVO;
   
	
	protected AjaxButton fatherButton;
	protected AjaxButton motherButton;
	protected AjaxButton twinButton;
	protected AjaxButton viewButton;
	//Move Export button to export link
//	protected AjaxButton exportButton;
	
	protected AbstractDetailModalWindow modalWindow;

	public SearchForm(String id,CompoundPropertyModel<PedigreeVo> cpmModel,WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup , ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id,cpmModel);
		
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		this.setMultiPart(true);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
//		this.listView = listView;
//		
//		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a subject or contact.");
	}
	
	protected void addSearchComponentsToForm() {		
		add(fatherButton);
		add(motherButton);
		add(twinButton);
		add(viewButton);
//		add(exportButton);
		add(modalWindow);
	}

	protected void initialiseSearchForm() {
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {
			
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getSearchPanelContainer());
				
			}
		};
		
		
		fatherButton = new AjaxButton(au.org.theark.core.Constants.FATHER){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Father");
				
				modalWindow.setContent(new PedigreeParentContainerPanel("content",arkContextMarkup,studyNameMarkup,studyLogoMarkup,modalWindow,Constants.MALE,getFormModelObject().getRelationshipList()));
				modalWindow.show(target);
			}						
		};
		
		motherButton = new AjaxButton(au.org.theark.core.Constants.MOTHER){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Mother");
				modalWindow.setContent(new PedigreeParentContainerPanel("content",arkContextMarkup,studyNameMarkup,studyLogoMarkup,modalWindow,Constants.FEMALE,getFormModelObject().getRelationshipList()));
				modalWindow.show(target);
			}						
		};
		
		twinButton = new AjaxButton(au.org.theark.core.Constants.TWIN){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Set Twins");
				modalWindow.setContent(new PedigreeTwinContainerPanel("content",modalWindow));
				modalWindow.show(target);
			}						
		};
		
		viewButton = new AjaxButton(au.org.theark.core.Constants.VIEW){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Pedigree View");
				modalWindow.setContent(new PedigreeDisplayPanel("content"));
				modalWindow.show(target);
				
			}						
		};
		
//		exportButton = new AjaxButton(au.org.theark.core.Constants.EXPORT){
//			
//			@Override
//			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				//TODO
//			}						
//		};
		
	}
	
	protected void disableSearchForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionId == null) {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
		else {
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}
	
	
	
	private PedigreeVo getFormModelObject(){
		return getModelObject();
	}
}
