// import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
// import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;
// import com.google.gson.Gson;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Repository.ContextRepo;
import Migration.ContextSchema;

//Importação dos arquivos
// import Repository.ContextRepo;
// import Repository.EnemyRepo;
// import Repository.PlayerInventoryRepo;
// import Components.Enemy;
// import Components.PlayerComponents.Armor.Slot;
import Components.Context;
// import Manegement.EnemyCreation;
import Manegement.ContextCreation;

public class Main {
    public static void main(String[] args) {
        ContextSchema.dropContext();
        EnemiesSchema.initEnemiesDb();
        PlayerInvSchema.initPlayerInvDb();
        ContextSchema.initContextDb();

        Context c;
        Boolean finalizar = false;
        Scanner scanner = new Scanner(System.in);
        int resposta = 0;

        do {
            c = ContextCreation.main();
            System.out.println(c.getDescription() + "\n");

            for(int i = 0; i < c.getOptions().size(); i++){
                System.out.println("Opção: " + (i + 1) + " " + c.getOptions().get(i) + "\n");
            }
            System.out.println("4 - Finalizar jogo? " + "\n");
            resposta = scanner.nextInt();

            if(resposta == 4) {
                finalizar = true;
            } else {
                ContextRepo.createContext(c.getOptions().get(resposta - 1));
            }
        } while (!finalizar);

        scanner.close();
    }
}



