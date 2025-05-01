package Manegement;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.Context;
import Repository.ContextRepo;
import Utils.JsonCleaner;
import Utils.GeminiSingleton;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;


public class ContextCreation {
  public static Context main() {
    Gson gson = new Gson();
    String contexto = "";
    List<String> resposta = new ArrayList<>();

    ChatLanguageModel model = GeminiSingleton.getInstance();

      // System.out.println("Contexto qeue será passado para a IA: ");
      // resposta = ContextRepo.listContext();
      // for(int i = 0; i < resposta.size(); i++) {
      //   System.out.println(resposta.get(i));
      // }
    
      PromptTemplate promptTemplate = PromptTemplate.from("""
        Você é um Dungeon Master de um jogo de RPG de Dungeons and Dragons com combate por turnos. 
        Seu objetivo aqui é criar a história, ela precisa ser progressiva, tanto nos monstros que aparecem quanto nos lugares. 
        O jogador sempre irá começar numa vila. Os detalhes de tudo, ambientes, construções, clima, horário, serão criados por você. 
        A história será gerada por blocos, você irá criar um contexto de situação. Então, irá fornecer 3 opções de ações. 
        Essas opções devem condizer com o contexto atual da história e as escolhas que o jogador fez anteriormente. 
        Caso não haja nenhuma história ou escolha no contexto da história fornecido, apenas comece a história. Você deve fazer necessariamente uma opção que levará para um combate, mas não precisa ser imediato. Por exemplo, pode-se fazer uma opção que leva o jogador para um dungeon, não terá combate de imediato, 
        mas dentro da dungeon você poderá criar uma opção que levará para um combate. Quando o jogador selecionar uma opção que irá levar a um inimigo, 
        você irá retornar no JSON o campo 'combate' como TRUE, senão FALSE. Se retornado como TRUE, você irá fornecer 3 opções na seguinte ordem: 
        1 - Atacar inimigo, 2 - Abrir inventário, 3 - Tentar Fugir. É de extrema importância que seja nesta ordem. Se a última linha do contexto estiver falando que o inimigo foi derrotado, você irá retornar no JSON combate: false e irá continuar a história com este inimigo derrotado.
        Além disso, nunca gere mais do que 1 inimigo por vez na história. 
        Este é o contexto atual da história: {{contextoHistoria}}. Leia-o e continue a história, não repita o que estiver no contexto e forneça opções que permitam um livre arbitrio do jogador. Como num jogo de RPG normal. Você irá criá-lo num formato JSON puro, sem markdown, seguindo este modelo: 
        {
          "description": "história",
          "options": [
            "String da opcao1",
            "String da opcao 2",
            "String da opcao3"
            ],
          "combate":false
        }
        """);

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
      
      System.out.println("Combate?: " + c.getCombate() + "\n");

      return c;
  }
}
