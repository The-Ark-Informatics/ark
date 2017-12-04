package au.org.theark.study.web.component.contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.EmailAccount;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.EmailAccountSubjectVo;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.contact.form.ContainerForm;

public class EmailListPanel extends Panel {
	private static final long											serialVersionUID	= 1L;
	private ContainerForm												containerForm;
	protected ArkCrudContainerVO										arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService											iArkCommonService;
	private ArkDataProvider<EmailAccount, IStudyService>						emailProvider;				// Display and navigate purposes only.
	private ArkDataProvider<EmailAccountSubjectVo, IStudyService>				subjectEmailProvider;	// Export purposes only.
	private DataView<EmailAccount>												dataViewEmailAccount;
	private DataView<EmailAccountSubjectVo>									dataViewEmailAccountSubject;
	private Long														sessionPersonId;
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService												studyService;
	private Person														person;
	private WebMarkupContainer											dataContainer;
	
	private FeedbackPanel 					feedBackPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public EmailListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm, FeedbackPanel feedbackPanel) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedbackPanel;
		initialiseDataview();

	}

	/**
	 * Initialize the data view relevant to phone list.
	 */
	private void initialiseDataview() {
		dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		/**
		 * phone Provider for the pageable list.
		 */
		emailProvider = new ArkDataProvider<EmailAccount, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<EmailAccount>			listEmailForSize;
			private List<EmailAccount>			listEmail;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getEmailAccountVo().getEmailAccount().setPerson(person);
						listEmailForSize = studyService.getPersonEmailAccountList(sessionPersonId);
						return listEmailForSize.size();
					}else{
						return 0;
					}
					
				}
				catch (ArkSystemException e) {
					e.printStackTrace();
					return 0;
				}
				catch (EntityNotFoundException e) {
					e.printStackTrace();
					return 0;
				}
			}

			public Iterator<EmailAccount> iterator(int first, int count) {
				listEmail = studyService.pageablePersonEmailLst(sessionPersonId, first, count);
				return listEmail.iterator();
			}
		};
		/**
		 * Subject phone provider for to include the subject id.
		 */
		subjectEmailProvider = new ArkDataProvider<EmailAccountSubjectVo, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<EmailAccount>			listEmailForSize;
			private List<EmailAccount>			listEmail;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getEmailAccountVo().getEmailAccount().setPerson(person);
						listEmailForSize = studyService.getPersonEmailAccountList(sessionPersonId);
						return listEmailForSize.size();
					}else{
						return 0;
					}
					
				}
				catch (ArkSystemException e) {
					e.printStackTrace();
					return 0;
				}
				catch (EntityNotFoundException e) {
					e.printStackTrace();
					return 0;
				}
			}

			public Iterator<EmailAccountSubjectVo> iterator(int first, int count) {
				listEmail = studyService.pageablePersonEmailLst(sessionPersonId, first, count);
				List<EmailAccountSubjectVo> emailVoList = new ArrayList<EmailAccountSubjectVo>();
				for (EmailAccount email : listEmail) {
					String sessionSubjectUId = SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID).toString();
					emailVoList.add(new EmailAccountSubjectVo(email, sessionSubjectUId));
				}
				return emailVoList.iterator();
			}
		};

		emailProvider.setModel(Model.of(containerForm.getModelObject().getEmailAccountVo().getEmailAccount()));
		dataViewEmailAccount = buildDataView(emailProvider);
		dataViewEmailAccountSubject = buildDataViewWithStudySubjectID(subjectEmailProvider);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("emailNavigator", dataViewEmailAccount) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataContainer);
			}
		};
		dataContainer.add(pageNavigator);

		List<IColumn<EmailAccountSubjectVo>> exportColumns = new ArrayList<IColumn<EmailAccountSubjectVo>>();
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("Subject UID"), "subjectUID"));
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("ID"), "id"));
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("Email"), "name"));
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("Email Type"), "emailAccountType"));
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("Email Status"), "emailStatus"));
		exportColumns.add(new ExportableTextColumn<EmailAccountSubjectVo>(Model.of("Preferred Email"), "primaryEmail"));
		
		
		DataTable exportTable = new DataTable("datatable", exportColumns, dataViewEmailAccountSubject.getDataProvider(), iArkCommonService.getRowsPerPage());
		List<String> headers = new ArrayList<String>(0);
		headers.add("Subject UID:");
		headers.add("ID:");
		headers.add("Email:");
		headers.add("Email Type:");
		headers.add("Email Status:");
		headers.add("Preferred Email:");
	
		String filename = sessionPersonId != null ? String.valueOf(sessionPersonId) + "_emailList" : "unknown" + "_emailList";
		RepeatingView toolbars = new RepeatingView("toolbars");
		//Disable the tool bar if session person not exsists.
		if(sessionPersonId==null){toolbars.setEnabled(false);}else{toolbars.setEnabled(true);}
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(exportTable, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		dataContainer.add(toolbars);
		dataContainer.add(dataViewEmailAccount);
		add(dataContainer);
	}

	/**
	 * Create a dataview for display and navigate purposes.
	 * 
	 * @param emailProvider
	 * @return
	 */
	private DataView<EmailAccount> buildDataView(ArkDataProvider<EmailAccount, IStudyService> emailProvider) {

		DataView<EmailAccount> emailListDataView = new DataView<EmailAccount>("emailList", emailProvider, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<EmailAccount> item) {

				EmailAccount email = item.getModelObject();
				item.add(buildLink(email));
				if (email.getId() != null) {
					item.add(new Label("emailAccount.id", email.getId().toString()));
				}
				else {
					item.add(new Label("emailAccount.id", ""));
				}

				if (email.getEmailAccountType() != null) {
					item.add(new Label("emailAccount.accountType.name", email.getEmailAccountType().getName()));
				}
				else {
					item.add(new Label("emailAccount.accountType.name", ""));
				}
				if (email.getEmailStatus() != null) {
					item.add(new Label("emailAccount.accountStatus.name", email.getEmailStatus().getName()));
				}
				else {
					item.add(new Label("emailAccount.accountStatus.name", ""));
				}
				
			    if(email.getPrimaryAccount()){
			    	item.add(new ContextImage("emailAccount.primaryAccount", new Model<String>("images/icons/tick.png")));
			    }
			    else{
			    	item.add(new Label("emailAccount.primaryAccount", ""));
			    }
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return emailListDataView;
	}

	/**
	 * Create a dataview for only export purposes.
	 * 
	 * @param emailProvider
	 * @return
	 */
	private DataView<EmailAccountSubjectVo> buildDataViewWithStudySubjectID(ArkDataProvider<EmailAccountSubjectVo, IStudyService> emailProvider) {

		DataView<EmailAccountSubjectVo> emailListDataView = new DataView<EmailAccountSubjectVo>("emailListWithSubjectID", subjectEmailProvider, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<EmailAccountSubjectVo> item) {

				EmailAccountSubjectVo emailAccountVO = item.getModelObject();
				if (emailAccountVO.getSubjectUID() != null) {
					item.add(new Label("subjectUId", emailAccountVO.getSubjectUID()));
				}
				else {
					item.add(new Label("subjectUId", ""));
				}
				if (emailAccountVO.getId() != null) {
					item.add(new Label("id", emailAccountVO.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				if (emailAccountVO.getName() != null) {
					item.add(new Label("email", emailAccountVO.getName()));
				}
				else {
					item.add(new Label("email", ""));
				}
				if (emailAccountVO.getEmailAccountType() !=null) {
					item.add(new Label("emailType", emailAccountVO.getEmailAccountType()));
				}
				else {
					item.add(new Label("emailType", ""));
				}
				if (emailAccountVO.getEmailStatus() !=null) {
					item.add(new Label("emailStatus", emailAccountVO.getEmailStatus()));
				}
				else {
					item.add(new Label("emailStatus", ""));
				}
				if (emailAccountVO.getPrimaryEmail() !=null) {
					item.add(new ContextImage("primaryEmail", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new Label("primaryEmail", ""));
				}
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return emailListDataView;
	}

	/**
	 * Build the link.
	 * 
	 * @param email
	 * @return
	 */
	private AjaxLink<String> buildLink(final EmailAccount email) {
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("emailAccount.name") {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				containerForm.getModelObject().getEmailAccountVo().setEmailAccount(email);
				
				ArkCRUDHelper.preProcessDetailPanelOnSearchResultsForMultiplePanels(target, arkCrudContainerVO, au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL,
						au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL, au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL);
			}
		};
		Label nameLinkLabel = new Label("email", email.getName());
		link.add(nameLinkLabel);
		return link;
	}


}
