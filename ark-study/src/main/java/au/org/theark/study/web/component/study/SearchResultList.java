package au.org.theark.study.web.component.study;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.study.model.entity.Study;


public class SearchResultList extends Panel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public PageableListView buildPageableListView(List<Study> list, int rowsPerPage){
		
		PageableListView  pageableListView = new PageableListView("studyList", studyList, rowsPerPage){

			@Override
			protected void populateItem(final ListItem item) {
				
				Study study = (Study) item.getModelObject();

				if(study.getStudyKey() != null){
					item.add(new Label("studyKey", study.getStudyKey().toString()));	
				}else{
					item.add(new Label("studyKey",""));
				}
				
				Link studyNameLink = buildLink(item);
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
				
				//item.add(new Label("vitalStatus", person.getVitalStatus()));
				
				//If we used DataView then can override newItem and return a Wicket Extension module back. EvenorOdd
				//For a PageableListView we will implement the even odd logic
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
	private Link buildLink(final ListItem item) {
		
		final Study study= (Study) item.getModelObject();

		return new Link("studyName", item.getModel()) {
			@Override
			public void onClick() {
				//TODO
				
			}
		};
		
	}
	
	

}
