import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.logging.Logger;

public class Database {
	String sqlPath;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public Database(String dbPath) throws FileNotFoundException, UnsupportedEncodingException {
		sqlPath = "jdbc:sqlite:"+dbPath;
		File db = new File(dbPath);
		if (!db.exists()) {
			try {
				LOGGER.info("Creating the Database");
				db.createNewFile();
				createTable();
			} catch (IOException e) {
				LOGGER.info("Error while Creating the Databasefile. Stacktrace: "+e.getStackTrace().toString());
			}
		}
	}

	public void createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			LOGGER.info("Connecting to the Database");
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqlPath);
			LOGGER.info("sucessfuly opened the Database");
			LOGGER.info("Creating the Database Table");
			stmt = c.createStatement();
			String sql = "CREATE TABLE Telefonbuch (" + "	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					+ "	`Vorname`	TEXT," + "	`Nachname`	TEXT," + "	`Strasse`	TEXT," + "	`Hausnummer`	TEXT,"
					+ "	`Postleitzahl`	TEXT," + "	`Ort`	TEXT," + "	`Telefonnummer`	TEXT," + "	`Faxnummer`	TEXT,"
					+ "	`Handynummer`	TEXT," + "   `Emailadresse` TEXT" + ");";
			stmt.executeUpdate(sql);
			LOGGER.info("Disconnect from the Database");
			stmt.close();
			c.close();
		} catch (Exception e) {
			LOGGER.warning("Failed to Create the Table. Stacktrace: "+e.getStackTrace().toString());
		}
		LOGGER.info("Sucessfuly created the Database and the Table");
	}

	public String readTable() {
		Connection c = null;
		PreparedStatement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			//Connect to the Database
			LOGGER.info("Connecting to the Database");
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqlPath);
			c.setAutoCommit(false);
			LOGGER.info("Sucessfuly connected to the Database");
			LOGGER.info("Creating the SQLCommand");
			stmt = c.prepareStatement("SELECT * FROM Telefonbuch;");
			//Read the complete Database
			LOGGER.info("Excecuting the SQLCommand");
			ResultSet rs = stmt.executeQuery();
			//Create an JSONmultidimensionalArray Formatted StringBuilder
			sb.append("[");
			int i = 0;
			while (rs.next()) {
				if(i == 1) {
					sb.append(",");
					}
					i++;
				int id = rs.getInt("id");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				String strasse = rs.getString("Strasse");
				String hausnummer = rs.getString("Hausnummer");
				String postleitzahl = rs.getString("Postleitzahl");
				String ort = rs.getString("Ort");
				String telefonnummer = rs.getString("Telefonnummer");
				String faxnummer = rs.getString("Faxnummer");
				String handynummer = rs.getString("Handynummer");
				String email = rs.getString("Emailadresse");

				sb.append("{\"id\": \"" + id + "\",");

				sb.append("\"Vorname\": \"" + vorname + "\",");

				sb.append("\"Nachname\": \"" + nachname + "\",");

				sb.append("\"Strasse\": \"" + strasse + "\",");

				sb.append("\"Hausnummer\":\"" + hausnummer + "\",");

				sb.append("\"Plz\": \"" + postleitzahl + "\",");
				
				sb.append("\"Ort\": \"" + ort + "\",");

				sb.append("\"Telefonnummer\": \"" + telefonnummer + "\",");

				sb.append("\"Faxnummer\": \"" + faxnummer + "\",");

				sb.append("\"Handynummer\": \"" + handynummer + "\",");

				sb.append("\"Emailadresse\": \"" + email + "\"}");
			}
			sb.append("]");
			//Disconnect form the Database
			LOGGER.info("Disconnect from the Database");
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			LOGGER.warning("Error while reading the Table. Stacktrace:"+e.toString());
			return "Aktion Fehlgeschlagen";
		}
		//Return the JsonString
		return sb.toString();
	}

	public String deleteItem(int id) {
		Connection c = null;
		PreparedStatement stmt = null;

		try {
			//Connect to the Database
			LOGGER.info("Connecting to the Database");
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqlPath);
			c.setAutoCommit(false);
			LOGGER.info("Sucessfuly connected to the Database");
			//Run SQLInjection safe Delete Command
			LOGGER.info("Creating the SQLCommand");
			stmt = c.prepareStatement("DELETE from Telefonbuch where ID=? ;");
			LOGGER.info("Insert the vars into the SQLCommand");
			stmt.setInt(1, id);
			LOGGER.info("Excecuting the SQLCommand");
			stmt.executeUpdate();
			c.commit();
			//Disconnect from the Database
			LOGGER.info("Disconnect from the Database");
			stmt.close();
			c.close();
		} catch (Exception e) {
			//Return fail statement
			LOGGER.warning("Error while Deleting the Contact with the id: "+id);
			return "Aktion Fehlgeschlagen";
		}
		//Return ok statement
		return "Aktion erfolgreich ausgef¸hrt";

	}

	public String searchItem(String querry) {
		Connection c = null;
		PreparedStatement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			//Connect to the Database
			LOGGER.info("Connecting to the Database");
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqlPath);
			c.setAutoCommit(false);
			LOGGER.info("Sucessfuly connected to the Database");
			//Run SQLInsertion safe Search command
			LOGGER.info("Creating the SQLCommand");
			stmt = c.prepareStatement("SELECT * FROM Telefonbuch  WHERE Vorname LIKE ? OR  Nachname LIKE ? OR  Straﬂe LIKE ? OR  Hausnummer LIKE ? OR  Postleitzahl LIKE ? OR  Ort LIKE ? OR  Telefonnummer LIKE ? OR  Faxnummer LIKE ? OR  Handynummer LIKE ? OR  Emailadresse LIKE ?;");
			LOGGER.info("Insert the vars into the SQLCommand");
			stmt.setString(1, "%"+querry+"%");
			stmt.setString(2, "%"+querry+"%");
			stmt.setString(3, "%"+querry+"%");
			stmt.setString(4, "%"+querry+"%");
			stmt.setString(5, "%"+querry+"%");
			stmt.setString(6, "%"+querry+"%");
			stmt.setString(7, "%"+querry+"%");
			stmt.setString(8, "%"+querry+"%");
			stmt.setString(9, "%"+querry+"%");
			stmt.setString(10, "%"+querry+"%");
			LOGGER.info("Excecuting the SQLCommand");
			ResultSet rs = stmt.executeQuery(); 
			sb.append("[");
			int i =0;
			while (rs.next()) {
				if(i == 1) {
				sb.append(",");
				}
				i++;
				int id = rs.getInt("id");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				String strasse = rs.getString("Straﬂe");
				String hausnummer = rs.getString("Hausnummer");
				String postleitzahl = rs.getString("Postleitzahl");
				String ort = rs.getString("Ort");
				String telefonnummer = rs.getString("Telefonnummer");
				String faxnummer = rs.getString("Faxnummer");
				String handynummer = rs.getString("Handynummer");
				String email = rs.getString("Emailadresse");
				
				sb.append("{\"id\": \"" + id + "\",");

				sb.append("\"Vorname\": \"" + vorname + "\",");

				sb.append("\"Nachname\": \"" + nachname + "\",");

				sb.append("\"Strasse\": \"" + strasse + "\",");

				sb.append("\"Hausnummer\":\"" + hausnummer + "\",");

				sb.append("\"Plz\": \"" + postleitzahl + "\",");
				
				sb.append("\"Ort\": \"" + ort + "\",");

				sb.append("\"Telefonnummer\": \"" + telefonnummer + "\",");

				sb.append("\"Faxnummer\": \"" + faxnummer + "\",");

				sb.append("\"Handynummer\": \"" + handynummer + "\",");

				sb.append("\"Emailadresse\": \"" + email + "\"}");
			}
			sb.append("]");
			//Disconnect from Database
			LOGGER.info("Disconnect from the Database");
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//Return Fail statement
			LOGGER.warning("Error while searching in the Database. Stacktrace: "+e.getStackTrace().toString());
			return "[]";
		}
		//Return Ok statement
		return sb.toString();
	}

	public String createKontakt(String vorname, String nachname, String strasse, String hausnummer, String plz,
			String ort, String telefonnummer, String faxnummer, String handynummer, String email) {
		Connection c = null;
		PreparedStatement stmt = null;

		try {
			//Connect to the Database
			LOGGER.info("Connecting to the Database");
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqlPath);
			c.setAutoCommit(false);
			LOGGER.info("Sucessfuly connected to the Database");
			//Create the Kontakt
			LOGGER.info("Creating the SQLCommand");
			stmt = c.prepareStatement("INSERT INTO Telefonbuch (Vorname, Nachname, Straﬂe, Hausnummer, Postleitzahl, Ort, Telefonnummer, Faxnummer, Handynummer, Emailadresse) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			LOGGER.info("Insert the vars into the SQLCommand");
			stmt.setString(1, vorname);
			stmt.setString(2, nachname);
			stmt.setString(3, strasse);
			stmt.setString(4, hausnummer);
			stmt.setString(5, plz);
			stmt.setString(6, ort);
			stmt.setString(7, telefonnummer);
			stmt.setString(8, faxnummer);
			stmt.setString(9, handynummer);
			stmt.setString(10, email);
			LOGGER.info("Excecuting the SQLCommand");
			stmt.executeUpdate();
			//Disconnect from the Database
			LOGGER.info("Disconnect from the Database");
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			//Return fail Statement
			LOGGER.warning("Error whlie creating the Contact. Stacktrace: "+e.getStackTrace().toString());
			return "Fehler beim erstellen des Kontaktes";
		}
		//Return ok Statement
		LOGGER.info("Sucessfuly created the Contact");
		return "Kontakt erfolgreich erstellt";
	}

}
