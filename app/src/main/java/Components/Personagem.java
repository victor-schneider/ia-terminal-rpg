package Components;

public class Personagem {
  protected String name;
  protected int level;
  protected float hp;
  protected float atk;
  protected float def;
  protected float dex;
  protected float exp;
  protected int id;

  public Personagem(String name, int level, float hp, float atk, float def, float dex, float exp, int id){
    this.name = name;
    this.level = level;
    this.hp = hp;
    this.atk = atk;
    this.def = def;
    this.dex = dex;
    this.exp = exp;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public float getHp() {
    return hp;
  }

  public int getId() {
    return id;
  }

  public float getDex() {
    return dex;
  }

  public float getExp() {
    return exp;
  }

  public float getAtk() {
    return atk;
  }

  public float getDef() {
    return def;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setHp(float hp) {
    this.hp = hp;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDex(float dex) {
    this.dex = dex;
  }

  public void setExp(float exp) {
    this.exp = exp;
  }

  public void setAtk(float atk) {
    this.atk = atk;
  }

  public void setDef(float def) {
    this.def = def;
  }
}
