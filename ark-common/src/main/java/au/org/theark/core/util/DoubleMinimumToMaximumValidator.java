package au.org.theark.core.util;



import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class DoubleMinimumToMaximumValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final TextField<?>[] components;
    
    private String labComp1;
    
    private String labComp2;
    
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public DoubleMinimumToMaximumValidator(TextField<?> min, TextField<?> max,String lableComponent1,String  lableComponent2) {
        components = new TextField<?>[] { min, max};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
    }
	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		Double min = (Double) components[0].getConvertedInput();
		Double max = (Double) components[1].getConvertedInput();
        if(min!=null && max!=null){
	        if (min > max){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("min",labComp1);
	        	ve.setVariable("max",labComp2);
	        	ve.addMessageKey("doubleMinValueAndMaxValue.range");
	        	components[0].error((IValidationError) ve);
	        }
       }
	}

}
