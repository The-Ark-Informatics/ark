package au.org.theark.core.web.component.settings;

import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.web.behavior.ArkPlaceholderBehaviour;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by george on 22/12/16.
 */
public class SettingNumberPanel extends Panel {

    public SettingNumberPanel(String id) {
        super(id);
    }

    public SettingNumberPanel(String id, IModel<Setting> model, String placeholder) {
        super(id, model);
        add(new TextField<Long>("propertyValue", new PropertyModel<Long>(model, "propertyValue"))
                .add(new ArkPlaceholderBehaviour(placeholder))
        );
    }
}
