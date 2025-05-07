package Gameplay;
import Components.PlayerComponents.Player;
import Components.PlayerComponents.Armor;
import Components.PlayerComponents.Item;
import Components.PlayerComponents.Player;
import Components.PlayerComponents.Weapon;
import Repository.PlayerInvRepo;
import Utils.ClearConsole;
import Utils.Verifiers;

import java.util.Scanner;

public class Inventory {
  public static void main (Player player) {
    Scanner scanner = new Scanner(System.in);

    int id = -1, respostaT = 0;       
    while (respostaT != 3) {
      ClearConsole.clearConsole();
      System.out.println("===== INVENTÁRIO =====");
      PlayerInvRepo.listItems();
      System.out.println("[1] - Equipar um item");
      System.out.println("[2] - Desequipar um item");
      System.out.println("[3] - Sair do inventario");
      respostaT = scanner.nextInt();
      switch (respostaT) {
          case 1:
                  System.out.println("\n[0] - Sair do inventario \nSelecione o ID do item a ser equipado: ");
                  id = scanner.nextInt();
                  
                  if (id == 0) break;
                  id = Verifiers.itemIdVerifier(id);
                  if (id == 0) break;
                  
                  Item item = PlayerInvRepo.getItem(id);

                  if (item.getType().equals("WEAPON")) {
                      Weapon weapon = PlayerInvRepo.getWeapon(id);

                      while (id != 0 && weapon.getEquipped()) {
                          System.out.println("A arma ja esta equipada, digite outro ID ou 0 para sair");
                          id = scanner.nextInt();

                          if(id == 0) break;
                          id = Verifiers.weaponIdVerifier(id);
                          if(id == 0) break;
                          weapon = PlayerInvRepo.getWeapon(id);
                      };

                      player.equipWeapon(weapon);
                  } else if(item.getType().equals("ARMOR")) {
                      Armor armor = PlayerInvRepo.getArmor(id);

                      while (id != 0 && armor.getEquipped()) {
                          System.out.println("A armadura já está equipada, digite outro ID ou 0 para sair");
                          id = scanner.nextInt();

                          if(id == 0) break;
                          id = Verifiers.armorIdVerifier(id);
                          if(id == 0) break;

                          armor = PlayerInvRepo.getArmor(id);
                      }

                      player.equipArmor(armor);
                  }
              break;

          case 2:
          ClearConsole.clearConsole();
          System.out.println("===== ITEMS EQUIPADOS =====");
              PlayerInvRepo.listEquippedItems();
              System.out.println("\n[0] - Sair do inventario \nSelecione o ID do item a ser desequipado: ");
              id = scanner.nextInt();

              if (id == 0) break; 
              id = Verifiers.itemIdVerifier(id);
              if (id == 0) break;

              item = PlayerInvRepo.getItem(id);

              if (item.getType().equals("WEAPON")) {
                  Weapon weapon = PlayerInvRepo.getWeapon(id);

                  while( !weapon.getEquipped() && id != 0) {
                      System.out.println("A arma já está desequipada. Digite outro ID ou 0 para sair.");
                      id = scanner.nextInt();
                      
                      if(id == 0) break;
                      id = Verifiers.weaponIdVerifier(id);
                      if(id == 0) break;

                      weapon = PlayerInvRepo.getWeapon(id);
                  }
                  if(id == 0) break;

                  player.unequipWeapon(weapon);

              } else {
                  Armor armor = PlayerInvRepo.getArmor(id);

                  while( !armor.getEquipped() && id != 0) {
                      System.out.println("A armadura já está desequipada. Digite outro ID ou 0 para sair.");
                      id = scanner.nextInt();

                      if(id == 0) break;
                      id = Verifiers.armorIdVerifier(id);
                      if(id == 0) break;

                      armor = PlayerInvRepo.getArmor(id);
                  }
                  if(id == 0) break;

                  player.unequipArmor(armor);
              }
              break;

          case 3:
              break;
      }

    }
   }
  }
