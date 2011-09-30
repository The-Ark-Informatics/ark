package au.org.theark.core.web.component.panel;

import org.apache.wicket.request.IRequestHandler;

public interface IDownloadRequestHandlerProvider {
	
	public abstract IRequestHandler getDownloadRequestHandler();

}
