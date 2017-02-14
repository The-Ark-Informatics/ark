package au.org.theark.core.web.component.settings;

import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.web.behavior.ArkPlaceholderBehaviour;
import org.apache.wicket.Component;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.function.Consumer;

/**
 * Created by george on 22/12/16.
 */
public class SettingCharacterPanel extends Panel {

    public SettingCharacterPanel(String id) {
        super(id);
    }

    public SettingCharacterPanel(String id, IModel<Setting> model, String placeholder) {
        super(id, model);
        add(new TextField<String>("propertyValue", new PropertyModel<String>(model, "propertyValue"))
                .add(new ArkPlaceholderBehaviour(placeholder))
                .setOutputMarkupId(true)
        );
    }
}
