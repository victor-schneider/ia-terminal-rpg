package Repository;

import Migration.Database;
import Model.PlayerComponents.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRepo {
  public static void createPlayer(Player player) {
    String sql = "INSERT INTO player(name, hp, atk, def, dex, lck, level, exp, nextLevel) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, player.getName());
          pstmt.setFloat(2, player.getHp());
          pstmt.setFloat(3, player.getAtk());
          pstmt.setFloat(4, player.getDef());
          pstmt.setFloat(5, player.getDex());
          pstmt.setFloat(6, player.getLck());
          pstmt.setFloat(7, player.getLevel());
          pstmt.setFloat(8, player.getExp());
          pstmt.setDouble(9, player.getNextLevel());
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

  public static Player getPlayer() {
    Player player = new Player(null, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    Weapon weapon = PlayerInvRepo.getEquippedWeapon();
    if( weapon != null) player.equipWeapon(weapon);

    Armor helmet = PlayerInvRepo.getEquippedHelmet();
    if ( helmet != null ) player.equipArmor(helmet);

    Armor chest = PlayerInvRepo.getEquippedChest();
    if ( chest != null ) player.equipArmor(chest);

    Armor legs = PlayerInvRepo.getEquippedLegs();
    if ( legs != null ) player.equipArmor(legs);
                
    Armor boots = PlayerInvRepo.getEquippedBoots();
    if ( boots != null ) player.equipArmor(boots);

    String sql = "SELECT * FROM player WHERE id = 1";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

          try (var rs = pstmt.executeQuery()) {
            if(rs.next()) {
              player.setName(rs.getString("name"));
              player.setHp(rs.getInt("hp"));
              player.setAtk(rs.getInt("atk"));
              player.setDef(rs.getInt("def"));
              player.setDex(rs.getInt("dex"));
              player.setLck(rs.getInt("lck"));
              player.setLevel(rs.getInt("level"));
              player.setExp(rs.getInt("exp"));
              player.setNextLevel(rs.getInt("nextLevel"));
              player.setId(rs.getInt("id"));
                
              return player;
            } else {
              throw new SQLException("No player found");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao procurar jogador: " + e.getMessage());
         }
         return null;
  }

  public static void updatePlayer(Player player) {
    String sql = "UPDATE player SET name = ?, hp = ?, atk = ?, def = ?, dex = ?, lck = ?, level = ?, exp = ?, nextLevel = ? WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, player.getName());
          pstmt.setFloat(2, player.getHp());
          pstmt.setFloat(3, player.getAtk());
          pstmt.setFloat(4, player.getDef());
          pstmt.setFloat(5, player.getDex());
          pstmt.setFloat(6, player.getLck());
          pstmt.setFloat(7, player.getLevel());
          pstmt.setFloat(8, player.getExp());
          pstmt.setDouble(9, player.getNextLevel());
          pstmt.setInt(10, player.getId());
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
