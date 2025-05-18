package Utils;

import java.util.Random;

import Model.PlayerComponents.Player;

import java.util.Arrays;

public class NumberGenerator {
  public static int main(int max) {
    Random random = new Random();

    int rng = random.nextInt(max - 0) + 1;
    return rng;
  }

  public static boolean numberVerifier(int rng, int[] genNumbers, float playerLck) {
    int numGen = 0;
    Boolean crit = false;
    Boolean found = false;
    while (playerLck > 0 && crit == false) {
      numGen = NumberGenerator.main(100); // 9
      // System.out.println("Numero gerado antes de tudo: " + numGen);

      if(numGen == rng) { // 9 != 4
        crit = true;
        return crit;

      } else if(genNumbers[0] == -1) {
        genNumbers[0] = numGen; // 7
        // System.out.println("O vetor foi inicializado com o numero: " + numGen);
      } else {
        int aux = 0;

        while(!found && aux < 100) {
          for(int i = 0; i < genNumbers.length; i++) {
            if(genNumbers[i] == numGen) {
              // System.out.println("O número: " + numGen + " É repetido!");
              found = true;
              playerLck++;
              break;
            } 
          }
          aux++;
         
        }
        if(!found) {
          for(int i = 0; i < genNumbers.length; i++) {
            if(genNumbers[i] == -1) {
              genNumbers[i] = numGen;
              break;
            }
          }
        }
      }
      if(!found) {
      }
      playerLck--;
      found = false;
     }
    return crit;
  }
}
