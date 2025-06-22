package Controller;

import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Model.DTO.PlayerDTO;
import Model.DTO.PlayerUpdateDTO;
import Model.DTO.WeaponDTO;
import Model.PlayerComponents.Player;
import Model.PlayerComponents.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.s;

import com.google.gson.Gson;

import Controller.dto.NewPlayerDto;
import Controller.dto.PlayerAttributes;
import Controller.dto.PlayerData;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlayerController {

  private static final Gson gson = new Gson();

  public static void registerRoutes(Javalin app) {
    app.get("/players/test", PlayerController::getPlayerDto);
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

  public static void getPlayerDto(Context ctx) {
    ctx.status(200).result(gson.toJson(new NewPlayerDto(new PlayerData(new PlayerAttributes("nominho", 12)))));
  }

  public static void getPlayer(Context ctx) {
    Player playerData = PlayerRepo.getPlayer();
    WeaponDTO weaponDTO = new WeaponDTO();
    List<WeaponDTO> weaponList = new ArrayList<>();

    Weapon weaponData = PlayerInvRepo.getEquippedWeapon();
    weaponDTO.setAtk(weaponData.getAtk());
    weaponDTO.setName(weaponData.getName());
    weaponDTO.setEquipped(weaponData.getEquipped());
    weaponDTO.setId(weaponData.getId());
    weaponDTO.setType(weaponData.getType());

    weaponList.add(weaponDTO);

    PlayerDTO player = new PlayerDTO();
    player.setName(playerData.getName());
    player.setHp(playerData.getHp());
    player.setAtk(playerData.getAtk());
    player.setDef(playerData.getDef());
    player.setDex(playerData.getDex());
    player.setLck(playerData.getLck());
    player.setLevel(playerData.getLevel());
    player.setExp(playerData.getExp());
    player.setNextLevel(playerData.getNextLevel());
    player.setEquippedWeapon(weaponList);
    player.setEquippedArmor(PlayerInvRepo.listEquippedArmor());

    Map<String, PlayerDTO> attributes = Map.of("attributes", player);
    Map<String, Map<String, PlayerDTO>> data = Map.of("data", attributes);


    if(player == null) {
      ctx.status(404).result("Player não achado");
      return;
    }

    ctx.status(200).result(gson.toJson(data));
  }

  public static void updatePlayer(Context ctx) {
    Player existingPlayer = PlayerRepo.getPlayer();

    try {
      // Novo padrão de parsing do JSON
      Map<String, PlayerUpdateDTO> map = gson.fromJson(ctx.body(), Map.class);
      String playerJson = gson.toJson(map.get("player"));
      PlayerUpdateDTO updateData = gson.fromJson(playerJson, PlayerUpdateDTO.class);

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


