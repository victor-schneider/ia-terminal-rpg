import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;
import com.google.gson.Gson;

import Migration.EnemiesDatabaseSchema;
import Migration.PlayerInventoryDatabaseSchema;
import Repository.EnemyRepo;
import Repository.PlayerInventoryRepo;
import Components.Enemy;
import Components.PlayerComponents.*;
import Components.PlayerComponents.Armor.Slot;
import Manegement.EnemyCreation;

public class Main {
    public static void main(String[] args) {
    EnemiesDatabaseSchema.initEnemiesDb();
    PlayerInventoryDatabaseSchema.initPlayerInventoryDb();
   }
  };



