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
        hp FLOAT NOT NULL,
        atk FLOAT NOT NULL,
        def FLOAT NOT NULL,
        lck FLOAT NOT NULL,
        dex FLOAT NOT NULL,
        level FLOAT NOT NULL,
        exp FLOAT NOT NULL,
        nextLevel DOUBLE NOT NULL
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
