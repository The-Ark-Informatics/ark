package au.org.theark.study.web.component.study;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.ListMultipleChoiceForm;
import au.org.theark.study.web.form.StudyForm;


@SuppressWarnings("serial")
public class SearchResultList extends Panel{
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	private List<Study> studyList;
	private Details detailsPanel;
	
	public SearchResultList(String id, List<Study> studyList, Component component) {
		
		super(id);
		this.studyList = studyList;
		this.detailsPanel = (Details)component;
		PageableListView pageableListView = buildPageableListView(studyList, 10);//Rows per page to be in properties file
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		ThemeUiHelper.componentRounded(pageableListView);
		ThemeUiHelper.componentRounded(pageNavigator);
		add(pageNavigator);
		add(pageableListView);
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	private PageableListView buildPageableListView(List<Study> list, int rowsPerPage){
		
		PageableListView  pageableListView = new PageableListView("studyList", studyList, rowsPerPage){

			@Override
			protected void populateItem(final ListItem item) {
				
				Study study = (Study) item.getModelObject();

				if(study.getStudyKey() != null){
					item.add(new Label("studyKey", study.getStudyKey().toString()));	
				}else{
					item.add(new Label("studyKey",""));
				}
				
				Link studyNameLink = buildLink(item, detailsPanel, study);
				/* Build the caption for the Link*/
				Label studyNameLinkLabel = new Label("studyNameLink", study.getName());
				studyNameLink.add(studyNameLinkLabel);
				item.add(studyNameLink);
				
				if(study.getContactPerson() != null){
					item.add(new Label("contact", study.getContactPerson()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("contact", ""));//the ID here must match the ones in mark-up
				}
				
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String dateOfApplication ="";
				if(study.getDateOfApplication() != null){
					dateOfApplication = simpleDateFormat.format(study.getDateOfApplication());
					item.add(new Label("dateOfApplication",dateOfApplication));
				}else{
					item.add(new Label("dateOfApplication",dateOfApplication));
				}
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		
		};
		return pageableListView;
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	private Link buildLink(final ListItem item, final Component detailsPanel, final Study study) {
		

		return new Link("studyName", item.getModel()) {
			@Override
			public void onClick() {
				
				Details studyDetails = (Details)detailsPanel;
				
				StudyForm studyForm = studyDetails.getStudyForm();
				studyForm.getStudyIdTxtFld().setEnabled(false);
				studyForm.getStudyNameTxtFld().setEnabled(false);
				
				StudyModel studyModel = (StudyModel) studyForm.getModelObject();
				Set<String> selectedApps = new HashSet<String>();
				try {
					
					selectedApps = studyService.getModulesLinkedToStudy(study.getName(),true);
					studyModel.setStudy(study);
					studyModel.setLmcSelectedApps(selectedApps);
					studyForm.setModelObject(studyModel);
					
					studyForm.remove("applicationSelector");
					List<ModuleVO> modules = userService.getModules(true);
					ApplicationSelector applicationSelector = new ApplicationSelector("applicationSelector", studyForm.getModelObject(), modules);
					applicationSelector.setupSelector();
					studyForm.add(applicationSelector);
					studyDetails.setVisible(true);
					
				} catch (ArkSystemException e) {
					System.out.println("An Error occured when loading the details panel from the onClick event of the list");
					error(e.getMessage());
				}
				
				//Place the selected study in session context for the user
				SecurityUtils.getSubject().getSession().setAttribute("studyId", study.getStudyKey());
			}
		};
		
	}
	
	

}
