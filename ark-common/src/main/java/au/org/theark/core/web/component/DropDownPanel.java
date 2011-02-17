/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.study.entity.ConsentAnswer;

/**
 * @author nivedann
 * @param <T>
 *
 */
public class DropDownPanel extends Panel{

	private DropDownChoice<ConsentAnswer> consentAnswerChoice;
	/**
	 * @param id
	 */
	public DropDownPanel(String id,String selectControlId, List<ConsentAnswer> choicesList) {
		
		super(id);
		
		ChoiceRenderer<ConsentAnswer>  choiceRenderer = new ChoiceRenderer<ConsentAnswer>("name", "id");
		consentAnswerChoice  = new DropDownChoice<ConsentAnswer>(selectControlId, (List)choicesList, choiceRenderer);
		add(consentAnswerChoice);
	}
	
	

}
