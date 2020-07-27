package chatClient;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

class reciveMessageThread extends Thread {
	private DataManager myData;
	private Chat_GUI myGUI;
	
	private Socket sock;
	private DataInputStream din;
		
	public reciveMessageThread(Socket importedSocket, Chat_GUI thisGUI) {
		sock = importedSocket;
		myGUI = thisGUI;
		myData = new DataManager(myGUI);
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
				
				String[] parsedMessage = new String[2];

				parsedMessage = recivedMessage.split("/");

				System.out.println(Arrays.toString(parsedMessage));
				
				if(parsedMessage[0].equals("message")) {
					myGUI.appendMessage(parsedMessage[2], parsedMessage[1]);
					
				}
			}
			catch(SocketTimeoutException e){
				myGUI.showMessage("No messages have been sent in five minutes. Disconnecting and closing room.");
			}
			catch(SocketException e1) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String sStackTrace = sw.toString();
				if(sStackTrace.startsWith("java.net.SocketException: Socket closed")){
					break;
				}
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
	private Socket tempSocket;
	private Socket s;

	public boolean connectedToServer;
	
	private DataManager myData;
	
	private DataOutputStream dout;

	private Chat_GUI myGUI;

	public NetworkManager(Chat_GUI thisGUI) {
		myGUI = thisGUI;
		myData = new DataManager(myGUI);
		connectedToServer = false;
	}
	
	public void connectToServer(String server, int port) {
		
		try {
			tempSocket=new Socket(server, port);
			tempSocket.setSoTimeout(10000);
			connectedToServer = true;

			System.out.println("HENLO");

			DataInputStream din = new DataInputStream(tempSocket.getInputStream());
			String serverResponse = din.readUTF();

			System.out.println("BILO");

			if(serverResponse.startsWith("personalThread")) {
				tempSocket.close();
				String[] parsedResponse = serverResponse.split("/");
				s = new Socket(server, Integer.parseInt(parsedResponse[1]));
				connectedToServer = true;
			}
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
	
	public void startNewServer(String server, int port) {
		try {
			s=new Socket(server, 10000);
			s.setSoTimeout(10000);
			
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			DataInputStream din = new DataInputStream(s.getInputStream());
			dout.writeUTF("startRoom/" + Integer.toString(port));
			
			String serverResponse = din.readUTF();
			if(serverResponse.equals("startRoom-success")) {
				connectToServer(server, port);
			}
					
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
			System.out.println("UTF flushed");
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
			dout.writeUTF("system/logout");
			dout.flush();
			s.close();
			connectedToServer = false;
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
		reciveMessageThread myThread = new reciveMessageThread(s, myGUI);		
		myThread.start();
	}
	
}

// TODO message status, user routing, login, check version
