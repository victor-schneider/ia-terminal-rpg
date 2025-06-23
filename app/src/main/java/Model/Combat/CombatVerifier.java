package Model.Combat;

public class CombatVerifier {
  public boolean playerWon = false;
  public boolean enemyWon = false;

  void setPlayerWon(boolean playerWon) {
    this.playerWon = playerWon;
  }

  void setEnemyWon(boolean enemyWon) {
    this.enemyWon = enemyWon;
  }
}
