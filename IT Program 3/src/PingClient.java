import java.io.*;
import java.net.*;
import java.util.*;

class PingClient extends UDPPinger implements Runnable{
	public static void main(String argv[]){
		//run client
		try {
			PingClient client = new PingClient();
			client.run();
		} catch(Exception e) {
			System.out.println("Error: unable to run client");
		}
	}
	
	public void run() {
		InetAddress address = null;
		try {
			address = InetAddress.getByName("localhost"); //get localhost addr
		} catch (UnknownHostException e) {
			System.out.println("Error: internet address cannot be set due to unknown host.");
		}
		
		//create datagram socket instance
		DatagramSocket dgsock = null;
		try {
			dgsock = new DatagramSocket();
			dgsock.connect(address, 5525);
		} catch (SocketException e) {
			System.out.println("Error: cannot create Datagram Socket in Client class.");
		}
		
		System.out.println("Contacting host: localhost at port 5525");
		
		try {
			dgsock.setSoTimeout(1000);
		} catch (SocketException e) {
			System.out.println("Error: cannot set socket timeout to 1 second.");
		}
		
		long[] RTTs = new long[10]; //array to hold RTTs for each packet
		
		//loop to send and receive 10 PING messages
		for (int i=0; i<10; i++) {
			//send ping
			long startTime = new Date().getTime(); 
			String pingPayload = "PING " + i + " " + startTime;
			PingMessage sping = new PingMessage(dgsock.getInetAddress(), dgsock.getPort(), pingPayload);
			sendPing(sping); //send ping to server
			
			//receive ping
			PingMessage rping = null;
			rping = receivePing(); //receive echo back 
			long endTime = new Date().getTime();
			long RTT = 0;
			if (received == false) {
				RTT = 1000;
			}else {
				RTT = endTime-startTime;
			}
			RTTs[i] = RTT;
		}
		
		try {
			dgsock.setSoTimeout(5000); //waiting for any replies that are on the way
		} catch (SocketException e) {
			System.out.println("Error: cannot set socket timeout to 5 seconds.");
		}
		dgsock.close();
		
		//calculate avg RTT
		long sum = 0;
		for (int i=0; i<10; i++) {
			sum += RTTs[i];
			String output = "PING " + i + ": ";
			if (RTTs[i] == 1000) { //was not a received packet
				output = output + "false RTT: " + RTTs[i];
			}else {
				output = output + "true RTT: " + RTTs[i];
			}
			System.out.println(output);
		}
		
		//calculate min, max RTT
		Arrays.sort(RTTs); //sort array of RTTs
		long min = RTTs[0];
		long max = RTTs[9];
		
		double avg = sum/(10.0);
		System.out.println("Minimum = " + min + "ms, Maximum = " + max + "ms, Average = " + avg + "ms.");
		
	}
	
}