package Migration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private static final String URL_Enemies = "jdbc:sqlite:data/Enemies.db";
  private static final String URL_PlayerInventory = "jdbc:sqlite:data/PlayerInventory.db";

  public static Connection connectEnemies() throws SQLException {
    return DriverManager.getConnection(URL_Enemies);
  }

  public static Connection connectPlayerInventory() throws SQLException {
    return DriverManager.getConnection(URL_PlayerInventory);
  }
}
