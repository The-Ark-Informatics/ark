package au.org.theark.lims.web.component.biospecimen.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BiospecimenIdGenerator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biospecimen.DetailPanel;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<LimsVO>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3103311665813442088L;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	private PageableListView<Biospecimen>	listView;
	private CompoundPropertyModel<LimsVO>		cpmModel;
	private TextField<String>						idTxtFld;
	private TextField<String>						biospecimenIdTxtFld;
	private DateTextField							sampleDateTxtFld;
	private DropDownChoice<BioSampletype>			sampleTypeDdc;
	private DetailPanel								detailPanel;
	private Long										sessionStudyId;
	private WebMarkupContainer						arkContextMarkup;
	private String subjectUIDInContext;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> model, PageableListView<Biospecimen> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextMarkup)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.setCpmModel(model);
		this.listView = listView;
		this.setDetailPanel(detailPanel);
		this.setArkContextMarkup(arkContextMarkup);
		initialiseFieldForm();
		
		subjectUIDInContext = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		if(subjectUIDInContext == null || subjectUIDInContext.isEmpty())
		{
			newButton.setVisible(false);
		}
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.setCpmModel(compoundPropertyModel);
		initialiseFieldForm();
	}

	public void initialiseFieldForm()
	{
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenIdTxtFld = new TextField<String>("biospecimen.biospecimenId");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);
		
		initSampleTypeDdc();
		
		addFieldComponents();
	}
	
	private void initSampleTypeDdc()
	{
		CompoundPropertyModel<LimsVO> limsCpm = cpmModel;
		PropertyModel<Biospecimen> biospecimenPm = new PropertyModel<Biospecimen>(limsCpm, "biospecimen");
		PropertyModel<BioSampletype> sampleTypePm = new PropertyModel<BioSampletype>(biospecimenPm, "sampleType");
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> sampleTypeRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", sampleTypePm, (List<BioSampletype>) sampleTypeList, sampleTypeRenderer);
	}

	private void addFieldComponents()
	{
		add(idTxtFld);
		add(biospecimenIdTxtFld);
		add(sampleDateTxtFld);
		add(sampleTypeDdc);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		LimsVO limsVo = getModelObject();
		limsVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		limsVo.getBiospecimen().setId(null); // must ensure Id is blank onNew

		// Set study for the new biospecimen
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		limsVo.getBiospecimen().setStudy(study);

		setModelObject(limsVo);
		preProcessDetailPanel(target);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// Refresh the FB panel if there was an old message from previous search result
		target.addComponent(feedbackPanel);

		// Get a list of biospecimens for the study/subject in context by default
		java.util.List<au.org.theark.core.model.lims.entity.Biospecimen> biospecimenList = new ArrayList<au.org.theark.core.model.lims.entity.Biospecimen>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if (sessionStudyId != null && sessionStudyId > 0)
		{
			// Study in context
			Study study = iArkCommonService.getStudy(sessionStudyId);
			getModelObject().getBiospecimen().setStudy(study);
			
			try
			{
				// Set biospecimen into model
				biospecimenList = iLimsService.searchBiospecimen(getModelObject().getBiospecimen());
				
				if (biospecimenList != null && biospecimenList.size() == 0)
				{
					this.info("Biospecimens with the specified criteria does not exist in the system.");
					target.addComponent(feedbackPanel);
				}
			}
			catch (ArkSystemException e)
			{
				this.error(e.getMessage());
			}
		}
		
		listView.removeAll();
		
		// Set results into model
		getModelObject().setBiospecimenList(biospecimenList);

		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
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

	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel()
	{
		return detailPanel;
	}

	/**
	 * @param cpmModel the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<LimsVO> cpmModel)
	{
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<LimsVO> getCpmModel()
	{
		return cpmModel;
	}

	/**
	 * @return the newButton
	 */
	public AjaxButton getNewButton()
	{
		return newButton;
	}
}