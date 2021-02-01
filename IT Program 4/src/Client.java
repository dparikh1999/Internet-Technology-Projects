import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField serverAddressText;
	private JFileChooser fileChooser;
	private JTextArea textArea;
	private JButton btnConnectUpload;
	private Socket sock;
	private int portNum;
	private String serverAddress;
	private File selectedFile; 
	private FileInputStream fstream;
	private byte nullByte = '\0';
	private DataInputStream instream;
	private OutputStream ostream; 

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
		setBounds(100, 100, 512, 607);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//create file chooser and filter 
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MSWord", "doc", "docx");
		fileChooser.setFileFilter(filter);
		
		JLabel lblServerAddress = new JLabel("Server Address: ");
		
		serverAddressText = new JTextField();
		serverAddressText.setColumns(10);
		
		btnConnectUpload = new JButton("Connect & Upload");
		CoUpListener listener = new CoUpListener();
		btnConnectUpload.addActionListener(listener);
		
		JLabel lblErrorMessages = new JLabel("Error Messages:");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, 492, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblErrorMessages)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblServerAddress)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(serverAddressText, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnConnectUpload))
								.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerAddress)
						.addComponent(serverAddressText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnectUpload))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblErrorMessages)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public class CoUpListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==btnConnectUpload) {
				selectedFile = fileChooser.getSelectedFile();
				serverAddress = serverAddressText.getText();
				
				//check if user correctly selected a file and inputted a server address
				if (selectedFile == null) {
					textArea.append("Error: please select a file.\n");
					return;
				}if (serverAddress.isEmpty() || serverAddress == null) {
					textArea.append("Error: please input a server address.\n");
					return;
				}
				
				//open socket
				try {
					portNum = 5520;
					sock = new Socket(serverAddress, portNum);
					textArea.append("Connected.\n");
					instream = new DataInputStream(sock.getInputStream());
				}catch(Exception x) {
					textArea.append("Error: cannot open socket \n");
					sock = null;
					return;
				}
				
				//create output stream
				ostream = null;
				try {
					ostream = sock.getOutputStream();
				} catch (IOException e1) {
					textArea.append("Error: cannot get socket output stream.\n");
				}
				
				sendNullTerminatedString(selectedFile.getName()); //send file name
				textArea.append("Sent file name: " + selectedFile.getName() + "\n");
				sendNullTerminatedString(Long.toString(selectedFile.length())); //send file length
				textArea.append("Sent file name: " + selectedFile.length() + "\n");
				sendFile(selectedFile.getPath());
				textArea.append("File sent. Waiting for the Server.....\n");
				
				//check if server sent '@' byte
				int servResponse;
				try {
					servResponse = instream.read();
					if (servResponse == 64) { //check if returns ascii "@"
						textArea.append("Upload O.K.\n");
					}else {
						textArea.append("Upload not received.\n");
					}
				} catch (IOException e1) {
					textArea.append("Error: unable to receive server response.\n");
				}
					
				textArea.append("Disconnected.\n");
				try {
					ostream.close();
				} catch (IOException e2) {
					textArea.append("Error: could not close output stream.\n");
				}
			}
		}
	}
	
	/**
	* This method takes a String s (either a file name or a file size,) as a
	* parameter, turns String s into a sequence of bytes ( byte[] ) by calling
	* getBytes() method, and sends the sequence of bytes to the Server. A null
	* character ‘\0’ is sent to the Server right after the byte sequence.
	*/
	private void sendNullTerminatedString(String s) {
		byte[] bytes = s.getBytes();
		//send null terminated bytes
		try {
			ostream.write(bytes);
			ostream.write(nullByte);
			ostream.flush();
		} catch (IOException e) {
			textArea.append("Error: unable to send null terminated string.\n");
		}
	}
	
	/**
	* This method takes a full-path file name, decomposes the file into smaller
	* chunks (each with 1024 bytes), and sends the chunks one by one to the
	* Server (loop until all bytes are sent.) A null character ‘\0’ is sent to
	* the Server right after the whole file is sent.
	*/
	private void sendFile(String fullPathFileName){
		byte[] buffer = new byte[1024];
		try {
			fstream = new FileInputStream(fullPathFileName);
		} catch (FileNotFoundException e2) {
			textArea.append("Error: cannot set up file input stream.\n");
		}
		
		try {
			textArea.append("Sending file.........\n");
			while (fstream.read(buffer) != -1) {
				ostream.write(buffer, 0, 1024);
			}
			ostream.write(nullByte);
			ostream.flush();
		} catch (IOException e) {
			textArea.append("Error: IOException.");
		}
		
	}
}
