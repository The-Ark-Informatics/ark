package au.org.theark.lims.web.component.subject.biospecimen.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractModalDetailForm<LimsVO>
{
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 2727419197330261916L;
	private static final Logger		log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	private TextField<String>					idTxtFld;
	private TextField<String>					biospecimenIdTxtFld;
	private TextArea<String>					commentsTxtAreaFld;
	private DateTextField						sampleDateTxtFld;
	private DropDownChoice<BioSampletype>	sampleTypeDdc;
	private DropDownChoice<BioCollection>	bioCollectionDdc;
	private TextField<String>					quantityTxtFld;

	private ModalWindow							modalWindow;
	private String									subjectUIDInContext;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, Form<LimsVO> containerForm)
	{
		super(id, feedBackPanel, arkCrudContainerVo, containerForm);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, Form<LimsVO> containerForm)
	{
		super(id, feedBackPanel, arkCrudContainerVo, containerForm);
		this.modalWindow = modalWindow;
	}

	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenIdTxtFld = new TextField<String>("biospecimen.biospecimenId");
		commentsTxtAreaFld = new TextArea<String>("biospecimen.comments");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);
		quantityTxtFld = new TextField<String>("biospecimen.quantity");

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);

		initSampleTypeDdc();
		initBioCollectionDdc();
		
		attachValidators();
		addComponents();
	}

	private void initSampleTypeDdc()
	{
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> sampleTypeRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", (List<BioSampletype>) sampleTypeList, sampleTypeRenderer);
	}
	
	private void initBioCollectionDdc()
	{
		// Get a list of collections for the study/subject in context by default
		java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<au.org.theark.core.model.lims.entity.BioCollection>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if (sessionStudyId != null && sessionStudyId > 0)
		{
			Study study = iArkCommonService.getStudy(sessionStudyId);
			containerForm.getModelObject().getBioCollection().setStudy(study);
		}
		
		subjectUIDInContext = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		// Subject in context
		if(subjectUIDInContext != null && !subjectUIDInContext.isEmpty())
		{
			try
			{
				// Subject in context
				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUIDInContext);
				containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);	
			}
			catch (EntityNotFoundException e)
			{
				log.error(e.getMessage());
			}
			catch(NullPointerException e)
			{
				log.error(e.getMessage());
			}
		}
		
		try
		{
			bioCollectionList = iLimsService.searchBioCollection(containerForm.getModelObject().getBioCollection());
		}
		catch (ArkSystemException e)
		{
			log.error(e.getMessage());
		}
		
		ChoiceRenderer<BioCollection> bioCollectionRenderer = new ChoiceRenderer<BioCollection>(Constants.NAME, Constants.ID);
		bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", (List<BioCollection>) bioCollectionList, bioCollectionRenderer);
	}
	

	protected void attachValidators()
	{
		biospecimenIdTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.biospecimenId.required", this, new Model<String>("Name")));
		sampleTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.sampleType.required", this, new Model<String>("Name")));
		bioCollectionDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.bioCollection.required", this, new Model<String>("Name")));
	}

	private void addComponents()
	{
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenIdTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTypeDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioCollectionDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(quantityTxtFld);
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target)
	{
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		subjectUIDInContext = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		try
		{
			linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUIDInContext);
			containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);

			if (containerForm.getModelObject().getBiospecimen().getId() == null)
			{
				// Save
				iLimsService.createBiospecimen(containerForm.getModelObject());
				this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenId() + " was created successfully");
				processErrors(target);
			}
			else
			{
				// Update
				iLimsService.updateBiospecimen(containerForm.getModelObject());
				this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenId() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
		}
		catch (EntityNotFoundException e)
		{
			this.error(e.getMessage());
		}
		catch(NullPointerException e)
		{
			this.error("Cannot save a Biospecimen without a Subject in context");
		}
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target)
	{
		iLimsService.deleteBiospecimen(containerForm.getModelObject());
		this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenId() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedbackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
		editCancelProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew()
	{
		/*
		 * if (containerForm.getModelObject().getBioCollection().getId() == null) { return true; } else { return false; }
		 */
		return true;
	}
}