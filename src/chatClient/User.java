package chatClient;

public class User {
	public String username;
	public void StartUser() {
		DataManager myData = new DataManager();
		Chat_GUI myGUI = new Chat_GUI();
		
		char fileExists = myData.testForFile();
		
		String password = "";
		
		if(fileExists == 'y') {
			String[] JSONData = myData.parseJSONData();
			username = JSONData[0];
			password = JSONData[1];
			
			System.out.println("UserPassword: |" + password + "|");
			
			while(true) {
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
		}
		else if(fileExists == 'n') {
			myGUI.createNewUser();
		}
		else {
			System.out.println("An error has occured. Code: 01");
			myGUI.errorHasOccured(01);
			System.exit(0);
		}
	}
	public void sendMessage(String messageToBeSent) {
		
	}
}
