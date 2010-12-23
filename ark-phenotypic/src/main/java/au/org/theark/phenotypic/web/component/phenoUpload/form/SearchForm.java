package au.org.theark.phenotypic.web.component.phenoUpload.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoUpload.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class SearchForm extends AbstractSearchForm<UploadVO>
{
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService						phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private PageableListView<Upload>				listView;
	private CompoundPropertyModel<UploadVO>	cpmModel;
	private DetailPanel								detailPanel;

	private TextField<String>						uploadIdTxtFld;
	private TextField<String>						uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>			fileFormatDdc;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<UploadVO> model, PageableListView<Upload> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();

		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		disableSearchButtons(sessionPhenoCollectionId, "There is no Phenotypic Collection in context. Please select a Phenotypic Collection");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<UploadVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	@SuppressWarnings("unchecked")
	private void initDropDownChoice()
	{
		// Initialise any drop-downs
		java.util.Collection<FileFormat> fileFormatCollection = phenotypicService.getFileFormats();
		CompoundPropertyModel<UploadVO> uploadCpm = cpmModel;
		PropertyModel<Upload> uploadPm = new PropertyModel<Upload>(uploadCpm, au.org.theark.phenotypic.web.Constants.UPLOAD);
		PropertyModel<FileFormat> fileFormatPm = new PropertyModel<FileFormat>(uploadPm, au.org.theark.phenotypic.web.Constants.FILE_FORMAT);
		ChoiceRenderer fileFormatRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME, au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, fileFormatPm, (List) fileFormatCollection, fileFormatRenderer);
	}

	public void initialiseFieldForm()
	{
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		uploadFilenameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// Set up fields on the form
		initDropDownChoice();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		// Add the field components
		add(uploadIdTxtFld);
		add(uploadFilenameTxtFld);
		add(fileFormatDdc);
	}
	
	// Reset button implemented in AbstractSearchForm

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		Upload searchUpload = getModelObject().getUpload();
		java.util.Collection<Upload> uploadCollection = phenotypicService.searchUpload(searchUpload);
		
		if (uploadCollection != null && uploadCollection.size() == 0)
		{
			this.info("Uploads with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);
	}
	
	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		UploadVO uploadVo = new UploadVO();
		uploadVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		setModelObject(uploadVo);
		preProcessDetailPanel(target);
		
		// Hide Delete button on New
		detailPanel.getDetailForm().getDeleteButton().setVisible(false);
	}

	protected boolean isSecure(String actionType)
	{
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW))
		{

			SecurityManager securityManager = ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			if (securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) || securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN))
			{
				flag = true;
			}
		}
		else
		{
			flag = true;
		}

		return flag;
	}
}