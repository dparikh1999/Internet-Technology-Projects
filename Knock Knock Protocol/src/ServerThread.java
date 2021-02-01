import java.io.*;
import java.net.*;
import java.util.*;

class ServerThread extends Thread{
	Socket sock; // Socket connected to the Client
	PrintWriter writeSock; // Used to write data to socket
	PrintWriter logFile; //Used to write data to log file
	BufferedReader readSock; // Used to read data from socket
	
	public ServerThread (Socket clientSock, PrintWriter logfile){ //constructor for ServerThread
		logFile = logfile;
		sock = clientSock;
		try {
			writeSock = new PrintWriter(sock.getOutputStream(), true);
			readSock = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			logFile.append("Error: cannot create PrintWriter and BufferedReader\n");
			logFile.flush();
		}
	}
	
	public void run(){
		//read information from each client thread
		boolean quitTime = false;
		while (quitTime != true){
			try {
				String inLine = readSock.readLine();
				String outLine;
				if (inLine.equals("quit")) { //check if user types "quit"
					quitTime = true;
					outLine = "Good Bye!\n";
				}
				else {
					outLine = (new PolyAlphabet(inLine)).result; //use PolyAlphabet class to get encrypted msg
				}
				writeSock.println(outLine);
			}catch(Exception e) { //catch if user clicks "disconnect"
				logFile.append("ServerThread Exception: " + e + ": user unexpectedly disconnected\n");
				quitTime = true;
				logFile.flush();
			}
			
		}
		
		//close threads
		try {
			logFile.append("Connection closed. Port: " + sock.getPort() + "\n"); 
			sock.close();
			logFile.flush();
		} catch (IOException e) {
			logFile.append("Error: unable to close socket\n");
			logFile.flush();
		}
		return;
	}
}