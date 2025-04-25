import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;
import com.google.gson.Gson;

import Migration.DatabaseSchema;
import Repository.EnemyRepo;
import Components.Enemy;

public class Main {
    public static void main(String[] args) {
    DatabaseSchema.init();
    EnemyRepo.createEnemy("Elf", 30, 50);
    // EnemyRepo.deleteEnemy(2);
    // EnemyRepo.updateEnemy(1, null, 15, 10);
    // EnemyRepo.getEnemy(1);
    // EnemyRepo.listEnemy();
    // new Main().executar();

    Gson gson = new Gson();
    Enemy enemy = new Enemy("Goblin", 5, 10, 20, 2, "Machete");

    String json = gson.toJson(enemy);
    System.out.println("JSON gerado:\n" + json);

    String jsonInput = "{"
    + "\"name\":\"Goblin Guerreiro\","
    + "\"level\":5,"
    + "\"hp\":45,"
    + "\"atk\":12,"
    + "\"def\":6,"
    + "\"arma\":\"Clava de Pedra\""
    + "}";
    Enemy goblin = gson.fromJson(jsonInput, Enemy.class);
    System.out.println("Imigo desserializado: " + goblin.getName());
   }

    public void executar() {
      Scanner scanner = new Scanner(System.in);
  
      while (true) {
          System.out.println("Digite sua pergunta ou digite 'sair':");
  
          // 1) Verifica se há uma linha disponível. Se não houver, sai do loop.
          if (!scanner.hasNextLine()) {
              System.out.println("Nenhuma entrada detectada. Encerrando o programa.");
              break;
          }
  
          // 2) Agora sim lemos a linha
          String pergunta = scanner.nextLine().trim();
  
          // 3) Se for "sair" (ignora maiúsculas/minúsculas), sai do loop
          if (pergunta.equalsIgnoreCase("sair")) {
              System.out.println("Programa encerrado pelo usuário.");
              break;
          }
  
          // 4) Gera a resposta
          ChatLanguageModel model = VertexAiGeminiChatModel.builder()
              .project(System.getenv("PROJECT_ID"))
              .location(System.getenv("LOCATION"))
              .modelName("gemini-1.5-flash-002")
              .build();
  
          String resposta = model.generate(pergunta);
          System.out.println("Resposta: " + resposta);
      }
  
      scanner.close();
    }
  };



