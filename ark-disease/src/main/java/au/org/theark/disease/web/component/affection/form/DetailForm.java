/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.disease.web.component.affection.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.disease.entity.Position;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionCustomDataVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.web.component.affection.AffectionCustomDataDataViewPanel;

public class DetailForm extends AbstractDetailForm<AffectionVO> {

	private static final long								serialVersionUID	= -9196914684971413116L;

	private WebMarkupContainer	arkContextMarkupContainer;

	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;

	private Long sessionStudyId;

	private HashMap<Integer, Position> position_storage = new HashMap<Integer, Position>(); //temporary storage for positions

	private DropDownChoice<Disease> diseaseDDC;
	private DropDownChoice<AffectionStatus> affectionStatusDDC;
	private DateTextField recordDateTxtFld;

	private AbstractCustomDataEditorForm<AffectionCustomDataVO> customFieldForm;
	private AffectionCustomDataDataViewPanel dataViewPanel;
	private AjaxPagingNavigator pageNavigator;

	private ListView<Position> positionListEditor;
	private LoadableDetachableModel<List<Position>> detachableModel;

	private DropDownChoice<Position> positionDDC;
	private Button newPositionBtn;

	private Study study;
	private LinkSubjectStudy lss;

	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(sessionStudyId);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			lss = iArkCommonService.getSubject(sessionPersonId, study);
		}
		catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		containerForm.getModelObject().getAffection().setLinkSubjectStudy(lss);
		containerForm.getModelObject().getAffection().setStudy(study);
		this.cpModel = new CompoundPropertyModel<AffectionVO>(containerForm.getModel());
	}

	@Override
	public void onBeforeRender() {
		position_storage.clear();
		List<AffectionCustomFieldData> acfdset = iArkDiseaseService.getAffectionCustomFieldData(containerForm.getModelObject().getAffection());
		AffectionCustomDataVO acdvo = new AffectionCustomDataVO(acfdset);
		customFieldForm.setModelObject(acdvo);

		newPositionBtn.setEnabled(!isNew());

		customFieldForm.setVisible(!acfdset.isEmpty());
		dataViewPanel.setVisible(!acfdset.isEmpty());
		pageNavigator.setVisible(!acfdset.isEmpty());

		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {

		initDiseaseDDC();
		initAffectionStatusDDC();		

		detachableModel = new LoadableDetachableModel<List<Position>>() {

			@Override
			protected List<Position> load() {
				List<Position> positions = iArkDiseaseService.getPositions(containerForm.getModelObject().getAffection());
				return positions;
			}
		};

		positionListEditor = new ListView<Position>("positionListEditor", detachableModel) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Position> item) {
				final Position position = item.getModelObject();
				if(position != null) {
					position_storage.put(item.getIndex(), position);
				}

				List<Gene> availableGenes = new ArrayList<Gene>();
				if(containerForm.getModelObject().getAffection().getDisease() != null && containerForm.getModelObject().getAffection().getDisease().getGenes() != null) {
					availableGenes = new ArrayList<Gene>(containerForm.getModelObject().getAffection().getDisease().getGenes());					
				}
				final DropDownChoice<Gene> geneDDC = new DropDownChoice<Gene>("affection.disease.genes", new Model<Gene>(position.getGene()), availableGenes, new ChoiceRenderer<Gene>("name", "id"));
				geneDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long	serialVersionUID	= 1L;

					protected void onUpdate(AjaxRequestTarget target) {
						Gene selectedGene = iArkDiseaseService.getGeneByID(Long.parseLong(geneDDC.getValue()));
						positionDDC.setChoices(new ArrayList<Position>(selectedGene.getPositions()));
						target.add(positionDDC);
					}
				});

				geneDDC.setOutputMarkupId(true);
				item.add(geneDDC);

				List<Position> availablePositions = new ArrayList<Position>();
				if(geneDDC.getModelObject() != null) {
					availablePositions = new ArrayList<Position>(geneDDC.getModelObject().getPositions());
				}
				LoadableDetachableModel<Position> positionModel = new LoadableDetachableModel<Position>(position) {

					@Override
					protected Position load() {
						return position;
					}
				};
				if(position != null) {
					positionModel.setObject(position);
				}
				positionDDC = new DropDownChoice<Position>("affection.positions", positionModel, availablePositions, new ChoiceRenderer<Position>("name", "id")){
					@Override
					protected void onBeforeRender() {
						if(geneDDC.getModelObject() != null && geneDDC.getModelObject().getId() != null && !geneDDC.getModelObject().getPositions().isEmpty()) {
							this.setChoices(new ArrayList<Position>(geneDDC.getModelObject().getPositions()));
						}
						if(this.getModelObject().getName() != null) {
							position_storage.put(item.getIndex(), this.getModelObject());
						}
						super.onBeforeRender();
					}
				};
				positionDDC.setOutputMarkupId(true);
				positionDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					protected void onUpdate(AjaxRequestTarget target) {
						Position selectedPosition = positionDDC.getModelObject();
						if(selectedPosition.getName() != null) {
							position_storage.put(item.getIndex(), selectedPosition);
						}
					}
				});
				item.add(positionDDC);

				item.add(new AjaxDeleteButton(Constants.DELETE, new Model<String>("Are you sure?"), new Model<String>("Delete")) {

					private static final long serialVersionUID = 1L;

					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
						try {
							//need to remove save containerform post position removal
							position_storage.remove(item.getIndex());
							positionListEditor.getModelObject().remove(position);
							containerForm.getModelObject().setAffection(iArkDiseaseService.getAffectionByID(containerForm.getModelObject().getAffection().getId()));
							
							save(containerForm, target);
							target.add(dataViewPanel);
							deleteCompleted("Row '" + position.getName() + "' deleted successfully.", true);
						} catch (Exception e) {
							e.printStackTrace();
							target.add(form);
							deleteCompleted("Error deleting row '" + position.getName() + "'. Row has data associated with it.", false);
						}
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						onDeleteConfirmed(target, form);
						target.add(form);
						target.add(feedBackPanel);
					}
				});

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};

		positionListEditor.setOutputMarkupId(true);

		newPositionBtn = new AjaxEditorButton(Constants.NEW) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				positionListEditor.getModelObject().add(new Position());
				target.add(form);
			}
		}.setDefaultFormProcessing(false);

		arkCrudContainerVO.getDetailPanelFormContainer().add(newPositionBtn);

		PropertyModel<Date> recordDateModel = new PropertyModel<Date>(containerForm.getModel(), "affection.recordDate");
		/*recordDateTxtFld = new DateTextField("recordDate", recordDateModel){
			@Override
			protected void onBeforeRender() {
				this.setModel(new PropertyModel<Date>(containerForm.getModel(), "affection.recordDate"));
				super.onBeforeRender();
			}
		};*/
		recordDateTxtFld =new DateTextField("recordDate", recordDateModel, new PatternDateConverter( au.org.theark.core.Constants.DD_MM_YYYY, false)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onBeforeRender() {
				this.setModel(new PropertyModel<Date>(containerForm.getModel(), "affection.recordDate"));
				super.onBeforeRender();
			}
		};
		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);

		AffectionCustomDataVO affectionCustomDataVO = new AffectionCustomDataVO();
		affectionCustomDataVO.setCustomFieldDataList(new ArrayList<AffectionCustomFieldData>(cpModel.getObject().getAffection().getAffectionCustomFieldDataSets()));

		final CompoundPropertyModel<AffectionCustomDataVO> affectionCustomDataModel = new CompoundPropertyModel<AffectionCustomDataVO>(affectionCustomDataVO);
		dataViewPanel = new AffectionCustomDataDataViewPanel("dataViewPanel", affectionCustomDataModel).initialisePanel(iArkCommonService.getCustomFieldsPerPage());
		customFieldForm = new AbstractCustomDataEditorForm<AffectionCustomDataVO>("customFieldForm", affectionCustomDataModel, feedBackPanel) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onEditSave(AjaxRequestTarget target, Form<?> form) {
				for(AffectionCustomFieldData acfd : this.cpModel.getObject().getCustomFieldDataList()) {
					iArkDiseaseService.save(acfd);
				}
			}

			@Override
			public void onBeforeRender() {
				if(!isNew()) {
					this.setModelObject(new AffectionCustomDataVO(iArkDiseaseService.getAffectionCustomFieldData(containerForm.getModelObject().getAffection())));
				}
				this.buttonsPanelWMC.setVisible(false);
				super.onBeforeRender();
			}
		}.initialiseForm(false);

		pageNavigator = new AjaxPagingNavigator("navigator", dataViewPanel.getDataView()) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(customFieldForm.getDataViewWMC());
				target.add(this);
			}
		};
		pageNavigator.setOutputMarkupId(true);
		customFieldForm.getDataViewWMC().add(dataViewPanel);
		arkCrudContainerVO.getDetailPanelFormContainer().add(pageNavigator);
		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	private void initAffectionStatusDDC() {
		CompoundPropertyModel<AffectionVO> affectionCPM = (CompoundPropertyModel<AffectionVO>) containerForm.getModel();
		PropertyModel<Affection> affectionPM = new PropertyModel<Affection>(affectionCPM, "affection");
		PropertyModel<AffectionStatus> affectionStatusPM = new PropertyModel<AffectionStatus>(affectionPM, "affectionStatus");
		ChoiceRenderer affectionStatusRenderer = new ChoiceRenderer("name", "id");
		List<AffectionStatus> possibleAffectionStatus = iArkDiseaseService.getAffectionStatus();
		affectionStatusDDC = new DropDownChoice<AffectionStatus>("affection.affectionStatus", affectionStatusPM, possibleAffectionStatus, affectionStatusRenderer){
			@Override
			protected void onBeforeRender() {
				if(!isNew()) { 
					this.setModelObject(containerForm.getModelObject().getAffection().getAffectionStatus());
				} else {
					this.setModel(null);
				}
				super.onBeforeRender();
			}
		};
	}

	@SuppressWarnings("unchecked")
	private void initDiseaseDDC() {
		CompoundPropertyModel<AffectionVO> affectionCpm = (CompoundPropertyModel<AffectionVO>) containerForm.getModel();
		PropertyModel<Affection> affectionPm = new PropertyModel<Affection>(affectionCpm, "affection");
		PropertyModel<Disease> diseasePm = new PropertyModel<Disease>(affectionPm, "disease");
		Collection<Disease> diseases = iArkDiseaseService.getAvailableDiseasesForStudy(iArkCommonService.getStudy(sessionStudyId));
		ChoiceRenderer diseaseRenderer = new ChoiceRenderer("name", "id");
		diseaseDDC = new DropDownChoice<Disease>("affection.disease", diseasePm, (List) diseases, diseaseRenderer){
			@Override
			protected void onBeforeRender() {
				if(!isNew()) {
					setModelObject(containerForm.getModelObject().getAffection().getDisease());
					this.setEnabled(false);
				} else {
					this.setEnabled(true);
					this.setModel(null);
				}
				super.onBeforeRender();
			}
		};
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(diseaseDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(affectionStatusDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(recordDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(customFieldForm);
		arkCrudContainerVO.getDetailPanelFormContainer().add(positionListEditor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target) {
		AffectionVO vo = new AffectionVO();
		vo.getAffection().setLinkSubjectStudy(lss);
		vo.getAffection().setStudy(study);
		containerForm.setModelObject(vo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		recordDateTxtFld.setRequired(true);
		diseaseDDC.setRequired(true);
		affectionStatusDDC.setRequired(true);
	}

	private void save(Form<AffectionVO> containerForm, AjaxRequestTarget target) {
		//Not sure why needed. Have to reload affection from database to resolve LazyInitializationExceptions.
		if(!isNew()) {
			containerForm.getModelObject().setAffection(iArkDiseaseService.getAffectionByID(containerForm.getModelObject().getAffection().getId()));
		}

		for(Entry<Integer, Position> entry : position_storage.entrySet()) {
			if(entry.getValue() != null && entry.getValue().getName() != null) {
				Position toInsert = entry.getValue();
				if(!containerForm.getModelObject().getAffection().getPositions().contains(toInsert)) {
					containerForm.getModelObject().getAffection().getPositions().add(toInsert);
				}
			}
		}
			
		for(Iterator<Position> iterator = containerForm.getModelObject().getAffection().getPositions().iterator(); iterator.hasNext();) {
			Position position = iterator.next();
			if(!position_storage.containsValue(position)) {
				iterator.remove();
			}
		}

		if(isNew()) {
			iArkDiseaseService.save(containerForm.getModelObject().getAffection());
		} else {
			iArkDiseaseService.update(containerForm.getModelObject().getAffection());
		}

		for(AffectionCustomFieldData acfd : customFieldForm.getModelObject().getCustomFieldDataList()) {
			acfd.setAffection(containerForm.getModelObject().getAffection());
		}
//		customFieldForm.onEditSave(target, containerForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<AffectionVO> containerForm, AjaxRequestTarget target) {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId == null) {
			// No study in context
			this.error("There is no study selected. Please select a study to manage diseases.");
			processErrors(target);
		}
		else {
			try {
				save(containerForm, target);
				customFieldForm.onEditSave(target, containerForm);
				target.add(this);
			} catch (DataIntegrityViolationException e) {
				this.error(getString("duplicate.keys.error"));
				processErrors(target);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		editCancelProcess(target);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		return containerForm.getModelObject().getAffection() == null || containerForm.getModelObject().getAffection().getId() == null;
	}

	protected void deleteCompleted(String feedback, boolean successful) {
		if(successful) { 
			this.deleteInformation();
			//this.info(feedback);
		} else {
			this.error(feedback);
		}
	}
}
