package Controller;

import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Model.Enemy;
import Model.DTO.ContextUpdateDTO;
import Model.DTO.PlayerDTO;
import Model.DTO.WeaponDTO;
import Model.DTO.EnemyDTO;
import Model.PlayerComponents.Player;
import Model.PlayerComponents.Weapon;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class BattleController {
  
  private static final Gson gson = new Gson();

  public static void registerRoutes (Javalin app) {
    app.patch("/battle/{id}", BattleController::turn);
  }

  public static void turn (Context ctx) {
    Model.Context context = new Model.Context(null, null, null, null, null, 0);
    String wrappedJson = "";
    Map<String, ContextUpdateDTO> contextResponse;

    int id = Integer.parseInt(ctx.pathParam("id"));
    
    Enemy enemy = EnemyRepo.getEnemy(id);
    context.combat(enemy);
    Player player = PlayerRepo.getPlayer();
    enemy = EnemyRepo.getEnemy(id);

    if( player.getHp() <= 0) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombat(false);
      updateData.setDescription(ContextRepo.getLastContext());
      contextResponse = Map.of("result", updateData);

      ctx.status(200).result(gson.toJson(contextResponse));

    } else if ( enemy.getHp() <= 0 ) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombat(false);
      updateData.setDescription(ContextRepo.getLastContext());
      contextResponse = Map.of("result", updateData);

      ctx.status(200).result(gson.toJson(contextResponse));

    } else {
      List<String> response = new ArrayList<>();
      Map<String, PlayerDTO> playerResponse;
      Map<String, EnemyDTO> enemyResponse;

      PlayerDTO playerDTO = new PlayerDTO();

      playerDTO.setName(player.getName());
      playerDTO.setHp(player.getHp());
      playerDTO.setAtk(player.getAtk());
      playerDTO.setDef(player.getDef());
      playerDTO.setDex(player.getDex());
      playerDTO.setLck(player.getLck());
      playerDTO.setLevel(player.getLevel());
      playerDTO.setExp(player.getExp());
      playerDTO.setNextLevel(player.getNextLevel());

      playerResponse = Map.of("player", playerDTO);
      response.add(gson.toJson(playerResponse));

      EnemyDTO enemyDTO = new EnemyDTO();
      enemyDTO.setName(enemy.getName());
      enemyDTO.setHp(enemy.getHp());
      enemyDTO.setAtk(enemy.getAtk());
      enemyDTO.setDef(enemy.getDef());
      enemyDTO.setDex(enemy.getDex());
      enemyDTO.setLevel(enemy.getLevel());
      enemyDTO.setExp(enemy.getExp());

      enemyResponse = Map.of("enemy", enemyDTO);
      response.add(gson.toJson(enemyResponse));
      
      ctx.status(200).result(response.toString());
    }

    
  }


}
