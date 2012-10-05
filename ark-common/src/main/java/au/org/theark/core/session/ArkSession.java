package au.org.theark.core.session;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * 
 * @author thilina
 *Ark web session to store selected nodes
 */
public class ArkSession extends WebSession {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object nodeObject;

	public ArkSession(Request request) {
		super(request);
	}

	public final Object getNodeObject() {
		return nodeObject;
	}

	public final void setNodeObject(Object nodeObject) {
		this.nodeObject = nodeObject;
	}

	public static ArkSession get() {
		return (ArkSession)Session.get();
	}
}
