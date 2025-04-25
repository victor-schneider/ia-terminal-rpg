import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;
import com.google.gson.Gson;

import Migration.DatabaseSchema;
import Repository.EnemyRepo;
import Components.Enemy;
import Manegement.EnemyCreation;

public class Main {
    public static void main(String[] args) {
    DatabaseSchema.init();
    // EnemyRepo.createEnemy("Elf", 30, 50);
    // EnemyRepo.deleteEnemy(2);
    // EnemyRepo.updateEnemy(1, null, 15, 10);
    // EnemyRepo.getEnemy(1);
    // EnemyRepo.listEnemy();
    // new Main().executar();
    
    // EnemyCreation.main();

   }
  };



