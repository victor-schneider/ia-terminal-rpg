package Components;

public class Personagem {
  protected String name;
  protected int level;
  protected int hp;
  protected int id;

  public Personagem(String name, int level, int hp, int id){
    this.name = name;
    this.level = level;
    this.hp = hp;
    this.id = id;
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

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public void setId(int id) {
    this.id = id;
  }
}
