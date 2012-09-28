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
package au.org.theark.lims.web.component.subjectlims.lims.biocollection.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionModalDetailPanel;

/**
 * @author cellis
 * @author elam
 * 
 */
@SuppressWarnings({ "unchecked"})
public class BioCollectionListForm extends Form<LimsVO> {

	private static final long										serialVersionUID	= 1L;
	private static final Logger									log					= LoggerFactory.getLogger(BioCollectionListForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>								iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService												iLimsService;

	protected CompoundPropertyModel<LimsVO>					cpModel;
	protected FeedbackPanel											feedbackPanel;
	protected AbstractDetailModalWindow							modalWindow;

	private Label														idLblFld;
	private Label														nameLblFld;
	private Label														commentsLblFld;
	private Label														collectionDateLblFld;
	private Label														surgeryDateLblFld;

	private Panel														modalContentPanel;
	protected ArkBusyAjaxButton									newButton;

	protected WebMarkupContainer									dataViewListWMC;
	private DataView<BioCollection>								dataView;
	private ArkDataProvider<BioCollection, ILimsService>	bioColectionProvider;

	public BioCollectionListForm(String id, FeedbackPanel feedbackPanel, AbstractDetailModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}

	public void initialiseForm() {
		modalContentPanel = new EmptyPanel("content");

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
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBioCollection().setStudy(study);
			}
		}

		super.onBeforeRender();
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		bioColectionProvider = new ArkDataProvider<BioCollection, ILimsService>(iLimsService) {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				return (int)service.getBioCollectionCount(model.getObject());
			}

			public Iterator<BioCollection> iterator(int first, int count) {
				List<BioCollection> listCollection = new ArrayList<BioCollection>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableBioCollections(model.getObject(), first, count);
				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		bioColectionProvider.setModel(new LoadableDetachableModel<BioCollection>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected BioCollection load() {
				return cpModel.getObject().getBioCollection();
			}
		});

		dataView = buildDataView(bioColectionProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {

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
		newButton = new ArkBusyAjaxButton(Constants.NEW) {

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
				this.error("Unexpected error: Unable to proceed with New BioCollection");
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
	public DataView<BioCollection> buildDataView(ArkDataProvider<BioCollection, ILimsService> bioCollectionProvider) {

		DataView<BioCollection> bioCollectionDataView = new DataView<BioCollection>("collectionList", bioCollectionProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<BioCollection> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final BioCollection bioCollection = item.getModelObject();

				WebMarkupContainer rowDetailsWMC = new WebMarkupContainer("rowDetailsWMC", item.getModel());
				ArkBusyAjaxLink listDetailsLink = new ArkBusyAjaxLink("listDetailsLink") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						BioCollection bc = (BioCollection) (getParent().getDefaultModelObject());
						CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
						newModel.getObject().getBioCollection().setId(bc.getId());
						showModalWindow(target, newModel);
					}

				};

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				idLblFld = new Label("bioCollection.id", String.valueOf(bioCollection.getId()));
				
				nameLblFld = new Label("bioCollection.name", bioCollection.getName());
				listDetailsLink.add(nameLblFld);
				rowDetailsWMC.add(listDetailsLink);

				if (bioCollection.getCollectionDate() != null) {
					collectionDateLblFld = new Label("bioCollection.collectionDate", simpleDateFormat.format(bioCollection.getCollectionDate()));
				}
				else {
					collectionDateLblFld = new Label("bioCollection.collectionDate", "");
				}

				if (bioCollection.getSurgeryDate() != null) {
					surgeryDateLblFld = new Label("bioCollection.surgeryDate", simpleDateFormat.format(bioCollection.getSurgeryDate()));
				}
				else {
					surgeryDateLblFld = new Label("bioCollection.surgeryDate", "");
				}

				commentsLblFld = new Label("bioCollection.comments", bioCollection.getComments());

				item.add(idLblFld);
				item.add(rowDetailsWMC);
				item.add(collectionDateLblFld);
				item.add(surgeryDateLblFld);
				item.add(commentsLblFld);

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}

		};

		return bioCollectionDataView;
	}

	protected void onNew(AjaxRequestTarget target) {
		// refresh the feedback messages
		target.add(feedbackPanel);

		// Set new BioCollection into model, then show modalWindow to save
		CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		Study study = getModelObject().getLinkSubjectStudy().getStudy();
		
		if(study!=null && !study.getAutoGenerateBiocollectionUid()){
			newModel.getObject().getBioCollection().setName("");
		}
		else{
			newModel.getObject().getBioCollection().setName(Constants.AUTO_GENERATED);
		}
		
		newModel.getObject().getBioCollection().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
		newModel.getObject().getBioCollection().setStudy(study);
		
		showModalWindow(target, newModel);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<LimsVO> cpModel) {
		modalContentPanel = new BioCollectionModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Collection Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onClose(AjaxRequestTarget target)
         {
				// Refresh the list form whenever the modalwindow is closed
         	target.add(BioCollectionListForm.this);	
         }
		});
	}
}
