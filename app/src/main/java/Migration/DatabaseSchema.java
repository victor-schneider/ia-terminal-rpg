package Migration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseSchema {
  public static void init() {
    String sql = """
        CREATE TABLE IF NOT EXISTS enemies (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name STRING NOT NULL,
          level INTEGER NOT NULL,
          weapon STRING NOT NULL,
          hp INTEGER NOT NULL,
          atk INTEGER NOT NULL,
          def INTEGER NOT NULL
        );
        """;
    
    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
           System.err.println("Erro criando tabela: " + e.getMessage());
         }
  }

  public static void dropTable(){
    String sql = "DROP TABLE enemies";

    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
          System.err.println("Erro ao deletar tabela " + e.getMessage());
         }
  }
}
