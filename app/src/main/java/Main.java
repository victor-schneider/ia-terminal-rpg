// import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
// import dev.langchain4j.model.chat.ChatLanguageModel;

// import java.util.Scanner;
// import com.google.gson.Gson;

// Importação dos Schemas do DB
import Migration.EnemiesSchema;
import Migration.PlayerInvSchema;
import Migration.ContextSchema;

//Importação dos arquivos
// import Repository.ContextRepo;
// import Repository.EnemyRepo;
// import Repository.PlayerInventoryRepo;
// import Components.Enemy;
// import Components.PlayerComponents.Armor.Slot;
// import Manegement.EnemyCreation;

public class Main {
    public static void main(String[] args) {
        EnemiesSchema.initEnemiesDb();
        PlayerInvSchema.initPlayerInvDb();
        ContextSchema.initContextDb();
    }
}



