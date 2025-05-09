package Controller;

import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Components.Enemy;
import Components.PlayerComponents.Player;
import com.google.gson.Gson;
import io.javalin.http.Context;

public class BattleController {
  
  private static final Gson gson = new Gson();

  private static void startBattle(Context ctx) {
    int playerId = Integer.parseInt(ctx.queryParam("playerId"));
    int enemyId = Integer.parseInt(ctx.queryParam("enemyId"));

    Player player = PlayerRepo.getPlayer(playerId);
    Enemy enemy = EnemyRepo.getEnemy(enemyId);

    if(player == null || enemy == null) {
      ctx.status(404).result("Player ou inimigo não encontrado");
    }
  }

}
