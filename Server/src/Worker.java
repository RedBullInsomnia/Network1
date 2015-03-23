import java.io.*;
import java.net.*;

public class Worker extends Thread {

	Socket s;

	Worker(Socket _s) {
		s = _s;
	}

	@Override
	public void run() {
		try {
			OutputStream out = s.getOutputStream();
			InputStream in = s.getInputStream();
			String str = "";
			byte msg[] = new byte[64];

			// wait for request
			while (true) {
				if (in.read(msg) <= 0)
					continue;
				str = str + new String(msg);

				if (str.contains("\r\n\r\n"))
					break;				
			}
			// handle request
			str = str.substring(0, str.indexOf("\r\n\r\n") + 4);
			if (false == Response.verifyRequest(str))
				out.write(Response.badRequest(str).getBytes(), 0, Response
						.badRequest(str).length()-1);
			else
				out.write(Response.ok(str).getBytes(), 0, Response.ok(str)
						.length()-1);

			out.flush(); // don’t wait for more
			s.close(); // acknowledge end of connection
		} catch (Exception any) {
			System.err.println("worker died " + any);
		}
	}
}
