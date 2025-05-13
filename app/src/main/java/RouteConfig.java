import Controller.EnemyController;
import Controller.PlayerController;
import Controller.PlayerInvController;
import io.javalin.Javalin;

public class RouteConfig {
  public static void registerRoutes(Javalin app) {
    PlayerController.registerRoutes(app);
    EnemyController.registerRoutes(app);
    PlayerInvController.registerRoutes(app);
  }
}
