package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface ISearchEventHandler {

	void onSearch(AjaxRequestTarget target);

	void onNew(AjaxRequestTarget target);

	
}
