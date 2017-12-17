
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.json.*;

public class Server {

	static ServerSocket anschluss;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void main(String[] args) throws IOException {
	logger.setup();
		int port = 6000;
		String dbPath = "telefonbuch.db";
		// Read config file
		try {
			File file = null;
			file = new File("config.xml");
			if(file.exists()) {
			LOGGER.info("Reading Configfile at: " + file.getAbsolutePath().toString());
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			try {
				LOGGER.info("Reading the Server Port from the Configfile");
				port = Integer.parseInt(document.getElementsByTagName("port").item(0).getTextContent());
			} catch (Exception e) {
				LOGGER.warning("Error while Reading the Port from the Configfile. Please make shure that the Configfile contains a Port-Tag with a valid Integer value");
			}
			try {
				LOGGER.info("Reading the Database Path form the Configfile");
			dbPath = document.getElementsByTagName("database-path").item(0).getTextContent();
			}catch(Exception e) {
			LOGGER.warning("Error while Reading the Database Path from the Configfile. Please make shure that the Configfile contains a \"database-path\"-Tag with a valid String value");
			}
			}else {
				LOGGER.info("No Configfile found");
			}
		} catch (Exception e) {
			LOGGER.warning("Error While try to Read the Configfile. Stacktrace: "+e.getStackTrace().toString());
		}
		// Start ServerSocket
		try {
			LOGGER.info("Starting the Serversocket at port: "+String.valueOf(port));
			anschluss = new ServerSocket(port);
		} catch (BindException e) {
			LOGGER.warning("Error while Starting the ServerSocket. Plase make shure that no other programm use Port: "+String.valueOf(port));
			System.exit(1);
		}
		// Closes the Server when the Program is closed;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					LOGGER.info("Shutting down the Server");
					anschluss.close();
				} catch (IOException e) {
					LOGGER.warning("Error while Shuttingdown the Server. Stacktrace: "+e.getStackTrace().toString());
				}
			}
		});
		LOGGER.info("Initializing the Database");
		Database dataBase = new Database(dbPath);
		// Starts an endless loop for listening for Client Input
		while (true) {
			// Start listening
			Socket lauschen = anschluss.accept();
			LOGGER.info("Server starts Listening");
			// Print Welcome Message
			System.out.println("Ich lausche auf anfragen von: " + lauschen.getInetAddress());
			LOGGER.info("The Server is now Listening for Request from: "+lauschen.getInetAddress());
			// Read the Client Input
			LOGGER.info("The Server is reading the Client request");
			InputStreamReader PortLeser = new InputStreamReader(lauschen.getInputStream());
			BufferedReader Eingabe = new BufferedReader(PortLeser);
			String S = Eingabe.readLine();
			LOGGER.info("The Client Request was: "+S);
			// Set up an PrintWriter
			PrintWriter Ausgabe = new PrintWriter(lauschen.getOutputStream(), true);
			// Handle Clientrequest
			LOGGER.info("The Server is handeling the Request");
			String dbOut = null;
			switch(Integer.parseInt(S)) {
			case 1:
				System.out.println("Kontakt erstellen " + lauschen.getInetAddress());
				LOGGER.info("Creating Phonecontact");
				String kontaktJson = Eingabe.readLine();
				LOGGER.info("Parsing the JSONString");
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
				LOGGER.info("Returning the Message");
				Ausgabe.println(dataBase.createKontakt(vorname, nachname, strasse, hausnummer, plz, ort, telefonnummer, faxnummer, handynummer, email));
				break;
			case 2:
				System.out.println("Kontakte anzeigen " + lauschen.getInetAddress() + ":" + lauschen.getPort());
				LOGGER.info("Reading Table");
				dbOut = dataBase.readTable();
				if (dbOut.equals("null") || dbOut.equals("")) {
					Ausgabe.println("Dein Telefonbuch ist leer.");
					LOGGER.info("Das Telfonbuch ist Leer.");
				} else {
					LOGGER.info("The Server is printing the Table");
					Ausgabe.println(dbOut);
				}
				break;
			case 3:
				LOGGER.info("Reading the Contact ID");
				String id = Eingabe.readLine();
				System.out.println(
						"Kontakt löschen \"" + id + "\"" + lauschen.getInetAddress() + ":" + lauschen.getPort());
				try {
					LOGGER.info("Try to delete Contact with id: "+id);
					dbOut = dataBase.deleteItem(Integer.parseInt(id));
					Ausgabe.println(dbOut);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.warning("Error while Deleting Contact with id: "+id+" . Stacktrace: "+e.getStackTrace().toString());
				}
				break;
			case 4:
				LOGGER.info("Reading the Searchstring");
				String querry = Eingabe.readLine();
				System.out.println(
						"Kontakt suchen \"" + querry + "\" " + lauschen.getInetAddress() + ":" + lauschen.getPort());
				LOGGER.info("Searching the Database for a Matching Contact");
				dbOut = dataBase.searchItem(querry);
				if (dbOut.equals("null") || dbOut.equals("")) {
					LOGGER.info("No entry found");
					Ausgabe.println("Der eintrag wurde nicht gefunden.");
				} else {
					LOGGER.info("Printing the Entries");
					Ausgabe.println(dbOut);
				}
				break;
			}
			Ausgabe.close();
		}
	}
}
