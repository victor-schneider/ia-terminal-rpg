package Repository;

import Migration.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInventoryRepo {
  public static int createItem(String name, String type, int atk, int def) {
    String sql = "INSERT INTO playerInventory(name, type, atk, def) VALUES(?, ?, ?, ?)";

    try (Connection conn = Database.connectPlayerInventory();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, name);
          pstmt.setString(2, type);
          pstmt.setInt(3, atk);
          pstmt.setInt(4, def);
          pstmt.executeUpdate();

          try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
            if (generatedKey.next()){
              return generatedKey.getInt(1);
            } else {
              throw new SQLException("Creating Item failed, no ID obtained");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao inserir item: " + e.getMessage());
         }
    return 1;
  }

  public static void getItem(int id) {
    String sql = "SELECT * FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connectPlayerInventory();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()){
            if(rs.next()) {
              System.out.printf("#%d %n Nome: %s %n Tipo: %s %n ATK: %d DEF:%d%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("type"),
              rs.getInt("atk"),
              rs.getInt("def"));
            } else {
              System.out.println("Nenhum item encontrado com ID " + id);
            }
          } 
        } catch (SQLException e) {
            System.err.println("Erro ao imprimir item: " + e.getMessage());
         }
  }

  public static void listItems() {
    String sql = "SELECT * FROM playerInventory";

    try (Connection conn = Database.connectPlayerInventory();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)){
          while (rs.next()) {
            System.out.printf("#%d %n Nome: %s %n Tipo: %s %n ATK: %d DEF:%d%n",
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getInt("atk"),
            rs.getInt("def"));
          }
         } catch (SQLException e) {
          System.err.println("Erro lendo items: " + e.getMessage());
         }
  }

  public static void updateItem(int id, String name, String type, int atk, int def){
    String sql = "UPDATE playerInventory SET name = ?, type = ?, atk = ?, def = ? WHERE id = ?";

    try (Connection conn = Database.connectPlayerInventory();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, name);
          pstmt.setString(2, type);
          pstmt.setInt(3, atk);
          pstmt.setInt(4, def);
          pstmt.setInt(5, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao atualizar item: " + e.getMessage());
         }
  }

  public static void deleteItem(int id){
    String sql = "DELETE FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connectPlayerInventory();
         var pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao deletar item: " + e.getMessage());
         }
  }
}
