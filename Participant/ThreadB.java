import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadB extends Thread{
	static ServerSocket serverSocket = null;
	String ip;
	Socket socket=null;
	int Id;
	int port;
	String logFile;
	boolean exit;
	public ThreadB(int ID, String iPAddr, int pPort, String File) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.Id=ID;
		this.ip=iPAddr;
		this.port=pPort;
		this.logFile=File;
		serverSocket=new ServerSocket(port);
		
		
	}	 @Override
	 public void run() {
		 try{
			 
			 while(true) {
				 if(exit)
					 break;
				 socket = serverSocket.accept();
				ObjectInputStream mInputStream = new  ObjectInputStream(socket.getInputStream());
				String message=(String)mInputStream.readObject();
				BufferedWriter bw = null;
				FileWriter fw = null;
		    	File file = new File(logFile);
		        FileWriter fr = new FileWriter(file, true);
		        BufferedWriter br = new BufferedWriter(fr);
		        PrintWriter pr = new PrintWriter(br);
		        pr.println(message);
		        pr.close();
		        br.close();
		        fr.close();
			 }

		 }catch(Exception e) {

		 }
	 }
	
	public void close(){
	    try{
	        
	    	serverSocket.close();
	 
	        exit = true;
	    }
	    catch(IOException e){
	      e.printStackTrace();
	    }
	  }

}
