/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gemini.workshop;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

import java.util.HashMap;
import java.util.Map;

public class TemplatePrompt {
    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-flash-002")
            .maxOutputTokens(500)
            .temperature(2f)
            .topK(40)
            .topP(0.95f)
            .maxRetries(3)
            .build();

        PromptTemplate promptTemplate = PromptTemplate.from(
            "Você é um Dangeon Master de Dangeons and Dragons. Seu objetivo será criar um curto contexto de história para o personagem {{personagem}}. Esse personagem é da classe {{classe}} e raça {{raca}}. O contexto deve ser o inicio da história, como o {{personagem}} está situado, se baseando em sua classe e raça."
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("personagem", "Roberto");
        variables.put("classe", "mago");
        variables.put("raca", "elfo");

        Prompt prompt = promptTemplate.apply(variables);

        Response<AiMessage> response = model.generate(prompt.toUserMessage());

        System.out.println(response.content().text());
    }
}
