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

import Components.Enemy;
import Components.PlayerComponents.Armor.Slot;
import Components.PlayerComponents.*;
import Components.Context;

import Manegement.EnemyCreation;
import Manegement.ContextCreation;

public class Main {
    public static void main(String[] args) {

        /*
         * TODO
         * 1 - Botar tudo para duas casas decimais - formatar duas casas
         * 2 - Possibilidade de fugir de um combate
         * 3 - Fazer a IA gerar item ao jogador
         * 4 - Croar JSON para aceitar items caso a IA retorne eles
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

        Player player = new Player("Victor", 100, 5, 2, 5, 5, 1, 0, 20, 1);
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
        float atk = player.getAttack();
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

                if (resposta == 4) {
                    int id = -1, respostaT = 0;                    

                    while (respostaT != 3) {
                        ClearConsole.clearConsole();
                        System.out.println("===== INVENTÁRIO =====");
                        PlayerInvRepo.listItems();
                        System.out.println("[1] - Equipar um item");
                        System.out.println("[2] - Desequipar um item");
                        System.out.println("[3] - Sair do inventario");
                        respostaT = scanner.nextInt();
                        switch (respostaT) {
                            case 1:
                                    System.out.println("\n[0] - Sair do inventario \nSelecione o ID do item a ser equipado: ");
                                    id = scanner.nextInt();
                                    
                                    if (id == 0) break;
                                    id = Verifiers.itemIdVerifier(id);
                                    if (id == 0) break;
                                    
                                    Item item = PlayerInvRepo.getItem(id);

                                    if (item.getType().equals("WEAPON")) {
                                        Weapon weapon = PlayerInvRepo.getWeapon(id);

                                        while (id != 0 && weapon.getEquipped()) {
                                            System.out.println("A arma ja esta equipada, digite outro ID ou 0 para sair");
                                            id = scanner.nextInt();

                                            if(id == 0) break;
                                            id = Verifiers.weaponIdVerifier(id);
                                            if(id == 0) break;
                                            weapon = PlayerInvRepo.getWeapon(id);
                                        };

                                        player.equipWeapon(weapon);
                                    } else if(item.getType().equals("ARMOR")) {
                                        Armor armor = PlayerInvRepo.getArmor(id);

                                        while (id != 0 && armor.getEquipped()) {
                                            System.out.println("A armadura já está equipada, digite outro ID ou 0 para sair");
                                            id = scanner.nextInt();

                                            if(id == 0) break;
                                            id = Verifiers.armorIdVerifier(id);
                                            if(id == 0) break;

                                            armor = PlayerInvRepo.getArmor(id);
                                        }

                                        player.equipArmor(armor);
                                    }
                                break;

                            case 2:
                            ClearConsole.clearConsole();
                            System.out.println("===== ITEMS EQUIPADOS =====");
                                PlayerInvRepo.listEquippedItems();
                                System.out.println("\n[0] - Sair do inventario \nSelecione o ID do item a ser desequipado: ");
                                id = scanner.nextInt();

                                if (id == 0) break; 
                                id = Verifiers.itemIdVerifier(id);
                                if (id == 0) break;

                                item = PlayerInvRepo.getItem(id);

                                if (item.getType().equals("WEAPON")) {
                                    Weapon weapon = PlayerInvRepo.getWeapon(id);

                                    while( !weapon.getEquipped() && id != 0) {
                                        System.out.println("A arma já está desequipada. Digite outro ID ou 0 para sair.");
                                        id = scanner.nextInt();
                                        
                                        if(id == 0) break;
                                        id = Verifiers.weaponIdVerifier(id);
                                        if(id == 0) break;

                                        weapon = PlayerInvRepo.getWeapon(id);
                                    }
                                    if(id == 0) break;

                                    player.unequipWeapon(weapon);

                                } else {
                                    Armor armor = PlayerInvRepo.getArmor(id);

                                    while( !armor.getEquipped() && id != 0) {
                                        System.out.println("A armadura já está desequipada. Digite outro ID ou 0 para sair.");
                                        id = scanner.nextInt();

                                        if(id == 0) break;
                                        id = Verifiers.armorIdVerifier(id);
                                        if(id == 0) break;

                                        armor = PlayerInvRepo.getArmor(id);
                                    }
                                    if(id == 0) break;

                                    player.unequipArmor(armor);
                                }
                                break;

                            case 3:
                                break;
                        }

                    }
                }
                    

            }

            respostaValida = false;

            if (contexto.getCombate() && resposta == 1) {
                Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
                float enemyAtk = enemy.getAtk();

                System.out.println("Combate!\n\n");
                enemy.getEnemyStatus(enemy);

                while (contexto.getCombate()) {
                    int opcao = 0;
                    int rng = NumberGenerator.main(100);

                    for (int i = 0; i < genNumbers.length; i++) {
                        genNumbers[i] = -1;
                    }

                    System.out.println("Você decide: \n[1] - Atacar");
                    opcao = scanner.nextInt();

                    if (opcao == 1) {
                        atk = player.getAtk();
                        if (NumberGenerator.numberVerifier(rng, genNumbers, player.getLck() + 10)) {
                            atk *= 2;
                            System.out.println("Dano critico!\n");
                        }
                        if (atk == player.getAtk()) {
                            atk -= ((atk * enemy.getDef()) / 100);
                        }

                        if (NumberGenerator.numberVerifier(rng, genNumbers, enemy.getDex())) {
                            System.out.println("O inimigo desviou do ataque!");
                        } else {
                            enemy.setHp(enemy.getHp() - atk);
                            System.out
                                    .println(player.getName() + " Infligiu " + atk + " de dano ao " + enemy.getName());
                            EnemyRepo.updateEnemy(enemy);
                            System.out.println(enemy.getName() + " HP: " + enemy.getHp());
                        }
                    }

                    if (enemyAtk == enemy.getAtk()) {
                        enemyAtk -= ((enemyAtk * player.getTotalDefense()) / 100);
                    }
                    for (int i = 0; i < genNumbers.length; i++) {
                        genNumbers[i] = -1;
                    }

                    if (NumberGenerator.numberVerifier(rng, genNumbers, player.getDex())) {
                        System.out.println("O jogador desviou do ataque!");

                    } else if (enemy.getHp() <= 0) {
                        System.out.println("O inimigo foi derrotado! Jogador ganhou: " + enemy.getExp() + " de XP\n");
                        player.setExp(enemy.getExp());
                        if (player.getExp() > player.getLevel()) {
                            player.setLevel(player.getLevel() + 1);
                            player.setExp(0);
                            System.out.println(player.getName() + " Subiu para o nível: " + player.getLevel() + "!\n");

                            player.setNextLevel(player.getNextLevel() * 1.2);

                            PlayerRepo.updatePlayer(player);
                        }

                        ContextRepo.createContext("Inimigo: " + enemy.getName()
                                + " Foi derrotado, combate volta a ser FALSE e continue a historia contando como o inimigo morreu e qual é a próxima decisão do jogador");
                        contexto = ContextCreation.main();
                        contexto.setCombate(false);
                        break;
                    } else {
                        System.out.println("O " + enemy.getName() + "  infligiu " + enemyAtk + " de dano ao jogador");
                        player.setHp(player.getHp() - enemyAtk);
                        PlayerRepo.updatePlayer(player);
                    }

                    if (player.getHp() <= 0) {
                        System.out.println("Voce perdeu, ruim demais");
                        contexto.setCombate(false);
                        ContextRepo.createContext("Jogador: " + player.getName()
                                + " Foi derrotado, finalize a história demonstrando como o jogador morreu para o inimigo: "
                                + enemy.getName() + " que possuia a arma: " + enemy.getWeapon());

                        contexto = ContextCreation.main();

                        System.out.println(contexto.getDescription());

                        finalizar = true;
                        scanner.close();
                        return;
                    }
                    System.out.println(player.getName() + "HP: " + player.getHp());
                }
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
