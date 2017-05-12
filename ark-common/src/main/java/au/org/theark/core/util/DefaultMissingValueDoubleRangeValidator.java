package au.org.theark.core.util;



import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class DefaultMissingValueDoubleRangeValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final TextField<?>[] components;
    
    private String labComp1;
    
    private String labComp2;
    
    private String labComp3;
    
    private String labComp4;
    
	/**
	 * 
	 * @param min
	 * @param max
	 * @param missingvValue
	 * @param lableComponent1
	 * @param lableComponent2
	 * @param lableComponent3
	 */
    public DefaultMissingValueDoubleRangeValidator(TextField<?> min, TextField<?> max,TextField<?> missingvValue,TextField<?> defaultValue,
    		String lableComponent1,String  lableComponent2,String  lableComponent3,String  lableComponent4) {
        components = new TextField<?>[] { min, max,missingvValue,defaultValue};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
        labComp3=lableComponent3;
        labComp4=lableComponent4;
        
    }
	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		Double min = (Double) components[0].getConvertedInput();
		Double max = (Double) components[1].getConvertedInput();
		//Double missingValueDouble = (Double) components[2].getConvertedInput();
		String missingValueString = (String) components[2].getConvertedInput();
		String defaultValueString = (String) components[3].getConvertedInput();
		if(missingValueString!=null && !missingValueString.isEmpty() && isDouble(missingValueString)){
			Double missingValueDouble=Double.valueOf(missingValueString);
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
		//Default valid Double but range validation 
		if(defaultValueString!=null && !defaultValueString.isEmpty() && isDouble(defaultValueString)){
			Double defaultValueDouble=Double.valueOf(defaultValueString);
	        if(min!=null && max!=null && defaultValueDouble !=null){
		        if (defaultValueDouble < min || defaultValueDouble > max){
		        	ValidationError ve1 = new ValidationError();
		        	ve1.setVariable("min",labComp1);
		        	ve1.setVariable("max",labComp2);
		        	ve1.setVariable("defaultValueDouble",labComp4);
		        	ve1.addMessageKey("defaultValueDoubleValidate.range");
		        	components[2].error((IValidationError) ve1);
		        }
	       }
	 	}
		//Default valid double value validation
		if(defaultValueString!=null && !defaultValueString.isEmpty() && !isDouble(defaultValueString)){
			ValidationError ve2 = new ValidationError();
        	ve2.setVariable("defaultValueDouble",labComp4);
        	ve2.addMessageKey("defaultValueDoubleValidate.notDouble");
        	components[2].error((IValidationError) ve2);
		}
	}
	private boolean isDouble(String input){
		try
		{
		  Double.parseDouble(input);
		}
		catch(NumberFormatException e)
		{
		  return false;
		}
		return true;
	}
}
