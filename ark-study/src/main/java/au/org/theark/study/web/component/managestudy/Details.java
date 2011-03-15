package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class Details  extends Panel{

	
	private FeedbackPanel fbPanel;
	private WebMarkupContainer arkContextMarkup;
	private Container studyContainerForm;
	
	private DetailForm detailForm;
	private StudyCrudContainerVO studyCrudContainerVO;
	

	/**
	 * Constructor
	 * @param id
	 * @param feedbackPanel
	 * @param studyCrudContainerVO
	 * @param containerForm
	 */
	public Details(String id,FeedbackPanel feedbackPanel, StudyCrudContainerVO studyCrudContainerVO, Container containerForm){
		super(id);
		studyContainerForm = containerForm;
		this.studyCrudContainerVO = studyCrudContainerVO;
		fbPanel = feedbackPanel;
	}
	
	@SuppressWarnings("serial")
	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm", studyCrudContainerVO,fbPanel,studyContainerForm);
		detailForm.initialiseDetailForm();
		add(detailForm); //Add the form to the panel
	}
	
		
}
