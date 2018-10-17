package au.org.spark.test;

import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class JustBConnectSSL {
	public static void main (String[] args) throws Exception {
//        if( args.length < 2 ){ System.out.println ("Usage: tohost port [fromaddr [fromport]]"); return; }
        Socket sock = SSLSocketFactory.getDefault().createSocket();
        if( args.length > 2 )
            sock.bind (new InetSocketAddress (args[2], args.length>3? Integer.parseInt(args[3]): 0));
        
        String host = "128.250.143.233";
//        String host = "localhost";
        int port=63342;
        
        sock.connect (new InetSocketAddress (host, port));
        System.out.println (sock.getInetAddress().getHostName() + " = " + sock.getInetAddress().getHostAddress());
        ((SSLSocket)sock).startHandshake();
        System.out.println ("connect okay " + ((SSLSocket)sock).getSession().getCipherSuite());
    }
}
