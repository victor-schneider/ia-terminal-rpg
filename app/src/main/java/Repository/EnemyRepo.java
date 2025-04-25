package Repository;

import Migration.Database; // Import the Database class
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnemyRepo {
  public static void createEnemy(String name, int hp, int atk) {
    String sql= "INSERT INTO enemies(name, hp, atk) VALUES(?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, name);
          pstmt.setInt(2, hp);
          pstmt.setInt(3, atk);
          pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error inserting enemy: " + e.getMessage());
    }
  }

  public static void getEnemy(int id) {
    String sql = "SELECT * FROM enemies WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql);) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()) {
            if(rs.next()){
              System.out.printf("#%d %s (HP=%d, ATK=%d)%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("hp"),
              rs.getInt("atk"));
            } else {
              System.out.println("Nenhum inimigo encontrado com ID " + id);
            }
          }
         } catch (SQLException e) {
           System.out.println("Erro ao listar inimigo: " + e.getMessage());
         }
  }

  public static void listEnemy() {
    String sql = "SELECT * FROM enemies";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)) {
           while(rs.next()) {
            System.out.printf("#%d %s (HP=%d, ATK=%d)%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("hp"),
              rs.getInt("atk"));
           }
         } catch (SQLException e) {
            System.err.println("Erro lendo inimigos: " + e.getMessage());
         }
  }

  public static void updateEnemy(int id, String name, int hp, int atk) {
    String sql = "UPDATE enemies SET name = ?, hp = ?, atk = ? WHERE id = ?";
    
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, name);
          pstmt.setInt(2, hp);
          pstmt.setInt(3, atk);
          pstmt.setInt(4, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.out.println("Erro ao atualizar inimigo: " + e.getMessage());
         }
  }

  public static void deleteEnemy(int id) {
    String sql = "DELETE FROM enemies WHERE id = ?";

    try (Connection conn = Database.connect();
         var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println("Erro deletando inimigo: " + e.getMessage());
         }
  }
}
