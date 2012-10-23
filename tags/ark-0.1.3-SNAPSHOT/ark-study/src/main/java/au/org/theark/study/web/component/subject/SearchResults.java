package au.org.theark.study.web.component.subject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.DetailsForm;
import au.org.theark.study.web.component.subject.form.SearchForm;

/**
 * @author nivedann
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchResults extends Panel{
	
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8517602411833622907L;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer arkContextMarkup;
	private ContainerForm subjectContainerForm;
	

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name =  au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService iStudyService;
	

	public SearchResults(String id, 
							WebMarkupContainer  detailPanelContainer,
							WebMarkupContainer  detailPanelFormContainer, 
							WebMarkupContainer searchPanelContainer,
							WebMarkupContainer searchResultContainer,
							WebMarkupContainer viewButtonContainer,
							WebMarkupContainer editButtonContainer,
							WebMarkupContainer arkContextMarkup,
							ContainerForm containerForm){
		
		super(id);
		
		this.detailPanelContainer = detailPanelContainer;
		this.subjectContainerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
	}
	
	public DataView<SubjectVO> buildDataView(ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider) {
		
		DataView<SubjectVO> studyCompDataView = new DataView<SubjectVO>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<SubjectVO> item)
			{
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				
				StringBuffer sb = new StringBuffer();
				String firstName = subject.getPerson().getFirstName();
				String midName = subject.getPerson().getMiddleName();
				String lastName = subject.getPerson().getLastName();
				
				if (firstName != null) {
					sb.append(subject.getPerson().getFirstName());
					sb.append(" ");
				}
				if (midName != null){
					sb.append(subject.getPerson().getMiddleName());
					sb.append(" ");
				}
				if (lastName != null){
					sb.append(subject.getPerson().getLastName());
				}
				
				item.getModelObject().setSubjectFullName(sb.toString());
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));
			
				if(subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null){
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}else{
					item.add(new Label("linkSubjectStudy.person.preferredName",""));
				}
				
				item.add(new Label("linkSubjectStudy.person.genderType.name",subject.getPerson().getGenderType().getName()));
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth ="";
				if(subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null){
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", dateOfBirth));
				}else{
					item.add(new Label("linkSubjectStudy.person.dateOfBirth",""));
				}
				
				item.add(new Label("linkSubjectStudy.person.vitalStatus.statusName",subject.getPerson().getVitalStatus().getName()));
				
				item.add(new Label("linkSubjectStudy.subjectStatus.name",subject.getSubjectStatus().getName()));
				
				
				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return studyCompDataView;
	}

	public PageableListView<SubjectVO> buildListView(IModel iModel){
		
		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>(Constants.SUBJECT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE){

			@Override
			protected void populateItem(final ListItem<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				
				StringBuffer sb = new StringBuffer();
				String firstName = subject.getPerson().getFirstName();
				String midName = subject.getPerson().getMiddleName();
				String lastName = subject.getPerson().getLastName();
				
				if (firstName != null) {
					sb.append(subject.getPerson().getFirstName());
					sb.append(" ");
				}
				if (midName != null){
					sb.append(subject.getPerson().getMiddleName());
					sb.append(" ");
				}
				if (lastName != null){
					sb.append(subject.getPerson().getLastName());
				}
				
				item.getModelObject().setSubjectFullName(sb.toString());
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));
			
				if(subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null){
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}else{
					item.add(new Label("linkSubjectStudy.person.preferredName",""));
				}
				
				item.add(new Label("linkSubjectStudy.person.genderType.name",subject.getPerson().getGenderType().getName()));
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth ="";
				if(subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null){
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", dateOfBirth));
				}else{
					item.add(new Label("linkSubjectStudy.person.dateOfBirth",""));
				}
				
				item.add(new Label("linkSubjectStudy.person.vitalStatus.statusName",subject.getPerson().getVitalStatus().getName()));
				
				item.add(new Label("linkSubjectStudy.subjectStatus.name",subject.getSubjectStatus().getName()));
				
				
				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return listView;
	}
	
	
	private AjaxLink buildLink(final  SubjectVO subject){
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				subject.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
				
				// We specify the type of person here as Subject
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getLinkSubjectStudy().getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			
				SubjectVO subjectFromBackend = new SubjectVO();
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subject);
				for (SubjectVO subjectVO2 : subjects) {
					subjectFromBackend = subjectVO2;
					break;
				}
				
				// Set SubjectUID into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getLinkSubjectStudy().getSubjectUID());
				
				subjectContainerForm.setModelObject(subjectFromBackend);
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getSubjectUID(), arkContextMarkup);
				
				Search searchPanel = (Search) searchPanelContainer.get("searchComponentPanel");
				SearchForm sfs = (SearchForm) searchPanel.get("searchForm");
				List<Country> countryList = iArkCommonService.getCountries();
				if(subjectFromBackend.getLinkSubjectStudy().getCountry() ==  null){
					subjectFromBackend.getLinkSubjectStudy().setCountry(countryList.get(0));
				}
				sfs.updateDetailFormPrerender(subjectFromBackend.getLinkSubjectStudy());
				
				detailPanelContainer.setVisible(true);
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				editButtonContainer.setVisible(false);
				
				// Always disable subjectUID
				Details details = (Details) detailPanelContainer.get("detailsPanel");
				DetailsForm detailsForm = (DetailsForm) details.get("detailsForm");
				detailsForm.getSubjectUIDTxtFld().setEnabled(false);
				
				target.addComponent(searchResultContainer);
				target.addComponent(detailPanelContainer);
				target.addComponent(detailPanelFormContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
				
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getLinkSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
}