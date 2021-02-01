import java.io.*;
import java.net.*;
import java.util.*;

class Server{
	private InputStream instream;
	private OutputStream ostream;
	private FileOutputStream fostream;
	private boolean success;
	
	public static void main(String argv[]){
		//run server 
		try {
			Server server = new Server();
			server.run();
		} catch(Exception e) {
			System.out.println("Error: unable to run server");
		}
	}
	
	public void run() {
		
		//create server socket
		System.out.println("Server running....");
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(5520);
		} catch (IOException e) {
			System.out.println("Error: unable to create server socket " + e);
		}
		
		//listen for all connections
		while(true){
			System.out.println("Waiting for connection....");
			Socket sock = null;
			try {
				sock = servSock.accept(); 
				Date currDate = java.util.Calendar.getInstance().getTime();  //get current date + time
				System.out.println("Got a connection: " + currDate.toString());
				System.out.println("Connected to: " + sock.getInetAddress() + " Port: " + sock.getPort());
			} catch (IOException e) {
				System.out.println("Error: no request found");
			} 
			
			//set up input and output stream
			try {
				instream = sock.getInputStream();
				ostream = sock.getOutputStream();
			} catch (IOException e) {
				System.out.println("Error: cannot set up input or output stream.");
			}
			
			//get file name and file length
			String filename = getNullTerminatedString();
			System.out.println("Got file name: " + filename);
			long filelength = Long.parseLong(getNullTerminatedString());
			System.out.println("File size: " + filelength);
			
			//get file
			getFile(filename, filelength);
			System.out.println("Got the file.");
			
			//send back "@" for successful transfer and anything else for unsuccessful
			try {
				if (success == true) {
					ostream.write("@".getBytes());
					ostream.flush();
				}else {
					ostream.write("f".getBytes());
					ostream.flush();
				}
			} catch (IOException e) {
				System.out.println("Error: unable to send back response.");
			}
		}
		
	}
	
	/**
	* This method reads the bytes (terminated by ‘\0’) sent from the Client, one
	* byte at a time and turns the bytes into a String.
	* Set up a loop to repeatedly read bytes until a ‘\0’ is reached.
	*/
	private String getNullTerminatedString(){
		String result = "";
		byte[] curr = new byte[1];
		try {
			instream.read(curr);
		} catch (IOException e) {
			System.out.println("Error: cannot get bytes from data input stream.");
		}
		while (curr[0] != '\0') {
			result += new String(curr);
			try {
				instream.read(curr);
			} catch (IOException e) {
				System.out.println("Error: cannot get bytes from data input stream.");
			}
		}
		return result;
	}
	
	/**
	* This method takes an output file name and its file size, reads the binary
	* data (in a 1024-byte chunk) sent from the Client, and writes to the output
	* file a chunk at a time.
	* Use the FileOutputStream class to write bytes to a binary file
	* Set up a loop to repeatedly read and write chunks.
	*/
	private void getFile(String filename, long size){
		byte[] buffer = new byte[1024];
		File file;
		try {
			file = new File(filename);
			file.createNewFile();
			fostream = new FileOutputStream(file);
		} catch (Exception e) {
			System.out.println("Error: error in creating the file/file output stream");
		}
		
		try {
			while (instream.read(buffer, 0, 1024) != 1) {
				fostream.write(buffer, 0, 1024);
			}
			fostream.flush();
			fostream.close();
			success = true;
		} catch (IOException e1) {
			System.out.println("Error: cannot write to file.");
			success = false;
		}
		
	}
}