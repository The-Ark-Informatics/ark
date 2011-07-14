package au.org.theark.lims.web.component.subjectLims.lims.bioCollection.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
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
@SuppressWarnings({ "unused" })
public class CollectionModalDetailForm extends AbstractModalDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2926069852602563767L;
	private static final Logger log = LoggerFactory.getLogger(CollectionModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;

	private int mode;

	private TextField<String> idTxtFld;
	private TextField<String> nameTxtFld;
	private TextField<String> collectionIdTxtFld;
	private TextArea<String> commentsTxtAreaFld;
	private DateTextField collectionDateTxtFld;
	private DateTextField surgeryDateTxtFld;
	private ModalWindow modalWindow;
	private WebMarkupContainer arkContextMarkup;
	
//	private ListDetailPanel listDetailPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param containerForm
	 * @param detailPanelContainer 
	 */
	public CollectionModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		nameTxtFld = new TextField<String>("bioCollection.name");
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField(
				"bioCollection.collectionDate",
				au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate",
				au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(
				new StringResourceModel("error.bioCollection.name.required",
						this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(
				idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer()
				.add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(
				collectionDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(surgeryDateTxtFld);
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) 
	{
		if (cpModel.getObject().getBioCollection().getId() == null)
		{
			// Save
			iLimsService.createBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was created successfully");
			if (target != null) {
				processErrors(target);
			}
		} 
		else 
		{
			// Update
			iLimsService.updateBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was updated successfully");
			if (target != null) {
				processErrors(target);
			}
		}
		if (target != null) {
			onSavePostProcess(target);
		}
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// Reset now handled for in BioCollectionListPanel.onBeforeRender()
//		// Reset the BioCollection (for criteria) in LimsVO
//		BioCollection resetBioCollection = new BioCollection();
//		resetBioCollection.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
//		resetBioCollection.setStudy(cpModel.getObject().getLinkSubjectStudy().getStudy());
//		cpModel.getObject().setBioCollection(resetBioCollection);
		
		target.addComponent(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		iLimsService.deleteBioCollection(cpModel.getObject());
		this.info("Biospecimen collection "
				+ cpModel.getObject().getBioCollection()
						.getName() + " was deleted successfully");

		onCancel(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getBioCollection().getId() == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onBeforeRender() {
		// Get fresh from backend
		BioCollection bc = cpModel.getObject().getBioCollection();
		if (bc.getId() != null) {
			try {
				cpModel.getObject().setBioCollection(iLimsService.getBioCollection(bc.getId()));
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				e.printStackTrace();
			} catch (ArkSystemException e) {
				// TODO Auto-generated catch block
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				e.printStackTrace();
			}
		}

		super.onBeforeRender();
	}
}