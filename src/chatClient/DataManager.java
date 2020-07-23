package chatClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {	
	public char testForFile() {
		try {
		      File myObj = new File("userData.json");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		        return 'n';
		      } else {
		        System.out.println("File already exists.");
		        return 'y';
		      }
		    } catch (IOException e1) {
		    	StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String sStackTrace = sw.toString(); // stack trace as a string
				createLog(sStackTrace);
		      e1.printStackTrace();
		      return 'e';
		    }
	}
	public void createFile(String username, String password) {
		JSONObject myJSON = new JSONObject();
        myJSON.put("username", username);
        myJSON.put("pass", password);
        
        try {
            FileWriter myWriter = new FileWriter("userData.json");
            String JSONtoWrite = myJSON.toJSONString();
            
            myWriter.write(JSONtoWrite);
            System.out.println(JSONtoWrite);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e1) {
        	  StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String sStackTrace = sw.toString(); // stack trace as a string
				createLog(sStackTrace);
            e1.printStackTrace();
          }
	}
	public String[] parseJSONData() {
		// parsing file "JSONExample.json" 
        Object obj;
		try {
			obj = new JSONParser().parse(new FileReader("userData.json"));
		} catch (FileNotFoundException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			createLog(sStackTrace);
			String[] output = {"noFile"};
			return output;
		} catch (IOException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			createLog(sStackTrace);
			String[] output = {"IOException"};
			return output;
		} catch (ParseException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			createLog(sStackTrace);
			String[] output = {"ParseExeption"};
			return output;
		} 
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
          
        // getting firstName and lastName 
        String username = (String) jo.get("username"); 
        String pass = (String) jo.get("pass");
        
        String[] output = {username, pass};
        return output;
		
	}
	public void createLog(String errorTrace) {
		Chat_GUI myGUI = new Chat_GUI();
		
		try {
		      FileWriter myWriter = new FileWriter("log.txt");
		      myWriter.write(errorTrace);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		      myGUI.loggedError();
		    } catch(FileNotFoundException e1) {
		    	File myObj = new File("log.txt");
		    } catch (IOException e1) {
		    	StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String sStackTrace = sw.toString(); // stack trace as a string
				createLog(sStackTrace);
		      e1.printStackTrace();
		    }
	}
}
