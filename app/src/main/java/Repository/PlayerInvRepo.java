package Repository;

import Migration.Database;
import Components.PlayerComponents.*;
import Components.PlayerComponents.Armor.Slot;
import Components.PlayerComponents.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO configura o equipped aqui

public class PlayerInvRepo {
  public static void createWeapon(Weapon weapon) {
    String sql = "INSERT INTO playerInventory(atk, name, equipped, type) VALUES(?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setInt(1, weapon.getAtk());
          pstmt.setString(2, weapon.getName());
          pstmt.setBoolean(3, weapon.getEquipped());
          pstmt.setString(4, weapon.getType());
          pstmt.executeUpdate();

          try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
            if( generatedKey.next() ) {
              weapon.setId(generatedKey.getInt(1));
            } else {
              throw new SQLException("Creating weapon failed, no ID obtained");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao criar arma: " + e.getMessage());
         }
  }

  public static void createArmor (Armor armor) {
    String sql = "INSERT INTO playerInventory(name, slot, def, equipped, type) VALUES(?, ?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, armor.getName());
          pstmt.setString(2, armor.getSlot().name());
          pstmt.setInt(3, armor.getDef());
          pstmt.setBoolean(4, armor.getEquipped());
          pstmt.setString(5, armor.getType());
          pstmt.executeUpdate();

          try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
            if( generatedKey.next() ) {
              armor.setId(generatedKey.getInt(1));
            } else {
              throw new SQLException("Creating armor failed, no ID obtained");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao criar armadura: " + e.getMessage());
         }
  }

  public static Item getItem(int id) {
    String sql = "SELECT name, equipped, id, type FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (var rs = pstmt.executeQuery()) {
              if(rs.next()) {
                Item item = new Item(
                  rs.getString("name"),
                  rs.getBoolean("equipped"),
                  rs.getInt("id"),
                  rs.getString("type")
                );
                return item;
              } else {
                System.out.println("Nenhum item achado com o ID: " + id);
                return null;
              }
            } 
         } catch (SQLException e) {
          System.err.println("Erro ao puxar item: " + e.getMessage());
        }
        return null;
  }

  public static Armor getArmor(int id) {
    String sql = "SELECT * FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()){
            if(rs.next()) {
              switch (rs.getString("slot")) {
                case ("HELMET"):
                  Armor helmet = new Armor(
                    rs.getString("name"),
                    Slot.HELMET,
                    rs.getInt("def"),
                    rs.getInt("id"),
                    rs.getBoolean("equipped"),
                    rs.getString("type")
                  );
                  return helmet;

                case ("CHEST"):
                  Armor chest = new Armor(
                  rs.getString("name"),
                  Slot.CHEST,
                  rs.getInt("def"),
                  rs.getInt("id"),
                  rs.getBoolean("equipped"),
                  rs.getString("type")
                 );
                 return chest;

                case("LEGS"):
                Armor legs = new Armor(
                  rs.getString("name"),
                  Slot.LEGS,
                  rs.getInt("def"),
                  rs.getInt("id"),
                  rs.getBoolean("equipped"),
                  rs.getString("type")
                );
                return legs;

                case("BOOTS"):
                Armor boots = new Armor(
                rs.getString("name"),
                Slot.BOOTS,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
               );
               return boots;

              }
            } else {
              System.out.println("Nenhum item encontrado com ID " + id);
              return null;
            }
          } 
        } catch (SQLException e) {
            System.err.println("Erro ao imprimir item: " + e.getMessage());
         }
    return null;
  }

  public static Weapon getWeapon(int id) {
    String sql = "SELECT * FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()){
            if(rs.next()) {
              Weapon weapon = new Weapon(
                rs.getInt("atk"),
                rs.getString("name"),
                rs.getBoolean("equipped"),
                rs.getInt("id"),
                rs.getString("type")
              );
              return weapon;
            } else {
              System.out.println("Nenhum item encontrado com ID " + id);
              return null;
            }
          } 
        } catch (SQLException e) {
            System.err.println("Erro ao imprimir item: " + e.getMessage());
         }
    return null;
  }

  public static void listItems() {
    String sql = "SELECT * FROM playerInventory";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)){
          while (rs.next()) {
            if(rs.getString("slot") == null) {
              System.out.printf("%nID: %d %nItem: %s %nTipo: %s %nATK: %d %nEquipado?: %b%n------%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("type"),
              rs.getInt("atk"),
              rs.getBoolean("equipped"));

            } else {
              System.out.printf("%nID:%d %nItem: %s %nTipo: %s %nSlot: %s %nDEF: %d %nEquipado?: %b%n------%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("type"),
              rs.getString("slot"),
              rs.getInt("def"),
              rs.getBoolean("equipped"));

            }
          }
         } catch (SQLException e) {
          System.err.println("Erro lendo items: " + e.getMessage());
         }
  }

  public static void listEquippedItems() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          while(rs.next()) {
            if(rs.getString("slot") == null) {
              System.out.printf("%nID: %d %nItem: %s %nTipo: %s %nATK: %d %nEquipado?: %b%n------%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("type"),
              rs.getInt("atk"),
              rs.getBoolean("equipped"));

            } else {
              System.out.printf("%nID:%d %nItem: %s %nTipo: %s %nSlot: %s %nDEF: %d %nEquipado?: %b%n------%n",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("type"),
              rs.getString("slot"),
              rs.getInt("def"),
              rs.getBoolean("equipped"));

            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao listar items equipados: " + e.getMessage());
         }
  }

  public static void updateWeapon(Weapon weapon){
    String sql = "UPDATE playerInventory SET atk = ?, name = ?, equipped = ? WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, weapon.getAtk());
          pstmt.setString(2, weapon.getName());
          pstmt.setBoolean(3, weapon.getEquipped());
          pstmt.setInt(4, weapon.getId());
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao atualizar arma: " + e.getMessage());
         }
  }

  public static void updateArmor(Armor armor){
    String sql = "UPDATE playerInventory SET name = ?, slot = ?, def = ?, equipped = ? WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, armor.getName());
          pstmt.setString(2, armor.getSlot().name());
          pstmt.setInt(3, armor.getDef());
          pstmt.setBoolean(4, armor.getEquipped());
          pstmt.setInt(5, armor.getId());
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao atualizar armadura: " + e.getMessage());
         }
  }

  public static void deleteItem(int id){
    String sql = "DELETE FROM playerInventory WHERE id = ?";

    try (Connection conn = Database.connect();
         var pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao deletar item: " + e.getMessage());
         }
  }
}
