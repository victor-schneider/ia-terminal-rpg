package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Model.Context;
import Model.PlayerComponents.Armor;
import Model.PlayerComponents.Item;
import Model.PlayerComponents.Player;
import Model.PlayerComponents.Weapon;
import Repository.ContextRepo;
import Repository.EnemyRepo;
import Repository.PlayerInvRepo;
import Repository.PlayerRepo;
import Utils.JsonCleaner;
import Utils.NumberGenerator;
import Utils.Verifiers;
import Utils.GeminiSingleton;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

import java.util.List;

public class Context {
  private String description;
  private  List<String> options;
  private Boolean weapon, armor, combat;

  public Context(String description, List<String> options, Boolean weapon, Boolean armor, Boolean combat) {
    this.description = description;
    this.options = options;
    this.weapon = weapon;
    this.armor = armor;
  }

  public static final Gson gson = new Gson();

  public String getDescription() {
    return description;
  }

  public List<String> getOptions() {
    return options;
  }

  public Boolean getCombat() {
    return combat;
  }

  public Boolean getArmor() {
    return armor;
  }

  public Boolean getWeapon() {
    return weapon;
  } 

  public void setDescription(String description) {
    this.description = description;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public void setCombat(Boolean combat) {
    this.combat = combat;
  }

  public void setWeapon(Boolean weapon) {
    this.weapon = weapon;
  }
  
  public void setArmor(Boolean armor) {
    this.armor = armor;
  }
  
  public void display() {
    System.out.println(description);

    for (int i = 0; i < options.size(); i++) {
      System.out.println((i + 1) + ". " + options.get(i));
    }
  }
  
  public List<String> verifyContext() {
    List<String> response = new ArrayList<>();

    if(combat) {
      createEnemy();

    } else if (armor) {
      String armorJson = gson.toJson(createArmor());
      response.add(armorJson);
      return response;

    } else if (weapon) {
      String weaponJson = gson.toJson(createWeapon());
      response.add(weaponJson);
      return response;
      
    } 
    return response;
  }
  
  public Context createContext() {
    Gson gson = new Gson();

    List<String> resposta = new ArrayList<>();
    String contexto = "";
    Player player = PlayerRepo.getPlayer();
    String statusJogador = player.getStatus();

    ChatLanguageModel model = GeminiSingleton.getInstance();
    
      PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons & Dragons com combate por turnos. Sua principal função é criar e narrar uma história interativa e progressiva, com elementos de exploração, combate e recompensas.

          ### Objetivo da história:
          - Criar uma narrativa contínua e envolvente.
          - A história deve evoluir gradualmente em dificuldade e complexidade.
          - A ambientação e os desafios devem refletir a progressão do jogador.

          ### Regras para geração da história:
            1. **Sempre crie um novo bloco de história** com três opções de ação relacionadas ao contexto atual.
            2. As opções devem respeitar as escolhas anteriores do jogador (fornecidas na seção `Dados Disponíveis`. Se ainda não houver contexto, apenas inicie a história.
            3. **Pelo menos uma das opções deve levar a um combate** — mas o combate pode ser indireto (ex: explorar uma dungeon antes do encontro).

          ### Início da jornada:
          - A história pode começar em qualquer lugar, mas de preferencia, em algum lugar civilizado.
          - Descreva o ambiente com riqueza de detalhes: construções, clima, horário do dia, e atmosfera geral.

          ### Dados disponíveis:
          - {{statusJogador}}: status atual do jogador (nome, vida, nível, etc.)
          - {{contextoHistoria}}: contexto atual da história (leia completamente antes de continuar)

          ### Estrutura da resposta:
          A IA deve sempre responder em **formato JSON puro (sem markdown)**, seguindo este modelo:

          {
            "description": "Texto com a descrição da cena ou contexto atual da história.",
            "options": [
              "Opção 1",
              "Opção 2",
              "Opção 3"
            ],
            "combat": false,
            "enemy": false,
            "weapon": false,
            "armor": false
          }

          ### Quando retornar cada atributo como true:
          - "combat": true → Quando a história levar a um combate iminente ou possível (ex: encontro com inimigos, entrada em dungeon perigosa, etc).
          - "enemy": true → Sempre que "combat" for true, também retorne "enemy": true.
          - "weapon": true → Quando o contexto envolver a descoberta, drop ou obtenção de uma arma branca (espada, lança, machado, etc).
          - "armor": true → Quando o contexto envolver a descoberta, drop ou obtenção de uma armadura (capacete, peitoral, calça, bota).

          ### O que cada item pode ser:
            - Arma branca (espadas, lanças, machados, etc.)
            - Armadura (capacete, peitoral, calças ou botas)
            - **Não** crie amuletos, anéis ou itens mágicos.

          ### importante
            * Nunca crie mais do que 3 opções para uma resposta. É de extrema importância que isso seja seguido

          ### Requisitos adicionais:
            - Nunca repita informações já presentes no contextoHistoria. Por exemplo, se você já mencionou que o jogador chegou em algum lugar, e forneceu detalhes desse lugar, não repita isso no próximo bloco de história.
            - Sempre forneça ao jogador **liberdade de escolha**, como em um RPG real.
            - Evite loops ou travamentos narrativos. Prossiga naturalmente.
        """);

      Map<String, Object> variables = new HashMap<>();

      resposta = ContextRepo.listContext();
      for(int i = 0; i < resposta.size(); i++) {
        contexto += resposta.get(i);
      }

      variables.put("contextoHistoria", contexto);
      variables.put("statusJogador", statusJogador);

      Prompt prompt = promptTemplate.apply(variables);
      Response<AiMessage> response = model.generate(prompt.toUserMessage());

      String wrappedJson = response.content().text().trim();
      wrappedJson = JsonCleaner.clean(wrappedJson);

      Context c = gson.fromJson(wrappedJson, Context.class);
      ContextRepo.createContext(c.getDescription());

      return c;
  }

  public Weapon createWeapon() {
    System.out.println("Create weapon foi chamado");
    Gson gson = new Gson();

    Player player = PlayerRepo.getPlayer();
    String statusJogador = player.getStatus();
    String contexto = ContextRepo.getLastContext();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons and Dragons com combate por turnos.

          Sua tarefa é criar a armas branca. Uma arma branca possui os seguintes atributos: `atk`, `id`, `equipped`, e `type`.

          ### Dados fornecidos:
          - `{{contexto}}`: contexto da história no momento em que a arma foi encontrada.
          - `{{statusJogador}}`: status atual do jogador (vida, ataque, defesa, etc.).

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
        """
      );

    Map<String, Object> variables = new HashMap<>();

    variables.put("contexto", contexto);
    variables.put("statusJogador", statusJogador);

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

  public Armor createArmor() {
    System.out.println("Create armor foi chamado");
    Gson gson = new Gson();

    Player player = PlayerRepo.getPlayer();
    String statusJogador = player.getStatus();
    String contexto = ContextRepo.getLastContext();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
          Você é um Dungeon Master de um jogo de RPG estilo Dungeons and Dragons com combate por turnos.

          Sua tarefa é criar uma armadura. A armadura possui os seguintes atributos: `slot`, `def`, `id`, `equipped`, e `type`.

          ### Dados fornecidos:
          - `{{contexto}}`: contexto da história no momento em que a armadura foi encontrada.
          - `{{statusJogador}}`: status atual do jogador (vida, ataque, defesa, etc.).

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

    variables.put("contexto", contexto);
    variables.put("statusJogador", statusJogador);

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

  public Enemy createEnemy() {
    Gson gson = new Gson();

    Player player = PlayerRepo.getPlayer();
    String contexto = ContextRepo.getLastContext();
    String playerStats = player.getStatus();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
      Você irá criar um inimigo para um jogo de RPG de turnos.  
      Esse RPG é baseado em Dungeons and Dragons.  
      O inimigo que você irá criar possuirá os seguintes componentes:  
      Name, Level, Weapon, HP, ATK, DEF, DEX, EXP. O inimigo level 1 irá dropar 15 de exp, a cada nivel subsequente, será 15 exp * 50%.
      Você irá receber o seguinte contexto para criar o inimigo {{contexto}}. Além disso, as informações atuais do jogador: {{jogador}} 
      Se baseie nesse contexto e nas informações fornecidas para criar um inimigo que esteja num nivel parecido com a do personagem, então se por exemplo, o inimigo tiver 5 de atk, significa que ele causará 5 de dano nos pontos de HP do jogador e vice versa. Os pontos de defesa causará com que o inimigo receba em certa porcentagem a menos de dano. 10 pontos de defesa signifcam menos 10% de dano. Leve tudo isso em consideração, junto com o contexto na hora de criar o inimigo e seus status 
      Você irá criá-lo num formato JSON puro, baseado na biblioteca GSON da Google, seguindo este modelo:  
      {
      "name":"Goblin Guerreiro",
      "level":5,
      "weapon":"Clava de Pedra",
      "hp":45,
      "atk":12,
      "def":6,
      "dex":5,
      "exp":15
      }
      Você só irá retornar o JSON puro, sem nenhuma outra palavra ou markdown, somente string.
      """
    );

    Map<String, Object> variables = new HashMap<>();
    variables.put("contexto", contexto);
    variables.put("jogador", playerStats);
    Prompt prompt = promptTemplate.apply(variables);
    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Enemy enemy = gson.fromJson(wrappedJson, Enemy.class);

    EnemyRepo.createEnemy(enemy);

    return enemy;
  }

  public Boolean combat(Enemy enemy) {
    int[] genNumbers = new int[100];
    for (int i = 0; i < genNumbers.length; i++) {
      genNumbers[i] = -1;
    }

    Context contexto = new Context(null, null, null, null, null);
    Player player = PlayerRepo.getPlayer();

    float atk = player.getAttack();
    float enemyAtk = enemy.getAtk();
    Scanner scanner = new Scanner(System.in);

    System.out.println("Combate!\n\n");
    enemy.getEnemyStatus(enemy);

    int opcao = 0;
    int rng = NumberGenerator.main(100);

    for (int i = 0; i < genNumbers.length; i++) {
      genNumbers[i] = -1;
    }

    atk = player.getAtk();
    if (NumberGenerator.numberVerifier(rng, genNumbers, player.getLck() + 10)) {
      atk *= 2;
      System.out.println("Dano critico!\n");
    }
    if (atk == player.getAtk()) {
      atk -= ((atk * enemy.getDef()) / 100);
      atk = Verifiers.roundNumbers(atk);
    }

    if (NumberGenerator.numberVerifier(rng, genNumbers, enemy.getDex())) {
      System.out.println("O inimigo desviou do ataque!");
    } else {
      enemy.setHp(Verifiers.roundNumbers(enemy.getHp() - atk));
      System.out.println(player.getName() + " Infligiu " + atk + " de dano ao " + enemy.getName());
      EnemyRepo.updateEnemy(enemy);

      if (enemy.getHp() <= 0) {
        System.out.println("O inimigo foi derrotado! Jogador ganhou: " + enemy.getExp() + " de XP\n");
        player.setExp(enemy.getExp());
        if (player.getExp() > player.getLevel()) {
          player.setLevel(player.getLevel() + 1);
          player.setExp(0);
          System.out.println(player.getName() + " Subiu para o nível: " + player.getLevel() + "!\n");

          player.setNextLevel(player.getNextLevel() * 1.2);

          PlayerRepo.updatePlayer(player);
        }

        ContextRepo.createContext("Inimigo: " + enemy.getName() + " Foi derrotado, combate volta a ser FALSE e continue a historia contando como o inimigo morreu e qual é a próxima decisão do jogador");

        contexto.createContext();

        scanner.close();
        return true;
      } else {
        System.out.println(enemy.getName() + " HP: " + enemy.getHp());
      }
    }

    if (enemyAtk == enemy.getAtk()) {
      enemyAtk -= ((enemyAtk * player.getTotalDefense()) / 100);
      enemyAtk = Verifiers.roundNumbers(enemyAtk);
    }
    for (int i = 0; i < genNumbers.length; i++) {
      genNumbers[i] = -1;
    }

    if (NumberGenerator.numberVerifier(rng, genNumbers, player.getDex())) {
      System.out.println("O jogador desviou do ataque!");
    } else {
      System.out.println("O " + enemy.getName() + "  infligiu " + enemyAtk + " de dano ao jogador");
      player.setHp(Verifiers.roundNumbers(player.getHp() - enemyAtk));
      PlayerRepo.updatePlayer(player);
    }

    if (player.getHp() <= 0) {
      System.out.println("Voce perdeu, ruim demais");
      contexto.setCombat(false);
      ContextRepo.createContext("Jogador: " + player.getName() + " Foi derrotado, finalize a história demonstrando como o jogador morreu para o inimigo: " + enemy.getName() + " que possuia a arma: " + enemy.getWeapon());

      contexto.createContext();

      System.out.println(contexto.getDescription());

      scanner.close();
      return true;
    }
    System.out.println(player.getName() + "HP: " + player.getHp());

    return false;
  }

}
