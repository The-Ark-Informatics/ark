package au.org.theark.arkcalendar;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;

import au.org.theark.arkcalendar.pages.login.SignIn2;

public abstract class TemplatePage extends WebPage
{
	private static final long serialVersionUID = 1L;

	public TemplatePage()
	{
		super();

	}
	

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(new GoogleAnalyticsBehavior(this));
	}

	static class GoogleAnalyticsBehavior extends Behavior
	{
		private static final long serialVersionUID = 1L;

		private final String url;

		public GoogleAnalyticsBehavior(final WebPage page)
		{
			this.url = GoogleAnalyticsBehavior.getUrl(page);
		}

		private IModel<Map<String, Object>> newResourceModel()
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", this.url);

			return Model.ofMap(map);
		}

		private ResourceReference newResourceReference()
		{
			return new TextTemplateResourceReference(GoogleAnalyticsBehavior.class, "gaq.js", this.newResourceModel());
		}

		@Override
		public void renderHead(Component component, IHeaderResponse response)
		{
			super.renderHead(component, response);

			response.render(JavaScriptHeaderItem.forReference(this.newResourceReference(), "gaq"));
		}

		private static String getUrl(WebPage page)
		{
			Url pageUrl = Url.parse(page.urlFor(page.getClass(), null).toString());
			Url baseUrl = new Url(page.getRequestCycle().getUrlRenderer().getBaseUrl());

			baseUrl.resolveRelative(pageUrl);

			return String.format("%s/%s", page.getRequest().getContextPath(), baseUrl);
		}
	}
}
