package au.org.theark.admin.web.component.settings;

import au.org.theark.core.Constants;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.web.behavior.ArkPlaceholderBehaviour;
import au.org.theark.core.web.component.ArkDatePicker;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

/**
 * Created by george on 22/12/16.
 */
public class SettingDatePanel extends Panel {

    public SettingDatePanel(String id) {
        super(id);
    }

    public SettingDatePanel(String id, IModel<Setting> model, String placeholder) {
        super(id, model);
        DateTextField dateTextField = new DateTextField("propertyValue", new PropertyModel<Date>(model, "propertyValue"), Constants.DD_MM_YYYY);
        ArkDatePicker datePicker = new ArkDatePicker();
        datePicker.bind(dateTextField);
        dateTextField.add(datePicker);
        dateTextField.add(new ArkPlaceholderBehaviour(placeholder));
        add(dateTextField);
    }
}
