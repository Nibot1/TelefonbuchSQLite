import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class Database {

	public Database() throws FileNotFoundException, UnsupportedEncodingException {
		File db = new File("telefonbuch.db");
		if (!db.exists()) {
			try {
				db.createNewFile();
				createTable();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefonbuch.db");
			System.out.println("Datenbank erfolgreich geöffnet");

			stmt = c.createStatement();
			String sql = "CREATE TABLE Telefonbuch (" + "	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					+ "	`Vorname`	TEXT," + "	`Nachname`	TEXT," + "	`Straße`	TEXT," + "	`Hausnummer`	TEXT,"
					+ "	`Postleitzahl`	TEXT," + "	`Ort`	TEXT," + "	`Telefonnummer`	TEXT," + "	`Faxnummer`	TEXT,"
					+ "	`Handynummer`	TEXT," + "   `Emailadresse` TEXT" + ");";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.out.println("Datenbank erstellen Fehlgeschlagen");
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println("Datenbank erfolgreich erstellt");
	}

	public String readTable() {
		Connection c = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			//Connect to the Database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefonbuch.db");
			c.setAutoCommit(false);
			System.out.println("Datenbank erfolgreich geöffnet");
			stmt = c.createStatement();
			//Read the complete Database
			ResultSet rs = stmt.executeQuery("SELECT * FROM Telefonbuch;");
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
				String straße = rs.getString("Straße");
				String hausnummer = rs.getString("Hausnummer");
				String postleitzahl = rs.getString("Postleitzahl");
				String ort = rs.getString("Ort");
				String telefonnummer = rs.getString("Telefonnummer");
				String faxnummer = rs.getString("Faxnummer");
				String handynummer = rs.getString("handynummer");
				String email = rs.getString("Emailadresse");

				sb.append("{\"id\": \"" + id + "\",");

				sb.append("\"Vorname\": \"" + vorname + "\",");

				sb.append("\"Nachname\": \"" + nachname + "\",");

				sb.append("\"Straße\": \"" + straße + "\",");

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
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Aktion Fehlgeschlagen";
		}
		//Return the JsonString
		return sb.toString();
	}

	public String deleteItem(int id) {
		Connection c = null;
		Statement stmt = null;

		try {
			//Connect to the Database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefobuch.db");
			c.setAutoCommit(false);
			System.out.println("Datenbank erfolgreich geöffnet");
			//Run Delete Command
			stmt = c.createStatement();
			String sql = "DELETE from COMPANY where ID=" + id + ";";
			stmt.executeUpdate(sql);
			c.commit();
			//Disconnect from the Database
			stmt.close();
			c.close();
		} catch (Exception e) {
			//Return fail statement
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Aktion Fehlgeschlagen";
		}
		//Return ok statement
		return "Aktion erfolgreich ausgeführt";

	}

	public String searchItem(String querry) {
		Connection c = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			//Connect to the Database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefonbuch.db");
			c.setAutoCommit(false);
			System.out.println("Datenbank erfolgreich geöffnet");
			//Run Search command
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM Telefonbuch  WHERE Vorname LIKE '%" + querry + "%' OR  Nachname LIKE '%" + querry
							+ "%' OR  Straße LIKE '%" + querry + "%' OR  Hausnummer LIKE '%" + querry
							+ "%' OR  Postleitzahl LIKE '%" + querry + "%' OR  Ort LIKE '%" + querry
							+ "%' OR  Telefonnummer LIKE '%" + querry + "%' OR  Faxnummer LIKE '%" + querry
							+ "%' OR  Handynummer LIKE '%" + querry + "%' OR  Emailadresse LIKE '%" + querry + "%';");
			//Create an JsonmultidimensionalArray String
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
				String straße = rs.getString("Straße");
				String hausnummer = rs.getString("Hausnummer");
				String postleitzahl = rs.getString("Postleitzahl");
				String ort = rs.getString("Ort");
				String telefonnummer = rs.getString("Telefonnummer");
				String faxnummer = rs.getString("Faxnummer");
				String handynummer = rs.getString("handynummer");
				String email = rs.getString("Emailadresse");

				sb.append("{\"id\": \"" + id + "\",");

				sb.append("\"Vorname\": \"" + vorname + "\",");

				sb.append("\"Nachname\": \"" + nachname + "\",");

				sb.append("\"Straße\": \"" + straße + "\",");

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
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//Return Fail statement
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Aktion Fehlgeschlagen";
		}
		//Return Ok statement
		return sb.toString();
	}

	public String createKontakt(String vorname, String nachname, String strasse, String hausnummer, String plz,
			String ort, String telefonnummer, String faxnummer, String handynummer, String email) {
		Connection c = null;
		Statement stmt = null;

		try {
			//Connect to the Database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefonbuch.db");
			c.setAutoCommit(false);
			System.out.println("Datenbank erfolgreich geöffnet");
			//Create the Kontakt
			stmt = c.createStatement();
			String sql = "INSERT INTO Telefonbuch (Vorname, Nachname, Straße, Hausnummer, Postleitzahl, Ort, Telefonnummer, Faxnummer, Emailadresse) "
					+ "VALUES ('" + vorname + "', '" + nachname + "', '" + strasse + "', '" + hausnummer + "', '" + plz
					+ "', '" + ort + "', '" + telefonnummer + "', '" + faxnummer + "', '" + email + "');";
			stmt.executeUpdate(sql);
			//Disconnect from the Database
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			//Return fail Statement
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Fehler beim erstellen des Kontaktes";
		}
		//Return ok Statement
		System.out.println("Kontakt erfolgreich erstellt");
		return "Kontakt erfolgreich erstellt";
	}

}
