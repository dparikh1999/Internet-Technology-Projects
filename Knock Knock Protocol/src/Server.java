import java.io.*;
import java.net.*;
import java.util.*;

class Server {
	public static void main(String argv[]){
		int portNum = 5520;
		
		//run server with given port number
		try {
			Server server = new Server();
			server.run(portNum);
		} catch(Exception e) {
			System.out.println("Error: unable to run server");
		}
	}
	
	public void run(int num){
		
		//create print writer for log file, used for logging errors and connection status
		PrintWriter logfile = null;
		try {
			logfile  = new PrintWriter(new FileOutputStream(new File("prog1b.log")), true);
		} catch (FileNotFoundException e) {
			System.out.println("Error: unable to create/find log file");
		}
		
		//create server socket
		int portNum = num; 
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(portNum);
		} catch (IOException e) {
			logfile.append("Error: unable to create server socket\n");
		}
		
		//listen for all connections, multithreaded server
		while(true){
			Socket sock = null;
			try {
				sock = servSock.accept(); 
				Date currDate = java.util.Calendar.getInstance().getTime();  //get current date + time of connection
				logfile.append("Got a connection: " + currDate.toString() + " " + sock.getInetAddress() + " Port: " + sock.getPort() + "\n");
				logfile.flush();
			} catch (IOException e) {
				logfile.append("Error: no connection found \n");
			} 
			//create thread per connection 
			ServerThread servThread = new ServerThread(sock, logfile); 
			servThread.start();
		}
	}
}