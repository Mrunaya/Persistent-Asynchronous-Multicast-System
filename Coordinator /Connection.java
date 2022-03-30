import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection extends Thread{
	int cPort;
	static Socket socket = null;
	Map<Integer, Map<String, Integer>> disconnectMulticastGrp= new HashMap<Integer,Map<String, Integer>>();
	Map<Integer, Map<String, Integer>> multicastGrp= new HashMap<Integer,Map<String, Integer>>();
	private static ServerSocket server;
	public Connection(int Port, Map<Integer, Map<String, Integer>> multicastGrp2) throws IOException {
		// TODO Auto-generated constructor stub
		this.cPort=Port;
		this.multicastGrp=multicastGrp2;
		server = new ServerSocket(cPort);
	} 
	 @Override
	 public void run() {
		 try{
			 while(true) {

				 System.out.println("Waiting for connection!");
				 socket = server.accept();
				 System.out.println("Participant connected!");
				 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

				 while(true) {
					 String inputCmd = (String) inputStream.readObject();

					 String[] userInput = inputCmd.split(" ");



					 if("quit".equalsIgnoreCase(userInput[0])){
						 outputStream.writeObject("quit");
						 inputStream.close();
						 outputStream.close();
						 break;
					 }
					 switch(userInput[0]) {
					 case "Register":
						 Map<String,Integer> map= new HashMap<String,Integer>();

						 int ID= Integer.parseInt(userInput[1]);
						 String ip= userInput[2];
						 int port= Integer.parseInt(userInput[3]);
						 map.put(ip, port);
						 multicastGrp.put(ID,map);
						 System.out.println("multicast grp after register "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after register "+disconnectMulticastGrp);
						 break;
					 case "Deregister":
						 Map<String,Integer> map1= new HashMap<String,Integer>();

						 ID= Integer.parseInt(userInput[1]);
						 ip= userInput[2];
						 port= Integer.parseInt(userInput[3]);
						 map1.put(ip, port);
						 multicastGrp.remove(ID);
						 System.out.println("multicast grp after deregister "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after deregister "+disconnectMulticastGrp);
						 break;
					 case "Disconnect":
						 System.out.println("In disconnect");
						 ID= Integer.parseInt(userInput[1]);
						 Map disconnectMap=multicastGrp.get(ID);
						 disconnectMulticastGrp.put(ID,disconnectMap);
						 multicastGrp.remove(ID);System.out.println("multicast grp after disconnect "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after disconnect "+disconnectMulticastGrp);
						 break;
					 case "Reconnect":
						 map= new HashMap<String,Integer>();

						 ID= Integer.parseInt(userInput[1]);
						 ip= userInput[2];
						 port= Integer.parseInt(userInput[3]);
						 map.put(ip, port);
						 multicastGrp.put(ID,map);
						 disconnectMulticastGrp.remove(ID);
						 System.out.println("multicast grp after Reconnect "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after Reconnect "+disconnectMulticastGrp);
						  
						 break;

					 }
				 }
			 }

		 }catch(Exception e) {
e.printStackTrace();
		 }
	 }

}
