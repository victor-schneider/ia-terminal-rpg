package Repository;

import Migration.Database;
import Components.PlayerComponents.*;
import Components.PlayerComponents.Armor.Slot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO configura o equipped aqui

public class PlayerInventoryRepo {
  public static int createItem(String name, String type, String slot,int atk, int def) {
    String sql = "INSERT INTO playerInventory(name, type, slot, atk, def) VALUES(?, ?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          pstmt.setString(1, name);
          pstmt.setString(2, type);
          pstmt.setString(3, slot);
          pstmt.setInt(4, atk);
          pstmt.setInt(5, def);
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
                    rs.getInt("def")
                  );
                  return helmet;

                case ("CHEST"):
                  Armor chest = new Armor(
                  rs.getString("name"),
                  Slot.CHEST,
                  rs.getInt("def")
                 );
                 return chest;

                case("LEGS"):
                Armor legs = new Armor(
                  rs.getString("name"),
                  Slot.LEGS,
                  rs.getInt("def")
                );
                return legs;

                case("BOOTS"):
                Armor boots = new Armor(
                rs.getString("name"),
                Slot.BOOTS,
                rs.getInt("def")
               );
               return boots;

              }
            } else {
              System.out.println("Nenhum item encontrado com ID " + id);
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
                rs.getString("name")
              );
              return weapon;
            } else {
              System.out.println("Nenhum item encontrado com ID " + id);
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

    try (Connection conn = Database.connect();
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

    try (Connection conn = Database.connect();
         var pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao deletar item: " + e.getMessage());
         }
  }
}
