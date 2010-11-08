/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.studycomponent.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.*;
import au.org.theark.study.model.vo.StudyCompVo;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<StudyCompVo>{

	private TextField<String> studyCompIdTxtFld;
	private TextField<String> compNameTxtFld;
	private TextArea<String> descriptionTxtArea;
	private TextArea<String> keywordTxtArea;

	protected void addSearchComponentsToForm(){
		add(studyCompIdTxtFld);
		add(compNameTxtFld);
		add(keywordTxtArea);
	}

	protected void initialiseSearchForm(){
		
		studyCompIdTxtFld = new TextField<String>("studyComponent.studyCompKey");
		compNameTxtFld = new TextField<String>("studyComponent.name");
		descriptionTxtArea = new TextArea<String>("studyComponent.description");
		keywordTxtArea = new TextArea<String>("studyComponent.keyword");
	}
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id, CompoundPropertyModel<StudyCompVo> cpmModel) {
		super(id, cpmModel);
		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		
	}

}
