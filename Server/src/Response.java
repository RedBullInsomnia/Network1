public class Response {
	// HTML Codes
	private static String badRequest = new String(
			"HTTP/1.1 400 Bad Request\r\n");
	private static String ok = new String("HTTP/1.1 200 OK\r\n");
	private static String notImplemented = new String(
			"HTTP/1.1 501 Not Implemented\r\n");

	// Server and content info
	private static String server = new String("Host : EchoServer\r\n");
	private static String contentType = new String(
			"Content-Type : text/html\r\n");
	private static String length = new String("Content-Length: ");

	// HTML doc
	private static String header = new String("<HTML>\r\n")
			+ new String(
					"  <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN>\r\n")
			+ new String("<HEAD>\r\n")
			+ new String("  <TITLE>EchoServer Results</TITLE>\r\n")
			+ new String("</HEAD>\r\n\r\n");
	private static String content = new String("<BODY BGCOLOR=\"#F2F2F2\">\r\n")
			+ new String(
					"<H1 ALIGN=\"CENTER\"><font color=\"#0B610B\">EchoServer Results</font></H1>\r\n")
			+ new String(
					"Here is the reqest line and request headers sent by your browser:\r\n");
	private static String request = new String("<PRE>");
	private static String end = new String("</PRE>\r\n</BODY>\r\n</HTML>\r\n");

	/*
	 * 
	 */
	public static boolean verifyRequest(String s) {
		String splits[] = s.split("\\s");

		if (splits[0].equals("GET") && splits[1].equals("/")
				&& splits[2].equals("HTTP/1.1"))
			return true;
		else
			return false;
	}

	/*
	 * 
	 */
	public static String badRequest(String s) {
		// compute length of html message to fill-in Content-Length field
		int len = (header.length() + content.length() + request.length())
				+ s.length() + 4;
		return badRequest + server + contentType + length + len + "\r\n\r\n"
				+ header + content + request + s + end;
	}

	/*
	 * 
	 */
	public static String ok(String s) {
		// compute length of html message to fill-in Content-Length field
		int len = (header.length() + content.length() + request.length())
				+ s.length() + 4;
		String answer = ok + server + contentType + length + len + "\r\n\r\n"
				+ header + content + request + s + end;
		System.out.print(answer);
		return answer;
	}

	/*
	 * 
	 */
	public static String notImplemented(String s) {
		int len = (header.length() + content.length() + request.length())
				+ s.length() + 4;
		return notImplemented + server + contentType + length + len + "\r\n\r\n"
				+ header + content + request + s + end;
	}
	
	/*
	 * 
	 */
	public static int parseRequest(String s) {
		String splits[];
		int ret = 200;

		if (s == null || s.length() <= 0)
			ret = 400;

		splits = s.split("\\s");
		if (splits.length != 3)
			ret = 400;

		if (!splits[0].equals("GET") || !splits[1].equals("/")
				|| !splits[2].equals("HTTP/1.1"))
			ret = 400;

		if (splits[0].equals("POST") || splits[0].equals("PUT")
				|| splits[0].equals("OPTIONS") || splits[0].equals("DELETE")
				|| splits[0].equals("TRACE") || splits[0].equals("CONNECT"))
			ret = 501; // not implemented

		return ret;
	}
}
