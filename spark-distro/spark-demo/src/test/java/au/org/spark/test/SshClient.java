package au.org.spark.test;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshClient {
	public static void main(String[] args) {
		System.out.println("Test SShClient");
		
		String out =connectAndExecute("tranaweera", "localhost", "@eArk8Wm8", "sleep 100 & jobs -p");
		
		
		System.out.println(out);
		
		
			
	}
	
	
	public static String connectAndExecute(String user, String host, String password, String command1) {
		String CommandOutput = null;
		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			// System.out.println("Connected");

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command1);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);

					if (i < 0)
						break;
					// System.out.print(new String(tmp, 0, i));
					CommandOutput = new String(tmp, 0, i);
				}

				if (channel.isClosed()) {
					// System.out.println("exit-status: " +
					// channel.getExitStatus());
					break;
				}
//				try {
//					Thread.sleep(1000);
//				} catch (Exception ee) {
//				}
			}
			channel.disconnect();
			session.disconnect();
			// System.out.println("DONE");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommandOutput;

	}
	 
	
}
