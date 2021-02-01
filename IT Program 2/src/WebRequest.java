import java.io.*;
import java.net.*;
import java.util.*;

class WebRequest extends Thread{
	Socket sock; // Socket connected to the Client
	BufferedReader readSock; // Used to read data from socket
	
	public WebRequest (Socket clientSock){ //constructor for WebRequest
		sock = clientSock;
		try {
			readSock = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error: cannot create BufferedReader");
		}
	}
	
	public void run(){
		//read request from each client thread
		try {
			HTTP http = new HTTP(sock, readSock);
			http.run();
		}catch(Exception e) { //catch if user illegally "disconnects"
			System.out.println("Error: " + e + " - user unexpectedly disconnected");
		}
		
		//close threads
		try {
			System.out.println("Connection closed. Port: " + sock.getPort()); 
			sock.close();
		} catch (IOException e) {
			System.out.println("Error: unable to close socket");
		}
		return;
	}
}