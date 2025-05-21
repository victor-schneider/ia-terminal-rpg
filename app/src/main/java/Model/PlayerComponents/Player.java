package Model.PlayerComponents;

import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

import Model.Personagem;
import Model.PlayerComponents.Armor.Slot;
import Repository.PlayerInvRepo;
import Utils.ClearConsole;
import Utils.Verifiers;

public class Player extends Personagem {
  private Weapon equippedWeapon;
  private Map<Slot, Armor> equippedArmor;
  private double nextLevel;
  private float lck;
  
  public Player(String name, float hp, float atk, float def, float dex, float lck, int level, float exp, double nextLevel, int id){
    super(name, level, hp, atk, def, dex, exp, id);
    this.exp = exp;
    this.lck = lck;
    this.nextLevel = nextLevel;
    this.equippedArmor = new EnumMap<>(Slot.class);
  }

  public void equipWeapon(Weapon weapon){
    if(getEquippedWeapon() == null) {
      weapon.setEquipped(true);
      this.equippedWeapon = weapon;
      PlayerInvRepo.updateWeapon(weapon);
      //System.out.println("Você equipou a arma: " + weapon.getName() + "\n");
      return;

    } else if(getEquippedWeapon().equals(weapon)) {
        System.out.println("A arma: " + weapon.getName() + " já está equipada");
        return;
  
      } else if(getEquippedWeapon().getEquipped()) {

        System.out.println("Você desequipou: " + getEquippedWeapon().getName());
        getEquippedWeapon().setEquipped(false);
        PlayerInvRepo.updateWeapon(getEquippedWeapon());
        System.out.println("Arma " + getEquippedWeapon().getName());
  
        this.equippedWeapon = weapon;
        weapon.setEquipped(true);
        PlayerInvRepo.updateWeapon(weapon);
        //System.out.println("Você equipou a arma: " + weapon.getName());
        return;
  
      } else {
        weapon.setEquipped(true);
        this.equippedWeapon = weapon;
        PlayerInvRepo.updateWeapon(weapon);
        //System.out.println("Você equipou a arma: " + weapon.getName() + "\n");
      }
    }

  public void unequipWeapon(Weapon weapon) {
    if(!weapon.getEquipped()) {
      System.out.println("A Arma ja esta desequipada!");
      return;
    }

    getEquippedWeapon().setEquipped(false);
    PlayerInvRepo.updateWeapon(getEquippedWeapon());
    System.out.println(getName() + " desequipou " + weapon.getName());
  }

  public void equipArmor(Armor armor){
    if(getEquippedArmor(armor.getSlot()) != null) {
      System.out.println("Você desequipou: " + getEquippedArmor(armor.getSlot()).getName());
      getEquippedArmor(armor.getSlot()).setEquipped(false);
      PlayerInvRepo.updateArmor(getEquippedArmor(armor.getSlot()));
      //System.out.println("Armadura " + getEquippedArmor(armor.getSlot()).getName() + " foi equipada!");

      equippedArmor.put(armor.getSlot(), armor);
      armor.setEquipped(true);
      PlayerInvRepo.updateArmor(armor);
      //System.out.println("Você equipou a Armadura: " + armor.getName() + "\n");
      return;
    }

    if(getEquippedArmor(armor.getSlot()) != null) {
      System.out.println("A Armadura: " + armor.getName() + " já está equipada");
      return;
    } else {
      equippedArmor.put(armor.getSlot(), armor);
      armor.setEquipped(true);
      PlayerInvRepo.updateArmor(armor);
      //System.out.println("Você equipou a Armadura: " + armor.getName() + "\n");
    }
  }

  public void unequipArmor( Armor armor) {
    if (!armor.getEquipped()) {
      System.out.println("Esta armadura já está desequipada!");
      return;
    }

    getEquippedArmor(armor.getSlot()).setEquipped(false);
    PlayerInvRepo.updateArmor(getEquippedArmor(armor.getSlot()));
    System.out.println(getName() + " desequipou " + armor.getName());
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
    attack += equippedWeapon.getAtk() + atk;
    return attack;
  }

  public void setExp(int exp) {
    this.exp = exp;
  }

  public double getNextLevel() {
    return nextLevel;
  }

  public void setNextLevel(double nextLevel) {
    this.nextLevel = nextLevel;
  }

  public float getLck() {
    return lck;
  }

  public void setLck(float lck) {
    this.lck = lck;
  }

