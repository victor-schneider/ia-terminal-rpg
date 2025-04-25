package Components;

public class Enemy extends Personagem {
  int atk;
  int def;
  String arma;

  public Enemy(String name, int level, int hp, int atk, int def, String arma) {
    super.name = name;
    super.level = level;
    super.hp = hp;
    this.atk = atk;
    this.def = def;
    this.arma = arma;
  }
}
