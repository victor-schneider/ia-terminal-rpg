package Repository;

import Migration.Database;
import Components.PlayerComponents.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRepo {
  public static void createPlayer(Player player) {
    String sql = "INSERT INTO player(name, hp, level, exp, nextLevel) VALUES(?, ?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, player.getName());
          pstmt.setInt(2, player.getHp());
          pstmt.setInt(3, player.getLevel());
          pstmt.setInt(4, player.getExp());
          pstmt.setInt(5, player.getNextLevel());
          pstmt.executeUpdate();

          try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
            if(generatedKey.next()) {
              player.setId(generatedKey.getInt(1));
            } else {
              throw new SQLException("Creating player failed, no ID obtained");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao inserir jogador: " + e.getMessage());
         }
  }

  public static Player getPlayer(int id) {
    String sql = "SELECT * FROM player WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()) {
            if(rs.next()) {
              Player player = new Player(
                rs.getString("name"),
                rs.getInt("hp"),
                rs.getInt("level"),
                rs.getInt("exp"),
                rs.getInt("nextLevel"),
                rs.getInt("id"));
                System.out.println("Jogador retornado com sucesso!");

                return player;
            } else {
              throw new SQLException("No player found with id: " + id);
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao procurar jogador: " + e.getMessage());
         }
         return null;
  }

  public static void updatePlayer(Player player) {
    String sql = "UPDATE player SET name = ?, hp = ?, level = ?, exp = ?, nextLevel = ? WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, player.getName());
          pstmt.setInt(2, player.getHp());
          pstmt.setInt(3, player.getLevel());
          pstmt.setInt(4, player.getExp());
          pstmt.setInt(5, player.getNextLevel());
          pstmt.setInt(6, player.getId());
          pstmt.executeUpdate();

         } catch (SQLException e) {
          System.err.println("Erro ao atualizar jogador: " + e.getMessage());
         }
  }

  public static void deletePlayer(int id) {
    String sql = "DELETE FROM player WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao deletar jogador: " + e.getMessage());
         }
  }
}
