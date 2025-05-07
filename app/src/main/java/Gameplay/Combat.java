package Gameplay;

import Components.Context;
import Components.Enemy;
import Components.PlayerComponents.Player;
import Manegement.ContextCreation;
import Manegement.EnemyCreation;
import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Utils.NumberGenerator;
import Utils.Verifiers;
import java.util.Scanner;

public class Combat {
  public static boolean main (Context contexto, Player player, Enemy enemy, int genNumbers[], boolean finalizar) {
    float atk = player.getAttack();
    Scanner scanner = new Scanner(System.in);
    
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
                            atk = Verifiers.roundNumbers(atk);
                        }

                        if (NumberGenerator.numberVerifier(rng, genNumbers, enemy.getDex())) {
                            System.out.println("O inimigo desviou do ataque!");
                        } else {
                            enemy.setHp( Verifiers.roundNumbers( enemy.getHp() - atk ) );
                            System.out.println(player.getName() + " Infligiu " + atk + " de dano ao " + enemy.getName());
                            EnemyRepo.updateEnemy(enemy);
                            System.out.println(enemy.getName() + " HP: " + enemy.getHp());
                        }
                    }

                    if (enemyAtk == enemy.getAtk()) {
                        enemyAtk -= ((enemyAtk * player.getTotalDefense()) / 100);
                        enemyAtk = Verifiers.roundNumbers(enemyAtk);
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

                        ContextRepo.createContext("Inimigo: " + enemy.getName() + " Foi derrotado, combate volta a ser FALSE e continue a historia contando como o inimigo morreu e qual é a próxima decisão do jogador");
                        contexto = ContextCreation.main();
                        contexto.setCombate(false);
                        break;
                    } else {
                        System.out.println("O " + enemy.getName() + "  infligiu " + enemyAtk + " de dano ao jogador");
                        player.setHp( Verifiers.roundNumbers( player.getHp() - enemyAtk) );
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
                        return finalizar;
                    }
                    System.out.println(player.getName() + "HP: " + player.getHp());
                }
    return false;
  }
}
