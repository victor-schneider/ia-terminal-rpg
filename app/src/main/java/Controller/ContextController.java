package Controller;

import Repository.ContextRepo;
import Repository.PlayerRepo;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import Model.DTO.ContextUpdateDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ContextController {
  
  private static final Gson gson = new Gson();

  public static void registerRoutes(Javalin app) {
    app.post("/contexts", ContextController::createContext);
    app.patch("/contexts", ContextController::updateContext);
  }

  public static void createContext(Context ctx) {
    try {
      Model.Context context = new Model.Context(null, null, null, null, null, null);
    
      ctx.status(200).result(gson.toJson(context.createContext()));

    } catch (Exception e ) {
      ctx.status(400).result("Falha ao criar contexto: " + e.getMessage());

    }
  }

  public static void updateContext(Context ctx) {
    try {
      ContextUpdateDTO updateData = gson.fromJson(ctx.body(), ContextUpdateDTO.class);

      ContextRepo.createContext(updateData.getOptions());
      ctx.status(200).result("Contexto atualizado com sucesso");

    } catch (Exception e) {
      ctx.status(400).result("Falha ao criar contexto: " + e.getMessage());
    }
  }
}
