package Model.Combat;

import Repository.PlayerRepo;
import Repository.ContextRepo;
import Repository.EnemyRepo;

import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.Player;

public class VictoryCheckHandler implements CombatHandler{
  private CombatHandler next;

  @Override
  public void setNext(CombatHandler next) {
    this.next = next;
  }

  @Override
  public boolean handle(Context context, Enemy enemy, Player player, CombatVerifier combatVerifier) {
    if (player.getHp() <= 0) {
      System.out.println("\n================== DERROTA ==================");
      System.out.println("Você foi derrotado por " + enemy.getName() + "...");
      context.setCombat(false);

      ContextRepo.createContext("Jogador: " + player.getName() + " Foi derrotado, finalize a história demonstrando como o jogador morreu para o inimigo: " + enemy.getName() + " que possuía a arma: " + enemy.getWeapon());

      context.setCombat(false);
      context.createContext();

      System.out.println(context.getDescription());
      System.out.println("=============================================\n");
      combatVerifier.setEnemyWon(true);
      return true;

    } else if (enemy.getHp() <= 0){
      System.out.println("\n================== VITÓRIA! ==================");
      System.out.println("O inimigo " + enemy.getName() + " foi derrotado!");
      System.out.println("Você ganhou " + enemy.getExp() + " de XP.");
      player.setExp(player.getExp() + enemy.getExp());

      if (player.getExp() > player.getLevel()) {
        player.setLevel(player.getLevel() + 1);
        player.setExp(0);
        System.out.println("\n*** " + player.getName() + " subiu para o nível " + player.getLevel() + "! ***");
        player.setNextLevel(player.getNextLevel() * 1.2);
        PlayerRepo.updatePlayer(player);

      }

      ContextRepo.createContext("Inimigo: " + enemy.getName() + " Foi derrotado, combate volta a ser FALSE e continue a historia contando como o inimigo morreu e qual é a próxima decisão do jogador");
      context.setCombat(false);
      

      System.out.println("==============================================\n");
      combatVerifier.setPlayerWon(true);
      return false;
    }

    return false;

  }
}
