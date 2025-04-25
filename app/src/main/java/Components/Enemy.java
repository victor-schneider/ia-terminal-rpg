package Components;

public class Enemy extends Personagem {
  int atk;
  int def;

  public Enemy(String name, int level, int hp, int atk, int def, String weapon) {
    super.name = name;
    super.level = level;
    super.weapon = weapon;
    super.hp = hp;
    this.atk = atk;
    this.def = def;
  }

  public int getAtk() {
    return atk;
  }

  public int getDef() {
    return def;
  }
}
