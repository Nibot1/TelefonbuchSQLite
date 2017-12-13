
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
public class Server {

	/**
	 * @param args
	 */
	static ServerSocket anschluss;

	public static void main(String[] args) throws IOException {
		// Start ServerSocket on port 6000
		try {
			anschluss = new ServerSocket(6002);
		} catch (BindException e) {
			e.printStackTrace();
		}
		// Closes the Server when the Program is closed;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					anschluss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Database dataBase = new Database();
		// Starts an endless loop for listening for Client Input
		while (true) {
			// Start listening
			Socket lauschen = anschluss.accept();
			// Print Welcome Message
			System.out.println("Ich lausche. IP: " + lauschen.getInetAddress());
			// Read the Client Input
			InputStreamReader PortLeser = new InputStreamReader(lauschen.getInputStream());
			BufferedReader Eingabe = new BufferedReader(PortLeser);
			String S = Eingabe.readLine();
			// Print the Input of the Client
			PrintWriter Ausgabe = new PrintWriter(lauschen.getOutputStream(), true);
			if(S =="anzeigen") {
				String dbOut = dataBase.readTabe();
				if(dbOut == "null") {
					Ausgabe.println("Dein Telefonbuch ist leer.");
				}else {
					Ausgabe.println(dataBase.readTabe());	
				}
			}
			Ausgabe.close();
		}
	}
}
