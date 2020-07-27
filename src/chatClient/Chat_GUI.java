package chatClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class Chat_GUI {
	public JTextArea ta = new JTextArea();
    private JFrame frame = new JFrame("Chat Frame");
	private DataManager myData = new DataManager(this);
	public User myUser;
	NetworkManager myNetwork = new NetworkManager(this);
	
	public Chat_GUI() {

		//Creating the Frame
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.setSize(400, 400);
	     frame.setFocusable(true);
	     
	     //setup web browser features for help button
	     Desktop d = Desktop.getDesktop();

	     //Creating the MenuBar and adding components
	     JMenuBar mb = new JMenuBar();
	     JMenu connectionMenu = new JMenu("Connection");
	     JMenu helpMenu = new JMenu("Help");	     
	     mb.add(connectionMenu);	     
	     mb.add(helpMenu);
	     
	     JMenuItem joinChatButton = new JMenuItem("Join existing chat");
	     joinChatButton.addActionListener(
		  			new ActionListener() {
		  				public void actionPerformed(ActionEvent e) {
							if(myNetwork.connectedToServer == false) {
								String port = JOptionPane.showInputDialog(frame, "Please enter the session ID of your chat");
								myNetwork.connectToServer("192.168.1.25", Integer.parseInt(port));
								if(myNetwork.connectedToServer == true) {
									myNetwork.listenForMessage();
								}
						  	} else {
								showMessage("You are already in a chat! Please leave your current chat before joining a new one!");
							}
		  				}
		  			}
		  		);
	     JMenuItem startChatButton = new JMenuItem("Start new chat");
	     startChatButton.addActionListener(
		  			new ActionListener() {
		  				public void actionPerformed(ActionEvent e) {
							if(myNetwork.connectedToServer == false) {
								String port = JOptionPane.showInputDialog(frame, "Please enter the session ID of your chat");
								myNetwork.startNewServer("192.168.1.25", Integer.parseInt(port));
								if(myNetwork.connectedToServer == true) {
										myNetwork.listenForMessage();
								} else {
									showMessage("An error has occured, and the chat could not be started.");
								}
							} else {
								showMessage("You are already in a chat! Please leave your current chat before starting a new one!");
							}
		  				}
		  			}
		  		);
	     JMenuItem join3rdPartyChatButton = new JMenuItem("Join existing chat on a third-party server");
	     join3rdPartyChatButton.addActionListener(
		  			new ActionListener() {
		  				public void actionPerformed(ActionEvent e) {
							if(myNetwork.connectedToServer == false){
								showMessage("You are joining a chat on an unmoderated third-party server. Please note that PixelFyre INC. is not responsible for anything that happens on this server, and cannot guarentee the functionality of our client on an uncontrolled server. Proceed with caution. \n By connecting to a third party server, you agree to the terms and conditions outlined in our 'Third-Party connections licence agreement' document.");
								String server = JOptionPane.showInputDialog(frame, "Please enter the server address for the third-party server");
								String port = JOptionPane.showInputDialog(frame, "Please enter the session ID of your chat");
								myNetwork.connectToServer(server, Integer.parseInt(port));
								if(myNetwork.connectedToServer == true) {
									myNetwork.listenForMessage();
								}
							} else {
								showMessage("You are already in a chat! Please leave your current chat before joining a new one!");
							}
		  				}
		  			}
		  		);
	     JMenuItem exitChatButton = new JMenuItem("Exit chat");
	     exitChatButton.addActionListener(
		  			new ActionListener() {
		  				public void actionPerformed(ActionEvent e) {
							if(myNetwork.connectedToServer == true) {
								  myNetwork.logoutFromServer(myUser.username);
							} else {
								showMessage("You are not in a chat! Please connect to a chat before disconnecting!");
							}
		  				}
		  			}
		  		);
	     JMenuItem quitProgramButton = new JMenuItem("Quit program");
	     quitProgramButton.addActionListener(
	  			new ActionListener() {
	  				public void actionPerformed(ActionEvent e) {
						if(myNetwork.connectedToServer == false) {
							System.out.println("Program exited with 'Quit Program' from the connection menu.");
	  					PrintStream out;
						try {
							out = new PrintStream(new FileOutputStream("output.txt"));
							System.setOut(out);
						} catch (FileNotFoundException e1) {
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							e1.printStackTrace(pw);
							String sStackTrace = sw.toString(); // stack trace as a string
							myData.createLog(sStackTrace);
							e1.printStackTrace();
						}
	  					
	  					System.exit(0);
						} else {
							showMessage("You are currently connected to a chat. Please click the 'Exit chat' button that is either beside the text box, or under the 'Connection' menu on the top bar.");
						}
	  				}
	  			}
	  		);
	     connectionMenu.add(joinChatButton);
	     connectionMenu.add(startChatButton);
	     connectionMenu.add(exitChatButton);
	     connectionMenu.add(quitProgramButton);
	     
	     JMenuItem openOnlineHelpButton = new JMenuItem("Open online help site");
	     openOnlineHelpButton.addActionListener(
		  			new ActionListener() {
		  				public void actionPerformed(ActionEvent e) {
		  					try {
								d.browse(new URI("https://owensite1.herokuapp.com"));
							} catch (IOException e1) {
								//error handler
								StringWriter sw = new StringWriter();
								PrintWriter pw = new PrintWriter(sw);
								e1.printStackTrace(pw);
								String sStackTrace = sw.toString(); // stack trace as a string
								myData.createLog(sStackTrace);
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								StringWriter sw = new StringWriter();
								PrintWriter pw = new PrintWriter(sw);
								e1.printStackTrace(pw);
								String sStackTrace = sw.toString(); // stack trace as a string
								myData.createLog(sStackTrace);
								e1.printStackTrace();
							}
		  				}
		  			}
		  		);
	     helpMenu.add(openOnlineHelpButton);
	     
	     // Text Area at the Center
	     ta.setEditable(false);
	     JScrollPane scroll = new JScrollPane (ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	     //Creating the panel at bottom and adding components
	     JPanel panel = new JPanel(); // the panel is not visible in output
	     JLabel label = new JLabel("Enter Text");
	     JTextField tf = new JTextField(10); // accepts upto 10 characters
	     tf.addActionListener(
		 			new ActionListener() {
		 				public void actionPerformed(ActionEvent e) {
		 					String textInput = tf.getText();
		 					sendMessage(textInput);
		 					tf.setText("");
		 				}
		 			}
		 		);
	     JButton send = new JButton("Send");
	     send.addActionListener(
	 			new ActionListener() {
	 				public void actionPerformed(ActionEvent e) {
	 					String textInput = tf.getText();
	 					sendMessage(textInput);
	 					tf.setText("");
	 				}
	 			}
	 		);
	     JButton exit = new JButton("Exit chat");
	     exit.addActionListener(
	 			new ActionListener() {
	 				public void actionPerformed(ActionEvent e) {
						if(myNetwork.connectedToServer == true) {
							System.out.println("Chat exited with 'Exit Chat' button");
							myNetwork.logoutFromServer(myUser.username);
						} else {
							showMessage("You are not in a chat! Please connect to a chat before disconnecting!");
						}
	 				}
	 			}
	 		);
	     panel.add(label); // Components Added using Flow Layout
	     panel.add(tf);
	     panel.add(send);
	     panel.add(exit);

	     

	     //Adding Components to the frame.
	     frame.getContentPane().add(BorderLayout.SOUTH, panel);
	     frame.getContentPane().add(BorderLayout.NORTH, mb);
	     frame.getContentPane().add(BorderLayout.CENTER, scroll);
	     frame.setVisible(true);
	}
	
	public void sendMessage(String message) {
		myNetwork.sendMessage(message, myUser.username);
	}
	
	public String authenticateUser(String username) {
		String password = JOptionPane.showInputDialog(frame, "Please enter the password for user: " + username, null);		
		return password;
	}
	public void improperAuth(String username) {
		JOptionPane.showMessageDialog(frame, "Incorrect password for user: " + username);
	}
	public void createNewUser() {
		JOptionPane.showMessageDialog(frame, "Welcome to AOL instant messanger ii! Before you can connect, you will need to sign up for an account.");
		
		String newUsername = JOptionPane.showInputDialog(frame, "What would you like your username to be? This will be public to users you chat with, so don't use your real name.", null);
		String newPassword = JOptionPane.showInputDialog(frame, "What would you like the password to be for user: " + newUsername + "?", null);

		myData.createFile(newUsername, newPassword);
	}
	//public void errorHasOccured(int errorNo) {
	//	JOptionPane.showMessageDialog(frame, "An error has occured. Error code: " + errorNo);
	//}
	public void showMessage(String messageToShow) {
		JOptionPane.showMessageDialog(frame, messageToShow);
	}
	public void loggedError() {
		JOptionPane.showMessageDialog(frame, "An error has occured. Please check log.txt for more information.");
	}
	public void appendMessage(String message, String user) {
		if(user.equals(myUser.username)){
			user = "You";
		}
		System.out.println("Entered string append");
		String displayString = user + ": " + message + "\r\n";
		ta.append(displayString);
		System.out.println("finished string append");
	}
	public void appendUserlessMessage(String message) {
		ta.append(message);
	}
}
