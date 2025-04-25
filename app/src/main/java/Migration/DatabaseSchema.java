package Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseSchema {
  public static void init() {
    String sql = """
        CREATE TABLE IF NOT EXISTS enemies (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          hp INTEGER NOT NULL,
          atk INTEGER NOT NULL
        );
        """;
    
    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
           System.err.println("Erro criando tabela: " + e.getMessage());
         }
  }
}
