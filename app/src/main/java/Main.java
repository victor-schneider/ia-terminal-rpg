import java.util.Scanner;
// import java.util.List;
// import java.util.ArrayList;
// import com.google.gson.Gson;

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
import Components.Enemy;
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
        EnemiesSchema.dropTable();
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

        Weapon espada = new Weapon(5, "Espada de ferro", false, 1, "WAEPON");
        PlayerInvRepo.createWeapon(espada);
        player.equipWeapon(espada);

        Context contexto;
        Boolean finalizar = false;
        Scanner scanner = new Scanner(System.in);
        int resposta = 0;

        do {
            contexto = ContextCreation.main();
            System.out.println(contexto.getDescription() + "\n");

            for(int i = 0; i < contexto.getOptions().size(); i++){
                System.out.println("Opção: " + (i + 1) + " " + contexto.getOptions().get(i) + "\n");
            }
            System.out.println("4 - Finalizar jogo? " + "\n");
            resposta = scanner.nextInt();

            if(contexto.getCombate() && resposta == 1) {
                Enemy enemy = EnemyCreation.main(player, contexto.getDescription());
                System.out.println("Combate!\n\n");
                enemy.getEnemyStatus(enemy);

                while(contexto.getCombate()) {
                    int opcao = 0;

                    if(enemy.getHp() <= 0) {
                        System.out.println("O inimigo foi derrotado! Jogador ganhou x de XP");
                        // TODO adicionar o quanto de xp o jogador vai receber pela morte do inimigo.
                        // TODO Talvez adicionar na tabela do inimigo o quanto de xp ele vai dropar
                        ContextRepo.createContext("Inimigo: " + enemy.getName() + " Foi derrotado, combate volta a ser FALSE e continue a historia");
                        contexto = ContextCreation.main();
                        contexto.setCombate(false);
                        break;
                    }

                    System.out.println("Você decidie: \n[1] - Atacar");
                    opcao = scanner.nextInt();

                    if(opcao == 1) {
                        enemy.setHp(enemy.getHp() - player.getAttack());
                        System.out.println(player.getName() + " Infligiu " + player.getAttack() + " de dano ao " + enemy.getName());
                        EnemyRepo.updateEnemy(enemy);
                        System.out.println(enemy.getName() + " HP: " + enemy.getHp() );
                    }

                    System.out.println("O " + enemy.getName() + " infligiu " + enemy.getAtk() + " de dano ao jogador");
                    player.setHp(player.getHp() - enemy.getAtk());
                    PlayerRepo.updatePlayer(player);
                    if (player.getHp() <= 0) {
                        System.out.println("Voce perdeu, ruim demais");
                        contexto.setCombate(false);
                        ContextRepo.createContext("Jogador: " + player.getName() + " Foi derrotado, finalize a história demonstrando como o jogador morreu para o inimigo: " + enemy.getName() + " que possuia a arma: " + enemy.getWeapon());
                        
                        contexto = ContextCreation.main();

                        System.out.println(contexto.getDescription());

                        finalizar = true;
                        scanner.close();
                        return;
                    }
                    System.out.println(player.getName() + "HP: " + player.getHp());
                }
            }

            // TODO tentar entender porque a AI ta respondendo a mesma coisa duas vezes e tirar esse systemout do if de derrotado
            if(resposta == 4) {
                finalizar = true;
            } else if(!finalizar) {
                ContextRepo.createContext(contexto.getOptions().get(resposta - 1));
            }
        } while (!finalizar);

        scanner.close();
     }
}
