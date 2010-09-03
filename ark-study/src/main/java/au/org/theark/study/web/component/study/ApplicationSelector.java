package au.org.theark.study.web.component.study;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.web.form.ListMultipleChoiceForm;

@SuppressWarnings("serial")
public class ApplicationSelector extends Panel{
	
	private List<ModuleVO> modules;
	private StudyModel studyModel;
	private ListMultipleChoiceForm form;
	
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModel> cpm;
	
	public CompoundPropertyModel<StudyModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModel> cpm) {
		this.cpm = cpm;
	}
	
	public ApplicationSelector(String id, StudyModel studyModel, List<ModuleVO> modules){
		super(id);
		this.modules = modules;
		this.studyModel =studyModel;
	}

	
	public ApplicationSelector(String id){
		super(id);
	}
	
	public void setupSelector() throws ArkSystemException{
		
//		form = new ListMultipleChoiceForm("appSelectorForm", studyModel, modules);
//		WebMarkupContainer container = form.initLMCContainer();
//		form.add(container);
//		this.add(form);
	}
	
	public void getSelectedItems(){
		
	}

}
