import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Participant {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader((String)args[0]));
		int ID = Integer.parseInt(reader.readLine());
		String logFile = reader.readLine();
		String[] ipPort = reader.readLine().split(" ");
		String IPAddr=ipPort[0];
		int cPort=Integer.parseInt(ipPort[1]);
		System.out.println("Participnat starting..");
		System.out.println("cPort "+cPort);
		Socket pSocket = new Socket(IPAddr,cPort);
		System.out.println("Participnat connected to coordinator..");
		String userCmd = "";
		ObjectOutputStream outputStream = new  ObjectOutputStream(pSocket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(pSocket.getInputStream());
		ThreadB threadB=null;

		try  {
			Scanner sc = new Scanner(System.in);
			while(!userCmd.equalsIgnoreCase("quit")) {
				//USER COMMANDS BEGIN FROM HERE----->
				userCmd = sc.nextLine();
				String[] cmdVal = userCmd.split(" ", 2);


				switch (cmdVal[0]) {
				case "Register":
					int pPort=Integer.parseInt(cmdVal[1]);
					 threadB= new ThreadB(ID,IPAddr,pPort);
					threadB.start();
					outputStream.writeObject("Register");
					break;
					
				case "Deregister":
					outputStream.writeObject("Deregister");
					//threadB.close();
					break;
					
				case "Disconnect":
					outputStream.writeObject("Disconnect");
					break;
					
				case "Reconnect":
					 pPort=Integer.parseInt(cmdVal[1]);
					 threadB= new ThreadB(ID,IPAddr,pPort);
					threadB.start();
					outputStream.writeObject("Reconnect");
					break;
					
				case "Multicast":
					outputStream.writeObject("Multicast "+cmdVal[1]);
					String acknow= (String)inputStream.readObject();
					
					break;
					
				}
			}


		}catch(Exception e) {

		}

	}
}
