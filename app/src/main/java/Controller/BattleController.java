package Controller;

import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Components.Enemy;
import Components.PlayerComponents.Player;
import Gameplay.Combat;

import Components.DTO.ContextUpdateDTO;

import java.util.List;
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
    List<String> response = new ArrayList<>();
    String wrappedJson = "";
    int id = Integer.parseInt(ctx.pathParam("id"));
    
    Combat.main(EnemyRepo.getEnemy(id));
    Player player = PlayerRepo.getPlayer();
    Enemy enemy = EnemyRepo.getEnemy(id);

    if( player.getHp() <= 0) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombate(false);
      updateData.setDescription(ContextRepo.getLastContext());

      ctx.status(200).result(gson.toJson(updateData));

    } else if ( enemy.getHp() <= 0 ) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombate(false);
      updateData.setDescription(ContextRepo.getLastContext());

      ctx.status(200).result(gson.toJson(updateData));
    } else {
      
      wrappedJson = gson.toJson(player);
      response.add(wrappedJson);
      wrappedJson = "";
      wrappedJson = gson.toJson(enemy);
      response.add(wrappedJson);
      
      ctx.status(200).result(response.toString());
    }

    
  }


}
