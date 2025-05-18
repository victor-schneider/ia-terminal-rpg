package Controller;

import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Model.Enemy;
import Model.DTO.ContextUpdateDTO;
import Model.PlayerComponents.Player;


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
    Model.Context context = new Model.Context(null, null, null, null, null, null);
    List<String> response = new ArrayList<>();
    String wrappedJson = "";
    int id = Integer.parseInt(ctx.pathParam("id"));
    
    context.combat(EnemyRepo.getEnemy(id));
    Player player = PlayerRepo.getPlayer();
    Enemy enemy = EnemyRepo.getEnemy(id);

    if( player.getHp() <= 0) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombat(false);
      updateData.setDescription(ContextRepo.getLastContext());

      ctx.status(200).result(gson.toJson(updateData));

    } else if ( enemy.getHp() <= 0 ) {
      ContextUpdateDTO updateData = new ContextUpdateDTO();
      updateData.setCombat(false);
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
