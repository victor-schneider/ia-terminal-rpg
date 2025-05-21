package Repository;

import Migration.Database;
import Model.DTO.ArmorDTO;
import Model.PlayerComponents.*;
import Model.PlayerComponents.Armor.Slot;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

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

  public static List<String> listItems() {
    Gson gson = new Gson();
    String sql = "SELECT * FROM playerInventory";
    List<String> response = new ArrayList<>();

    String divider = "╠════════════════════";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)){

      boolean first = true;
      while (rs.next()) {
        if (!first) {
          System.out.println(divider);
        }
        first = false;

        int id = rs.getInt("id");
        String type = rs.getString("type");
        String name = rs.getString("name");
        boolean equipped = rs.getBoolean("equipped");

        if (rs.getString("slot") == null) {
          // Weapon
          int atk = rs.getInt("atk");
          System.out.printf("  ID: %d\n", id);
          System.out.printf("  Tipo: %s\n", type);
          System.out.printf("  Nome: %s\n", name);
          System.out.printf("  ATK: %d\n", atk);
          System.out.printf("  Equipada: %s\n", equipped ? "Sim" : "Não");
          String weaponJson = gson.toJson(new Weapon(atk, name, equipped, id, type));
          response.add(weaponJson);
        } else {
          // Armor
          String slot = rs.getString("slot");
          int def = rs.getInt("def");
          System.out.printf("  ID: %d\n", id);
          System.out.printf("  Tipo: %s\n", type);
          System.out.printf("  Nome: %s - %s\n", slot, name);
          System.out.printf("  DEF: %d\n", def);
          System.out.printf("  Equipada: %s\n", equipped ? "Sim" : "Não");
          String armorJson = gson.toJson(new Armor(name, Slot.valueOf(slot), def, id, equipped, type));
          response.add(armorJson);
        }
      }
      return response;
    } catch (SQLException e) {
      System.err.println("Erro lendo items: " + e.getMessage());
    }
    return null;
  }

  public static void listEquippedItems() {
    String divider = "╠════════════════════";

    String sql = "SELECT * FROM playerInventory WHERE equipped = 1";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {

      boolean first = true;
      while (rs.next()) {
        if (!first) {
          System.out.println(divider);
        }
        first = false;

        int id = rs.getInt("id");
        String type = rs.getString("type");
        String name = rs.getString("name");
        boolean equipped = rs.getBoolean("equipped");

        if (rs.getString("slot") == null) {
          // Weapon
          int atk = rs.getInt("atk");
          System.out.printf("  ID: %d\n", id);
          System.out.printf("  Tipo: %s\n", type);
          System.out.printf("  Nome: %s\n", name);
          System.out.printf("  ATK: %d\n", atk);
          System.out.printf("  Equipada: %s\n", equipped ? "Sim" : "Não");
        } else {
          // Armor
          String slot = rs.getString("slot");
          int def = rs.getInt("def");
          System.out.printf("  ID: %d\n", id);
          System.out.printf("  Tipo: %s\n", type);
          System.out.printf("  Nome: %s - %s\n", slot, name);
          System.out.printf("  DEF: %d\n", def);
          System.out.printf("  Equipada: %s\n", equipped ? "Sim" : "Não");
        }
      }
    } catch (SQLException e) {
      System.err.println("Erro ao listar items equipados: " + e.getMessage());
    }
  }

  public static Weapon getEquippedWeapon() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND type = 'WEAPON'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          if(rs.next()) {
              Weapon weapon = new Weapon(
                rs.getInt("atk"),
                rs.getString("name"),
                rs.getBoolean("equipped"),
                rs.getInt("id"),
                rs.getString("type")
              );
              
              return weapon;
            } 

         } catch (SQLException e) {
          System.err.println("Erro ao listar items equipados: " + e.getMessage());
         }
    return null;
  }

  public static Armor getEquippedHelmet() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND slot = 'HELMET'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          if(rs.next()) {
              Armor armor = new Armor(
                rs.getString("name"),
                Slot.HELMET,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
              );
              
              return armor;
            } 

         } catch (SQLException e) {
          System.err.println("Erro ao listar helmet: " + e.getMessage());
          return null;
         }
    return null;
  }

  public static Armor getEquippedChest() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND slot = 'CHEST'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          if(rs.next()) {
              Armor armor = new Armor(
                rs.getString("name"),
                Slot.CHEST,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
              );
              
              return armor;
            } 

         } catch (SQLException e) {
          System.err.println("Erro ao listar chest: " + e.getMessage());
         }
    return null;
  }

  public static Armor getEquippedLegs() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND slot = 'LEGS'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          if(rs.next()) {
              Armor armor = new Armor(
                rs.getString("name"),
                Slot.LEGS,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
              );
              
              return armor;
            } 

         } catch (SQLException e) {
          System.err.println("Erro ao listar legs: " + e.getMessage());
         }
    return null;
  }

  public static Armor getEquippedBoots() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND slot = 'BOOTS'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          
          if(rs.next()) {
              Armor armor = new Armor(
                rs.getString("name"),
                Slot.BOOTS,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
              );
              
              return armor;
            } 

         } catch (SQLException e) {
          System.err.println("Erro ao listar boots: " + e.getMessage());
         }
    return null;
  }

  public static List<ArmorDTO> listEquippedArmor() {
    String sql = "SELECT * FROM playerInventory WHERE equipped = 1 AND type = 'ARMOR'";

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql);) {
          List<ArmorDTO> response = new ArrayList<>();
          Gson gson = new Gson();

          while(rs.next()) {
              ArmorDTO armorDTO = new ArmorDTO();

              Slot slot = Slot.HELMET;
              if(rs.getString("type") == "HELMET") slot = Slot.HELMET;
              if(rs.getString("type") == "CHEST") slot = Slot.CHEST ;
              if(rs.getString("type") == "LEGS") slot = Slot.LEGS ;
              if(rs.getString("type") == "BOOTS") slot = Slot.BOOTS ;
              Armor armor = new Armor(
                rs.getString("name"),
                slot,
                rs.getInt("def"),
                rs.getInt("id"),
                rs.getBoolean("equipped"),
                rs.getString("type")
              );

              armorDTO.setName(armor.getName());
              armorDTO.setType(armor.getType());
              armorDTO.setSlot(armor.getSlot().toString());
              armorDTO.setDef(armor.getDef());
              armorDTO.setEquipped(armor.getEquipped());
              armorDTO.setId(armor.getId());

              response.add(armorDTO);

            }

            return response;
         } catch (SQLException e) {
          System.err.println("Erro ao listar items equipados: " + e.getMessage());
         }
    return null;
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
