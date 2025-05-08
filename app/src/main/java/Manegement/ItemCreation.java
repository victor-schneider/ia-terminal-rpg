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
import Repository.PlayerRepo;
import Utils.JsonCleaner;
import Utils.GeminiSingleton;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

public class ItemCreation {
  public static void main(Context contexto, Player player) {
    Gson gson = new Gson();
    List<String> statusJogador = new ArrayList<>();
    
    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons and Dragons com combate por turnos.

          Sua função é **criar um único item** com base no seguinte contexto e status do jogador:

          - `{{contexto}}`: trecho da história onde o item foi encontrado.
          - `{{statusJogador}}`: atributos atuais do jogador (vida, ataque, defesa, etc.).

          ### Tipos de item possíveis:
          Você deve escolher **apenas um tipo de item** entre os dois abaixo com base no contexto.

          1. **Arma Branca (tipo: "WEAPON")**
            - Descrição: espadas, lanças, machados, adagas, clavas, etc.
            - Campos obrigatórios:
              - `name` (string): nome da arma
              - `atk` (inteiro): dano da arma
              - `equipped` (boolean): sempre `false`
              - `id` (inteiro): sempre `0`
              - `type` (string): `"WEAPON"`

          2. **Armadura (tipo: "ARMOR")**
            - Descrição: capacete, peitoral, calças ou botas.
            - Campos obrigatórios:
              - `name` (string): nome da armadura
              - `slot` (string): um dos seguintes — `"HELMET"`, `"CHEST"`, `"LEGS"`, `"BOOTS"` (definido com base no nome)
              - `def` (inteiro): defesa da armadura
              - `equipped` (boolean): sempre `false`
              - `id` (inteiro): sempre `0` e somente `0`
              - `type` (string): `"ARMOR"`

          ### Regras:
          - Gere apenas **um** item por vez.
          - O campo `id` deve ser sempre `0` e somente `0`, pois será definido pelo banco de dados.
          - O campo `equipped` deve ser sempre `false`.
          - O item deve estar de acordo com o contexto da história e ser coerente com o progresso do jogador.
          - **Nunca gere anéis, amuletos ou itens mágicos.**

          ### Formato da resposta:
          Retorne **apenas um JSON puro (sem markdown)** com os campos apropriados ao tipo escolhido.

          #### Exemplo — Arma:
          {
            "name": "Espada de Ferro",
            "atk": 10,
            "equipped": false,
            "id": 0,
            "type": "WEAPON"
          }
          ### Exemplo - Armadura:
          {
            "name": "Capacete de Ferro",
            "slot": "HELMET",
            "def": 5,
            "equipped": false,
            "id": 0,
            "type": "ARMOR"
          }
        """);

        statusJogador.add("Atk: " + player.getAttack() + "Def: " + player.getTotalDefense() + " Hp: " + player.getHp() + " level: " + player.getLevel());

        Map<String, Object> variables = new HashMap<>();

        variables.put("contexto", contexto.getDescription());
        variables.put("statusJogador", statusJogador);
        
        Prompt prompt = promptTemplate.apply(variables);
        Response<AiMessage> response = model.generate(prompt.toUserMessage());

        String wrappedJson = response.content().text().trim();
        wrappedJson = JsonCleaner.clean(wrappedJson);

        Item item = gson.fromJson(wrappedJson, Item.class);

        if(item.getType().equals("WEAPON")) {
          WeaponCreation.main(item, contexto, statusJogador);

        } else if (item.getType().equals("ARMOR")) {
          ArmorCreation.main(item, contexto, statusJogador);
          
        }
  }
}
