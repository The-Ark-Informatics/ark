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
package au.org.theark.phenotypic.web.component.phenodataentry.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenotypicCollectionModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class PhenotypicCollectionListForm extends Form<PhenoDataCollectionVO> {
	/**
	 * 
	 */
	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(PhenotypicCollectionListForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService								iPhenotypicService;

	protected CompoundPropertyModel<PhenoDataCollectionVO>			cpModel;
	protected FeedbackPanel									feedbackPanel;
	protected AbstractDetailModalWindow					modalWindow;

	private Label												idLbl;
	private Label												questionnaireLbl;
	private Label												nameLbl;
	private Label												descriptionLbl;
	private Label												recordDateLbl;
	private Label												reviewedDateLbl;
	private Label												statusLbl;

	private Panel												modalContentPanel;
	protected ArkBusyAjaxButton							newButton;

	protected WebMarkupContainer							dataViewListWMC;
	private DataView<PhenotypicCollection>				dataView;
	private ArkDataProvider2<PhenoDataCollectionVO, PhenotypicCollection>	phenotypicCollectionProvider;

	public PhenotypicCollectionListForm(String id, FeedbackPanel feedbackPanel, AbstractDetailModalWindow modalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}

	public void initialiseForm() {
		modalContentPanel = new EmptyPanel("content");
//		ArkFunction associatedPrimaryFn = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
//		CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCPM = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
//		phenoDataCPM.getObject().setArkFunction(associatedPrimaryFn);
//		modalContentPanel = new PhenoDataEntryContainerPanel("content", phenoDataCPM).initialisePanel();
		
		initialiseDataView();
		initialiseNewButton();

		add(modalWindow);
	}

	@Override
	public void onBeforeRender() {
		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				// Successfully loaded from backend
				cpModel.getObject().getCustomFieldGroup().setStudy(study);
				cpModel.getObject().getPhenotypicCollection().setLinkSubjectStudy(linkSubjectStudy);
			}
		}

		super.onBeforeRender();
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		phenotypicCollectionProvider = new ArkDataProvider2<PhenoDataCollectionVO, PhenotypicCollection>() {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				return iPhenotypicService.getPhenotypicCollectionCount(criteriaModel.getObject());
			}

			public Iterator<PhenotypicCollection> iterator(int first, int count) {
				List<PhenotypicCollection> biospecimenList = new ArrayList<PhenotypicCollection>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					biospecimenList = iPhenotypicService.searchPageablePhenotypicCollections(criteriaModel.getObject(), first, count);
				}
				return biospecimenList.iterator();
			}
		};
		// Set the criteria into the data provider's model
		phenotypicCollectionProvider.setCriteriaModel(cpModel);

		dataView = buildDataView(phenotypicCollectionProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataViewListWMC);
			}
		};
		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);
		add(dataViewListWMC);

	}

	private void initialiseNewButton() {
		newButton = new ArkBusyAjaxButton("listNewButton", new StringResourceModel("listNewKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				boolean isVisible = true;

				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && sessionSubjectUID != null);

				return isVisible;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to proceed with New Biospecimen");
			}
		};
		newButton.setDefaultFormProcessing(false);

		add(newButton);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public DataView<PhenotypicCollection> buildDataView(ArkDataProvider2<PhenoDataCollectionVO, PhenotypicCollection> phenotypicCollectionProvider) {

		DataView<PhenotypicCollection> phenotypicCollectionDataView = new DataView<PhenotypicCollection>("phenotypicCollectionList", phenotypicCollectionProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<PhenotypicCollection> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final PhenotypicCollection phenotypicCollection = item.getModelObject();

				WebMarkupContainer rowDetailsWMC = new WebMarkupContainer("rowDetailsWMC", item.getModel());
				ArkBusyAjaxLink listDetailsLink = new ArkBusyAjaxLink("listDetailsLink") {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						PhenotypicCollection phenotypicCollection = (PhenotypicCollection) (getParent().getDefaultModelObject());
						CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
						newModel.getObject().getPhenotypicCollection().setId(phenotypicCollection.getId());
						newModel.getObject().setCustomFieldGroup(cpModel.getObject().getCustomFieldGroup());
						showModalWindow(target, newModel);
					}
				};

				idLbl = new Label("phenotypicCollection.id", String.valueOf(phenotypicCollection.getId()));
				listDetailsLink.add(idLbl);
				rowDetailsWMC.add(listDetailsLink);				

				questionnaireLbl = new Label("phenotypicCollection.questionnaire", phenotypicCollection.getQuestionnaire().getName());
				nameLbl = new Label("phenotypicCollection.name", phenotypicCollection.getName());
				descriptionLbl = new Label("phenotypicCollection.description", phenotypicCollection.getDescription());
				statusLbl = new Label("phenotypicCollection.status", phenotypicCollection.getStatus().getName());
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				recordDateLbl = new Label("phenotypicCollection.recordDate", simpleDateFormat.format(phenotypicCollection.getRecordDate()));
				
				if (phenotypicCollection.getReviewedDate() == null){
					reviewedDateLbl = new Label("phenotypicCollection.reviewedDate", "");
				}
				else {
					reviewedDateLbl = new Label("phenotypicCollection.reviewedDate", simpleDateFormat.format(phenotypicCollection.getReviewedDate()));	
				}

				item.add(rowDetailsWMC);
				item.add(nameLbl);
				item.add(questionnaireLbl);
				item.add(descriptionLbl);
				item.add(statusLbl);
				item.add(recordDateLbl);
				item.add(reviewedDateLbl);
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

		};

		return phenotypicCollectionDataView;
	}

	protected void onNew(AjaxRequestTarget target) {
		// Needs CREATE permission AND at least one CustomFieldGroup (Questionnaire) to select from
		boolean hasQuestionnaires = false;

		CustomFieldGroup questionnaire = new CustomFieldGroup();
		questionnaire.setArkFunction(cpModel.getObject().getArkFunction());
		questionnaire.setStudy(cpModel.getObject().getPhenotypicCollection().getLinkSubjectStudy().getStudy());
		hasQuestionnaires = (iArkCommonService.getCustomFieldGroupCount(questionnaire) > 0);

		if (hasQuestionnaires) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			newModel.getObject().getPhenotypicCollection().setLinkSubjectStudy(getModelObject().getPhenotypicCollection().getLinkSubjectStudy());
			// the following should be replaced by a real "Questionnaire" selection when the new form is presented
			newModel.getObject().getPhenotypicCollection().setQuestionnaire(questionnaire);
			newModel.getObject().setCustomFieldGroup(cpModel.getObject().getCustomFieldGroup());

			showModalWindow(target, newModel); // listDetailsForm);
		}
		else {
			this.error("No Phenotypic Questionnaires exist. Please create at least one Phenotypic Questionnaire for the study.");
		}
		// refresh the feedback messages
		target.add(feedbackPanel);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY));
		modalContentPanel = new PhenotypicCollectionModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Phenotypic Collection Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}

	/**
	 * @return the newButton
	 */
	public ArkBusyAjaxButton getNewButton() {
		return newButton;
	}

	/**
	 * @param newButton the newButton to set
	 */
	public void setNewButton(ArkBusyAjaxButton newButton) {
		this.newButton = newButton;
	}
}