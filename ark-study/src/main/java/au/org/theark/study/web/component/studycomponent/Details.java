package au.org.theark.study.web.component.studycomponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.SiteForm;

import java.util.*;

public class Details extends Panel{
	
	private CompoundPropertyModel<StudyCompVo> cpm;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	
	private SiteForm siteForm;
	
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	
	public CompoundPropertyModel<StudyCompVo> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyCompVo> cpm) {
		this.cpm = cpm;
	}

	public Details(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer){
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
	}
	
	public void initialisePanel(){
		
		
	}

}
