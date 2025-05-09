import Controller.PlayerController;
import io.javalin.Javalin;

public class RouteConfig {
  public static void registerRoutes(Javalin app) {
    PlayerController.registerRoutes(app);
  }
}
