package Manegement;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.Context;
import Components.PlayerComponents.Player;
import Components.PlayerComponents.Weapon;
import Components.PlayerComponents.Armor;
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

public class ArmorCreation {
  public static Armor main (Item item, Context contexto, List<String> statusJogador) {
    Gson gson = new Gson();
    List<String> statusItem =  new ArrayList<>();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons and Dragons com combate por turnos.

          Sua tarefa é **completar os dados de uma armadura** que já foi criada. Você deve apenas preencher os atributos restantes da armadura: `slot`, `def`, `id`, `equipped`, e `type`.

          ### Dados fornecidos:
          - `{{contexto}}`: contexto da história no momento em que a armadura foi encontrada.
          - `{{statusJogador}}`: status atual do jogador (vida, ataque, defesa, etc.).
          - `{{armor}}`: nome da armadura criada.

          ### Regras para gerar os atributos:
          1. O campo `slot` deve ser preenchido com base no nome da armadura:
            - Se o nome contém “capacete”, use: `"HELMET"`
            - Se o nome contém “peitoral” ou “armadura de peito”, use: `"CHEST"`
            - Se o nome contém “calça” ou “perneira”, use: `"LEGS"`
            - Se o nome contém “bota” ou “grevas”, use: `"BOOTS"`

          2. O campo `def` representa a defesa da armadura e deve ser um **número inteiro**, não string.
          3. O campo `id` deve ser sempre `0`. O sistema atribuirá um ID automaticamente.
          4. O campo `equipped` deve ser sempre `false`.
          5. O campo `type` deve sempre ser `"ARMOR"`.

          ### Formato da resposta:
          Retorne um **JSON puro, sem markdown**, seguindo exatamente esta estrutura (com os tipos corretos):

          {
            "name": "Capacete de Ferro",
            "slot": "HELMET",
            "def": 5,
            "id": 0,
            "equipped": false,
            "type": "ARMOR"
          }

          Gere a armadura com base nesses dados.
        """);

    Map<String, Object> variables = new HashMap<>();

    statusItem.add("Nome: " + item.getName() + "type: " + " equipped: " +item.getEquipped() + "id: " + item.getId() + "type: " + item.getType());

    variables.put("contexto", "contexto");
    variables.put("statusJogador", statusJogador);
    variables.put("armor", statusItem);

    Prompt prompt = promptTemplate.apply(variables);
    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Armor armor = gson.fromJson(wrappedJson, Armor.class);

    PlayerInvRepo.createArmor(armor);
    System.out.println(armor.getName() + " foi inserida no inventario!");
    ContextRepo.createContext(armor.getName() + " foi inserida no inventario do jogador, retorne item como false!");

    return armor;
  }
}
