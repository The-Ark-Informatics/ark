package au.org.theark.study.web.form;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IUserService;

public class DetailStudyComp  extends Form<StudyCompVo>{
	
	
	@SpringBean( name = "userService")
	private IUserService userService;
	private WebMarkupContainer  resultListContainer;
	
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	public DetailStudyComp(String id){
		super(id);
	}

}
