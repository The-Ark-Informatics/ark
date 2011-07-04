package au.org.theark.lims.web.component.subjectSub.biospecimen.form;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.security.ArkSecurity;
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
import au.org.theark.lims.web.component.subjectSub.biospecimen.DetailPanel;
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
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private TextField<String>						sampleTypeTxtFld;
	private TextField<String>						collectionTxtFld;
	private TextField<String>						commentsTxtFld;
	private TextField<String>						quantityTxtFld;
	private ListDetailPanel							listDetailPanel;
	private DetailPanel								detailPanel;
	private DetailModalWindow						modalWindow;

	public ListDetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, DetailModalWindow modalWindow, ListDetailPanel listDetailPanel)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.modalWindow = modalWindow;
		this.listDetailPanel = listDetailPanel;
		
		this.detailPanel = new DetailPanel("content", modalWindow, containerForm);
		
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
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
		
		modalWindow.setTitle("Biospecimen Detail");
		modalWindow.setContent(detailPanel);
		modalWindow.setListDetailPanel(listDetailPanel);
		
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
				return (ArkSecurity.isActionPermitted(au.org.theark.core.Constants.NEW) && hasBioCollections);
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
				final Biospecimen biospecimen = item.getModelObject();

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
						
						Biospecimen bioSpecimenSelected = biospecimen;
						containerForm.getModelObject().setBiospecimen(bioSpecimenSelected);
							
						// Set the modalWindow title and content
						modalWindow.setListDetailForm(listDetailsForm);
						modalWindow.show(target);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkSecurity.isActionPermitted(au.org.theark.core.Constants.EDIT);
					}
				};
				
				item.addOrReplace(listEditButton);

				idTxtFld = new TextField<String>("id");
				idTxtFld.setEnabled(false);
				nameTxtFld = new TextField<String>("biospecimenId");
				sampleTypeTxtFld = new TextField<String>("sampleType.name");
				collectionTxtFld = new TextField<String>("bioCollection.name");
				commentsTxtFld = new TextField<String>("comments");
				quantityTxtFld = new TextField<String>("quantity");

				item.add(idTxtFld);
				item.add(nameTxtFld);
				item.add(sampleTypeTxtFld);
				item.add(collectionTxtFld);
				item.add(commentsTxtFld);
				item.add(quantityTxtFld);

				if (biospecimen.getId() != null)
				{
					item.get("id").setEnabled(false);
					item.get("biospecimenId").setEnabled(false);
					item.get("sampleType.name").setEnabled(false);
					item.get("bioCollection.name").setEnabled(false);
					item.get("comments").setEnabled(false);
					item.get("quantity").setEnabled(false);
				}

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
						try
						{
							LimsVO limsVo = (LimsVO) form.getModelObject();
							limsVo.setBiospecimen(biospecimen);
							iLimsService.deleteBiospecimen(limsVo);
							this.info("Biospecimen " + biospecimen.getBiospecimenId() + " was deleted successfully");
						}
						catch(org.hibernate.NonUniqueObjectException noe)
						{
							this.error(noe.getMessage());
							target.addComponent(form);
						}

						// Display delete confirmation message
						target.addComponent(feedbackPanel);
					}

					@Override
					public boolean isEnabled()
					{
						return (biospecimen.getId() != null);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkSecurity.isActionPermitted(au.org.theark.core.Constants.DELETE);
					}

				};
				item.addOrReplace(deleteButton);

				attachValidators();
			}
		};

		return (AbstractListEditor<Biospecimen>) listEditor;
	}

	public void initialiseList()
	{
		java.util.List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>(0);
		try
		{
			biospecimenList = iLimsService.searchBiospecimen(getModelObject().getBiospecimen());
			getModelObject().setBiospecimenList(biospecimenList);
		}
		catch (ArkSystemException e)
		{
			error(e.getMessage());
		}
	}

	protected void attachValidators()
	{
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
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
		
		// Set the modalWindow title and content
		modalWindow.setListDetailForm(listDetailsForm);
		modalWindow.show(target);
	}
}