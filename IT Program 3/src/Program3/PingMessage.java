import java.io.*;
import java.net.*;
import java.util.*;

class PingMessage{ //use this class to create a ping message to be sent to the server
	InetAddress address; 
	int portNum;
	String payload;
	
	public PingMessage(InetAddress addr, int port, String payload){ //constructor
		address = addr;
		portNum = port;
		this.payload = payload;
		
	}public InetAddress getIP(){ //get the destination IP address
		return address;
		
	}public int getPort(){ //get the destination port number
		return portNum;
		
	}public String getPayload(){ //get the content of the payload
		return payload;
	}
}