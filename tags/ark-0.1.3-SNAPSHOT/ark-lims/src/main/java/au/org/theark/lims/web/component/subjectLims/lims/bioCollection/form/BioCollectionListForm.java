package au.org.theark.lims.web.component.subjectLims.lims.bioCollection.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.bioCollection.CollectionModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked", "rawtypes" })
public class BioCollectionListForm extends Form<LimsVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;

	protected CompoundPropertyModel<LimsVO> cpModel;
	protected FeedbackPanel feedbackPanel;
	protected AbstractDetailModalWindow modalWindow;

	protected WebMarkupContainer dataviewWMC;
	private Label idLblFld;
	private Label nameLblFld;
	private Label commentsLblFld;
	private Label collectionDateLblFld;
	private Label surgeryDateLblFld;
	// private BioCollectionListPanel bioCollectionListPanel;
	private Panel modalContentPanel;
	protected AjaxButton newButton;

	protected WebMarkupContainer dataViewListWMC;
	private DataView<BioCollection> dataView;
	private ArkDataProvider<BioCollection, ILimsService> bioColectionProvider;

	public BioCollectionListForm(String id, FeedbackPanel feedbackPanel,
			AbstractDetailModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
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
		// Reset the BioCollection (for criteria) in LimsVO
		// This prevents the manual modal "X" close button from not reseting the criteria
		cpModel.getObject().setBioCollection(new BioCollection());
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

			public int size() {
				return service.getBioCollectionCount(model.getObject());
			}

			public Iterator<BioCollection> iterator(int first, int count) {
				List<BioCollection> listCollection = new ArrayList<BioCollection>();
				if (ArkPermissionHelper
						.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableBioCollections(model
							.getObject(), first, count);
				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		bioColectionProvider.setModel(new LoadableDetachableModel<BioCollection>() {
			@Override
			protected BioCollection load() {
				return cpModel.getObject().getBioCollection();
			}
		});
		
		dataView = buildDataView(bioColectionProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(dataViewListWMC);
			}
		};
		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);
		add(dataViewListWMC);

	}

	private void initialiseNewButton() {
		newButton = new AjaxButton("listNewButton", new StringResourceModel(
				"listNewKey", this, null)) {
			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (form.equals(this)) {
					target.addComponent(feedbackPanel);
					target.addComponent(this);
				}
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
	public DataView<BioCollection> buildDataView(
			ArkDataProvider<BioCollection, ILimsService> bioCollectionProvider) {

		DataView<BioCollection> bioCollectionDataView = new DataView<BioCollection>(
				"collectionList", bioCollectionProvider) {
			@Override
			protected void populateItem(final Item<BioCollection> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final BioCollection bioCollection = item.getModelObject();

				WebMarkupContainer rowEditWMC = new WebMarkupContainer("rowEditWMC", item.getModel());
				/*
				 * When any AjaxButton in the form is clicked on, it eventually calls form.inputChanged().
				 * This then goes through all its children to check/call isVisible() and isEnabled().
				 * By avoiding the use of AjaxButtons when not required, no form submit is caused and thus
				 * less processing is required.
				 */
//				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel("editKey", this, null)) {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = -6032731528995858376L;
//
//					@Override
//					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						// Refresh any feedback
////						target.addComponent(feedbackPanel);
//
//						// // Set selected item into model.context, then show
//						// modalWindow for editing
//						// Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
//						//						
//						BioCollection bc = (BioCollection)(getParent().getDefaultModelObject());
//						cpModel.getObject().setBioCollection(bc);
//						showModalWindow(target, cpModel);
//					}
//
//					@Override
//					public boolean isVisible() {
//						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.EDIT);
//					}
//				};
				AjaxLink listEditLink = new AjaxLink("listEditLink") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						BioCollection bc = (BioCollection)(getParent().getDefaultModelObject());
						CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
						newModel.getObject().getBioCollection().setId(bc.getId());
						showModalWindow(target, newModel);
					}
					
					@Override
					public boolean isVisible() {
						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.EDIT);
					}
				};
				
//				listEditButton.setDefaultFormProcessing(false);
				Label nameLinkLabel = new Label("lblEditLink", "Edit");
				listEditLink.add(nameLinkLabel);
				rowEditWMC.add(listEditLink);
//				rowEditWMC.add(listEditButton);
				item.add(rowEditWMC);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						au.org.theark.core.Constants.DD_MM_YYYY);

				idLblFld = new Label("bioCollection.id", String.valueOf(bioCollection.getId()));
				nameLblFld = new Label("bioCollection.name", bioCollection.getName());

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
				item.add(nameLblFld);
				item.add(collectionDateLblFld);
				item.add(surgeryDateLblFld);
				item.add(commentsLblFld);

				WebMarkupContainer rowDeleteWMC = new WebMarkupContainer("rowDeleteWMC", item.getModel());
				AjaxButton deleteButton = new AjaxButton("listDeleteButton", new StringResourceModel(Constants.DELETE, this, null)) {
					IModel confirm = new StringResourceModel("confirmDelete", this, null);
					/**
					 * 
					 */
					private static final long serialVersionUID = -585048033031888283L;

					/*
					 * When any AjaxButton in the form is clicked on, it eventually calls form.inputChanged().
					 * This then goes through all its children to check/call isVisible() and isEnabled().
					 * Thus, it is best to keep the isVisible() and isEnabled() very light-weight
					 * (e.g. avoid hitting the database to work this out)
					 */ 
//					@Override
//					public boolean isEnabled() {
//						final BioCollection bioCollectionSelected = (BioCollection)(getParent().getDefaultModelObject());
//						return (!iLimsService.hasBiospecimens(bioCollectionSelected));
//					}

					@Override
					public boolean isVisible() {
						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.DELETE);
					}

					@Override
					protected IAjaxCallDecorator getAjaxCallDecorator() {
						return new AjaxPreprocessingCallDecorator(super
								.getAjaxCallDecorator()) {
							private static final long serialVersionUID = 7495281332320552876L;

							@Override
							public CharSequence preDecorateScript(
									CharSequence script) {
								StringBuffer sb = new StringBuffer();
								sb.append("if(!confirm('");
								sb.append(confirm.getObject());
								sb.append("'))");
								sb.append("{ ");
								sb.append("	return false ");
								sb.append("} else { ");
								sb.append("	this.disabled = true;" );
								sb.append("};");
								sb.append(script);
								return sb;
							}
						};
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						target.addComponent(form);
						onDeleteConfirmed(target, form);
					}

					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
						final BioCollection bioCollectionSelected = (BioCollection)(getParent().getDefaultModelObject());
						if (!iLimsService.hasBiospecimens(bioCollectionSelected)) {
							// Leave the cpModel's BioCollection as-is
							LimsVO bioCollectionSelectdLimsVO = new LimsVO();
							bioCollectionSelectdLimsVO.setBioCollection(bioCollectionSelected);

							iLimsService.deleteBioCollection(bioCollectionSelectdLimsVO);
							this.info("Biospecimen collection " + bioCollectionSelected.getName() + " was deleted successfully");
					
							// Display delete confirmation message
							target.addComponent(feedbackPanel);
							target.addComponent(form);			
						}
						else {
							this.error("Biospecimen collection " + bioCollectionSelected.getName() + " can not be deleted because there are biospecimens attached");
							target.addComponent(feedbackPanel);
						}
					}

				};
				deleteButton.setDefaultFormProcessing(false);
				rowDeleteWMC.add(deleteButton);
				item.add(rowDeleteWMC);

				item.add(new AttributeModifier(Constants.CLASS, true,
						new AbstractReadOnlyModel() {

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
		target.addComponent(feedbackPanel);

		// Set new BioCollection into model, then show modalWindow to save
		CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
//		newModel.getObject().setBioCollection(new BioCollection());
		newModel.getObject().getBioCollection().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
		newModel.getObject().getBioCollection().setStudy(getModelObject().getLinkSubjectStudy().getStudy());

		showModalWindow(target, newModel); // listDetailsForm);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<LimsVO> cpModel) {
		modalContentPanel = new CollectionModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Collection Detail");
		modalWindow.setContent(modalContentPanel);
		// modalWindow.setListDetailPanel(listDetailPanel);
		// modalWindow.setListDetailForm(this);
		modalWindow.show(target);
	}

}