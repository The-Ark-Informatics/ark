package au.org.theark.lims.web.component.subject.biospecimen.form;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkSecurity;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BiospecimenIdGenerator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.biospecimen.DetailModalWindow;

/**
 * @author cellis
 * 
 */
public class ListDetailForm extends Form<LimsVO>
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
	protected FeedbackPanel							feedBackPanel;

	private LinkSubjectStudy						linkSubjectStudy;
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private TextField<String>						sampleTypeTxtFld;
	private TextField<String>						collectionTxtFld;
	private TextField<String>						commentsTxtFld;
	private TextField<String>						quantityTxtFld;
	private DetailModalWindow						modalWindow;
	private ArkCrudContainerVO						arkCrudContainerVo	= new ArkCrudContainerVO();
	private Boolean									hasBioCollections = false;

	public ListDetailForm(String id, CompoundPropertyModel<LimsVO> compoundPropertyModel, FeedbackPanel feedBackPanel)
	{
		super(id, compoundPropertyModel);
		this.feedBackPanel = feedBackPanel;
		modalWindow = new DetailModalWindow("detailModalWindow", arkCrudContainerVo, this)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1230939436872065515L;

			@Override
			public void close(AjaxRequestTarget target)
			{
				super.close(target);
				onCloseModalWindow(target);
			}
		};
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
	}

	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		setModelObject(new LimsVO());
		getModelObject().setLinkSubjectStudy(linkSubjectStudy);
		getModelObject().setBiospecimen(new Biospecimen());
		Study study = linkSubjectStudy.getStudy();
		getModelObject().getBiospecimen().setStudy(study);
		getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
		
		initialiseForm();
		target.addComponent(this);
		target.addComponent(feedBackPanel);
	}

	@SuppressWarnings("unchecked")
	public void initialiseForm()
	{
		initialiseList();
		listEditor = new AbstractListEditor<Biospecimen>("biospecimens", new PropertyModel(getModelObject(), "biospecimenList"))
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
						// Set selected item into model.context, then show modalWindow for editing
						Form<LimsVO> containerForm = (Form<LimsVO>) form;
						containerForm.setModelObject(new LimsVO());
						containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
						containerForm.getModelObject().setBiospecimen(biospecimen);
						Study study = linkSubjectStudy.getStudy();
						containerForm.getModelObject().getBiospecimen().setStudy(study);
						containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
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
					protected void onDeleteConfirmed(AjaxRequestTarget target)
					{
						LimsVO limsVo = new LimsVO();
						limsVo.setBiospecimen(biospecimen);
						iLimsService.deleteBiospecimen(limsVo);
						this.info("Biospecimen " + biospecimen.getBiospecimenId() + " was deleted successfully");

						// Display delete confirmation message
						target.addComponent(feedBackPanel);
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

		addComponents();
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
		// Used when editing ListDetail directly (ie not using modalWindow)
		//nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
	}

	@SuppressWarnings("unchecked")
	private void addComponents()
	{
		AjaxButton newButton = new AjaxButton("listNewButton")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8505652280527122102L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Set new LimsVO into model for new Biospecimen, then show modalWindow to save
				Form<LimsVO> containerForm = (Form<LimsVO>) form;
				containerForm.setModelObject(new LimsVO());
				containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
				containerForm.getModelObject().setBiospecimen(new Biospecimen());
				Study study = linkSubjectStudy.getStudy();
				containerForm.getModelObject().getBiospecimen().setStudy(study);
				containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
				
				// TODO: Replace with Study specific generated biospecimenId's
				// Create new BiospecimenUID
				containerForm.getModelObject().getBiospecimen().setBiospecimenId(BiospecimenIdGenerator.generateBiospecimenId());
				
				modalWindow.show(target);
			}

			@Override
			public boolean isVisible()
			{
				// Needs CREATE permission AND a BioCollection to select from
				hasBioCollections = iLimsService.hasBioCollections(linkSubjectStudy);
				
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
		};
		
		newButton.setDefaultFormProcessing(false);

		addOrReplace(newButton);
		addOrReplace(listEditor);
		addOrReplace(modalWindow);
	}

	@SuppressWarnings("unchecked")
	protected void onSave(Form<?> form, AjaxRequestTarget target)
	{
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Form<LimsVO> containerForm = (Form<LimsVO>) form;

		java.util.List<Biospecimen> biospecimenList = containerForm.getModelObject().getBiospecimenList();

		for (Iterator iterator = biospecimenList.iterator(); iterator.hasNext();)
		{
			Biospecimen biospecimen = (Biospecimen) iterator.next();
			try
			{
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
				biospecimen.setLinkSubjectStudy(linkSubjectStudy);
				biospecimen.setStudy(linkSubjectStudy.getStudy());
				LimsVO limsVo = new LimsVO();
				limsVo.setBiospecimen(biospecimen);

				if (limsVo.getBiospecimen().getId() == null)
				{
					// Save
					iLimsService.createBiospecimen(limsVo);
					this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenId() + " was created successfully");
					processErrors(target);
				}
				else
				{
					// Update
					iLimsService.updateBioCollection(limsVo);
					this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenId() + " was updated successfully");
					processErrors(target);
				}
			}
			catch (EntityNotFoundException e)
			{
				error(e.getMessage());
				target.addComponent(feedBackPanel);
			}

		}
	}

	protected void saveOnErrorProcess(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
		target.addComponent(this);
	}

	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
		target.addComponent(this);
	}

	/**
	 * @param linkSubjectStudy
	 *           the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy)
	{
		this.linkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy()
	{
		return linkSubjectStudy;
	}
}