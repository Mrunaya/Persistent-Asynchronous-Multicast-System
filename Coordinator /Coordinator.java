import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Coordinator {
 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader((String)args[0]));
	    int mPort = Integer.parseInt(reader.readLine());
		int tTime = Integer.parseInt(reader.readLine());
		Map<Integer, Map<String,Integer>> multicastGrp= new HashMap<Integer,Map<String,Integer>>();
		Map<Integer, Map<String, Integer>> disconnectMulticastGrp= new HashMap<Integer,Map<String, Integer>>();
		Map<Long, String> offlinemsgs= new HashMap<Long,String>();
		System.out.println("Coordinator starting..");
		Connection cThread = new Connection(mPort,multicastGrp,disconnectMulticastGrp,tTime,offlinemsgs);
		cThread.start();
		
		Multicast mThread = new Multicast(mPort,multicastGrp,disconnectMulticastGrp,tTime,offlinemsgs);
		mThread.start(); 
	}

}
