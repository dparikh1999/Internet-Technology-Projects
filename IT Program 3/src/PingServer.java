import java.io.*;
import java.net.*;
import java.util.*;

class PingServer{
	int portNum = 5525;
	int packetSize = 512;
	double LOSS_RATE = 0.3;
	int AVERAGE_DELAY = 100;
	int DOUBLE = 2;
	
	public static void main(String argv[]){
		//run server 
		try {
			PingServer server = new PingServer();
			server.run();
		} catch(Exception e) {
			System.out.println("Error: unable to run server");
		}
	}
	
	public void run(){
		//create the socket for receiving UDP packets
		DatagramSocket udpSocket = null;
		try {
			udpSocket = new DatagramSocket(portNum);
			System.out.println("Ping server running....");
		}catch (Exception e) {
			System.out.println("Error: cannot create socket for receiving UDP packets");
		}
		
		//set up buffer
		byte[] buff = new byte[packetSize]; 
		
		Random random = new Random(new Date().getTime());
		
		while(true) { //infinite loop listening for incoming UDP packets
			System.out.println("Waiting for UDP packet....");
			
			//create empty UDP packet
			DatagramPacket inpacket = new DatagramPacket(buff, packetSize);
			
			//receive next UDP packet
			try {
				udpSocket.receive(inpacket);
			} catch (Exception e) {
				System.out.println("Error: cannot receive next UDP packet.");
			}
			
			//convert data to string
			String payload = new String(inpacket.getData(), inpacket.getOffset(), inpacket.getLength());
			
			System.out.println("Received from: " + inpacket.getAddress() + " " + payload);
			
			//simulate packet loss
			if(random.nextDouble() < LOSS_RATE) {
				System.out.println("Packet loss...., reply not sent.");
			}else {
				//simulate transmission delay
				try {
					Thread.sleep((int)(random.nextDouble() * DOUBLE * AVERAGE_DELAY));
				} catch (InterruptedException e) {
					System.out.println("Error: cannot simulate transmission delay.");
				}
				
				//make an outgoing UDP packet
				DatagramPacket outpacket = new DatagramPacket(inpacket.getData(), inpacket.getLength(), inpacket.getAddress(), inpacket.getPort());
				try {
					udpSocket.send(outpacket); //send reply
					System.out.println("Reply sent.");
				} catch (IOException e) {
					System.out.println("Error: cannot send outgoing UDP packet.");
				}
				
			}
			
		}
		
	}	
	
}