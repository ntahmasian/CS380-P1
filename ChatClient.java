import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable {
	protected Socket socket;

	public ChatClient(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args)  {
		try (Socket socket = new Socket("codebank.xyz", 38001)) {
			// take the user name 
			String userName;
			Scanner scan = new Scanner (System.in);
			System.out.println("Please enter your username: ");
			userName = scan.nextLine();
			
			//Sends username to server
			PrintStream sendToServer = new PrintStream(socket.getOutputStream(), true, "UTF-8");
			sendToServer.println(userName);
			
			// make a thread
			Thread thread = new Thread(new ChatClient(socket));
			thread.start();
			
			// loop
			while(true) {
				userName = scan.nextLine();
				sendToServer.println(userName);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {		
		try {  			
			while(true) {
				//Communicate with the server 
				InputStream getIS = socket.getInputStream();
				InputStreamReader readIS = new InputStreamReader(getIS, "UTF-8");
				BufferedReader serverMassage = new BufferedReader(readIS);
				String massage = serverMassage.readLine();
				System.out.println(massage);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}		
	} 
}
