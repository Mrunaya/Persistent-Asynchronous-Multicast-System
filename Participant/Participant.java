import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
		String userCmd = "";
		Socket pSocket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		ThreadB threadB=null;
		int mPort = 0;
 
		try  {
			
			while(!userCmd.equalsIgnoreCase("quit")) {
				//USER COMMANDS BEGIN FROM HERE----->
				Scanner sc = new Scanner(System.in);
				System.out.println("Command : ");
				userCmd = sc.nextLine();	
				String[] cmdVal = userCmd.split(" ", 2);


				switch (cmdVal[0]) {
				case "Register":
					pSocket= new Socket(IPAddr,cPort);
					outputStream= new  ObjectOutputStream(pSocket.getOutputStream());
					inputStream= new ObjectInputStream(pSocket.getInputStream());
					
					int pPort=Integer.parseInt(cmdVal[1]);
					threadB= new ThreadB(ID,IPAddr,pPort,logFile);
					threadB.start();
					outputStream.writeObject(cmdVal[0]+" "+ID+" "+Inet4Address.getLocalHost().getHostAddress()+" "+pPort);
					 mPort=(int) inputStream.readObject();
					break;
					
				case "Deregister":
					pSocket= new Socket(IPAddr,cPort);
					outputStream= new  ObjectOutputStream(pSocket.getOutputStream());
					
					inputStream= new ObjectInputStream(pSocket.getInputStream());
					
					outputStream.writeObject(cmdVal[0]+" "+ID);
					threadB.close();
					pSocket.close();
					outputStream.close();
					inputStream.close();
					break;
					
				case "Disconnect":
					pSocket= new Socket(IPAddr,cPort);
					outputStream= new  ObjectOutputStream(pSocket.getOutputStream());
					inputStream= new ObjectInputStream(pSocket.getInputStream());
					
					outputStream.writeObject("Disconnect "+ID);
					threadB.close();
					break;
					
				case "Reconnect":
					pSocket= new Socket(IPAddr,cPort);
					outputStream= new  ObjectOutputStream(pSocket.getOutputStream());
					inputStream= new ObjectInputStream(pSocket.getInputStream());
					
					 pPort=Integer.parseInt(cmdVal[1]);
					 threadB= new ThreadB(ID,IPAddr,pPort,logFile);
					threadB.start();
					outputStream.writeObject(cmdVal[0]+" "+ID+" "+Inet4Address.getLocalHost().getHostAddress()+" "+pPort);
					break;
					
				case "Multicast":
					
					Socket mSocket = new Socket(IPAddr,mPort);
					ObjectOutputStream mOutputStream = new  ObjectOutputStream(mSocket.getOutputStream());
					ObjectInputStream mInputStream = new ObjectInputStream(mSocket.getInputStream());
					
					mOutputStream.writeObject(cmdVal[1]);
					String acknow= (String)mInputStream.readObject();
					
					break;
					
				}
			}


		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}
