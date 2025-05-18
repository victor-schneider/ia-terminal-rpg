package Controller;

import Repository.PlayerRepo;
import Model.DTO.PlayerUpdateDTO;
import Model.PlayerComponents.Player;

import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlayerController {

  private static final Gson gson = new Gson();

  public static void registerRoutes(Javalin app) {
    app.get("/players", PlayerController::getPlayer);
    app.post("/players", PlayerController::createPlayer);
    app.patch("/players", PlayerController::updatePlayer);
    app.delete("/players", PlayerController::deletePlayer);
  }

  public static void createPlayer(Context ctx) {
    Player player = gson.fromJson(ctx.body(), Player.class);
    if(player == null) {
      ctx.status(401).result("Erro ao inserir player");
    }
    PlayerRepo.createPlayer(player);  
    ctx.status(200).result("Player Created");
  }

  public static void getPlayer(Context ctx) {
    Player player = PlayerRepo.getPlayer();

    if(player == null) {
      ctx.status(404).result("Player não achado");
      return;
    }

    ctx.status(200).result(gson.toJson(player));
  }

  public static void updatePlayer(Context ctx) {
    Player existingPlayer = PlayerRepo.getPlayer();

    try {
    PlayerUpdateDTO updateData = gson.fromJson(ctx.body(), PlayerUpdateDTO.class);

    if (updateData.getName() != null) existingPlayer.setName(updateData.getName());
    if (updateData.getHp() != null) existingPlayer.setHp(updateData.getHp());
    if (updateData.getAtk() != null) existingPlayer.setAtk(updateData.getAtk());
    if (updateData.getDef() != null) existingPlayer.setDef(updateData.getDef());
    if (updateData.getDex() != null) existingPlayer.setDex(updateData.getDex());
    if (updateData.getLck() != null) existingPlayer.setLck(updateData.getLck());
    if (updateData.getLevel() != null) existingPlayer.setLevel(updateData.getLevel());
    if (updateData.getExp() != null) existingPlayer.setExp(updateData.getExp());
    if (updateData.getNextLevel() != null) existingPlayer.setNextLevel(updateData.getNextLevel());

      PlayerRepo.updatePlayer(existingPlayer);

    } catch (Exception e) {
      ctx.status(401).result("fail to update player" + e.getMessage());
      return;
    }

    ctx.status(200).result("Player Updated");
    
  }

  public static void deletePlayer(Context ctx) {
    try {
      PlayerRepo.deletePlayer(1);

    } catch (Exception e) {
      ctx.status(400).result("Falha ao deletar jogador: " + e.getMessage());
      return;
    }

    ctx.status(200).result("Jogador deletado com sucesso!");
  }
}


