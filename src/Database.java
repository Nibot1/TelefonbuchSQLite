import java.sql.*;

import org.sqlite.*;

/**
 * @author Tobin Rosenau
 *
 */
public class Database {
public Database() {
}
public void connect() {
	Connection c = null;
    try {
       Class.forName("org.sqlite.JDBC");
       c = DriverManager.getConnection("jdbc:sqlite:test.db");
    } catch ( Exception e ) {
       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
       System.exit(0);
    }
    System.out.println("Opened database successfully");
}
}
