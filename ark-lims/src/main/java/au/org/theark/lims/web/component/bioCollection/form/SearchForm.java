package au.org.theark.lims.web.component.bioCollection.form;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<LimsVO>
{
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private PageableListView<BioCollection>	listView;
	private CompoundPropertyModel<LimsVO>		cpmModel;
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private TextArea<String>						commentsTxtAreaFld;
	private DateTextField							collectionDateTxtFld;
	private DateTextField							surgeryDateTxtFld;
	private DetailPanel								detailPanel;
	private Long										sessionStudyId;
	private WebMarkupContainer						arkContextMarkup;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> model, PageableListView<BioCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextMarkup)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		this.setArkContextMarkup(arkContextMarkup);
		initialiseFieldForm();

		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//disableSearchForm(sessionPersonId, "There is no subject in context. Please select a Subject.");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	public void initialiseFieldForm()
	{
		idTxtFld = new TextField<String>("bioCollection.id");
		nameTxtFld = new TextField<String>("bioCollection.name");
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(idTxtFld);
		add(nameTxtFld);
		add(collectionDateTxtFld);
		add(surgeryDateTxtFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		LimsVO limsVo = getModelObject();
		limsVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		limsVo.getBioCollection().setId(null); // must ensure Id is blank onNew

		// Set study for the new collection
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		limsVo.getBioCollection().setStudy(study);

		setModelObject(limsVo);
		preProcessDetailPanel(target);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// Refresh the FB panel if there was an old message from previous search result
		target.addComponent(feedbackPanel);

		// Get a list of collections for the study/subject in context by default
		java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<au.org.theark.core.model.lims.entity.BioCollection>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0)
		{
			Study study = iArkCommonService.getStudy(sessionStudyId);
			BioCollection bioCollection = new BioCollection();
			bioCollection.setStudy(study);
			// Subject in context
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
			
			try
			{
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
				bioCollection.setLinkSubjectStudy(linkSubjectStudy);
				getModelObject().setBioCollection(bioCollection);
				
				bioCollectionList = iLimsService.searchBioCollection(getModelObject().getBioCollection());
			}
			catch (ArkSystemException e)
			{
				this.error(e.getMessage());
			}
			catch (EntityNotFoundException e)
			{
				this.info(e.getMessage());
			}
		}

		if (bioCollectionList != null && bioCollectionList.size() == 0)
		{
			this.info("Collections with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setBioCollectionList(bioCollectionList);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
	}

	protected boolean isSecure(String actionType)
	{
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW))
		{
			SecurityManager securityManager = ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			if (securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) || securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN))
			{
				flag = true;
			}
			;
		}
		else
		{
			flag = true;
		}
		return flag;
	}

	/**
	 * @param arkContextMarkup
	 *           the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup)
	{
		this.arkContextMarkup = arkContextMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup()
	{
		return arkContextMarkup;
	}
}