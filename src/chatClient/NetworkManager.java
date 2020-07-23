package chatClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class reciveMessageThread extends Thread {
	private DataManager myData = new DataManager();
	private Chat_GUI myGUI = new Chat_GUI();
	
	private Socket sock;
	private DataInputStream din;
		
	public reciveMessageThread(Socket importedSocket) {
		sock = importedSocket;
	}
	
	public void run()
    { 
		
		while(true) 
		{
	        try
	        { 
				sock.setSoTimeout(300000);
	        	din=new DataInputStream(sock.getInputStream());
				String recivedMessage = din.readUTF();
				System.out.println(recivedMessage);
	            
				String[] parsedMessage = recivedMessage.split("/");
				
				if(parsedMessage[0] == "message") {
					myGUI.appendMessage(parsedMessage[2], parsedMessage[1], Chat_GUI.ta);
				}
			}
			catch(SocketTimeoutException e){
				myGUI.showMessage("No messages have been sent in five minutes. Disconnecting and closing room.");
			}
	        catch (Exception e1) 
	        { 
	        	StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String sStackTrace = sw.toString(); // stack trace as a string
				myData.createLog(sStackTrace); 
				System.out.println ("Exception is caught"); 
				e1.printStackTrace();
	        }
    	}
    }
}

public class NetworkManager {
	public double versionNumber = 0.2;
	private Socket s;

	public boolean connectedToServer = false;
	
	private DataManager myData = new DataManager();
	
	private DataOutputStream dout;
	
	public void connectToServer(String server, int port) {
		Chat_GUI myGUI = new Chat_GUI();
		
		try {
			s=new Socket(server, port);
			s.setSoTimeout(10000);
			connectedToServer = true;
		} catch (UnknownHostException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			myData.createLog(sStackTrace);			
			myGUI.showMessage("That host does not exist. Please make sure the server is online and functioning correctly. If connecting to a third party server, please make sure you spelt the hostname correctly.");
			e1.printStackTrace();
		} catch(SocketTimeoutException e) {
			myGUI.showMessage("The connection timed out. Please make sure the server is online and functioning correctly. If connecting to a third party server, please make sure you spelt the hostname correctly.");

		} catch(ConnectException e) {
			//error logger
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			myData.createLog(sStackTrace);	
			myGUI.showMessage("The connection was refused. Please make sure you entered the correct session ID. If connecting to a third party server, please make sure you spelt the hostname correctly.");
		} catch (IOException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			myData.createLog(sStackTrace);
			e1.printStackTrace();
		}
	}
	
	public void sendMessage(String message, String username) {
		try {
			dout=new DataOutputStream(s.getOutputStream());
			String outputString = "message/" + username + "/" + message;
			
			dout.writeUTF(outputString);
			dout.flush();
		} catch (IOException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			myData.createLog(sStackTrace);
			e1.printStackTrace();
		}

	}
	
	public void logoutFromServer(String username) {
		try {
			dout=new DataOutputStream(s.getOutputStream());
			dout.writeUTF("system/" + username + "/logout");
			dout.flush();
		} catch (IOException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			myData.createLog(sStackTrace);
			e1.printStackTrace();
			e1.printStackTrace();
		}
	}
	
	public void listenForMessage() {
		reciveMessageThread myThread = new reciveMessageThread(s);		
		myThread.start();
	}
	
}

// TODO message status, user routing, login, check version
