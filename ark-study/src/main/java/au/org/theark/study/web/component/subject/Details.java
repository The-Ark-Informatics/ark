/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.DetailsForm;

/**
 * @author nivedann
 *
 */
public class Details extends Panel {

	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	private DetailsForm detailsForm;
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	private Study study;

	
	/**
	 * Constructor
	 * @param id
	 */
	public Details(	String id, 
					final WebMarkupContainer listContainer, 
					FeedbackPanel feedBackPanel,
					WebMarkupContainer detailsContainer,
					WebMarkupContainer searchPanelContainer,
					ContainerForm containerForm) {
		
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.containerForm = containerForm;
	}
	
	public void initialisePanel(){
		
		detailsForm = new DetailsForm("detailsForm",this,listContainer,detailsContainer,searchPanelContainer,containerForm, feedBackPanel){
			
			protected void onCancel(AjaxRequestTarget target){
//				SubjectVO subjectVO = new SubjectVO();
//				containerForm.setModelObject(subjectVO);
//				searchPanelContainer.setVisible(true);
//				detailsContainer.setVisible(false);
//				listContainer.setVisible(false);
//				target.addComponent(searchPanelContainer);
//				target.addComponent(detailsContainer);
//				target.addComponent(feedBackPanel);
			}
			
			protected void processErrors(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
		};
		
		detailsForm.initialiseForm();
		add(detailsForm);
	}

	public DetailsForm getDetailsForm() {
		return detailsForm;
	}

	public void setDetailsForm(DetailsForm detailsForm) {
		this.detailsForm = detailsForm;
	}

}
