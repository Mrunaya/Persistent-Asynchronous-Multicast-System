import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread{
	int cPort;
	static Socket socket = null;
	private static ServerSocket server;
	public Connection(int Port) throws IOException {
		// TODO Auto-generated constructor stub
		this.cPort=Port;
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

				 String inputCmd = (String) inputStream.readObject();

				 String[] userInput = inputCmd.split(" ", 2);

				 if("quit".equalsIgnoreCase(userInput[0])){
					 outputStream.writeObject("quit");
					 inputStream.close();
					 outputStream.close();
					 break;
				 }
				 switch(userInput[0]) {
				 case "Register":
					 break;
				 case "Deregister":
					 break;
				 case "Disconnect":
					 break;
				 case "Reconnect":
					 break;
					 
				 }
			 }

		 }catch(Exception e) {

		 }
	 }

}
