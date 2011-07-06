package au.org.theark.lims.web.component.subjectSub.biospecimen.form;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.AbstractListDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BiospecimenIdGenerator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectSub.DetailModalWindow;
import au.org.theark.lims.web.component.subjectSub.biospecimen.BiospecimenModalDetailPanel;
import au.org.theark.lims.web.component.subjectSub.biospecimen.ListDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListDetailForm extends AbstractListDetailForm<LimsVO>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1613064945179661988L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	private AbstractListEditor<Biospecimen>	listEditor				= null;
	protected FeedbackPanel							feedbackPanel;

	private LinkSubjectStudy						linkSubjectStudy;
	private Label									idLblFld;
	private Label									nameLblFld;
	private Label									sampleTypeLblFld;
	private Label									collectionLblFld;
	private Label									commentsLblFld;
	private Label									quantityLblFld;
	private ListDetailPanel							listDetailPanel;
	private BiospecimenModalDetailPanel				detailSubPanel;
	private DetailModalWindow						modalWindow;

	public ListDetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, DetailModalWindow modalWindow, ListDetailPanel listDetailPanel)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.modalWindow = modalWindow;
		this.listDetailPanel = listDetailPanel;
		
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		this.detailSubPanel = new BiospecimenModalDetailPanel("content", modalWindow, containerForm, listDetailPanel);
		
		if(subjectUID != null)
		{
			try
			{
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
			}
			catch (EntityNotFoundException e)
			{
			}
		}
		
		// Override newButton, determining if bioCollections exist
		initialiseNewButton();
	}

	private void initialiseNewButton() 
	{
		newButton = new AjaxButton("listNewButton")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8505652280527122102L;

			@Override
			public boolean isVisible()
			{
				// Needs CREATE permission AND a BioCollection to select from
				boolean hasBioCollections = false;
				
				if(linkSubjectStudy!= null)
				{
					hasBioCollections = iLimsService.hasBioCollections(linkSubjectStudy);
				}
				else
				{
					hasBioCollections = iLimsService.hasBioCollections(containerForm.getModelObject().getLinkSubjectStudy());
				}
				
				if(!hasBioCollections)
				{
					hasBioCollections = false;
					this.error("No Biospecimen Collections exist. Please create at least one Collection.");
				}
				else
				{
					hasBioCollections = true;
				}
				return (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && hasBioCollections);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(target, containerForm);	
			}
		};
		
		newButton.setDefaultFormProcessing(false);

		addOrReplace(newButton);
	}

	@Override
	protected AbstractListEditor<Biospecimen> initialiseListEditor()
	{
		listEditor = new AbstractListEditor<Biospecimen>("listDetails", new PropertyModel(containerForm.getModelObject(), "biospecimenList"))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -2309178929151712969L;

			@Override
			protected void onPopulateItem(final ListItem<Biospecimen> item)
			{
				item.setOutputMarkupId(true);
				item.setModel(new CompoundPropertyModel(item.getModel()));
				final Biospecimen bioSpecimenSelected = item.getModelObject();

				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel("editKey", this, null))
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -6032731528995858376L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						// Refresh any feedback
						target.addComponent(feedbackPanel);
						
						// Set selected item into model.context, then show modalWindow for editing
						Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
						
						containerForm.getModelObject().setBiospecimen(bioSpecimenSelected);						

//						if (bioSpecimenSelected.getId() != null) {
//							// Reload the biospecimen from the database
//							try {
//								containerForm.getModelObject().setBiospecimen(iLimsService.getBiospecimen(bioSpecimenSelected.getId()));
//								containerForm.getModelObject().getBiospecimen().setComments("no modal window test");
//								iLimsService.updateBiospecimen(containerForm.getModelObject());
//							} catch (EntityNotFoundException enfe) {
//								log.error(enfe.toString());
//								this.error("Unable to edit the selected biospecimen - biospecimen record is no longer valid (e.g. has since been removed)");
//							}
//						}
						showModalWindow(target, listDetailsForm);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.EDIT);
					}
				};
				
				item.addOrReplace(listEditButton);

				idLblFld = new Label("id", item.getModelObject().getId().toString());
				nameLblFld = new Label("biospecimenId");
				sampleTypeLblFld = new Label("sampleType.name");
				collectionLblFld = new Label("bioCollection.name");
				commentsLblFld = new Label("comments");
				quantityLblFld = new Label("quantity");

				item.add(idLblFld);
				item.add(nameLblFld);
				item.add(sampleTypeLblFld);
				item.add(collectionLblFld);
				item.add(commentsLblFld);
				item.add(quantityLblFld);

				AjaxListDeleteButton deleteButton = new AjaxListDeleteButton("listDeleteButton", new StringResourceModel("confirmDelete", this, null),
						new StringResourceModel(Constants.DELETE, this, null))
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -585048033031888283L;

					@Override
					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form)
					{
//						try
//						{
							LimsVO limsVo = (LimsVO) form.getModelObject();
							limsVo.setBiospecimen(bioSpecimenSelected);
							iLimsService.deleteBiospecimen(limsVo);
							this.info("Biospecimen " + bioSpecimenSelected.getBiospecimenId() + " was deleted successfully");
//						}
//						catch(org.hibernate.NonUniqueObjectException noe)
//						{
//							this.error(noe.getMessage());
//							target.addComponent(form);
//						}

						// Display delete confirmation message
						target.addComponent(feedbackPanel);
						target.addComponent(form);
					}

					@Override
					public boolean isEnabled()
					{
						return (bioSpecimenSelected.getId() != null);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.DELETE);
					}

				};
				item.addOrReplace(deleteButton);
				
				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 7193815665666917507L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));

				attachValidators();
			}
		};

		return (AbstractListEditor<Biospecimen>) listEditor;
	}

