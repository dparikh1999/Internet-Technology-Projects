import java.io.*;
import java.net.*;
import java.util.*;

class WebServer{
	public static void main(String argv[]){
		int portNum = 5520;
		
		//run server with given port number
		try {
			WebServer server = new WebServer();
			server.run(portNum);
		} catch(Exception e) {
			System.out.println("Error: unable to run server");
		}
	}
	
	public void run(int num){
		
		//create server socket
		int portNum = num; 
		System.out.println("Running server on port " + portNum);
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(portNum);
		} catch (IOException e) {
			System.out.println("Error: unable to create server socket");
		}
		
		//listen for all connections
		while(true){
			Socket sock = null;
			try {
				sock = servSock.accept(); 
				Date currDate = java.util.Calendar.getInstance().getTime();  //get current date + time
				System.out.println("Got a request: " + currDate.toString() + " " + sock.getInetAddress() + " Port: " + sock.getPort());
			} catch (IOException e) {
				System.out.println("Error: no request found");
			} 
			WebRequest servThread = new WebRequest(sock);
			servThread.start();
		}
	}
	
}