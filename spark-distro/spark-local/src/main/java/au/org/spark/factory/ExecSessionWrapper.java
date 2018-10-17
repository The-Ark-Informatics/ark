package au.org.spark.factory;

import java.util.concurrent.atomic.AtomicInteger;

import com.jcraft.jsch.Session;

public class ExecSessionWrapper {

	private final Session session;

	private final AtomicInteger channels = new AtomicInteger();

	ExecSessionWrapper(Session session) {
		this.session = session;
	}

	public void addChannel() {
		this.channels.incrementAndGet();
	}

	public void close() {
		if (channels.decrementAndGet() <= 0) {
			this.session.disconnect();
		}
	}

	public final Session getSession() {
		return session;
	}

	public boolean isConnected() {
		return session.isConnected();
	}

}
