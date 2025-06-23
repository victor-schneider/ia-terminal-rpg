package Model.Combat;

import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.Player;
import Repository.EnemyRepo;
import Repository.PlayerRepo;
import Utils.NumberGenerator;
import Utils.Verifiers;

public class PlayerAttackHandler implements CombatHandler {
  private CombatHandler next;

  @Override
  public void setNext(CombatHandler next) {
    this.next = next;
  }

  @Override
  public boolean handle(Context context, Enemy enemy, Player player, CombatVerifier combatVerifier) {
        System.out.println("entrou no Player Handler");
        int[] genNumbers = new int[100];
        for (int i = 0; i < genNumbers.length; i++) genNumbers[i] = -1;

        float atk = player.getAttack();
        int rng = NumberGenerator.main(100);

        // Lógica de ataque crítico
        if (NumberGenerator.numberVerifier(rng, genNumbers, player.getLck() + 10)) {
            atk *= 2;
            System.out.println("\n*** GOLPE CRÍTICO! ***");
            System.out.println(player.getName() + " acerta um golpe devastador!");
        }
        if (atk == player.getAtk()) {
            atk -= ((atk * enemy.getDef()) / 100);
            atk = Verifiers.roundNumbers(atk);
        }

        // Esquiva do inimigo
        if (NumberGenerator.numberVerifier(rng, genNumbers, enemy.getDex())) {
            System.out.println("\n>> O inimigo esquivou agilmente do seu ataque!");
        } else {
            enemy.setHp(Verifiers.roundNumbers(enemy.getHp() - atk));
            System.out.println("\n>> " + player.getName() + " infligiu " + atk + " de dano ao " + enemy.getName() + "!");
            EnemyRepo.updateEnemy(enemy);
        }

        
        // Passa para o próximo handler (ex: ataque do inimigo)
        if (next != null) {

          return next.handle(context, enemy, player, combatVerifier);
        }
        return next.handle(context, enemy, player, combatVerifier);
  }
}
