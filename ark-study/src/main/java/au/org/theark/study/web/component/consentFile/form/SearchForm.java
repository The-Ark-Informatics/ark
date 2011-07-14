package au.org.theark.study.web.component.consentFile.form;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consentFile.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial" })
public class SearchForm extends AbstractSearchForm<ConsentVO>
{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	protected IStudyService studyService;

	protected DetailPanel detailPanel;
	protected PageableListView<ConsentFile> pageableListView;
	protected CompoundPropertyModel<ConsentVO> cpmModel;
	
	/**
	 * Form Components
	 */
	protected TextField<String> consentFileId;
	protected TextField<String> consentFileName;
	
	/**
	 * @param id
	 */
	public SearchForm(String id,
						CompoundPropertyModel<ConsentVO> model, 
						PageableListView<ConsentFile> listView, 
						FeedbackPanel feedBackPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer, 
						WebMarkupContainer detailContainer, 
						WebMarkupContainer detailPanelFormContainer, 
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.pageableListView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionConsentId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID);
		disableSearchForm(sessionConsentId, "There is no consent in context. Please select a Consent.");
	}

	protected void initialiseSearchForm(){
		consentFileId = new TextField<String>(Constants.CONSENT_FILE_ID);
		consentFileName = new TextField<String>(Constants.CONSENT_FILE_FILENAME);
	}

	protected void addSearchComponentsToForm(){
		add(consentFileId);
		add(consentFileName);
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		ConsentFile consentFile  = new ConsentFile();
		Collection<ConsentFile> consentFileList = new ArrayList<ConsentFile>();
		
		try 
		{
			Long sessionConsentId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID);
			Consent consent = new Consent();
			
			// Set consentFile.consent reference
			if(sessionConsentId != null)
			{
				consent = studyService.getConsent(sessionConsentId);
				consentFile.setConsent(consent);
				// Look up based on criteria via back end.
				consentFileList =  studyService.searchConsentFile(consentFile);
			}
			
			if(consentFileList != null && consentFileList.size() == 0)
			{
				this.info("There are no consent files for the specified criteria.");
				target.addComponent(feedbackPanel);
			}
			
			getModelObject().setConsentFileList(consentFileList);
			pageableListView.removeAll();
			listContainer.setVisible(true);
			target.addComponent(listContainer);
			
			
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		// ARK-108:: no longer do full reset to VO
		getModelObject().getConsentFile().setId(null);	//only reset ID (not user definable)
			
		preProcessDetailPanel(target);
	}

}