//	public void initialiseList()
//	{
//		java.util.List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>(0);
//		try
//		{
//			Biospecimen biospecimen = new Biospecimen();
//			biospecimen.setLinkSubjectStudy(linkSubjectStudy);
//			biospecimen.setStudy(linkSubjectStudy.getStudy());
//			biospecimenList = iLimsService.searchBiospecimen(biospecimen);
//			getModelObject().setBiospecimenList(biospecimenList);
//		}
//		catch (ArkSystemException e)
//		{
//			error(e.getMessage());
//		}
//	}

	protected void attachValidators()
	{
	}

	/**
	 * @return the listDetailPanel
	 */
	public ListDetailPanel getListDetailPanel()
	{
		return listDetailPanel;
	}

	/**
	 * @param listDetailPanel the listDetailPanel to set
	 */
	public void setListDetailPanel(ListDetailPanel listDetailPanel)
	{
		this.listDetailPanel = listDetailPanel;
	}

	protected void saveOnErrorProcess(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		target.addComponent(this);
	}

	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		target.addComponent(this);
	}

	@Override
	protected void onNew(AjaxRequestTarget target, Form<LimsVO> form)
	{
		// refresh the feedback messages 
		target.addComponent(feedbackPanel);
		
		// Set new LimsVO into model for new Biospecimen, then show modalWindow to save
		Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
		containerForm.getModelObject().setBiospecimen(new Biospecimen());
		
		if(linkSubjectStudy != null)
		{
			containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
			containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
			containerForm.getModelObject().getBiospecimen().setStudy(linkSubjectStudy.getStudy());
		}
		else
		{
			containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
			containerForm.getModelObject().getBiospecimen().setStudy(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
		}
		
		// TODO: Replace with Study specific generated biospecimenId's
		// Create new BiospecimenUID
		containerForm.getModelObject().getBiospecimen().setBiospecimenId(BiospecimenIdGenerator.generateBiospecimenId());
		
		showModalWindow(target, listDetailsForm);
	}
	
	protected void showModalWindow(AjaxRequestTarget target, Form<LimsVO> form)
	{
		// Set the modalWindow title and content
		modalWindow.setTitle("Biospecimen Detail");
		modalWindow.setContent(detailSubPanel);
		modalWindow.setListDetailPanel(listDetailPanel);
		modalWindow.setListDetailForm(this);
		modalWindow.show(target);
	}
}