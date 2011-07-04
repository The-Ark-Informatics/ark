package au.org.theark.lims.web.component.subjectSub.bioCollection.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.form.ContainerForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "unused" })
public class DetailForm extends AbstractModalDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2926069852602563767L;
	private static final Logger log = LoggerFactory.getLogger(DetailForm.class);
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

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow,
			ContainerForm containerForm) {
		super(id, feedBackPanel, arkCrudContainerVo, containerForm);
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
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getBioCollection().getId() == null) {
			// Save
			iLimsService.createBioCollection(containerForm.getModelObject());
			this.info("Biospecimen collection "
					+ containerForm.getModelObject().getBioCollection()
							.getName() + " was created successfully");
			processErrors(target);
		} else {
			// Update
			iLimsService.updateBioCollection(containerForm.getModelObject());
			this.info("Biospecimen collection "
					+ containerForm.getModelObject().getBioCollection()
							.getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// Reset LimsVO
		LimsVO limsVo = new LimsVO();
		limsVo.setSubjectVo(containerForm.getModelObject().getSubjectVo());
		limsVo.setLinkSubjectStudy(containerForm.getModelObject()
				.getLinkSubjectStudy());
		limsVo.setLinkSubjectStudy(containerForm.getModelObject()
				.getLinkSubjectStudy());
		limsVo.getBioCollection().setLinkSubjectStudy(
				containerForm.getModelObject().getLinkSubjectStudy());
		limsVo.getBioCollection()
				.setStudy(
						containerForm.getModelObject().getLinkSubjectStudy()
								.getStudy());
		try {
			limsVo.setBioCollectionList(iLimsService.searchBioCollection(limsVo
					.getBioCollection()));
		} catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		limsVo.getBiospecimen().setLinkSubjectStudy(
				containerForm.getModelObject().getLinkSubjectStudy());
		limsVo.getBiospecimen()
				.setStudy(
						containerForm.getModelObject().getLinkSubjectStudy()
								.getStudy());
		try {
			limsVo.setBiospecimenList(iLimsService.searchBiospecimen((limsVo
					.getBiospecimen())));
		} catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		containerForm.setModelObject(limsVo);
		target.addComponent(feedbackPanel);
		target.addComponent(containerForm);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		try {
			iLimsService.deleteBioCollection(containerForm.getModelObject());
			this.info("Biospecimen collection "
					+ containerForm.getModelObject().getBioCollection()
							.getName() + " was deleted successfully");
		} catch (org.hibernate.NonUniqueObjectException noe) {
			this.error(noe.getMessage());
		}

		// Display delete confirmation message
		target.addComponent(feedbackPanel);
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
		if (containerForm.getModelObject().getBioCollection().getId() == null) {
			return true;
		} else {
			return false;
		}
	}
}