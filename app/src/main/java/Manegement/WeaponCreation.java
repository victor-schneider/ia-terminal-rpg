package Manegement;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.Context;
import Components.PlayerComponents.Player;
import Components.PlayerComponents.Weapon;
import Components.PlayerComponents.Item;

import Repository.ContextRepo;
import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Utils.JsonCleaner;
import Utils.GeminiSingleton;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

public class WeaponCreation {
  public static Weapon main (Item item, Context contexto, List<String> statusJogador) {
    Gson gson = new Gson();
    List<String> statusItem =  new ArrayList<>();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons and Dragons com combate por turnos.

          Sua tarefa é **completar os dados de uma arma branca** que já foi criada. Você deve apenas preencher os atributos restantes da arma: `atk`, `id`, `equipped`, e `type`.

          ### Dados fornecidos:
          - `{{contexto}}`: contexto da história no momento em que a arma foi encontrada.
          - `{{statusJogador}}`: status atual do jogador (vida, ataque, defesa, etc.).
          - `{{weapon}}`: nome da arma criada.

          ### Regras para gerar os atributos:
          1. A arma deve ser **exclusivamente branca** (ex: espadas, lanças, machados, adagas, clavas, etc.). **Nunca inclua armas mágicas, arcos ou armas de fogo.**

          2. O campo `atk` representa o ataque da arma e deve ser um **número inteiro**, não string.

          3. O campo `id` deve ser sempre `0` e nada além de `0`. O sistema atribuirá um ID automaticamente ao salvar no banco de dados.

          4. O campo `equipped` deve ser sempre `false`.

          5. O campo `type` deve sempre ser `"WEAPON"`.

          ### Formato da resposta:
          Retorne um **JSON puro, sem markdown**, seguindo exatamente esta estrutura (com os tipos corretos):

          {
            "atk": 10,
            "name": "Espada de Ferro",
            "equipped": false,
            "id": 0,
            "type": "WEAPON"
          }
        """);

    Map<String, Object> variables = new HashMap<>();

    statusItem.add("Nome: " + item.getName() + "type: " + " equipped: " +item.getEquipped() + "id: " + item.getId() + "type: " + item.getType());

    variables.put("contexto", "contexto");
    variables.put("statusJogador", statusJogador);
    variables.put("weapon", statusItem);

    Prompt prompt = promptTemplate.apply(variables);
    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Weapon weapon = gson.fromJson(wrappedJson, Weapon.class);

    PlayerInvRepo.createWeapon(weapon);
    System.out.println(weapon.getName() + " foi inserida no inventario!");
    ContextRepo.createContext(weapon.getName() + " foi inserida no inventario do jogador, retorne item como false!");

    return weapon;
  }
}
