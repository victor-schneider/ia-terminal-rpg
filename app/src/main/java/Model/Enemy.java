package Model;

public class Enemy extends Personagem {
  String weapon;

  public Enemy(String name, int level, float hp, int id, float atk, float def, float dex, int exp, String weapon) {
    super(name, level, hp, atk, def, dex, exp, id);
    this.weapon = weapon;
  }

  public String getWeapon(){
    return weapon;
  }

  public void setWeapon(String weapon) {
    this.weapon = weapon;
  }

  public void getEnemyStatus(Enemy enemy) {
    System.out.println("===== STATUS DO INIMIGO =====");
    System.out.println("Nome  : " + enemy.getName());
    System.out.println("Level : " + enemy.getLevel());
    System.out.println("Arma  : " + enemy.getWeapon());
    System.out.println("HP    : " + enemy.getHp());
    System.out.println("ATK   : " + enemy.getAtk());
    System.out.println("DEF   : " + enemy.getDef());
    System.out.println("=============================");
  }
}
