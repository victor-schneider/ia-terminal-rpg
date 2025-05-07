package Utils;

import Components.PlayerComponents.Item;
import Repository.PlayerInvRepo;

import java.util.Scanner;

public class Verifiers {
  public static int itemIdVerifier(int id) {
    Scanner scanner = new Scanner(System.in);
    Item item = PlayerInvRepo.getItem(id);
    if(item == null) System.out.println("O item é nulo");

    while (item == null) {
      System.out.println("ID inválido, digite novamente, Selectione 0 para sair do inventário");
      id = scanner.nextInt();

      if(id == 0) {
        return 0; 
      }

      item = PlayerInvRepo.getItem(id);
    }

    return id;
  }

  public static int weaponIdVerifier(int id) {
    Scanner scanner = new Scanner(System.in);
    Item item = PlayerInvRepo.getWeapon(id);

    while (item == null) {
      System.out.println("ID inválido, digite novamente, Selectione 0 para sair do inventário");
      id = scanner.nextInt();

      if(id == 0) {
        return 0; 
      }

      item = PlayerInvRepo.getWeapon(id);
    }

    return id;
  }

  public static int armorIdVerifier(int id) {
    Scanner scanner = new Scanner(System.in);
    Item item = PlayerInvRepo.getArmor(id);

    while (item == null) {
      System.out.println("ID inválido, digite novamente, Selectione 0 para sair do inventário");
      id = scanner.nextInt();

      if(id == 0) {
        return 0; 
      }

      item = PlayerInvRepo.getArmor(id);
    }

    return id;
  }

  public static float roundNumbers (float number) {
    number = Math.round(number * 100f) / 100f;
    return number;
  }
}
