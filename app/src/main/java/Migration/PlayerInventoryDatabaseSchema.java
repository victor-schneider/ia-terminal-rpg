package Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerInventoryDatabaseSchema {
  public static void initPlayerInventoryDb() {
    String sql = """
        CREATE TABLE IF NOT EXISTS playerInventory (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name STRING NOT NULL,
          type STRING NOT NULL,
          atk INTEGER,
          def INTEGER,
        );
        """;
    
    try (Connection conn = Database.connectPlayerInventory();
         Statement stmt = conn.createStatement()) {
          stmt.execute(sql);
         } catch (SQLException e) {
          System.err.println("Erro criando tabela: " + e.getMessage());
         }
  }

  public static void dropTable(){
    String sql = "DROP TABLE playerInventory";

    try (Connection conn = Database.connectPlayerInventory();
       Statement stmt = conn.createStatement()) {
        stmt.execute(sql);
       } catch (SQLException e) {
        System.err.println("Erro ao deletar tabela: " + e.getMessage());
       }
  }
  
}
