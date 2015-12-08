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

public class CharactorMissingAndEncodedValueValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final AbstractTextComponent<?>[] components;
    private String labComp1;
    private String labComp2;
    
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public CharactorMissingAndEncodedValueValidator(TextArea<?> encoded, TextField<?> missingValue,String lableComponent1,String  lableComponent2) {
        components = new AbstractTextComponent<?>[] { encoded, missingValue};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
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
        if(encoded!=null && missingValue!=null){
	        if (encodedLstContainMissingValue(encodedValuLst, missingValue)){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("encoded",labComp1);
	        	ve.setVariable("missingCharactorValue",labComp2);
	        	ve.addMessageKey("charactorMissingValueInEncodedValue.validate.error");
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
}
