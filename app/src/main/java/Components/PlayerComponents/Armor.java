package Components.PlayerComponents;

public class Armor extends Item{
  private int def;
  public enum Slot { HELMET, CHEST, LEGS, BOOTS }
  private Slot slot;

  public Armor(String name, Slot slot, int def){
    super(name);
    this.slot = slot;
    this.def = def;
  }

  public Slot getSlot() {
    return slot;
  }

  public void setSlot(Slot slot){
    this.slot = slot;
  }

  public int getDef() {
    return def;
  }

  public void setDef(int def){
    this.def = def;
  }
}
