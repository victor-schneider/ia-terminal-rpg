package Controller;

import Components.DTO.ContextUpdateDTO;
import Manegement.ContextCreation;
import Repository.ContextRepo;
import Repository.PlayerRepo;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ContextController {
  
  private static final Gson gson = new Gson();

  public static void registerRoutes(Javalin app) {
    app.post("/context", ContextController::createContext);
  }

  public static void createContext(Context ctx) {
    try {
      Components.Context context = ContextCreation.generateContext(PlayerRepo.getPlayer());

      ctx.status(200).result(gson.toJson(context));

    } catch (Exception e ) {
      ctx.status(400).result("Falha ao criar contexto: " + e.getMessage());
    }

  }

}
