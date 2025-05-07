import java.util.Scanner;
import java.util.logging.ErrorManager;
// import java.util.List;
// import java.util.ArrayList;
// import com.google.gson.Gson;
import java.util.random.RandomGenerator;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Migration.PlayerSchema;
import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerInvRepo;
// import Repository.PlayerInventoryRepo;
import Repository.PlayerRepo;
import Migration.ContextSchema;

// Importação dos arquivos
// import Repository.ContextRepo;
// import Repository.EnemyRepo;
// import Repository.PlayerInventoryRepo;

import Utils.NumberGenerator;
import Utils.ClearConsole;
import Utils.Verifiers;
import Utils.NumberGenerator;


import Components.Enemy;
import Components.PlayerComponents.Armor.Slot;
import Gameplay.Combat;
import Gameplay.Inventory;
import Components.PlayerComponents.*;
import Components.Context;

import Manegement.EnemyCreation;
import Manegement.ContextCreation;

public class Main {
    public static void main(String[] args) {

        /*
         * TODO
         * 3 - Fazer a IA gerar item ao jogador
         * 4 - Criar JSON para aceitar items caso a IA retorne eles
         * 5 - Adicionar lógia de entrada de items ao inventário
         */

        ContextSchema.dropTable();
        EnemiesSchema.dropTable();
        PlayerInvSchema.dropTable();

        PlayerSchema.dropTable();
        EnemiesSchema.initEnemiesDb();
        PlayerInvSchema.initPlayerInvDb();
        PlayerSchema.initPlayer();
        ContextSchema.initContextDb();

        Player player = new Player("Victor", 100, 5, 2, 90, 5, 1, 0, 20, 1);
        PlayerRepo.createPlayer(player);

        Armor helmet = new Armor("Capcete de ferro", Slot.HELMET, 10, 0, true, "ARMOR");
        PlayerInvRepo.createArmor(helmet);
        player.equipArmor(helmet);

        Armor chestPlate = new Armor("Peitoral de ouro", Slot.CHEST, 2, 1, false, "ARMOR");
        PlayerInvRepo.createArmor(chestPlate);

        Weapon espada = new Weapon(5, "Espada de ferro", true, 1, "WEAPON");
        PlayerInvRepo.createWeapon(espada);
        player.equipWeapon(espada);

        Weapon espada2 = new Weapon(10, "Espada de adamantium", false, 1, "WEAPON");
        PlayerInvRepo.createWeapon(espada2);

        Context contexto;
        Boolean finalizar = false, respostaValida = false;
        Scanner scanner = new Scanner(System.in);
        int resposta = 0;

        int crit;
        
        int[] genNumbers = new int[100];
        for (int i = 0; i < genNumbers.length; i++) {
            genNumbers[i] = -1;
        }

        do {
            contexto = ContextCreation.main();

            while (respostaValida == false) {
                ClearConsole.clearConsole();
                System.out.println("----- Historia -----\n");
                System.out.println(contexto.getDescription() + "\n");

                System.out.println("----- Opcoes -----\n");
                for (int i = 0; i < contexto.getOptions().size(); i++) {
                    System.out.println("[" + (i + 1) + "] - " + contexto.getOptions().get(i) + "\n");
                }
                System.out.println("----- Menu -----\n\n[4] - Acessar inventario " + "\n[5] - Finalizar jogo");
                System.out.println("\nSua escolha: ");

                resposta = scanner.nextInt();

                while (resposta < 0 || resposta > 5) {
                    System.out.println("\nOpcao invalida! Digite novamente.\n");
                    resposta = scanner.nextInt();
                }

                if (resposta == 1 || resposta == 2 || resposta == 3) {
                    respostaValida = true;
                }

                // CHAMA O INVENTÁRIO
                if (resposta == 4) {
                    Inventory.main(player);
                }

            }

            respostaValida = false;

            if (contexto.getCombate() && resposta == 1) {
                Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
                finalizar = Combat.main(contexto, player, enemy, genNumbers, finalizar);

            } else if (contexto.getCombate() && resposta == 2) {
                Inventory.main(player);

            } else if (contexto.getCombate() && resposta == 3) {
                Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
                int rng = NumberGenerator.main(100);
                if(NumberGenerator.numberVerifier(rng, genNumbers, player.getDex())) {
                    System.out.println(player.getName() + " Conseguiu fugir com sucesso!");
                    ContextRepo.createContext(player.getName() + " Conseguiu fugir com sucesso do inimigo: " + enemy.getName());
                    finalizar = false;

                } else {
                    System.out.println(player.getName() + " Falhou em fugir!");
                    finalizar = Combat.main(contexto, player, enemy, genNumbers, finalizar);

                };
            }

            if (resposta == 5) {
                finalizar = true;
            } else if (!finalizar) {
                ContextRepo.createContext(contexto.getOptions().get(resposta - 1));
            }
        } while (!finalizar);

        scanner.close();

    }
}
