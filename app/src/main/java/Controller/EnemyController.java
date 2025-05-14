package Controller;

import Repository.EnemyRepo;
import Components.DTO.EnemyUpdateDTO;
import Manegement.EnemyCreation;
import Components.Enemy;
import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class EnemyController {
  
  private static final Gson gson = new Gson();

  public static void registerRoutes(Javalin app) {
    app.get("/enemy/{id}", EnemyController::getEnemy);
    app.post("/enemy", EnemyController::createEnemy);
    app.patch("/enemy/{id}", EnemyController::updateEnemy);
    app.delete("/enemy/{id}", EnemyController::deleteEnemy);
  }

  // C R U D
  // Create Read Update Delete
  private static void createEnemy(Context ctx) {
    try {
      Enemy enemy = EnemyCreation.main();

      ctx.status(200).result("Inimigo criado com sucesso!").result(gson.toJson(enemy));
    } catch (Exception e) {
      ctx.status(401).result("Falha ao criar inimigo: " + e.getMessage());
    }
  }

  private static void getEnemy(Context ctx) {
    try {
      int enemyId = Integer.parseInt(ctx.pathParam("id"));

      Enemy enemy = EnemyRepo.getEnemy(enemyId);

      ctx.status(200).result(gson.toJson(enemy));
    } catch (Exception e) {
      ctx.status(400).result("Falha ao encontrar inimig: " + e.getMessage());
    }
  }

  private static void updateEnemy(Context ctx) {
    try {
      int enemyId = Integer.parseInt(ctx.pathParam("id"));

      Enemy existingEnemy = EnemyRepo.getEnemy(enemyId);

      EnemyUpdateDTO updateData = gson.fromJson(ctx.body(), EnemyUpdateDTO.class);

      if (updateData.getName() != null) existingEnemy.setName(updateData.getName());
      if (updateData.getLevel() != null) existingEnemy.setLevel(updateData.getLevel());
      if (updateData.getHp() != null) existingEnemy.setHp(updateData.getHp());
      if (updateData.getAtk() != null) existingEnemy.setAtk(updateData.getAtk());
      if (updateData.getDef() != null) existingEnemy.setDef(updateData.getDef());
      if (updateData.getDex() != null) existingEnemy.setDex(updateData.getDex());
      if (updateData.getExp() != null) existingEnemy.setExp(updateData.getExp());
      if (updateData.getId() != null) existingEnemy.setId(updateData.getId());
      if (updateData.getName() != null) existingEnemy.setName(updateData.getName());
      if (updateData.getWeapon() != null) existingEnemy.setWeapon(updateData.getWeapon());
      
      EnemyRepo.updateEnemy(existingEnemy);
      ctx.status(200).result("Inimigo atualizado!");

    } catch (Exception e) {
      ctx.status(400).result("Falha ao atualizar inimigo: " + e.getMessage());

    }
  }

  public static void deleteEnemy(Context ctx) {
    try {
      int enemyId = Integer.parseInt(ctx.pathParam("id"));
      EnemyRepo.deleteEnemy(enemyId);
      ctx.status(200).result("Inimigo deletado com sucesso!");

    } catch (Exception e) {
      ctx.status(400).result("Falha ao deletar inimigo: " + e.getMessage());
      
    }
  }
}
