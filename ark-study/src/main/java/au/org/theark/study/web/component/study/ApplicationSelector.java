package au.org.theark.study.web.component.study;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.web.form.ListMultipleChoiceForm;

@SuppressWarnings("serial")
public class ApplicationSelector extends Panel{
	
	private List<ModuleVO> modules;
	private StudyModel studyModel;
	private ListMultipleChoiceForm form;
	public ApplicationSelector(String id, StudyModel studyModel, List<ModuleVO> modules){
		super(id);
		this.modules = modules;
		this.studyModel =studyModel;
	}
	
	public void setupSelector() throws ArkSystemException{
		
		form = new ListMultipleChoiceForm("appSelectorForm", studyModel, modules);
		WebMarkupContainer container = form.initLMCContainer();
		form.add(container);
		this.add(form);
	}
	
	public void getSelectedItems(){
		
	}

}
