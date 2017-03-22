package au.org.theark.core.util;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class CharacterDefaultMissingAndEncodedValueValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final AbstractTextComponent<?>[] components;
    private String labComp1;
    private String labComp2;
    private String labComp3;
    
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public CharacterDefaultMissingAndEncodedValueValidator(TextArea<?> encoded, TextField<?> missingValue,TextField<?> defaultValue,
    		String lableComponent1,String  lableComponent2,String  lableComponent3) {
        components = new AbstractTextComponent<?>[] { encoded, missingValue,defaultValue};
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
		String  encoded= (String) components[0].getConvertedInput();
		List<String> encodedValuLst=filterValuesOfEncoded(encoded);
		String missingValue = (String) components[1].getConvertedInput();
		String defaultValue=(String) components[2].getConvertedInput();
        if(encoded!=null && missingValue!=null && defaultValue!=null){
	        if (encodedLstContainMissingValue(encodedValuLst, missingValue)){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("encoded",labComp1);
	        	ve.setVariable("missingCharacterValue",labComp2);
	        	ve.addKey("characterMissingValueInEncodedValue.validate.error");
	        	components[0].error((IValidationError) ve);
	        }
	        if(!encodedLstContaindefaultValue(encodedValuLst, defaultValue)){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("encoded",labComp1);
	        	ve.setVariable("defaultCharacterValue",labComp3);
	        	ve.addKey("characterDefaultValueInEncodedValue.validate.error");
	        	components[0].error((IValidationError) ve);
	        }
       }
	}
	/**
	 * 
	 * @param encoded
	 * @return
	 */
	private List<String> filterValuesOfEncoded(String encoded ){
		if(encoded!=null){
			List<String> allItemsLst = Arrays.asList(encoded.split(";"));
			List<String> onlyvaluesLst=new ArrayList<String>();
		for (String string : allItemsLst) {
			onlyvaluesLst.add(string.substring(string.indexOf("=")+1));
		}
			return onlyvaluesLst;
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @param encodedValuLst
	 * @return
	 */
	private boolean encodedLstContainMissingValue(List<String> encodedValuLst,String missingValue){
		for(String encodeVal: encodedValuLst) {
		    if(encodeVal.trim().equalsIgnoreCase(missingValue))
		       return true;
		}
		return false;
	}
	private boolean encodedLstContaindefaultValue(List<String> encodedValuLst,String defaultValue){
		for(String encodeVal: encodedValuLst) {
		    if(encodeVal.trim().equalsIgnoreCase(defaultValue))
		       return true;
		}
		return false;
		
	}
}
