package au.org.theark.arkcalendar.util;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.threeten.bp.LocalDateTime;

import com.googlecode.wicket.kendo.ui.form.datetime.local.DateTimePicker;

public class DateDependencyInputValidator extends AbstractFormValidator {

    private static final long serialVersionUID = 1L;

    private boolean equalAllowed;
    /** form components to be checked. */
    private final FormComponent<?>[] components;


    /**
     * Construct.
     *
     * @param fromDateComponent
     *            a DateTextField
     * @param toDateComponenet
     *            a DateTextField
     * @param allowEqual
     *             true = allow dates to be equal
     *             false = the dates cannot be equal
     */
    public DateDependencyInputValidator(DateTimePicker fromDateComponent,
    		DateTimePicker toDateComponent, boolean equalAllowed) {

        if (fromDateComponent == null) {
            throw new IllegalArgumentException("argument fromDateComponent cannot be null");
        }
        if (toDateComponent == null) {
            throw new IllegalArgumentException("argument toDateComponent cannot be null");
        }
        components = new FormComponent[] { fromDateComponent,toDateComponent };
        this.equalAllowed = equalAllowed;
    }



    /**
     * @see
org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
     */
    public FormComponent<?>[] getDependentFormComponents() {
        return components;
    }



    /**
     * @see
org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    public void validate(Form<?> form) {

        LocalDateTime fromDate;
        LocalDateTime toDate;


        final DateTimePicker fromDateComponent = (DateTimePicker)components[0];
        final DateTimePicker toDateComponent = (DateTimePicker) components[1];

        fromDate = fromDateComponent.getConvertedInput();
        toDate = toDateComponent.getConvertedInput();

        // Compare the dates.
        // If the dates are equal and we are not allowing them to be
        // OR
        // The begin date is greater than the end date
        int compDate = fromDate.compareTo(toDate);
        if ( (compDate == 0 && ! equalAllowed) || compDate > 0) {
            error(toDateComponent);
        }

    }
} 
