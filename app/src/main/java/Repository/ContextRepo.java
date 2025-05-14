package Repository;

import Migration.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ContextRepo {
  public static int createContext(String contexto) {
    String sql = "INSERT INTO context(contexto) VALUES(?)";

    try (Connection conn = Database.connect();
         PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
          conn.setAutoCommit(false);
          stmt.setString(1, contexto);
          stmt.executeUpdate();
          conn.commit();

          try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              return generatedKeys.getInt(1);
            } else {
              throw new SQLException("Creating context failed, no ID obtained");
            }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao criar contexto: " + e.getMessage());
         }
    return -1;
  }

  public static void getContext(int id) {
    String sql = "SELECT * FROM context WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);

          try (var rs = pstmt.executeQuery()) {
           if(rs.next()) {
            System.out.printf("#%d Contexto: %s",
            rs.getInt("id"),
            rs.getString("contexto"));
           } else {
            System.out.println("Nenhum contexto encontrado com ID " + id);
           }
          }
         } catch (SQLException e) {
          System.err.println("Erro ao pegar contexto: " + e.getMessage());
         }
  }

  public static String getLastContext() {
    String sql = "SELECT * FROM context ORDER BY id DESC LIMIT 1";

    try ( Connection conn = Database.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (var rs = pstmt.executeQuery()) {
              if(rs.next()) {
                return rs.getString("contexto");
              }
            } catch (Exception e) {
              System.err.println("Erro ao procurar por contexto: " + e.getMessage());
              return null;
            }
          } catch (SQLException e) {
            System.err.println("Erro ao procurar contexto: " + e.getMessage());
          }
    return null;
  }

  public static List<String> listContext() {
    String sql = "SELECT * FROM context";
    List<String> response = new ArrayList<>();

    try (Connection conn = Database.connect();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery(sql)) {
          while(rs.next()) {
            response.add(rs.getString("contexto"));
          }
          return response;
         } catch (SQLException e) {
          System.err.println("Erro lendo contexto: " + e.getMessage());
         }
    return null;
  }

  public static void updateContext(int id, String contexto) {
    String sql = "UPDATE context SET contexto = ? WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, contexto);
          pstmt.setInt(2, id);
          pstmt.executeUpdate();
         } catch (SQLException e) {
          System.err.println("Erro ao atualizar contexto: " + e.getMessage());
         }
  }

  public static void deleteContext(int id) {
    String sql = "DELETE FROM context WHERE id = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
          
         } catch (SQLException e) {
          System.err.println("Erro ao excluir contexto: " + e.getMessage());
         }
  }
}
