import java.io.*;
import java.net.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textIPaddress;
	private JTextField textPortNum;
	private JTextField textMessage;
	private Socket socket;
	private PrintWriter writeSock;
	private BufferedReader readSock;
	private JButton btnConnect;
	private JButton btnSend;
	private JTextArea textArea;
	private int portNumber;
	private String hostAddress;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 548);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setVerticalAlignment(SwingConstants.TOP);
		
		textIPaddress = new JTextField();
		textIPaddress.setColumns(20);
		
		JLabel lblNewLabel = new JLabel("Port Number");
		
		textPortNum = new JTextField();
		textPortNum.setText("5520");
		textPortNum.setColumns(10);
		
		btnConnect = new JButton("Connect");
		ConnectListener coListen = new ConnectListener();
		btnConnect.addActionListener(coListen);
		
		JLabel lblMessageToServer = new JLabel("Message to Server");
		
		textMessage = new JTextField();
		textMessage.setColumns(10);
		
		btnSend = new JButton("Send");
		SendListener seListen = new SendListener();
		btnSend.addActionListener(seListen);
		
		JLabel lblClientserverCommunication = new JLabel("Client/Server Communication");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		//setting up GUI
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblIpAddress)
									.addGap(18)
									.addComponent(textIPaddress, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addGap(18)
									.addComponent(textPortNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnConnect))
								.addComponent(lblMessageToServer)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(textMessage, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
						.addComponent(btnSend)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblClientserverCommunication))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIpAddress)
						.addComponent(textIPaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textPortNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
					.addGap(18)
					.addComponent(lblMessageToServer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSend)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblClientserverCommunication)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public class ConnectListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==btnConnect) {
				if (btnConnect.getText().equals("Connect")) {
					//open socket after "Connect" button is clicked
					try {
						hostAddress = textIPaddress.getText();
						portNumber = Integer.parseInt(textPortNum.getText());
						socket = new Socket(hostAddress, portNumber);
						writeSock = new PrintWriter(socket.getOutputStream(), true);
						readSock = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						//change text to "Disconnect" and append connection status
						btnConnect.setText("Disconnect");
						textArea.append("Connected to Server\n");
					}catch(Exception x) {
						textArea.append("Error: " + x);
						socket = null;
					}
				}else { 
					//close socket after "Disconnect" button is clicked
					try {
						readSock.close();
						writeSock.close();
						socket.close();
						socket = null;
						//change text to "Connect" and append connection status
						btnConnect.setText("Connect");
						textArea.append("Disconnected!\n");
					}catch(Exception x) {
						textArea.append("Error: " + x);
						socket = null;
					}
				}
			}
		}
	}
	
	public class SendListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==btnSend) {
				//wrap in try/catch
				if (btnConnect.getText().equals("Connect")) {
					//trying to send message without connecting
					textArea.append("Error: Client has not connected to server yet.\n");
				}
				else if (socket != null) {
					writeSock.println(textMessage.getText());
					textArea.append("Client: " + textMessage.getText() + "\n");
					String dataRead = null;
					try {
						dataRead = readSock.readLine();
						textArea.append("Server: " + dataRead + "\n");
					} catch (Exception x) {
						textArea.append("Error: " + x);
					}
					
					//close socket if server says "Good Bye!"
					if (dataRead.equals("Good Bye!")) {
						//close socket
						try {
							readSock.close();
							writeSock.close();
							socket.close();
							socket = null;
							btnConnect.setText("Connect");
							textArea.append("Disconnected!\n");
						}catch(Exception x) {
							textArea.append("Error: " + x);
							socket = null;
						}
					}
				}
			}
		}
	}
}
