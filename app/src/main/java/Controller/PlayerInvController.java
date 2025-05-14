package Controller;

import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Repository.ContextRepo;
import Manegement.ContextCreation;
import Manegement.ItemCreation;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.DTO.ArmorUpdateDTO;
import Components.DTO.WeaponUpdateDTO;
import Components.PlayerComponents.Armor;
import Components.PlayerComponents.Item;
import Components.PlayerComponents.Weapon;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlayerInvController {
  
  private static final Gson gson = new Gson();
  
  public static void registerRoutes(Javalin app) {
    app.post("/inventory", PlayerInvController::createInventoryItem);
    app.get("/inventory/{id}", PlayerInvController::getInventoryItem);
    app.get("/inventory", PlayerInvController::listInventoryItem);
    app.patch("/inventory/{id}", PlayerInvController::updateInventoryItem);
    app.delete("/inventory/{id}", PlayerInvController::deleteInventoryItem);
  }

  public static void createInventoryItem(Context ctx) {
    try {
      Item item = ItemCreation.main(); // Lembrar de tirar este retorno de Item, O objetico é simplesmente dizer que retornou e que está no inventário para ser equipado.
      ctx.status(200).result(gson.toJson(item));
    } catch (Exception e) {
      ctx.status(400).result("Falha ao criar item!: " + e.getMessage());
    }
  }

  public static void getInventoryItem(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));

      if(PlayerInvRepo.getItem(id).getType().equals("WEAPON")) {
        ctx.status(200).result(gson.toJson(PlayerInvRepo.getWeapon(id)));

      } else if (PlayerInvRepo.getItem(id).getType().equals("ARMOR")) {
        ctx.status(200).result(gson.toJson(PlayerInvRepo.getArmor(id)));

      }
    } catch (Exception e) {
      ctx.status(400).result("Erro ao puxar item: " + e.getMessage());
      
    }
  }

  public static void listInventoryItem(Context ctx) {
    try {
      List<String> response = PlayerInvRepo.listItems();
      ctx.status(200).result(response.toString());

    } catch (Exception e) {
      ctx.status(400).result("Erro ao listar items do inventário: " + e.getMessage());

    }
  }

  public static void updateInventoryItem(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      if ( PlayerInvRepo.getItem(id).getType().equals("WEAPON")) {
        Weapon existingWeapon = PlayerInvRepo.getWeapon(id);

        WeaponUpdateDTO updateData = gson.fromJson(ctx.body(), WeaponUpdateDTO.class);

        if (updateData.getAtk() != null) existingWeapon.setAtk(updateData.getAtk());
        if (updateData.getName() != null) existingWeapon.setName(updateData.getName());
        if (updateData.getEquipped() != null) existingWeapon.setEquipped(updateData.getEquipped());
        if (updateData.getId() != null) existingWeapon.setId(updateData.getId());
        if (updateData.getType() != null) existingWeapon.setType(updateData.getType());

        PlayerInvRepo.updateWeapon(existingWeapon);
        
        ctx.status(200).result(gson.toJson(PlayerInvRepo.getWeapon(id)));

      } else  if ( PlayerInvRepo.getItem(id).getType().equals("ARMOR")) {
        Armor existingArmor = PlayerInvRepo.getArmor(id);

        ArmorUpdateDTO updateData = gson.fromJson(ctx.body(), ArmorUpdateDTO.class);

        if (updateData.getName() != null) existingArmor.setName(updateData.getName());
        if (updateData.getSlot() != null) existingArmor.setSlot(Armor.Slot.valueOf(updateData.getSlot().toString()));
        if (updateData.getName() != null) existingArmor.setName(updateData.getName());
        if (updateData.getDef() != null) existingArmor.setDef(updateData.getDef());
        if (updateData.getId() != null) existingArmor.setDef(updateData.getDef());
        if (updateData.getEquipped() != null) existingArmor.setEquipped(updateData.getEquipped());
        if (updateData.getType() != null) existingArmor.setType(updateData.getType());

        PlayerInvRepo.updateArmor(existingArmor);

        ctx.status(200).result(gson.toJson(PlayerInvRepo.getArmor(id)));
        
      }
    } catch (Exception e) {
      ctx.status(400).result("Erro ao atualizar item: " + e.getMessage());
    }
  }

  public static void deleteInventoryItem(Context ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    try {
      PlayerInvRepo.deleteItem(id);
      ctx.status(200).result("Item deletado com sucesso!");
    } catch (Exception e) {
      ctx.status(400).result("Erro ao deletar item: " + e.getMessage());
    }
  }

}


