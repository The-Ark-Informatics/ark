package au.org.theark.core.web.component.wizard;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;

@SuppressWarnings("serial")
public class ArkCommonWizard extends Wizard
{

	/*
	 * Create a wizard with a default model and three custom steps. Each step
	 * is a form with some default data in the model for example purpose.
	 */
	public ArkCommonWizard(String id){

		super(id);
		
		WizardModel model = new WizardModel();
		
//		YourObject theObj = new YourObject();
//		theObj.setElementdescription("First item");
//		theObj.setElementlabel("Label me");
//		theObj.setElementvalue("my value");
//		
//		YourObject theObj2 = new YourObject();
//		theObj2.setElementdescription("Second item");
//		theObj2.setElementlabel("Label me2");
//		theObj2.setElementvalue("my value2");
//		
//		YourObject theObj3 = new YourObject();
//		theObj3.setElementdescription("Third item");
//		theObj3.setElementlabel("Label me3");
//		theObj3.setElementvalue("my value3");
		
//		// Add the steps
//		model.add(new ArkWizardStep(theObj));
//		model.add(new ArkWizardStep(theObj2));
//		model.add(new ArkWizardStep(theObj3));
		
		init(model);
	}

	/**
	 * On finish. Put any specific logic you want when the user hits the "finish" button eventually Here we print to error log and send them to the
	 * home page. Normally used for saving unclean objects to database if you did not do it in the WizardStep
	 */
	@Override
	public void onFinish()
	{
		System.err.println("DONE WITH THE WIZ!");
		setResponsePage(ArkWizardIndex.class);
	}

	/**
	 * On cancel. Put your cancel logic in for when users hit the ‘Cancel’ button. I send them to the home page
	 */

	@Override
	public void onCancel()
	{
		setResponsePage(ArkWizardIndex.class);
	}

	protected Component newButtonBar(java.lang.String id)
	{
		return new ArkWizardButtonBarPanel(id, this);
	}

	protected Component newOverviewBar(java.lang.String id)
	{
		return new ArkWizardOverviewPanel(id);
	}
}