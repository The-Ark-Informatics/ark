package au.org.theark.core.util;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by george on 23/2/17.
 */
public class EventPayload {

    private String eventName;
    private AjaxRequestTarget target;

    public EventPayload(String eventName, AjaxRequestTarget target) {
        this.eventName = eventName;
        this.target = target;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }

    public void setTarget(AjaxRequestTarget target) {
        this.target = target;
    }
}
