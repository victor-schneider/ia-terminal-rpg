package Components.PlayerComponents;

import java.util.EnumMap;
import java.util.Map;

import Components.Personagem;
import Components.PlayerComponents.Armor.Slot;

public class Player extends Personagem {
  private Weapon equippedWeapon;
  private Map<Slot, Armor> equippedArmor;
  private int exp;
  private int nextLevel;
  
  public Player(String name, int level, int hp, int id, int exp, int nextLevel ){
    super(name, level, hp, id);
    this.exp = exp;
    this.nextLevel = nextLevel;
    this.equippedArmor = new EnumMap<>(Slot.class);
  }

  public void equipWeapon(Weapon weapon){
    this.equippedWeapon = weapon;
    System.out.println(name + " equipou a arma: " + weapon.getName());
  }

  public void equipArmor(Armor armor){
    equippedArmor.put(armor.getSlot(), armor);
    System.out.println(name + " equipou a armadura: " + armor.getName() + " no slot: " + armor.getSlot());
  }

  public Weapon getEquippedWeapon() {
    return equippedWeapon;
  }

  public Armor getEquippedArmor(Slot slot) {
    return equippedArmor.get(slot);
  }

  public int getTotalDefense(){
    int totalDefense = 0;
    for(Armor armor : equippedArmor.values()) {
      totalDefense += armor.getDef();
    }
    return totalDefense;
  }

  public int getAttack() {
    int attack = 0;
    attack += equippedWeapon.getAtk();
    return attack;
  }

  public int getExp() {
    return exp;
  }

  public void setExp(int exp) {
    this.exp = exp;
  }

  public int getNextLevel() {
    return nextLevel;
  }

  public void setNextLevel(int nextLevel) {
    this.nextLevel = nextLevel;
  }
}
