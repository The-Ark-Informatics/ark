package au.org.theark.core.util;



import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class MissingValueDoubleRangeValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final TextField<?>[] components;
    
    private String labComp1;
    
    private String labComp2;
    
    private String labComp3;
    
	/**
	 * 
	 * @param min
	 * @param max
	 * @param missingvValue
	 * @param lableComponent1
	 * @param lableComponent2
	 * @param lableComponent3
	 */
    public MissingValueDoubleRangeValidator(TextField<?> min, TextField<?> max,TextField<?> missingvValue,
    		String lableComponent1,String  lableComponent2,String  lableComponent3) {
        components = new TextField<?>[] { min, max,missingvValue};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
        labComp3=lableComponent3;
        
    }
	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		Double min = (Double) components[0].getConvertedInput();
		Double max = (Double) components[1].getConvertedInput();
		Double missingValueDouble = (Double) components[2].getConvertedInput();
        if(min!=null && max!=null && missingValueDouble !=null){
	        if (missingValueDouble > min && missingValueDouble < max){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("min",labComp1);
	        	ve.setVariable("max",labComp2);
	        	ve.setVariable("missingvValueDouble",labComp3);
	        	ve.addMessageKey("missingValueDoubleValidate.range");
	        	components[2].error((IValidationError) ve);
	        }
       }
	}

}
