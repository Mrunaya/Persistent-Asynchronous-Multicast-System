import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Coordinator {
 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader((String)args[0]));
	    int mPort = Integer.parseInt(reader.readLine());
		int tTime = Integer.parseInt(reader.readLine());
		int cPort=6600;
		System.out.println("Coordinator starting..");
		Connection cThread = new Connection(cPort);
		cThread.start();
		
		Multicast mThread = new Multicast(mPort);
		mThread.start();
	}

}
