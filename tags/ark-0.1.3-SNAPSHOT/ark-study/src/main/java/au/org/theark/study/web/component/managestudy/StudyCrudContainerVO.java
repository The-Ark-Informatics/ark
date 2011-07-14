package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class StudyCrudContainerVO extends ArkCrudContainerVO{
	
	protected PageableListView<Study> pageableListView;
	protected WebMarkupContainer summaryContainer;
	protected WebMarkupContainer studyLogoImageContainer;
	protected WebMarkupContainer studyLogoUploadContainer;
	
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;
	protected WebMarkupContainer arkContextMarkup;
	
	public StudyCrudContainerVO(){
		super();
		
		summaryContainer = new WebMarkupContainer("summaryPanel");
		summaryContainer.setOutputMarkupPlaceholderTag(true);
		summaryContainer.setVisible(false);
		
		studyLogoImageContainer = new WebMarkupContainer("studyLogoImageContainer");
		studyLogoImageContainer.setOutputMarkupPlaceholderTag(true);
		
		studyLogoUploadContainer = new WebMarkupContainer("studyLogoUploadContainer");
		studyLogoUploadContainer.setOutputMarkupPlaceholderTag(true);
	}
	
	
	public WebMarkupContainer getSummaryContainer() {
		return summaryContainer;
	}

	public void setSummaryContainer(WebMarkupContainer summaryContainer) {
		this.summaryContainer = summaryContainer;
	}

	public WebMarkupContainer getStudyLogoImageContainer() {
		return studyLogoImageContainer;
	}

	public void setStudyLogoImageContainer(
			WebMarkupContainer studyLogoImageContainer) {
		this.studyLogoImageContainer = studyLogoImageContainer;
	}

	public WebMarkupContainer getStudyLogoUploadContainer() {
		return studyLogoUploadContainer;
	}

	public void setStudyLogoUploadContainer(
			WebMarkupContainer studyLogoUploadContainer) {
		this.studyLogoUploadContainer = studyLogoUploadContainer;
	}


	public WebMarkupContainer getStudyNameMarkup() {
		return studyNameMarkup;
	}


	public void setStudyNameMarkup(WebMarkupContainer studyNameMarkup) {
		this.studyNameMarkup = studyNameMarkup;
	}


	public WebMarkupContainer getStudyLogoMarkup() {
		return studyLogoMarkup;
	}


	public void setStudyLogoMarkup(WebMarkupContainer studyLogoMarkup) {
		this.studyLogoMarkup = studyLogoMarkup;
	}


	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}


	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}


	public PageableListView<Study> getPageableListView() {
		return pageableListView;
	}


	public void setPageableListView(PageableListView<Study> pageableListView) {
		this.pageableListView = pageableListView;
	}

}
