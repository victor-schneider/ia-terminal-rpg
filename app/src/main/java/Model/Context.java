package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Model.Context;
import Model.Combat.CombatHandler;
import Model.Combat.CombatVerifier;
import Model.Combat.EnemyAttackHandler;
import Model.Combat.PlayerAttackHandler;
import Model.Combat.VictoryCheckHandler;
import Model.PlayerComponents.Armor;
import Model.PlayerComponents.Armor.Slot;
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
import Utils.ClearConsole;
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
  private int id;

  public Context(String description, List<String> options, Boolean weapon, Boolean armor, Boolean combat, int id) {
    this.description = description;
    this.options = options;
    this.weapon = weapon;
    this.armor = armor;
    this.id = id;
  }

  public static final Gson gson = new Gson();
  public static final Scanner scanner = new Scanner(System.in);

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

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
  
  public void displayDescription() {
    int width = 110;
    int textWidth = width - 2; // espaço para bordas
    int wrapWidth = width - 10; // quebra um pouco antes do final para evitar cortes abruptos
    String border = "\u001B[37m" + "═".repeat(width) + "\u001B[0m";
    System.out.println("\u001B[37m╔" + "═".repeat(width) + "╗\u001B[0m");
    System.out.printf("\u001B[37m║%s║\u001B[0m\n", String.format("%-" + width + "s", "                                   HISTÓRIA"));
    System.out.println("\u001B[37m╠" + "═".repeat(width) + "╣\u001B[0m");

    String[] lines = getDescription().split("\n");
    for (String line : lines) {
      int start = 0;
      while (start < line.length()) {
        int end = Math.min(start + wrapWidth, line.length());
        // Tenta quebrar em espaço antes do limite, se possível
        if (end < line.length()) {
          int lastSpace = line.lastIndexOf(' ', end);
          if (lastSpace > start) {
            end = lastSpace;
          }
        }
        String part = line.substring(start, end).trim();
        System.out.printf("\u001B[37m║ %-"+textWidth+"s \u001B[0m║\n", part);
        start = end;
        // Pula espaço se a quebra foi em espaço
        while (start < line.length() && line.charAt(start) == ' ') start++;
      }
    }
    System.out.println("\u001B[37m╚" + "═".repeat(width) + "╝\u001B[0m");
  }

  public Boolean displayOptions() {
    int width = 110;
    int opcao = 0;
    do {
      if (!combat) {
      System.out.println("\u001B[37m╔" + "═".repeat(width) + "╗\u001B[0m");
      System.out.printf("\u001B[37m║%s║\u001B[0m\n", String.format("%-" + width + "s", "                                   OPÇÕES"));
      System.out.println("\u001B[37m╠" + "═".repeat(width) + "╣\u001B[0m");
      for (int i = 0; i < options.size(); i++) {
        String option = options.get(i);
        int maxOptionWidth = width - 8;
        int start = 0;
        boolean firstLine = true;
        while (start < option.length()) {
        int end = Math.min(start + maxOptionWidth, option.length());
        String part = option.substring(start, end);
        if (firstLine) {
          System.out.printf("\u001B[37m║  [%d] - %-"+maxOptionWidth+"s║\u001B[0m\n", (i + 1), part);
          firstLine = false;
        } else {
          System.out.printf("\u001B[37m║        %-"+maxOptionWidth+"s║\u001B[0m\n", part);
        }
        start = end;
        }
      }
      System.out.println("\u001B[37m╠" + "═".repeat(width) + "╣\u001B[0m");
      System.out.printf("\u001B[33m║  [4] - %-"+(width-8)+"s║\u001B[0m\n", "Abrir Inventário");
      System.out.printf("\u001B[31m║  [5] - %-"+(width-8)+"s║\u001B[0m\n", "Finalizar Jogo");
      System.out.println("\u001B[37m╚" + "═".repeat(width) + "╝\u001B[0m");
      System.out.print("Escolha uma opção: ");
      opcao = scanner.nextInt();

      if (opcao < 1 || opcao > 5) {
        System.out.println("\u001B[31mOpção inválida, digite novamente!\u001B[0m\n");
      }
      } else if (combat) {
      return false;
      }

      switch (opcao) {
      case 4:
        Player player = PlayerRepo.getPlayer();
        player.inventory();
        displayDescription();
        break;
      case 5:
        return true;
      }

    } while (opcao < 1 || opcao > 3);

    

    ContextRepo.createContext(options.get(opcao - 1));
    return false;
  }
  
  public Boolean verifyContext() {

    if(combat) {
      Enemy enemy = createEnemy();
      Player player = PlayerRepo.getPlayer();
      
      Boolean fugiu = false;
      int opcao = 0;
      do {
          System.out.printf("""
            ╔═════════ COMBATE ═════════╗
              Você vai:
              [1] - Atacar Inimigo
              [2] - Abrir inventário
              [3] - Tentar Fugir
            ╚═══════════════════════════╝
            """);
          opcao = scanner.nextInt();

          switch (opcao) {
            case (2):
              player.inventory();

            break;

            case (3):
            int rng = NumberGenerator.main(100);
            int[] genNumbers = new int[100];

              for (int i = 0; i < genNumbers.length; i++) {
                genNumbers[i] = -1;
              }
            if (NumberGenerator.numberVerifier(rng, genNumbers, player.getLck() + 10)) {
              System.out.println(player.getName() + " conseguiu escapar!");
              ContextRepo.createContext("O jogador consegiu escapador do inimigo: " + enemy.getName() + " continue a história contando como ele conseguiu escapar deste inimigo");
              fugiu = true;
            }
            break;
          }

        } while (opcao != 1 && !fugiu);

        if(!fugiu) return combat(enemy);

    } else if (armor) {
      createArmor();

    } else if (weapon) {
      createWeapon();
    } 
    return false;
  }
  
  public Context createContext() {
    Gson gson = new Gson();
    List<String> resposta = new ArrayList<>();
    String contexto = "";
    Player player = PlayerRepo.getPlayer();
    String statusJogador = player.getStatus();

    ChatLanguageModel model = GeminiSingleton.getInstance();

    PromptTemplate promptTemplate = PromptTemplate.from("""
      You are the Dungeon Master of a turn-based Dungeons & Dragons-style RPG. Your task is to generate a continuous, evolving narrative in discrete “blocks” of play. Each block must follow these rules:

      1. **Output JSON only**, sem markdown:
         {
           "description": "...",      // descrição rica em detalhes do cenário atual
           "options": ["…","…","…"], // sempre 3 escolhas
           "combat": false,          // true se o próximo passo levar a combate imediato (ex: o jogador encontrou um goblin perdido e a resposta do jogador demonstra a intenção de atacar o goblin)
           "weapon": false,          // true se houver obtenção de arma branca
           "armor": false            // true se houver obtenção de armadura
         }

      2. **Cenário inicial**  
         - Comece em um local civilizado (ex.: vila, taverna, mercado)  
         - Descreva clima, construções, habitantes, sons e cores.

      3. **Opções de ação**  
         - Sempre 3 escolhas distintas:  
           1. Avançar na história  
           2. Alternativa de fuga ou contorno  
           3. Gancho inesperado (surpresa narrativa)  
         - Pelo menos uma opção deve apontar para um combate indireto (ex.: aceitar missão de dungeon, investigar sons estranhos, etc.).
         - Utilize essas orientações por padrão, mas não se limite a elas. Caso você achar alguma oportunidade para deixar a história mais dinâmica, você possui a liberdade para criar opções diferentes.

      4. **Variáveis de contexto**  
         - `{{contextoHistoria}}`: tudo que já ocorreu e as decisões do jogador (última linha = escolha atual).  
         - `{{statusJogador}}`: estado atual do jogador (nome, vida, nível, equipamentos).

      5. **Progressão**  
         - A dificuldade e a complexidade dos desafios devem aumentar gradualmente.  
         - Nunca repita informação já descrita em `contextoHistoria`.  

      6. **Itens**  
         - Só armas brancas (espadas, lanças, machados) e armaduras (capacete, peitoral, calça, bota).  
         - Nunca crie amuletos, anéis ou itens mágicos.

      7. **Evite**  
         - Loops narrativos ou travamentos.  
         - Respostas fora do formato JSON ou com mais/menos de 3 opções.

      Use este template para cada geração, preenchendo `description`, `options` e os booleanos conforme as regras acima. 
    """);

    Map<String, Object> variables = new HashMap<>();

    resposta = ContextRepo.listContext();
    for(int i = 0; i < resposta.size(); i++) {
      contexto += resposta.get(i);
    }

    variables.put("contextoHistoria", contexto);
    variables.put("statusJogador", statusJogador);

    Prompt prompt = promptTemplate.apply(variables);

    final boolean[] loading = {true};
    Thread loadingThread = new Thread(() -> {
      String[] loadingStates = {"|", "/", "-", "\\"};
      int idx = 0;
      System.out.print("Carregando história ");
      while (loading[0]) {
        System.out.print("\rCarregando história " + loadingStates[idx++ % loadingStates.length]);
        try {
          Thread.sleep(150);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      System.out.print("\r");
    });

    loadingThread.start();

    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    loading[0] = false;
    try {
      loadingThread.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Context c = gson.fromJson(wrappedJson, Context.class);

    c.setId(ContextRepo.createContext(c.getDescription()));

    this.description = c.getDescription();
    this.options = c.getOptions();
    this.combat = c.getCombat();
    this.weapon = c.getWeapon();
    this.armor = c.getArmor();

    return c;
  }

  public Weapon createWeapon() {
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

    final boolean[] loading = {true};
    Thread loadingThread = new Thread(() -> {
      String[] loadingStates = {"|", "/", "-", "\\"};
      int idx = 0;
      System.out.print("Criando Inimigo ");
      while (loading[0]) {
        System.out.print("\rCriando Inimigo " + loadingStates[idx++ % loadingStates.length]);
        try {
          Thread.sleep(150);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      System.out.print("\r");
    });

    loadingThread.start();

    Response<AiMessage> response = model.generate(prompt.toUserMessage());

    loading[0] = false;
    try {
      loadingThread.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String wrappedJson = response.content().text().trim();
    wrappedJson = JsonCleaner.clean(wrappedJson);

    Enemy enemy = gson.fromJson(wrappedJson, Enemy.class);

    EnemyRepo.createEnemy(enemy);

    return enemy;
  }

  public Boolean combat(Enemy enemy) {
      Context contexto = new Context(null, null, null, null, null, 0);
      Player player = PlayerRepo.getPlayer();
      CombatVerifier combatVerifier = new CombatVerifier();
      Boolean resultado;

      Scanner scanner = new Scanner(System.in);

      System.out.println("Combate!\n\n");
      ClearConsole.clearConsole();
      do {
        int opcao = 0;

        System.out.println("\n================== TURNO DO JOGADOR ==================");
        System.out.printf("O que deseja fazer?\n");
        System.out.println("[1] - Atacar");
        System.out.println("======================================================");
        opcao = scanner.nextInt();

        if(opcao == 1) {
          CombatHandler playerAttack = new PlayerAttackHandler();
          CombatHandler enemyAttack = new EnemyAttackHandler();
          CombatHandler victoryCheck = new VictoryCheckHandler();

          playerAttack.setNext(enemyAttack);
          enemyAttack.setNext(victoryCheck);

          playerAttack.handle(contexto, enemy, player, combatVerifier);
        }

        if (combatVerifier.playerWon) {
          return false;
        } else if (combatVerifier.enemyWon) {
          return true;
        }

        System.out.println(enemy.getName() + " HP restante: " + enemy.getHp());

        System.out.println("\nStatus atual:");
        System.out.println(player.getName() + " HP: " + player.getHp());
        System.out.println(enemy.getName() + " HP: " + enemy.getHp());
        System.out.println("======================================================\n");

      } while (combatVerifier.enemyWon == false && combatVerifier.playerWon == false);

    return false;
  }

  public void startGame() {
    int opcao = 0;

    // MENU DO JOGO
    do {
      ClearConsole.clearConsole();

      System.out.println("╔══════════════════════════════════════════════╗");
      System.out.println("║           \u001B[35mRPG de Terminal\u001B[0m                    ║");
      System.out.println("╠══════════════════════════════════════════════╣");
      System.out.println("║ [1] - \u001B[32mComeçar jogo\u001B[0m                           ║");
      System.out.println("║ [2] - \u001B[36mSobre\u001B[0m                                  ║");
      System.out.println("║ [3] - \u001B[31mFinalizar\u001B[0m                              ║");
      System.out.println("╚══════════════════════════════════════════════╝");
      System.out.print("Escolha uma opção: ");
      opcao = scanner.nextInt();

      switch (opcao) {
        case (2):
          ClearConsole.clearConsole();
          System.out.println("╔══════════════════════════════════════════════╗");
          System.out.println("║                \u001B[36mCriadores\u001B[0m                     ║");
          System.out.println("╠══════════════════════════════════════════════╣");
          System.out.println("║  - Victor Schneider                          ║");
          System.out.println("║  - Allan Slomski                             ║");
          System.out.println("║  - Eduardo Doli                              ║");
          System.out.println("╚══════════════════════════════════════════════╝");
          System.out.println("\n[Aperte qualquer tecla para voltar]");
          scanner.next();
        break;

        case (3):
          return;
      }
      if(opcao < 1 || opcao > 3) {
        System.out.println("\u001B[31mOpção inválida, digite novamente!\u001B[0m\n");
      }
    } while (opcao != 1);

    // Criação do personagem
    String name;

    ClearConsole.clearConsole();
    System.out.println("╔══════════════════════════════════════════════╗");
    System.out.println("║         \u001B[33mCriação do Personagem\u001B[0m                ║");
    System.out.println("╚══════════════════════════════════════════════╝");
    System.out.print("Digite o nome do personagem: ");
    name = scanner.next();

    Player player = new Player(name, 100, 5, 2, 90, 5, 1, 0, 20, 1);
    PlayerRepo.createPlayer(player);

    Armor helmet = new Armor("Capacete de ferro", Slot.HELMET, 10, 0, true, "ARMOR");
    PlayerInvRepo.createArmor(helmet);
    player.equipArmor(helmet);

    Weapon espada = new Weapon(5, "Espada de ferro", true, 1, "WEAPON");
    PlayerInvRepo.createWeapon(espada);
    player.equipWeapon(espada);

    // Contexto inicial
    System.out.println("\n\u001B[34mBem-vindo ao mundo de aventuras, " + name + "!\u001B[0m");
    System.out.println("Sua jornada está prestes a começar...\n");
    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    createContext();
  }

}
