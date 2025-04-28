package Components.PlayerComponents;

import java.util.EnumMap;
import java.util.Map;

import Components.Personagem;
import Components.PlayerComponents.Armor.Slot;
import Repository.PlayerInvRepo;

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
    if(getEquippedWeapon() != null) {
      System.out.println(name + " desequipou: " + getEquippedWeapon().getName());
      getEquippedWeapon().setEquipped(false);
      PlayerInvRepo.updateWeapon(getEquippedWeapon());
      System.out.println("Arma " + getEquippedWeapon().getName() + " equipado?: " + getEquippedWeapon().getEquipped());

      this.equippedWeapon = weapon;
      weapon.setEquipped(true);
      PlayerInvRepo.updateWeapon(weapon);
      System.out.println(name + " equipou a arma: " + weapon.getName() + "\n" + "Equipado?: " + weapon.getEquipped());
      return;
    }

    if(getEquippedWeapon() != null) {
      System.out.println("A arma: " + weapon.getName() + " já está equipada");
      return;
    } else {
      this.equippedWeapon = weapon;
      weapon.setEquipped(true);
      PlayerInvRepo.updateWeapon(weapon);
      System.out.println(name + " equipou a arma: " + weapon.getName() + "\n" + "Equipado?: " + weapon.getEquipped());
    }
  }

  public void equipArmor(Armor armor){
    if(getEquippedArmor(armor.getSlot()) != null) {
      System.out.println(name + " desequipou: " + getEquippedArmor(armor.getSlot()).getName());
      getEquippedArmor(armor.getSlot()).setEquipped(false);
      PlayerInvRepo.updateArmor(getEquippedArmor(armor.getSlot()));
      System.out.println("Armadura " + getEquippedArmor(armor.getSlot()).getName() + " equipado?: " + getEquippedArmor(armor.getSlot()).getEquipped());

      equippedArmor.put(armor.getSlot(), armor);
      armor.setEquipped(true);
      PlayerInvRepo.updateArmor(armor);
      System.out.println(name + " equipou a Armadura: " + armor.getName() + "\n" + "Equipado?: " + armor.getEquipped());
      return;
    }

    if(getEquippedArmor(armor.getSlot()) != null) {
      System.out.println("A Armadura: " + armor.getName() + " já está equipada");
      return;
    } else {
      equippedArmor.put(armor.getSlot(), armor);
      armor.setEquipped(true);
      PlayerInvRepo.updateArmor(armor);
      System.out.println(name + " equipou a Armadura: " + armor.getName() + "\n" + "Equipado?: " + armor.getEquipped());
    }
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
