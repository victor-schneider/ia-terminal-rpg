package Manegement;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.Context;
import Repository.ContextRepo;
import Utils.JsonCleaner;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class ContextCreation {
  public static Context main() {
    Gson gson = new Gson();
    String contexto = "";
    List<String> resposta = new ArrayList<>();

    ChatLanguageModel model = VertexAiGeminiChatModel.builder()
      .project(System.getenv("PROJECT_ID"))
      .location(System.getenv("LOCATION"))
      .modelName("gemini-2.0-flash")
      .maxOutputTokens(1000)
      .temperature(2f)
      .build();
    
      PromptTemplate promptTemplate = PromptTemplate.from(
        "Você é um Dangeon Master de um jogo de RPG de Dangeons and Dragons com combate por turnos. Seu objetivo aqui é criar a história, ela precisa ser prograssiva, tanto nos monstros que aparecem quanto nos lugares. O jogador sempre irá começar numa vila. Os detalhes de tudo ambientes, construções, clima, horário será criada por você." +
        "A história será gerada por blocos, você irá criar um contexto de situação. Então, irá fornecer 3 opções de ações. Essas opções devem condizer com o contexto atual da história e as escolhas que o jogador fez anteriormente, caso ele não tenha feito nenhuma escolha, apenas começe a história. Você deve fazer necessáriamente uma opção que levará para um combate, mas não precisa de imediato. Por exemplo, pode-se fazer uma opção que leva o jogador para um dungeon, não terá combate de imediato, mas dentro da dungeon você poderá criar uma opção que levará para um combate." +
        "Quando o jogador selecionar uma opção que irá levar à um inimigo, você irá retornar no JSON o campo 'combate' como TRUE, senão FALSE "+
        "Este é contexto atual da história {{contextoHistoria}} uso-o para se basear nas próximas decisões de como a história irá progradir, lembre-se que ela precisa ser progressiva, tanto em dificuldade quanto em localidades. Como num jogo de rpg normal" +
        "Você irá criá-lo num formato JSON puro, sem o markdown do json, seguindo este modelo: " +
        "{\"description\":\"história.\",\"options\":[\"String da opcao1.\",\"String da opcao 2.\",\"String da opcao3\"],\"combate\":false}" +
        "Você só irá retornar o JSON puro, sem nenhuma outra palavra ou markdown, somente string."
      );

      Map<String, Object> variables = new HashMap<>();
     
      resposta = ContextRepo.listContext();
      
      for(int i = 0; i < resposta.size(); i++) {
        contexto += resposta.get(i);
      }

      variables.put("contextoHistoria", contexto);

      Prompt prompt = promptTemplate.apply(variables);

      Response<AiMessage> response = model.generate(prompt.toUserMessage());

      String wrappedJson = response.content().text().trim();

      wrappedJson = JsonCleaner.clean(wrappedJson);

      Context c = gson.fromJson(wrappedJson, Context.class);

      ContextRepo.createContext(c.getDescription());

      return c;
  }
}
