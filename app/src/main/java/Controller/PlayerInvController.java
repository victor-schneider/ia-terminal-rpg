package Controller;

import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Repository.ContextRepo;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.google.gson.Gson;

import Model.DTO.ArmorDTO;
import Model.DTO.ArmorUpdateDTO;
import Model.DTO.WeaponUpdateDTO;
import Model.DTO.WeaponDTO;
import Model.PlayerComponents.Armor;
import Model.PlayerComponents.Item;
import Model.PlayerComponents.Weapon;
import io.javalin.Javalin;
import io.javalin.http.Context;
import opennlp.tools.stemmer.snowball.arabicStemmer;

public class PlayerInvController {
  
  private static final Gson gson = new Gson();
  
  public static void registerRoutes(Javalin app) {
    app.post("/inventories", PlayerInvController::createInventoryItem);
    app.get("/inventories/{id}", PlayerInvController::getInventoryItem);
    app.get("/inventories", PlayerInvController::listInventoryItem);
    app.patch("/inventories/{id}", PlayerInvController::updateInventoryItem);
    app.delete("/inventories/{id}", PlayerInvController::deleteInventoryItem);
  }

  public static void createInventoryItem(Context ctx) {
    try {
      Map<String, Model.Context> map = gson.fromJson(ctx.body(), Map.class);
      String contextJson = gson.toJson(map.get("context"));
      Model.Context context = gson.fromJson(contextJson, Model.Context.class);
      List<String> response = new ArrayList<>();

      context.verifyContext();
      
      ctx.status(200).result("Item criado com suceso");

    } catch (Exception e) {
      ctx.status(400).result("Falha ao criar item!: " + e.getMessage());
    }
  }

  public static void getInventoryItem(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));

      if(PlayerInvRepo.getItem(id).getType().equals("WEAPON")) {
        Weapon weaponData = PlayerInvRepo.getWeapon(id);
        WeaponDTO weapon = new WeaponDTO();
        
        weapon.setAtk(weaponData.getAtk());
        weapon.setName(weaponData.getName());
        weapon.setEquipped(weaponData.getEquipped());
        weapon.setId(weaponData.getId());
        weapon.setType(weaponData.getType());

        Map<String, WeaponDTO> response = Map.of("weapon", weapon);

        ctx.status(200).result(gson.toJson(response));

      } else if (PlayerInvRepo.getItem(id).getType().equals("ARMOR")) {
        Armor data = PlayerInvRepo.getArmor(id);
        ArmorDTO armor = new ArmorDTO();

        armor.setName(data.getName());
        armor.setType(data.getType());
        armor.setSlot(data.getSlot().toString());
        armor.setDef(data.getDef());
        armor.setEquipped(data.getEquipped());
        armor.setId(data.getId());

        Map<String, ArmorDTO> response = Map.of("armor", armor);

        ctx.status(200).result(gson.toJson(armor));

      }
    } catch (Exception e) {
      ctx.status(400).result("Erro ao puxar item: " + e.getMessage());
      
    }
  }

  public static void listInventoryItem(Context ctx) {
    try {
      List<String> response = PlayerInvRepo.listItems();
     

      ctx.status(200).result(gson.toJson(response));

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


