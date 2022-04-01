import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Multicast extends Thread {
	int mPort;
	static Socket socket = null;
	Map<Integer,  Map<String, Integer>> multicastGrp= new HashMap<Integer, Map<String, Integer>>();
	Map<Integer, Map<String, Integer>> disconnectMulticastGrp= new HashMap<Integer,Map<String, Integer>>();
	Map<Long, String> offlinemsgs;
	private static ServerSocket server;
	public Multicast(int Port, Map<Integer, Map<String, Integer>> mMap, Map<Integer, Map<String, Integer>> dMap, int tTime, Map<Long, String> oMap) throws IOException {
		// TODO Auto-generated constructor stub
		this.mPort=Port;
		this.multicastGrp=mMap;
		this.disconnectMulticastGrp=dMap;
		this.offlinemsgs=oMap;
		server = new ServerSocket(mPort);
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
				 

				 String inputCmd = (String) inputStream.readObject();

				 String[] userInput = inputCmd.split(" ", 2);

				 if("quit".equalsIgnoreCase(userInput[0])){
					 outputStream.writeObject("quit");
					 inputStream.close();
					 outputStream.close();
					 break;
				 }
				 String msg=userInput[0];// msg to be multicasted
				 outputStream.writeObject("message received!");
				 System.out.println("multicastGrp "+multicastGrp);
				 System.out.println("disconnectMulticastGrp "+disconnectMulticastGrp);
				 //Set<Integer> keys = multicastGrp.keySet();
				 Integer[] Ids = multicastGrp.keySet().toArray(new Integer[multicastGrp.size()]);
				 System.out.println("Ids "+Ids);
				 for(int i=0; i<Ids.length; i++) {
					 Map innerMap=multicastGrp.get(Ids[i]);
					 Set<String> ipAddress=innerMap.keySet();
					 String[] ip = ipAddress.toArray(new String[ipAddress.size()]);
					 int port= (int) innerMap.get(ip[0]);
					 System.out.println("port "+port);
					 Socket mSocket = new Socket(ip[0],port);
					 ObjectOutputStream mOutputStream = new  ObjectOutputStream(mSocket.getOutputStream());
					 mOutputStream.writeObject(msg);
					 System.out.println(mOutputStream);

				 }
				/* Integer[] disconnectedIds = disconnectMulticastGrp.keySet().toArray(new Integer[disconnectMulticastGrp.size()]);
				 long timestampSeconds= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
				 System.out.println(timestampSeconds);
				 for(int i=0; i<disconnectedIds.length; i++) {
					 Map innerMap=disconnectMulticastGrp.get(disconnectedIds[i]);
					 Set<String> ipAddress=innerMap.keySet();
					 String[] ip = ipAddress.toArray(new String[ipAddress.size()]);
					 int port= (int) innerMap.get(ip[0]);
					// offlinemsgs.put(timestampSeconds, msg);

				 }*/
				 if(!disconnectMulticastGrp.isEmpty()) {
					 long timestampSeconds= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
					 offlinemsgs.put(timestampSeconds, msg);
				 }
				 socket.close();
				
			 }

		 }catch(Exception e) {
e.printStackTrace();
		 }
	 }

}
