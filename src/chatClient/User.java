package chatClient;

public class User {
	DataManager myData;
	NetworkManager myNet;
	public Chat_GUI myGUI;
	public String username;

	public User(Chat_GUI thisGUI) {
		myGUI = thisGUI;
		myNet = new NetworkManager(thisGUI);
		myData = new DataManager(thisGUI);
	}
	public void StartUser() {		
		char fileExists = myData.testForFile();
		
		String password = "";

		//Chat_GUI myGUI = new Chat_GUI();
		while(true) {
			try {
				if(fileExists == 'y') {
					String[] JSONData = myData.parseJSONData();
					username = JSONData[0];
					password = JSONData[1];
					
					System.out.println("UserPassword: |" + password + "|");
					
					
					String userAuth = myGUI.authenticateUser(username);
					
					System.out.println("User input: |" + userAuth + "|");
	
					if(userAuth.equals(password)) {
						System.out.println("Password right");
						break;
					}
					else {
						System.out.println("Password wrong");
						myGUI.improperAuth(username);
					}				
				}
				else if(fileExists == 'n') {
					myGUI.createNewUser();
				}
				else {
					System.out.println("An error has occured. Code: 01");
					myGUI.showMessage("An error has occured. Code: 01");
					System.exit(0);
				}
				myGUI.ta.append("Welcome " + username + "\r\n");
			} catch(NullPointerException e) {
				System.out.println("Null entry");
				myGUI.improperAuth(username);
			}
		}
	}
	public void sendMessage(String messageToBeSent) {
		myNet.sendMessage(messageToBeSent, username);
	}
}
