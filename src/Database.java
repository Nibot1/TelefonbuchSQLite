import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * @author Tobin Rosenau
 *
 */
public class Database {
	public Database() {
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

	public String createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:telefonbuch.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE `Telefonbuch` (" + "	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					+ "	`Vorname`	TEXT," + "	`Nachname`	TEXT," + "	`Stra�e`	TEXT," + "	`Hausnummer`	TEXT,"
					+ "	`Postleitzahl`	TEXT," + "	`Ort`	INTEGER," + "	`Telefonnummer`	TEXT,"
					+ "	`Faxnummer`	TEXT," + "	`Handynummer`	TEXT" + "   `Emailadresse` TEXT" + ");";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Operation failed";
			//System.exit(0);
		}
		//System.out.println("Operation done successfully");
		return "Operation done successfully";
	}

	public String readTabe() {
		Connection c = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Telefonbuch;");

			while (rs.next()) {
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
				String email = rs.getString("Emailaddresse");

				sb.append("ID: " + id);
				sb.append("\n");
				sb.append("Vorname: " + vorname);
				sb.append("\n");
				sb.append("Nachname: " + nachname);
				sb.append("\n");
				sb.append("Starße, Hausnummer: " + straße + ", " + hausnummer);
				sb.append("\n");
				sb.append("Plz, Ort: " + postleitzahl + ", " + ort);
				sb.append("\n");
				sb.append("Telefonnummer: " + telefonnummer);
				sb.append("\n");
				sb.append("Faxnummer: " + faxnummer);
				sb.append("\n");
				sb.append("Handynummer: " + handynummer);
				sb.append("\n");
				sb.append("Emailadresse: " + email);
				sb.append("\n");

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Operation failed";
			//System.exit(0);
		}
		//System.out.println("Operation done successfully");
		return sb.toString();
	}

	public String deleteItem(int id) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "DELETE from COMPANY where ID=" + id + ";";
			stmt.executeUpdate(sql);
			c.commit();

			ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Operation failed";
			//System.exit(0);
		}
		//System.out.println("Operation done successfully");
		return "Operation done successfully";

	}
	
	public String searchItem(String querry) {
		Connection c = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Telefonbuch WHERE Vorname LIKE '%"+querry+"%' OR WHERE Nachname LIKE '%"+querry+"%' OR WHERE Stra�e LIKE '%"+querry+"%' OR WHERE Hausnummer LIKE '%"+querry+"%' OR WHERE Postleitzahl LIKE '%"+querry+"%' OR WHERE Ort LIKE '%"+querry+"%' OR WHERE Telefonnummer LIKE '%"+querry+"%' OR WHERE Faxnummer LIKE '%"+querry+"%' OR WHERE Handynummer LIKE '%"+querry+"%' OR WHERE Emailadresse LIKE '%"+querry+"%';");
			while (rs.next()) {
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
				String email = rs.getString("Emailaddresse");

				sb.append("ID: " + id);
				sb.append("\n");
				sb.append("Vorname: " + vorname);
				sb.append("\n");
				sb.append("Nachname: " + nachname);
				sb.append("\n");
				sb.append("Starße, Hausnummer: " + straße + ", " + hausnummer);
				sb.append("\n");
				sb.append("Plz, Ort: " + postleitzahl + ", " + ort);
				sb.append("\n");
				sb.append("Telefonnummer: " + telefonnummer);
				sb.append("\n");
				sb.append("Faxnummer: " + faxnummer);
				sb.append("\n");
				sb.append("Handynummer: " + handynummer);
				sb.append("\n");
				sb.append("Emailadresse: " + email);
				sb.append("\n");

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return "Operation failed";
			//System.exit(0);
		}
		//System.out.println("Operation done successfully");
		return sb.toString();
	}
}
