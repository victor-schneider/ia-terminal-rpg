import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;

import Migration.DatabaseSchema;
import Repository.EnemyRepo;

public class Main {
    public static void main(String[] args) {
    DatabaseSchema.init();
    EnemyRepo.createEnemy("Elf", 30, 50);
    // EnemyRepo.deleteEnemy(2);
    // EnemyRepo.updateEnemy(1, null, 15, 10);
    // EnemyRepo.getEnemy(1);
    // EnemyRepo.listEnemy();
    // new Main().executar();
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



