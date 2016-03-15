package au.org.spark.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ExecSession {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final Session jschSession;

	private volatile ChannelExec channel;

	private volatile boolean closed;
	
	private final ExecSessionWrapper wrapper;

	public ExecSession(Session jschSession) {
		Assert.notNull(jschSession, "jschSession must not be null");
		this.jschSession = jschSession;
		this.wrapper=null;
	}
	
	public ExecSession(ExecSessionWrapper wrapper) {
		Assert.notNull(wrapper, "wrapper must not be null");
		this.jschSession = wrapper.getSession();
		this.wrapper = wrapper;
	}

	public void close() {
		this.closed = true;
		if (this.jschSession.isConnected()) {
			this.jschSession.disconnect();
		}
	}

	public boolean isOpen() {
		return !this.closed && this.jschSession.isConnected();
	}

	void connect() {
		try {
			if (!this.jschSession.isConnected()) {
				this.jschSession.connect();
			}
			this.channel = (ChannelExec) this.jschSession.openChannel("exec");
//			if (this.channel != null && !this.channel.isConnected()) {
//				this.channel.connect();
//			}
		} catch (JSchException e) {
			this.close();
			throw new IllegalStateException("failed to connect", e);
		}
	}

	public ChannelExec getClientInstance() {
		return this.channel;
	}

}
