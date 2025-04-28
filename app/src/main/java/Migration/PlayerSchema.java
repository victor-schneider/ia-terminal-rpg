package Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerSchema {
  public static void initPlayer() {
    String sql = """
        CREATE TABLE IF NOT EXISTS player (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name STRING NOT NULL,
        hp INTEGER NOT NULL,
        level INTEGER NOT NULL,
        exp INTEGER NOT NULL,
        nextLevel INTEGER NOT NULL
        );
        """;
        
    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
          System.err.println("Erro ao incializar tabela player: " + e.getMessage());
         }
  }

  public static void dropTable() {
    String sql = "DROP TABLE player";

    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
          System.err.println("Erro ao deletar tabela player: " + e.getMessage());
         }
  }
}
