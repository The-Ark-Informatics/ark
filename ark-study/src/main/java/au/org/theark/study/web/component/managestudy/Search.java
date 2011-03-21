package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.SearchForm;

public class Search extends Panel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4770548021154298531L;
	private Container containerForm;
	private FeedbackPanel feedBackPanel;
	private StudyCrudContainerVO studyCrudContainerVO;
	
	/**
	 * Constructor That uses the VO
	 * @param id
	 * @param studyCrudContainerVO
	 */
	public Search(String id, StudyCrudContainerVO studyCrudContainerVO,	FeedbackPanel feedbackPanel, Container containerForm){
		
		super(id);
		this.studyCrudContainerVO = studyCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedbackPanel;
	}
	

	public void initialisePanel(CompoundPropertyModel<StudyModelVO> studyModelVOCpm){
	
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, studyModelVOCpm, studyCrudContainerVO, feedBackPanel, containerForm);
		add(searchForm);
	}

}