  public String getStatus() {
    return "Atk: " + getAttack() + "Def: " + getTotalDefense() + " Hp: " + getHp() + " level: " + getLevel() + "Name: " + getName();
  }

  public void inventory() {
    Scanner scanner = new Scanner(System.in);

    int id = -1, respostaT = 0;
    while (respostaT != 3) {
        ClearConsole.clearConsole();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║         INVENTÁRIO                 ║");
        System.out.println("╠════════════════════════════════════╣");
        PlayerInvRepo.listItems();
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ [1] Equipar um item                ║");
        System.out.println("║ [2] Desequipar um item             ║");
        System.out.println("║ [3] Sair do inventário             ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("Escolha uma opção: ");
        respostaT = scanner.nextInt();

        switch (respostaT) {
            case 1:
                System.out.print("\n[0] - Voltar\nDigite o ID do item para EQUIPAR: ");
                id = scanner.nextInt();

                if (id == 0) break;
                id = Verifiers.itemIdVerifier(id);
                if (id == 0) break;

                Item item = PlayerInvRepo.getItem(id);

                if (item.getType().equals("WEAPON")) {
                    Weapon weapon = PlayerInvRepo.getWeapon(id);

                    while (id != 0 && weapon.getEquipped()) {
                        System.out.println("Esta arma já está equipada! Digite outro ID ou 0 para voltar:");
                        id = scanner.nextInt();

                        if (id == 0) break;
                        id = Verifiers.weaponIdVerifier(id);
                        if (id == 0) break;
                        weapon = PlayerInvRepo.getWeapon(id);
                    }

                    equipWeapon(weapon);
                    System.out.println("Pressione ENTER para continuar...");
                    scanner.nextLine(); scanner.nextLine();
                } else if (item.getType().equals("ARMOR")) {
                    Armor armor = PlayerInvRepo.getArmor(id);

                    while (id != 0 && armor.getEquipped()) {
                        System.out.println("Esta armadura já está equipada! Digite outro ID ou 0 para voltar:");
                        id = scanner.nextInt();

                        if (id == 0) break;
                        id = Verifiers.armorIdVerifier(id);
                        if (id == 0) break;

                        armor = PlayerInvRepo.getArmor(id);
                    }

                    equipArmor(armor);
                    System.out.println("Pressione ENTER para continuar...");
                    scanner.nextLine(); scanner.nextLine();
                }
                break;

            case 2:
                ClearConsole.clearConsole();
                System.out.println("╔════════════════════════════════════╗");
                System.out.println("║        ITENS EQUIPADOS            ║");
                System.out.println("╠════════════════════════════════════╣");
                PlayerInvRepo.listEquippedItems();
                System.out.println("╚════════════════════════════════════╝");
                System.out.println("\n[0] - Voltar\nDigite o ID do item para DESEQUIPAR:");
                id = scanner.nextInt();

                if (id == 0) break;
                id = Verifiers.itemIdVerifier(id);
                if (id == 0) break;

                item = PlayerInvRepo.getItem(id);

                if (item.getType().equals("WEAPON")) {
                    Weapon weapon = PlayerInvRepo.getWeapon(id);

                    while (!weapon.getEquipped() && id != 0) {
                        System.out.println("Esta arma já está desequipada! Digite outro ID ou 0 para voltar:");
                        id = scanner.nextInt();

                        if (id == 0) break;
                        id = Verifiers.weaponIdVerifier(id);
                        if (id == 0) break;

                        weapon = PlayerInvRepo.getWeapon(id);
                    }
                    if (id == 0) break;

                    unequipWeapon(weapon);
                    System.out.println("Pressione ENTER para continuar...");
                    scanner.nextLine(); scanner.nextLine();

                } else {
                    Armor armor = PlayerInvRepo.getArmor(id);

                    while (!armor.getEquipped() && id != 0) {
                        System.out.println("Esta armadura já está desequipada! Digite outro ID ou 0 para voltar:");
                        id = scanner.nextInt();

                        if (id == 0) break;
                        id = Verifiers.armorIdVerifier(id);
                        if (id == 0) break;

                        armor = PlayerInvRepo.getArmor(id);
                    }
                    if (id == 0) break;

                    unequipArmor(armor);
                    System.out.println("Pressione ENTER para continuar...");
                    scanner.nextLine(); scanner.nextLine();
                }
                break;

            case 3:
                ClearConsole.clearConsole();
                System.out.println("Saindo do inventário...");
                return;
        }
    }
  }
}
