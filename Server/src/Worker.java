import java.io.*;
import java.net.*;

/**
 * 
 * @author hwk
 *
 */
public class Worker extends Thread {

	private static int numAlive = 0;
	private Socket s;
	private int num;
	private int echos;
	private int bufferSize = 512;

	Worker(Socket _s, int _n) {
		s = _s;
		num = _n;
		numAlive++;
		echos = 0;
	}

	@Override
	public void run() {
		String request = "", buffer = "", answer = "";
		byte msg[] = new byte[bufferSize];
		int len = 0;
		boolean timeout = false;

		// wait for request
		System.out.println("Worker " + num + " executed");
		try {
			s.setSoTimeout(1000);
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();

			while (true) {
				// Read incoming bytes
				len = in.read(msg);
				if (len <= 0)
					break;

				// Add incoming to buffer
				buffer += new String(msg, 0, len);

				// Bound length
				if (buffer.length() >= 8096)
					break;

				// If we received the entire request, we can parse it
				if (buffer.contains("\r\n\r\n")) {
					request = buffer.substring(0,
							buffer.indexOf("\r\n\r\n") + 4);

					// Reset buffer for future messages
					if (!buffer.endsWith("\r\n\r\n")) {
						buffer = buffer
								.substring(buffer.indexOf("\r\n\r\n") + 4,
										buffer.length());
					} else {
						buffer = "";
					}

					// Send back answer
					answer = HTTPRequest.echo(request);
					out.write(answer.getBytes(), 0, answer.length());
					out.flush(); // don’t wait for more
					ackAnswer();

					// Can we close the connection or not ?
					if (request.contains("Connection: keep-alive")) {
						if (!s.getKeepAlive()) {
							s.setKeepAlive(true);
							s.setSoTimeout(5000);
						}
					} else {
						break;
					}
				}
			}
			s.close(); // acknowledge end of connection

		} catch (SocketTimeoutException to) {
			timeout = true;
			try {
				s.close();
			} catch (IOException io) {
				System.err.println("Error on socket: " + io.getMessage());
			}
		} catch (IOException io) {
			System.err.println("Error on socket: " + io.getMessage());
		} catch (Exception e) {
			System.err.println("Houston we have a problem: " + e.getMessage());
			e.printStackTrace();
		}

		ackClose(timeout);
	}

	/*
	 * Print acknowledgement to terminal
	 */
	public void ackAnswer() {
		echos++;
		System.out
				.println("Worker " + num + ": treated " + echos + " requests");
	}

	/*
	 * Print that worker finished its work and update numAlive
	 */
	public void ackClose(boolean timeout) {
		if (timeout)
			System.out.println("Worker " + num + " closed : timeout");
		else
			System.out.println("Worker " + num + " closed");
		numAlive--;
		System.out.println(numAlive + " workers still alive");
	}

}
