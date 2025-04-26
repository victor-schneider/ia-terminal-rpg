package Repository;

import Migration.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnemyRepo {
  public static int createEnemy(String name, int level, String weapon, int hp, int atk, int def) {
    String sql= "INSERT INTO enemies(name, level, weapon, hp, atk, def) VALUES(?, ?, ?, ?, ?, ?)";

    try (Connection conn = Database.connectEnemies();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, name);
          pstmt.setInt(2, level);
          pstmt.setString(3, weapon);
          pstmt.setInt(4, hp);
          pstmt.setInt(5, atk);
          pstmt.setInt(6, def);
          
          
          pstmt.executeUpdate();

          try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              return generatedKeys.getInt(1);
            } else {
              throw new SQLException("Creating Enemy failed, no ID obtained");
            }
          }
    } catch (SQLException e) {
      System.err.println("Error creating enemy: " + e.getMessage());
    }
    
    return -1;
    
  }

  public static void getEnemy(int id) {
    String sql = "SELECT * FROM enemies WHERE id = ?";

    try (Connection conn = Database.connectEnemies();
         PreparedStatement pstmt = conn.prepareStatement(sql);) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()) {
            if(rs.next()){
              System.out.printf("#%d %s %n Level: %d - %n Arma: %s %n Status: [HP:%d, ATK:%d, DEF:%d]%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getString("weapon"),
              rs.getInt("hp"),
              rs.getInt("atk"),
              rs.getInt("def"));
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

    try (Connection conn = Database.connectEnemies();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)) {
           while(rs.next()) {
            System.out.printf("#%d %s %n Level: %d - %n Arma: %s %n Status: [HP:%d, ATK:%d, DEF:%d]%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getString("weapon"),
              rs.getInt("hp"),
              rs.getInt("atk"),
              rs.getInt("def"));
           }
         } catch (SQLException e) {
            System.err.println("Erro lendo inimigos: " + e.getMessage());
         }
  }

  public static void updateEnemy(int id, String name, int level, String weapon, int hp, int atk, int def) {
    String sql = "UPDATE enemies SET name = ?, level = ?, weapon = ?, hp = ?, atk = ?, def = ? WHERE id = ?";
    
    try (Connection conn = Database.connectEnemies();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, name);
          pstmt.setInt(2, level);
          pstmt.setString(3, weapon);
          pstmt.setInt(4, hp);
          pstmt.setInt(5, atk);
          pstmt.setInt(6, def);
          pstmt.setInt(7, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.out.println("Erro ao atualizar inimigo: " + e.getMessage());
         }
  }

  public static void deleteEnemy(int id) {
    String sql = "DELETE FROM enemies WHERE id = ?";

    try (Connection conn = Database.connectEnemies();
         var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println("Erro deletando inimigo: " + e.getMessage());
         }
  }
}
