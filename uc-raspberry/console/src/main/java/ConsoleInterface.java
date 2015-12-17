import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by bas on 12-12-15.
 */

public class ConsoleInterface {
    public ConsoleInterface(){}


	public void run(){
		System.out.println("Welcome to the Alarm System. Input your commands below:");
		while (true){
			BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
			String response = "";
			try {
				response = bis.readLine();

			} catch (IOException e) {
				e.printStackTrace();
			}

			String[] commands = response.split(" ");
			if(commands[0].equals("alarm")) {
				//System.out.println(commands[1]);
				if(commands[1].matches("([0-1]??[0-9]|[2][0-3]):[0-5][0-9]")) {

					System.out.println("Time set for tomorrow " + commands[1]);
				}else{
					System.out.println("Time parsing failed. Expected hh:mm .");
				}
			}else if (commands[0].equals("quit") || commands[0].equals("exit") || commands[0].equals("exsquid")){
				System.out.println("Bye bye!");
				System.exit(0);
			}else {
				printHelp();
			}
		}
	}

	private void printHelp(){
		System.out.println("Valid commands are:");
		System.out.println("alarm [time]");
		System.out.println("\ttime is formatted as hh:mm");
	}


	public static void main(String[] args){
		ConsoleInterface con =  new ConsoleInterface();
	    con.run();
    }
}
