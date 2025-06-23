package Model.Combat;

import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.Player;
import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Utils.NumberGenerator;
import Utils.Verifiers;

public class EnemyAttackHandler implements CombatHandler {
  
  private CombatHandler next;

  @Override
  public void setNext(CombatHandler next){
    this.next = next;
  }

  @Override
  public boolean handle(Context context, Enemy enemy, Player player, CombatVerifier combatVerifier){
    System.out.println("entrou no Enemy Handler");
    if(enemy.getHp() <= 0){
      if (next != null) return next.handle(context, enemy, player, combatVerifier);
      return false;
    }
    Float enemyAtk = enemy.getAtk();
    int[] genNumbers = new int[100];
    int rng = NumberGenerator.main(100);

    System.out.println("\n================== TURNO DO INIMIGO ==================");
    if (enemyAtk == enemy.getAtk()) {
        enemyAtk -= ((enemyAtk * player.getTotalDefense()) / 100);
        enemyAtk = Verifiers.roundNumbers(enemyAtk);
    }
    for (int i = 0; i < genNumbers.length; i++) {
      genNumbers[i] = -1;
    }

    if (NumberGenerator.numberVerifier(rng, genNumbers, player.getDex())) {
      System.out.println(">> Você esquivou do ataque do inimigo!");
      return next.handle(context, enemy, player, combatVerifier);
      
    } else {
      System.out.println(">> " + enemy.getName() + " ataca e inflige " + enemyAtk + " de dano em você!");
      player.setHp(Verifiers.roundNumbers(player.getHp() - enemyAtk));
      PlayerRepo.updatePlayer(player);

      return next.handle(context, enemy, player, combatVerifier);
    }
  }
}
