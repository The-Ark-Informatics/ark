package au.org.theark.lims.web.component.subject.bioCollection.form;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
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
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.bioCollection.DetailModalWindow;

/**
 * @author cellis
 * 
 */
public class ListDetailForm extends Form<LimsVO>
{
	/**
	 * 
	 */
	private static final long						serialVersionUID		= 5402491244761953070L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	private AbstractListEditor<BioCollection>	listEditor				= null;
	protected FeedbackPanel							feedBackPanel;

	private LinkSubjectStudy						linkSubjectStudy;
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private TextField<String>						commentsTxtFld;
	private DateTextField							collectionDateTxtFld;
	private DateTextField							surgeryDateTxtFld;
	private DetailModalWindow						modalWindow;
	private ArkCrudContainerVO						arkCrudContainerVo	= new ArkCrudContainerVO();

	public ListDetailForm(String id, FeedbackPanel feedBackPanel)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
	}

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
		getModelObject().setBioCollection(new BioCollection());
		Study study = linkSubjectStudy.getStudy();
		getModelObject().getBioCollection().setStudy(study);
		getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
		
		initialiseForm();
		target.addComponent(this);
		target.addComponent(feedBackPanel);
	}

	@SuppressWarnings("unchecked")
	public void initialiseForm()
	{
		initialiseList();
		listEditor = new AbstractListEditor<BioCollection>("bioCollections", new PropertyModel(getModelObject(), "bioCollectionList"))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1972072222086035973L;

			@Override
			protected void onPopulateItem(final ListItem<BioCollection> item)
			{
				item.setOutputMarkupId(true);
				item.setModel(new CompoundPropertyModel(item.getModel()));
				final BioCollection bioCollection = item.getModelObject();

				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel("editKey", this, null))
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -6032731528995858376L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						// used to edit item within listEditor 
						//item.get("id").setEnabled(true);
						//item.get("name").setEnabled(true);
						//item.get("collectionDate").setEnabled(true);
						//item.get("surgeryDate").setEnabled(true);
						//item.get("comments").setEnabled(true);
						//target.addComponent(item);

						// Set selected item into model.context, then show modalWindow for editing
						Form<LimsVO> containerForm = (Form<LimsVO>) form;
						containerForm.setModelObject(new LimsVO());
						containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
						containerForm.getModelObject().setBioCollection(bioCollection);
						Study study = linkSubjectStudy.getStudy();
						containerForm.getModelObject().getBioCollection().setStudy(study);
						containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
						modalWindow.show(target);
					}

					@Override
					public boolean isEnabled()
					{
						return (bioCollection.getId() != null);
					}
				};
				
				item.addOrReplace(listEditButton);

				idTxtFld = new TextField<String>("id");
				idTxtFld.setEnabled(false);
				nameTxtFld = new TextField<String>("name");
				collectionDateTxtFld = new DateTextField("collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
				surgeryDateTxtFld = new DateTextField("surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);
				commentsTxtFld = new TextField<String>("comments");

				ArkDatePicker datePicker = new ArkDatePicker();
				datePicker.bind(collectionDateTxtFld);
				collectionDateTxtFld.add(datePicker);

				ArkDatePicker datePicker2 = new ArkDatePicker();
				datePicker2.bind(surgeryDateTxtFld);
				surgeryDateTxtFld.add(datePicker2);

				item.add(idTxtFld);
				item.add(nameTxtFld);
				item.add(collectionDateTxtFld);
				item.add(surgeryDateTxtFld);
				item.add(commentsTxtFld);

				if (bioCollection.getId() != null)
				{
					item.get("id").setEnabled(false);
					item.get("name").setEnabled(false);
					item.get("collectionDate").setEnabled(false);
					item.get("surgeryDate").setEnabled(false);
					item.get("comments").setEnabled(false);
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
						limsVo.setBioCollection(bioCollection);
						iLimsService.deleteBioCollection(limsVo);
						this.info("Biospecimen collection " + bioCollection.getName() + " was deleted successfully");

						// Display delete confirmation message
						target.addComponent(feedBackPanel);
					}

					@Override
					public boolean isEnabled()
					{
						return (bioCollection.getId() != null);
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
		java.util.List<BioCollection> bioCollectionList = new ArrayList<BioCollection>(0);
		try
		{
			bioCollectionList = iLimsService.searchBioCollection(getModelObject().getBioCollection());
			getModelObject().setBioCollectionList(bioCollectionList);
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
				// Set new LimsVO into model for new BioCollection, then show modalWindow to save
				Form<LimsVO> containerForm = (Form<LimsVO>) form;
				containerForm.setModelObject(new LimsVO());
				containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
				containerForm.getModelObject().setBioCollection(new BioCollection());
				Study study = linkSubjectStudy.getStudy();
				containerForm.getModelObject().getBioCollection().setStudy(study);
				containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
				modalWindow.show(target);
			}

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.NEW);
			}
		};
		
		newButton.setDefaultFormProcessing(false);

		addOrReplace(newButton);
		addOrReplace(listEditor);
		addOrReplace(modalWindow);
	}

	protected boolean isActionPermitted(String actionType)
	{
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.NEW))
		{
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE) || 
					securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.SAVE))
		{
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE) || 
					securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT))
		{
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE))
		{
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}

		return flag;
	}

	@SuppressWarnings("unchecked")
	protected void onSave(Form<?> form, AjaxRequestTarget target)
	{
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Form<LimsVO> containerForm = (Form<LimsVO>) form;

		java.util.List<BioCollection> bioCollectonList = containerForm.getModelObject().getBioCollectionList();

		for (Iterator iterator = bioCollectonList.iterator(); iterator.hasNext();)
		{
			BioCollection bioCollection = (BioCollection) iterator.next();
			try
			{
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
				bioCollection.setLinkSubjectStudy(linkSubjectStudy);
				bioCollection.setStudy(linkSubjectStudy.getStudy());
				LimsVO limsVo = new LimsVO();
				limsVo.setBioCollection(bioCollection);

				if (limsVo.getBioCollection().getId() == null)
				{
					// Save
					iLimsService.createBioCollection(limsVo);
					this.info("Biospecimen collection " + limsVo.getBioCollection().getName() + " was created successfully");
					processErrors(target);
				}
				else
				{
					// Update
					iLimsService.updateBioCollection(limsVo);
					this.info("Biospecimen collection " + limsVo.getBioCollection().getName() + " was updated successfully");
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