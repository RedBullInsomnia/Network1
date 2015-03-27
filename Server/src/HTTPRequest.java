/**
 * HTTPRequest class
 * 
 * @author Hwk
 *
 */
public class HTTPRequest {
	// HTML Codes
	private static String HTTP = "HTTP/1.1 ";

	// Server and content info
	private static String server = "Host : EchoServer\r\n";
	private static String contentType = "Content-Type : text/html\r\n";
	private static String length = "Content-Length: ";

	// HTML doc
	private static String goodAnswer = "<HTML>\r\n"
			+ "  <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN>\r\n"
			+ "<HEAD>\r\n"
			+ "  <TITLE>EchoServer Results</TITLE>\r\n"
			+ "</HEAD>\r\n\r\n"
			+ "<BODY BGCOLOR=\"#F2F2F2\">\r\n"
			+ "<H1 ALIGN=\"CENTER\"><font color=\"#0B610B\">EchoServer Results</font></H1>\r\n"
			+ "Your request was <font color=\"#008000\">good</font> and here are the request line and request headers sent by your browser:\r\n"
			+ "<PRE>\r\n";
	private static String badAnswer = "<HTML>\r\n"
			+ "  <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN>\r\n"
			+ "<HEAD>\r\n"
			+ "  <TITLE>EchoServer Results</TITLE>\r\n"
			+ "</HEAD>\r\n\r\n"
			+ "<BODY BGCOLOR=\"#F2F2F2\">\r\n"
			+ "<H1 ALIGN=\"CENTER\"><font color=\"#0B610B\">EchoServer Results</font></H1>\r\n"
			+ "Your request was <font color=\"#FF0000\">bad</font> but here are the request line and request headers sent by your browser:\r\n"
			+ "<PRE>\r\n";
	private static String end = "</PRE>\r\n</BODY>\r\n</HTML>\r\n";

	/*
	 * Parses and send back request encapsulated in html code
	 */
	public static String echo(String s) {

		int len;
		String resp = parseRequest(s);
		String answer = "";
		if (resp.equals("200 OK\r\n")) {
			len = goodAnswer.length() + s.length() + end.length();
			answer = goodAnswer + s;
		} else if (resp.equals("400 Bad Request\r\n")) {
			len = badAnswer.length() + s.length() + end.length();
			answer = badAnswer + s;
		} else {
			len = badAnswer.length() + s.length() + end.length();
			answer = badAnswer + s;
		}
		String info = server + contentType + length + len + "\r\n\r\n";
		return HTTP + resp + info + answer + end;
	}

	/*
	 * Parse request and return corresponding HTTP code 200 if OK 400 if Bad
	 * Request 501 if Not implemented
	 */
	public static String parseRequest(String s) {
		String splits[];

		if (s == null || s.length() <= 0)
			return "400 Bad Request\r\n";

		String str = s.substring(0, s.indexOf("\r\n"));
		splits = str.split("\\s");
		if (splits.length != 3 || !s.contains("Host: "))
			return "400 Bad Request\r\n";

		if (splits[0].equals("POST") || splits[0].equals("PUT")
				|| splits[0].equals("OPTIONS") || splits[0].equals("DELETE")
				|| splits[0].equals("TRACE") || splits[0].equals("CONNECT")
				|| splits[0].equals("HEAD"))
			return "501 Not Implemented\r\n";

		if (!splits[0].equals("GET") || !splits[1].startsWith("/")
				|| !splits[2].equals("HTTP/1.1"))
			return "400 Bad Request\r\n";

		return "200 OK\r\n";
	}
}
