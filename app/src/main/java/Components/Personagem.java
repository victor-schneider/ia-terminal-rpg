package Components;

public class Personagem {
  protected String name;
  protected int level;
  protected int hp;

  public Personagem(String name, int level, int hp){
    this.name = name;
    this.level = level;
    this.hp = hp;
  }

  public String getName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public int getHp() {
    return hp;
  }
}
