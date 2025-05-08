package Manegement;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Components.Context;
import Components.PlayerComponents.Player;
import Repository.ContextRepo;
import Repository.PlayerRepo;
import Utils.JsonCleaner;
import Utils.GeminiSingleton;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;


public class ContextCreation {
  public static Context main(Player player) {
    Gson gson = new Gson();
    String contexto = "";
    List<String> resposta = new ArrayList<>();
    List<String> statusJogador = new ArrayList<>();

    ChatLanguageModel model = GeminiSingleton.getInstance();
    
      PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons & Dragons com combate por turnos. Sua principal função é criar e narrar uma história interativa e progressiva, com elementos de exploração, combate e recompensas.

          ### Objetivo da história:
          - Criar uma narrativa contínua e envolvente.
          - A história deve evoluir gradualmente em dificuldade e complexidade.
          - A ambientação e os desafios devem refletir a progressão do jogador.

          ### Início da jornada:
          - A história sempre começa em uma vila.
          - Descreva o ambiente com riqueza de detalhes: construções, clima, horário do dia, e atmosfera geral.

          ### Estrutura da resposta:
          A IA deve sempre responder em **formato JSON puro (sem markdown)**, seguindo este modelo:

          {
            "description": "Texto com a descrição da cena ou contexto atual da história.",
            "options": [
              "Opção 1",
              "Opção 2",
              "Opção 3"
            ],
            "combate": false,
            "item": false
          }

          ### Regras para geração da história:
          1. **Sempre crie um novo bloco de história** com três opções de ação relacionadas ao contexto atual.
          2. As opções devem respeitar as escolhas anteriores do jogador (fornecidas em `{{contextoHistoria}}`). Se ainda não houver contexto, apenas inicie a história.
          3. **Pelo menos uma das opções deve levar a um combate** — mas o combate pode ser indireto (ex: explorar uma dungeon antes do encontro).
          4. Quando um combate estiver prestes a ocorrer, defina `"combate": true`.
            - Neste caso, as opções **devem ser exatamente nesta ordem**:
              1. Atacar inimigo
              2. Abrir inventário
              3. Tentar fugir
          5. Após um inimigo ser derrotado (verificado na última linha do contexto), continue a história normalmente e defina `"combate": false`.

          ### Regras sobre itens:
          1. Se o contexto incluir a descoberta de um item (ex: baú, loot de inimigo), defina `"item": true`.
          2. O item pode ser:
            - Arma branca (espadas, lanças, machados, etc.)
            - Armadura (capacete, peitoral, calças ou botas)
            - **Não** crie amuletos, anéis ou itens mágicos.
          3. As opções para itens devem ser:
            1. Pegar item
            2. Deixar item
            3. Examinar item
          4. Se o jogador escolher “Examinar item”, forneça uma descrição detalhada e mantenha as mesmas opções, mas altere “Examinar item” para “Examinar item [Examinado]”.
          5. **Apenas um item por vez** deve ser gerado.
          6. Ao gerar o campo `id` de um item, **sempre use o valor 0 e nada além de 0**. O sistema atribuirá um ID automaticamente no backend.

          ### Dados disponíveis:
          - `{{statusJogador}}`: status atual do jogador (nome, vida, nível, etc.)
          - `{{contextoHistoria}}`: contexto atual da história (leia completamente antes de continuar)

          ### Requisitos adicionais:
          - Nunca repita informações já presentes no `contextoHistoria`. Por exemplo, se você já mencionou que o jogador chegou na vila incial, e forneceu detalhes da vila, não repita isso no próximo bloco de história.
          - Sempre forneça ao jogador **liberdade de escolha**, como em um RPG real.
          - Evite loops ou travamentos narrativos. Prossiga naturalmente.

          Gere a próxima parte da história no formato acima.
        """);

      Map<String, Object> variables = new HashMap<>();

      resposta = ContextRepo.listContext();
      for(int i = 0; i < resposta.size(); i++) {
        contexto += resposta.get(i);
      }

      statusJogador.add("Atk: " + player.getAttack() + "Def: " + player.getTotalDefense() + " Hp: " + player.getHp() + " level: " + player.getLevel());

      variables.put("contextoHistoria", contexto);
      variables.put("statusJogador", statusJogador);

      Prompt prompt = promptTemplate.apply(variables);
      Response<AiMessage> response = model.generate(prompt.toUserMessage());

      String wrappedJson = response.content().text().trim();
      wrappedJson = JsonCleaner.clean(wrappedJson);

      Context c = gson.fromJson(wrappedJson, Context.class);
      ContextRepo.createContext(c.getDescription());
      
      System.out.println("Item criado?: " + c.getItem() + "\n");

      return c;
  }
}
