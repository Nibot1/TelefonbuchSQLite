
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.*;

public class Server {

	static ServerSocket anschluss;
	public static void main(String[] args) throws IOException {

		// Start ServerSocket on port 6000
		try {
			anschluss = new ServerSocket(6000);
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
			System.out.println("Ich lausche auf anfragen von: " + lauschen.getInetAddress());
			// Read the Client Input
			InputStreamReader PortLeser = new InputStreamReader(lauschen.getInputStream());
			BufferedReader Eingabe = new BufferedReader(PortLeser);
			String S = Eingabe.readLine();
			// Set up an PrintWriter
			PrintWriter Ausgabe = new PrintWriter(lauschen.getOutputStream(), true);
			//Handle Clientrequest
			if(S.equals("2")) {
				System.out.println("Kontakte anzeigen "+lauschen.getInetAddress()+":"+lauschen.getPort());
				String dbOut = dataBase.readTable();
				if(dbOut.equals("null") || dbOut.equals("")) {
					Ausgabe.println("Dein Telefonbuch ist leer.");
				}else {
					Ausgabe.println(dbOut);
				}
			}
			if(S.equals("4")) {
				String querry = Eingabe.readLine();
				System.out.println("Kontakt suchen \""+querry+"\" "+lauschen.getInetAddress()+":"+lauschen.getPort());
				String dbOut = dataBase.searchItem(querry);
				if(dbOut.equals("null") || dbOut.equals("")) {
					Ausgabe.println("Der eintrag wurde nicht gefunden.");
				}else {
					Ausgabe.println(dbOut);	
				}
			}
			if(S.equals("3")) {
				String id = Eingabe.readLine();
				System.out.println("Kontakt löschen \""+id+"\""+lauschen.getInetAddress()+":"+lauschen.getPort());
				try {
				String dbOut = dataBase.deleteItem(Integer.parseInt(id));
				Ausgabe.println(dbOut);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(S.equals("1")) {
				System.out.println("Kontakt erstellen "+lauschen.getInetAddress());
				String kontaktJson = Eingabe.readLine();
				JSONObject kontakt = new JSONObject(kontaktJson);
				String vorname = kontakt.getString("Vorname");
				String nachname = kontakt.getString("Nachname");
				String strasse = kontakt.getString("Strasse");
				String hausnummer = kontakt.getString("Hausnummer");
				String plz = kontakt.getString("Postleitzahl");
				String ort = kontakt.getString("Ort");
				String telefonnummer = kontakt.getString("Telefonnummer");
				String faxnummer = kontakt.getString("Faxnummer");
				String handynummer = kontakt.getString("Handynummer");
				String email = kontakt.getString("Email");
				Ausgabe.println(dataBase.createKontakt(vorname, nachname, strasse, hausnummer, plz, ort, telefonnummer, faxnummer, handynummer, email));
			}
			Ausgabe.close();
		}
	}
}
