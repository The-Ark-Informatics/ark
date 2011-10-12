package au.org.theark.core.web.component.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;


/**
 * Based on code by Sven Meier and Ernesto Reinaldo Barreiro (reiern70@gmail.com),
 * but made compatible with Wicket 1.5.0
 * See: https://cwiki.apache.org/WICKET/ajax-update-and-file-download-in-one-blow.html
 */
public class AjaxDownloadBehaviour extends AbstractAjaxBehavior
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private IDownloadRequestHandlerProvider downloadProvider;

	public AjaxDownloadBehaviour(IDownloadRequestHandlerProvider downloadProvider) {
		super();
		if (downloadProvider == null) {
			throw new IllegalArgumentException("Parameter can not be null: downloadProvider");
		}
		this.downloadProvider = downloadProvider;
	}

	/**
	 * Call this method to initiate the download.
	 */
	public void initiate(AjaxRequestTarget target)
	{
		CharSequence url = super.getCallbackUrl();

		target.appendJavaScript("window.location.href='" + url + "'");
	}

	public void onRequest()
	{
		getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(downloadProvider.getDownloadRequestHandler());
	}

}
