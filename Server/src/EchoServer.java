import java.net.*;

public class EchoServer {

	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(8163);
		while(true)
		{
			Socket ts = ss.accept();
			Worker w = new Worker(ts);
			w.start();
		}
	}

}
