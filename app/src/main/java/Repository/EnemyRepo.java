package Repository;

import Migration.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Components.Enemy;

public class EnemyRepo {
  public static int createEnemy(Enemy enemy) {
    String sql= "INSERT INTO enemies(name, level, weapon, hp, atk, def, dex, exp) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, enemy.getName());
          pstmt.setInt(2, enemy.getLevel());
          pstmt.setString(3, enemy.getWeapon());
          pstmt.setFloat(4, enemy.getHp());
          pstmt.setFloat(5, enemy.getAtk());
          pstmt.setFloat(6, enemy.getDef());
          pstmt.setFloat(7, enemy.getDex());
          pstmt.setFloat(8, enemy.getExp());

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

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql);) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()) {
            if(rs.next()){
              System.out.printf("#%d %s %n Level: %d - %n Arma: %s %n Status: [HP:%d, ATK:%d, DEF:%d, DEX:%d]%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getString("weapon"),
              rs.getFloat("hp"),
              rs.getFloat("atk"),
              rs.getFloat("def"),
              rs.getFloat("dex"));
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
            System.out.printf("#%d %s %n Level: %d - %n Arma: %s %n Status: [HP:%d, ATK:%d, DEF:%d, DEX:%d]%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getString("weapon"),
              rs.getFloat("hp"),
              rs.getFloat("atk"),
              rs.getFloat("def"),
              rs.getFloat("dex"));
           }
         } catch (SQLException e) {
            System.err.println("Erro lendo inimigos: " + e.getMessage());
         }
  }

  public static void updateEnemy(Enemy enemy) {
    String sql = "UPDATE enemies SET name = ?, level = ?, weapon = ?, hp = ?, atk = ?, def = ?, dex = ?, exp = ? WHERE id = ?";
    
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, enemy.getName());
          pstmt.setInt(2, enemy.getLevel());
          pstmt.setString(3, enemy.getWeapon());
          pstmt.setFloat(4, enemy.getHp());
          pstmt.setFloat(5, enemy.getAtk());
          pstmt.setFloat(6, enemy.getDef());
          pstmt.setFloat(7, enemy.getDex());
          pstmt.setFloat(8, enemy.getExp());
          pstmt.setInt(9, enemy.getId());
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao atualizar inimigo: " + e.getMessage());
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
