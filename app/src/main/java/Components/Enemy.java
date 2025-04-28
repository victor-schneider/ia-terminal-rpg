package Components;

public class Enemy extends Personagem {
  String weapon;
  int atk;
  int def;

  public Enemy(String name, int level, int hp, int id, int atk, int def, String weapon) {
    super(name, level, hp, id);
    this.weapon = weapon;
    this.atk = atk;
    this.def = def;
  }

public String getWeapon(){
  return weapon;
}

  public int getAtk() {
    return atk;
  }

  public int getDef() {
    return def;
  }
}
