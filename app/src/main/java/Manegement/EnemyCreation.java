package Manegement;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import Components.Enemy;
import Components.PlayerComponents.*;
import Repository.EnemyRepo;
import Utils.JsonCleaner;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class EnemyCreation {
  public static Enemy main(Player player, String contexto) {
    Gson gson = new Gson();
    String playerStats = null;

    playerStats += "ATK: " + player.getAttack() + "DEF: " + player.getTotalDefense() + "Level: " + player.getLevel();

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
      "Você irá receber o seguinte contexto para criar o inimigo {{contexto}}. Além disso, as informações atuais do jogador: {{jogador}}" +
      "Se baseie nesse contexto e nas informações fornecidas para criar um inimigo que esteja num nivel parecido com a do personagem, então se por exemplo, o inimigo tiver 5 de atk, significa que ele causará 5 de dano nos pontos de HP do jogador e vice versa. Os pontos de defesa causará com que o inimigo receba em certa porcentagem a menos de dano. 10 pontos de defesa signifcam menos 10% de dano. Leve tudo isso em consideração, junto com o contexto na hora de criar o inimigo e seus status" +
      "Você irá criá-lo num formato JSON puro, baseado na biblioteca GSON da Google, seguindo este modelo: " +
      "{\"name\":\"Goblin Guerreiro\",\"level\":5,\"hp\":45,\"atk\":12,\"def\":6,\"weapon\":\"Clava de Pedra\"}. " +
      "Você só irá retornar o JSON puro, sem nenhuma outra palavra ou markdown, somente string."
    );

    Map<String, Object> variables = new HashMap<>();
    variables.put("contexto", contexto);
    variables.put("jogador", playerStats);
    Prompt prompt = promptTemplate.apply(variables);
    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Enemy e = gson.fromJson(wrappedJson, Enemy.class);

    EnemyRepo.createEnemy(e);

    return e;
  }
}
