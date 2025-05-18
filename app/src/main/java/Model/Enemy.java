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
    System.out.println("Inimigo: " + enemy.getName() + "\nLevel: " + enemy.getLevel() + "\nArma: " + enemy.getWeapon() + "\nHP: " + enemy.getHp() + "\nATK: " + enemy.getAtk() + "\nDEF: " + enemy.getDef());
  }
}
