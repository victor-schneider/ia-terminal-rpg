// Importação dos Util
import java.util.Scanner;
import java.util.logging.ErrorManager;
import java.util.random.RandomGenerator;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Migration.PlayerSchema;
import Model.Context;
import Model.Enemy;
import Model.PlayerComponents.*;
import Model.PlayerComponents.Armor.Slot;
import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Migration.ContextSchema;

// Importação das classes Utils
import Utils.NumberGenerator;
import Utils.ClearConsole;
import Utils.Verifiers;
import Utils.NumberGenerator;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;

public class Main {
     public static void main(String[] args) {
        ContextSchema.dropTable();
        EnemiesSchema.dropTable();
        PlayerInvSchema.dropTable();

        PlayerSchema.dropTable();
        EnemiesSchema.initEnemiesDb();
        PlayerInvSchema.initPlayerInvDb();
        PlayerSchema.initPlayer();
        ContextSchema.initContextDb();

        Scanner scanner = new Scanner(System.in);
        
        Context context = new Context(null, null, null, null, null, 0);

        context.startGame();

        Boolean finalizar = false;

        do {
            // Se quiser triggar combate descomente o codigo abaixo
            // context.setCombat(false);
            ClearConsole.clearConsole();
            context.displayDescription();
            if(context.displayOptions()) finalizar = true;
            if(!finalizar){
                if(context.verifyContext()) finalizar = true;
                context.setArmor(false);
                context.setWeapon(false);
                context.createContext();
            } else {
                return;
            }
            

        } while (!finalizar);

        scanner.close();
    }
}
