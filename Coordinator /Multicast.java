import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Multicast extends Thread {
	int mPort;
	static Socket socket = null;
	private static ServerSocket server;
	public Multicast(int Port) throws IOException {
		// TODO Auto-generated constructor stub
		this.mPort=Port;
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
				 String msg=userInput[1];// msg to be multicasted
				
			 }

		 }catch(Exception e) {

		 }
	 }

}
