import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Connection extends Thread{
	int cPort=6600;
	int mPort;
	static Socket socket = null;
	Map<Integer, Map<String, Integer>> disconnectMulticastGrp= new HashMap<Integer,Map<String, Integer>>();
	Map<Integer, Map<String, Integer>> multicastGrp= new HashMap<Integer,Map<String, Integer>>();
	private static ServerSocket server;
	Map<Long, String> offlinemsgs;
	int td;
	public Connection(int Port, Map<Integer, Map<String, Integer>> mMap, Map<Integer, Map<String, Integer>> dMap, int tTime, Map<Long, String> oMsg) throws IOException {
		// TODO Auto-generated constructor stub
		this.mPort=Port;
		this.multicastGrp=mMap;
		this.disconnectMulticastGrp=dMap;
		this.offlinemsgs=oMsg;
		this.td=tTime;
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

				// while(true) {
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
						 outputStream.writeObject(mPort);
						 System.out.println("multicast grp after register "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after register "+disconnectMulticastGrp);
						 break;
					 case "Deregister":
						
						 ID= Integer.parseInt(userInput[1]);
						multicastGrp.remove(ID);
						 System.out.println("multicast grp after deregister "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after deregister "+disconnectMulticastGrp);
						 break;
					 case "Disconnect":
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
						 

						 long timestampSeconds= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())-td;
						// Set<Long> keys = offlinemsgs.keySet();
						 Long[] Ids = offlinemsgs.keySet().toArray(new Long[offlinemsgs.size()]);
						 for(int i=0;i<Ids.length;i++) {
							if(Ids[i]>=timestampSeconds) {
							 socket = new Socket(ip, port);
		     	               outputStream = new ObjectOutputStream(socket.getOutputStream());
		     		             outputStream.writeObject(offlinemsgs.get(Ids[i]));
							}
						 }
						 
						 map.put(ip, port);
						 multicastGrp.put(ID,map);
						 disconnectMulticastGrp.remove(ID);
						 System.out.println("multicast grp after Reconnect "+multicastGrp);
						 System.out.println("disconnectMulticastGrp grp after Reconnect "+disconnectMulticastGrp);
						  
						 break;

					 }
				 //}
			 }

		 }catch(Exception e) {
e.printStackTrace();
		 }
	 }

}
