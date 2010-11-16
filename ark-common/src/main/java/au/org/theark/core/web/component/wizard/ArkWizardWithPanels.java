package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.StaticContentStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;

/**
 * This is kind of the hello world example for wizards. It doesn't do anything useful, except displaying some static text and following static flow.
 * <p>
 * {@link StaticContentStep static content steps} are useful when you have some text to display that you don't want to define seperate panels for.
 * E.g. when the contents come from a database, this is a convenient class to use.
 * </p>
 * 
 * @author Eelco Hillenius
 */
@SuppressWarnings("serial")
public class ArkWizardWithPanels extends Wizard
{
	private Class<ArkWizardIndex> wizardIndexClass;

	/**
	 * The first step of this wizard.
	 */
	private static final class Step1 extends WizardStep
	{
		/**
		 * Construct.
		 */
		public Step1()
		{
			super("One", "The first step");
		}
	}

	/**
	 * The second step of this wizard.
	 */
	private static final class Step2 extends WizardStep
	{
		/**
		 * Construct.
		 */
		public Step2()
		{
			super("Two", "The second step");
		}
	}

	/**
	 * The third step of this wizard.
	 */
	private static final class Step3 extends WizardStep
	{
		/**
		 * Construct.
		 */
		public Step3()
		{
			super("Three", "The third step");
		}
	}
	
	/**
	 * The fourth step of this wizard.
	 */
	private static final class Step4 extends WizardStep
	{
		/**
		 * Construct.
		 */
		public Step4()
		{
			super("Four", "The fourth step");
		}
	}
	
	/**
	 * The fifth step of this wizard.
	 */
	private static final class Step5 extends WizardStep
	{
		/**
		 * Construct.
		 */
		public Step5()
		{
			super("Fifth", "The fifth step");
		}
	}

	/**
	 * Default Constructor
	 * 
	 * @param id
	 *           The component id
	 */
	public ArkWizardWithPanels(String id)
	{
		super(id);
		wizardIndexClass = ArkWizardIndex.class;

		// create a model with a couple of custom panels
		// still not that spectacular, but at least it
		// will give you a hint of how nice it is to
		// be able to work with custom panels
		WizardModel model = new WizardModel();
		model.add(new Step1());
		model.add(new Step2());
		model.add(new Step3());
		model.add(new Step4());
		model.add(new Step5());

		// initialize the wizard
		init(model);
	}
	
	/**
	 * Constructor with specified wizard home/index page
	 * 
	 * @param id
	 *           The component id
	 * @param wizardIndexClass
	 *           The home/index page the wizard will go to on finish
	 */
	public ArkWizardWithPanels(String id, Class<ArkWizardIndex> wizardIndexClass)
	{
		super(id);

		// create a model with a couple of custom panels
		// still not that spectacular, but at least it
		// will give you a hint of how nice it is to
		// be able to work with custom panels
		WizardModel model = new WizardModel();
		model.add(new Step1());
		model.add(new Step2());
		model.add(new Step3());
		model.add(new Step4());
		model.add(new Step5());

		// initialize the wizard
		init(model);
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.Wizard#onCancel()
	 */
	@Override
	public void onCancel()
	{
		setResponsePage(getWizardIndexClass());
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.Wizard#onFinish()
	 */
	@Override
	public void onFinish()
	{
		setResponsePage(getWizardIndexClass());
	}

	/**
	 * @param arkWizardIndexClass the wizardIndexClass to set
	 */
	public void setWizardIndexClass(Class<ArkWizardIndex> arkWizardIndexClass)
	{
		this.wizardIndexClass = arkWizardIndexClass;
	}

	/**
	 * @return the wizardIndexClass
	 */
	public Class<ArkWizardIndex> getWizardIndexClass()
	{
		return wizardIndexClass;
	}
}