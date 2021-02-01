import java.io.*;
import java.net.*;
import java.util.*;

class HTTP{
	String response; //encrypted message
	Socket sock; 
	BufferedReader readSock; // Used to read data from socket
	FileInputStream fstream;
	
	//constructor, takes in user message
	public HTTP(Socket sockEt, BufferedReader readsock) {
		sock = sockEt;
		readSock = readsock;
	}
	
	public void run() {
		String request = "";
		try {
			request = readSock.readLine();
		} catch (IOException e1) {
			System.out.println("Error: IOException trying to read from socket.");
		}
		
		System.out.println("Client Request: " + request);
		
		String version = "HTTP/1.0 ";
		String statusPhrase;
		String headerName = "Content type: ";
		String CRLF = "\r\n";
		
		String statusLine;
		String headerLine;
		String entityBody;
		
		//break up request into tokens
		StringTokenizer tokens = new StringTokenizer(request);
		String method = tokens.nextToken();
		String fileName = "." + tokens.nextToken();
		
		boolean fileExists = true;
		
		try {
			fstream = new FileInputStream(fileName);
			statusPhrase = "200 OK";
		}catch (FileNotFoundException e) {
			fileExists = false;
			statusPhrase = "404 Not Found";
		}
		
		DataOutputStream dstream = null;
		try {
			dstream = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			System.out.println("Error: cannot get socket output stream.");
		}
		
		statusLine = version + statusPhrase + CRLF;
		headerLine = headerName + contentType(fileName) + CRLF;
		
		try {
			dstream.writeBytes(statusLine);
			dstream.flush();
			dstream.writeBytes(headerLine);
			dstream.flush();
		} catch (IOException e) {
			System.out.println("Error: cannot write status line or response header to socket.");
		}
		
		if (fileExists == false) {
			entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
			try {
				dstream.writeBytes(entityBody);
				dstream.flush();
				dstream.close();
			} catch (IOException e) {
				System.out.println("Error: cannot write entity body to socket.");
			}
		}else {
			writeFile();
		}
		return;
	}
	
	private void writeFile() {
		byte[] buffer = new byte[1024];
		OutputStream ostream = null;
		try {
			ostream = sock.getOutputStream();
		} catch (IOException e1) {
			System.out.println("Error: cannot get socket output stream.");
		}
		
		try {
			while (fstream.read(buffer) != -1) {
				ostream.write(buffer, 0, 1024);
			}
			ostream.flush();
			ostream.close();
		} catch (IOException e) {
			System.out.println("Error: IOException.");
		}
	}
	
	private String contentType(String filename) {
		if (filename.endsWith(".htm") || filename.endsWith(".html")) {
			return "text/html\r\n";
		}else if (filename.endsWith(".gif")) {
			return "image/gif\r\n";
		}else if (filename.endsWith(".bmp")) {
			return "image/bmp\r\n";
		}else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg\r\n";
		}else {
			return "application/octet-stream\r\n";
		}
	}
}