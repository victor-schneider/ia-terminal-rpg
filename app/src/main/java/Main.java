import java.util.Scanner;
// import java.util.List;
// import java.util.ArrayList;
// import com.google.gson.Gson;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Migration.PlayerSchema;
import Repository.ContextRepo;
import Repository.PlayerInvRepo;
// import Repository.PlayerInventoryRepo;
import Repository.PlayerRepo;
import Migration.ContextSchema;

// Importação dos arquivos
// import Repository.ContextRepo;
// import Repository.EnemyRepo;
// import Repository.PlayerInventoryRepo;
// import Components.Enemy;
import Components.PlayerComponents.Armor.Slot;
import Components.PlayerComponents.Player;
import Components.PlayerComponents.Weapon;
import Components.PlayerComponents.*;
import Components.Context;

import Manegement.EnemyCreation;
import Manegement.ContextCreation;

public class Main {
    public static void main(String[] args) {
        // TODO criar o sistema de turnos
        
        ContextSchema.dropTable();
        // EnemiesSchema.dropTable();
        PlayerInvSchema.dropTable();

        PlayerSchema.dropTable();
        EnemiesSchema.initEnemiesDb();
        PlayerInvSchema.initPlayerInvDb();
        PlayerSchema.initPlayer();
        ContextSchema.initContextDb();

        Player player = new Player("Victor", 1, 50, 1, 0, 20);
        PlayerRepo.createPlayer(player);

        Armor helmet = new Armor("Capcete de ferro", Slot.HELMET, 10, 0, false, "ARMOR");
        PlayerInvRepo.createArmor(helmet);
        player.equipArmor(helmet);

        Weapon espada = new Weapon(20, "Espada de ferro", false, 1, "WAEPON");
        PlayerInvRepo.createWeapon(espada);
        player.equipWeapon(espada);

        Context c;
        Boolean finalizar = false;
        Scanner scanner = new Scanner(System.in);
        int resposta = 0;

        do {
            c = ContextCreation.main();
            System.out.println(c.getDescription() + "\n");

            for(int i = 0; i < c.getOptions().size(); i++){
                System.out.println("Opção: " + (i + 1) + " " + c.getOptions().get(i) + "\n");
            }
            System.out.println("4 - Finalizar jogo? " + "\n");
            resposta = scanner.nextInt();

            if(c.getCombate()) {                
                EnemyCreation.main(player, c.getDescription());
            }

            if(resposta == 4) {
                finalizar = true;
            } else {
                ContextRepo.createContext(c.getOptions().get(resposta - 1));
            }
        } while (!finalizar);

        scanner.close();
     }
}
