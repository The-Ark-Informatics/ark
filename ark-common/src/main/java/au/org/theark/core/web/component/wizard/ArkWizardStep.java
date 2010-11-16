package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings({ "unchecked", "serial" })
public class ArkWizardStep extends WizardStep
{
	public ArkWizardStep(Object object1){
		setDefaultModel(new CompoundPropertyModel(object1));
		add(new Label("elementlabel"));
		add(new Label("elementdescription").setEscapeModelStrings(false));
		TextField theField = new TextField("elementvalue",new PropertyModel(object1, "elementvalue"));
		theField.setRequired(true);
		add(theField);
	}

	/**
	 * This is called when the next button that was clicked,
	 * I assume you want to use your for for data gathering..
	 * Normally you’d save the object the the database
	 * Or just leave it in memory and save it in the Wizard onFinish()
	 */
	public void applyState(){
		System.err.println("Applying state!");
		setComplete(true); // Set this step as done, you should put custom logic here
	}
}