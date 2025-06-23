package Model.Combat;

import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.Player;

public interface CombatHandler {
  void setNext(CombatHandler next);
  boolean handle(Context context, Enemy ennemy, Player player, CombatVerifier combatVerifier);
}
