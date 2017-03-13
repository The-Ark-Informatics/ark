package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class ArkPlaceholderBehaviour extends Behavior{
    private final String placeholder;

    public ArkPlaceholderBehaviour(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("placeholder", this.placeholder);
    }
}
