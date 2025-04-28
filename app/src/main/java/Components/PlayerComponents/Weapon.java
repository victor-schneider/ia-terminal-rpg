package Components.PlayerComponents;

public class Weapon extends Item {
  private int atk;

  public Weapon(int atk, String name, Boolean equipped, int id, String type) {
    super(name, equipped, id, type);
    this.atk = atk;
  }

  public void setAtk(int atk){
    this.atk = atk;
  }
  public int getAtk(){
    return atk;
  }
}
