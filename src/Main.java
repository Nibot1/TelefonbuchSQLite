/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Tobin Rosenau
 *
 */
public class Main {

	/**
	 * @param args
	 */
	static ServerSocket anschluss;
	public static void main(String[] args) throws IOException{		
		// Start ServerSocket on port 6001
		try {
		 anschluss = new ServerSocket(6001);
		}catch(BindException e) {
			e.printStackTrace();
		}
		//Closes the Server when the Program is closed;
		  Runtime.getRuntime().addShutdownHook(new Thread()
			{
			    @Override
			    public void run()
			    {
			        try {
						anschluss.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			});
		//Starts an endless loop for listening for Client Input
		  while (true) {
		// Start listening
		    Socket lauschen=anschluss.accept();
		// Print Welcome Message
		    System.out.println("Ich lausche.");
		// Read the Client Input
		    InputStreamReader PortLeser=new
		    InputStreamReader(lauschen.getInputStream());
		    BufferedReader Eingabe= new BufferedReader(PortLeser);
		    String S=Eingabe.readLine();
	   // Print the Input of the Client
		    PrintWriter Ausgabe= new PrintWriter(lauschen.getOutputStream(),true);
		    Ausgabe.println("Ebenfalls " + S);
		    Ausgabe.close();
		  }
		}

	
	//Closes Server
	public void closeServer() {
		try {
			anschluss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	}


