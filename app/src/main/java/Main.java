import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;
import com.google.gson.Gson;

import Migration.DatabaseSchema;
import Repository.EnemyRepo;
import Components.Enemy;
import Components.PlayerComponents.*;
import Components.PlayerComponents.Armor.Slot;
import Manegement.EnemyCreation;

public class Main {
    public static void main(String[] args) {
    DatabaseSchema.init();
    // EnemyRepo.createEnemy("Elf", 30, 50);
    // EnemyRepo.deleteEnemy(2);
    // EnemyRepo.updateEnemy(1, null, 15, 10);
    // EnemyRepo.getEnemy(1);
    // EnemyRepo.listEnemy();
    // new Main().executar();
    
    Player player = new Player("Teste", 5, 20);

    Armor helmet = new Armor("Capacete de ferro", Slot.HELMET, 5);
    Armor chestPlate = new Armor("Peitoral de ferro", Slot.CHEST, 20);
    Armor boots = new Armor("Botas de ferro", Slot.BOOTS, 15);

    Weapon sword = new Weapon(20, "Espada de fero");

    player.equipArmor(helmet);
    player.equipArmor(chestPlate);
    player.equipArmor(boots);

    player.equipWeapon(sword);

    System.out.println("Armadura no slot capacete: " + player.getEquippedArmor(Slot.HELMET).getName());
    System.out.println("Armadura no slot peitoral: " + player.getEquippedArmor(Slot.CHEST).getName());
    System.out.println("Armadura no slot botas: " + player.getEquippedArmor(Slot.BOOTS).getName());
    System.out.println("Arma equipada: " + player.getEquippedWeapon().getName());
    System.out.println("Defesa total: " + player.getTotalDefense());

   }
  };



