import java.net.*;

/**
 * EchoServer
 * @author Hwk
 *
 */
public class EchoServer {

	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(8163);
		while(true)
		{
			Socket ts = ss.accept();
			ts.setSoTimeout(1000);
			Worker w = new Worker(ts);
			w.start();
		}
	}

}
