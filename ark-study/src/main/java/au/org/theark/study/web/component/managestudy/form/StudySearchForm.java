/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.managestudy.form;

import java.util.List;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.SearchBaseForm;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.model.vo.StudyModel;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class StudySearchForm extends SearchBaseForm<StudyModel>{

	protected   CompoundPropertyModel<StudyModel> cpmModel;
	
	/**
	 * @param id
	 * @param model
	 */
	public StudySearchForm(String id, CompoundPropertyModel<StudyModel> model, List<StudyStatus>  statusList) {
		super(id);
		cpmModel = model;
		idTxtFld  =new TextField<String>(Constants.STUDY_SEARCH_KEY);
		nameTxtFld =new TextField<String>(Constants.STUDY_SEARCH_NAME);
	}

}
