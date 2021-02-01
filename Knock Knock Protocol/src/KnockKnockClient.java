import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.FlowLayout;

public class KnockKnockClient extends javax.swing.JFrame{
	public static void main(String args[]) {
		JFrame frame = new JFrame("Program 1a: Knock Knock Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel IPaddress = new JLabel("IP Address:");
		JTextField ipadd = new JTextField(30);
		ipadd.setMaximumSize(ipadd.getPreferredSize());
        panel.add(IPaddress);
        panel.add(ipadd);
        
        JLabel portnumber = new JLabel("Port Number:");
		JTextField portnum = new JTextField(10);
		portnum.setMaximumSize(ipadd.getPreferredSize());
		panel.add(portnumber);
        panel.add(portnum);
        JButton connect = new JButton("Connect");
        panel.add(connect);
        
        JLabel sendmessage = new JLabel("Message to Server:");
        JTextField message = new JTextField(50);
        JButton send = new JButton("Send");
        panel.add(sendmessage);
        panel.add(message);
        panel.add(send);
		
        frame.add(panel);
		frame.setVisible(true);
		
	}
	
	    /*public static void main(String args[]){
	       JFrame frame = new JFrame("My First GUI");
	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       frame.setSize(300,300);
	       JButton button = new JButton("Press");
	       frame.getContentPane().add(button); // Adds Button to content pane of frame
	       frame.setVisible(true);
	    }*/
}