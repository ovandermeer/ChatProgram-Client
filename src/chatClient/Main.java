package chatClient;

public class Main {

	public static void main(String[] args) {
		Chat_GUI myGUI = new Chat_GUI();

		User myUser = new User(myGUI);
		myGUI.myUser = myUser;

		myUser.StartUser();
	}

}
