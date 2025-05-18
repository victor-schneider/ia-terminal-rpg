// Importação dos Util
import java.util.Scanner;
import java.util.logging.ErrorManager;
import java.util.random.RandomGenerator;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Migration.PlayerSchema;
import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.*;
import Model.PlayerComponents.Armor.Slot;
import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Migration.ContextSchema;

// Importação das classes Utils
import Utils.NumberGenerator;
import Utils.ClearConsole;
import Utils.Verifiers;
import Utils.NumberGenerator;
// Importação da Gameplay
import Gameplay.Inventory;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;

public class Main {
     public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.anyHost();
                    
                });
            });
        });

        RouteConfig.registerRoutes(app);
        app.start(7070);

        
        /*
         * TODO
         * 6 - Refatorar os arquivos de Manegement, Combat e Inventory para se adequar à orientação a objetos
         * * Juntar todos os Manegement num arquivo dentro de contexto
         *  Repository vira uma classe e Connection conn = Database.connect()
         * 7 - Adicionar no GET do player o left join com inventário com os items equipadas.
         * 8 - Talvez refatorar o banco de dados e separar weapons e armor.
         * 9 - Refatorar o context criation para criar além do contexto, os items. JSON conteria uma aba de Items onde ele retornaria o tipo de item mencionado.
         * 10 - Adicionar junto ao status do jogador os items equipados para a IA ter não inventar items.
         * 11 - O jogador, quando equipa um item do inventário, não necessariamente esta o usando na classe player. Teremos que achar uma forma de, quando equipar um item o método equipWeapon(weapon) ou equipArmor(armor) ser chamado para equipar este novo item.
         * 12 - O método de desviar de um ataque pode ficar na classe Personagem, pois ambos player e inimigo usam este método
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

        // Enemy enemy = new Enemy("Goblin", 1, 20, 0, 20, 5, 15, 30, "Clava de pedra");
        // EnemyRepo.createEnemy(enemy);

        Armor helmet = new Armor("Capcete de ferro", Slot.HELMET, 10, 0, true, "ARMOR");
        PlayerInvRepo.createArmor(helmet);
        player.equipArmor(helmet);

        // Armor chestPlate = new Armor("Peitoral de couro", Slot.CHEST, 2, 1, true, "ARMOR");
        // PlayerInvRepo.createArmor(chestPlate);

        Weapon espada = new Weapon(5, "Espada de ferro", true, 1, "WEAPON");
        PlayerInvRepo.createWeapon(espada);
        player.equipWeapon(espada);

        
        
        // Weapon espada2 = new Weapon(10, "Espada de adamantium", false, 1, "WEAPON");
        // PlayerInvRepo.createWeapon(espada2);

        // Context contexto;
        // Boolean finalizar = false, respostaValida = false;
        // Scanner scanner = new Scanner(System.in);
        // int resposta = 0;

        // int crit;
        
        

        // do {
        //     contexto = ContextCreation.main(player);

        //     while (respostaValida == false) {
        //         ClearConsole.clearConsole();
        //         System.out.println("----- Historia -----\n");
        //         System.out.println(contexto.getDescription() + "\n");

        //         System.out.println("----- Opcoes -----\n");
        //         for (int i = 0; i < contexto.getOptions().size(); i++) {
        //             System.out.println("[" + (i + 1) + "] - " + contexto.getOptions().get(i) + "\n");
        //         }
        //         System.out.println("----- Menu -----\n\n[4] - Acessar inventario " + "\n[5] - Finalizar jogo");
        //         System.out.println("\nSua escolha: ");

        //         resposta = scanner.nextInt();

        //         while (resposta < 0 || resposta > 5) {
        //             System.out.println("\nOpcao invalida! Digite novamente.\n");
        //             resposta = scanner.nextInt();
        //         }

        //         if (resposta == 1 || resposta == 2 || resposta == 3) {
        //             respostaValida = true;
        //         }
        //         if(contexto.getItem() && resposta == 1) {
        //             ItemCreation.main(contexto, player);
        //             contexto.setItem(false);
        //         }
        //         // CHAMA O INVENTÁRIO
        //         if (resposta == 4) {
        //             Inventory.main(player);
        //         }

        //     }

        //     respostaValida = false;

        //     if (contexto.getCombate() && resposta == 1) {
        //         Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
        //         finalizar = Combat.main(contexto, player, enemy, genNumbers, finalizar);

        //     } else if (contexto.getCombate() && resposta == 2) {
        //         Inventory.main(player);

        //     } else if (contexto.getCombate() && resposta == 3) {
        //         Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
        //         int rng = NumberGenerator.main(100);

        //         if(NumberGenerator.numberVerifier(rng, genNumbers, player.getDex())) {
        //             System.out.println(player.getName() + " Conseguiu fugir com sucesso!");
        //             ContextRepo.createContext(player.getName() + " Conseguiu fugir com sucesso do inimigo: " + enemy.getName());
        //             finalizar = false;

        //         } else {
        //             System.out.println(player.getName() + " Falhou em fugir!");
        //             finalizar = Combat.main(contexto, player, enemy, genNumbers, finalizar);

        //         };
        //     }

        //     if (resposta == 5) {
        //         finalizar = true;
        //     } else if (!finalizar) {
        //         ContextRepo.createContext(contexto.getOptions().get(resposta - 1));
        //     }
        // } while (!finalizar);

        // scanner.close();

    }
}
