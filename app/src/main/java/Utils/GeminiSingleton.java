package Utils;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class GeminiSingleton {
  private static ChatLanguageModel instance;

  private GeminiSingleton() {

  }

  public static ChatLanguageModel getInstance() {
    if (instance == null) {
      synchronized (GeminiSingleton.class) {
        if (instance == null) {
          instance = VertexAiGeminiChatModel.builder()
          .project(System.getenv("PROJECT_ID"))
          .location(System.getenv("LOCATION"))
          .modelName("gemini-2.0-flash")
          .maxOutputTokens(1000)
          .temperature(2f)
          .build();
        }
      }
    }
    return instance;
  }
}
