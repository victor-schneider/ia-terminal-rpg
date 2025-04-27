package Manegement;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import Components.Enemy;
import Repository.EnemyRepo;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class EnemyCreation {
  public static Enemy main() {
    Gson gson = new Gson();
    int id;

    ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-flash-002")
            .maxOutputTokens(500)
            .temperature(2f)
            .build();

    PromptTemplate promptTemplate = PromptTemplate.from(
      "Você irá criar um inimigo para um jogo de RPG de turnos. " +
      "Esse RPG é baseado em Dungeons and Dragons. " +
      "O inimigo que você irá criar possuirá os seguintes componentes: " +
      "Name, Level, HP, ATK, DEF, Weapon. " +
      "Você irá criá-lo num formato JSON puro, baseado na biblioteca GSON da Google, seguindo este modelo: " +
      "{\"name\":\"Goblin Guerreiro\",\"level\":5,\"hp\":45,\"atk\":12,\"def\":6,\"weapon\":\"Clava de Pedra\"}. " +
      "Você só irá retornar o JSON puro, sem nenhuma outra palavra ou markdown, somente string."
    );

    Map<String, Object> variables = new HashMap<>();

    Prompt prompt = promptTemplate.apply(variables);

    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    System.out.println(response.content().text());

    String wrappedJson = response.content().text().trim();

    Enemy e = gson.fromJson(wrappedJson, Enemy.class);

    id = EnemyRepo.createEnemy(e.getName(), e.getLevel(), e.getWeapon(), e.getHp(), e.getAtk(), e.getDef());

    EnemyRepo.getEnemy(id);

    return e;
  }
}
