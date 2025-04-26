package Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ContextSchema {
  public static void initContextDb() {
    String sql = """
        CREATE TABLE IF NOT EXISTS context (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        contexto STRING
        );
        """;

    try (Connection conn = Database.connect();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
          System.err.println("Erro ao criar tabela contexto: " + e.getMessage());
         }
  }

  public static void dropContext() {
    String sql = "DROP TABLE context";

   try (Connection conn = Database.connect();
        Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
        } catch (SQLException e) {
          System.err.println("Erro ao deletar tabela contexto: " + e.getMessage());
        }
  }
}
