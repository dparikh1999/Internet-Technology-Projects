import java.io.*;
import java.net.*;
import java.util.*;

class UDPPinger{ //use this class to send a ping message and receive echo message from Server
	public DatagramSocket socket = null;
	public boolean received = true;
	
	//sends a UDP packet with an instance of PingMessage
	public void sendPing(PingMessage ping){
		byte[] buff = new byte[512];
		buff = ping.getPayload().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buff, buff.length, ping.getIP(), ping.getPort());
		
		try {
			socket = new DatagramSocket();
			socket.connect(sendPacket.getAddress(), sendPacket.getPort());
		}catch (Exception e) {
			System.out.println("Error: cannot create socket to send UDP datagram.");
		}
		
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Error: cannot send packet.");
		}
		return;
	} 
	
	//receives UDP packet from server
	public PingMessage receivePing(){ 
		byte[] buff = new byte[512];
		DatagramPacket ppacket = new DatagramPacket(buff, 512);
		try {
			socket.setSoTimeout(1000);
		} catch (SocketException e1) {
			System.out.println("Error: could not set up socket timeout.");
		}
		
		try {
			socket.receive(ppacket);
			Date currDate = java.util.Calendar.getInstance().getTime();
			System.out.println("Received packet from " + ppacket.getAddress() + " " + ppacket.getPort() + " " + currDate);
			received = true;
		} catch (Exception e) {
			System.out.println("Error: receivePing...." + e);
			received = false;
		}
		PingMessage ping = new PingMessage(ppacket.getAddress(), ppacket.getPort(), ppacket.getData().toString());
		return ping;
	}
	
}