package Migration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private static final String URL = "jdbc:sqlite:data/Rpg.db";

  private static Connection conn = null;

  public static Connection connect() throws SQLException {
    if(conn == null || conn.isClosed()) {
      return DriverManager.getConnection(URL);
    }
    return conn;

  }
}
