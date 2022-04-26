public class GameLauncher{ 
  /**
  * Only CHANGE constant values as appropriate
  *
  * THERE SHOULD NOT BE ANYTHING SUBSTANTIAL TO **ADD** TO THIS CLASS
  * 
  * The Scrolling Game is described the Project Handout. 
  *  
  * @author Elodie Fourquet 
  * @date March, 2021
  */
  
  private static final int BASE = 0;
  private static final int CREATIVE = 1;
  
  private static final int RUNNING = CREATIVE;
  
  public static void main(String[] args) {
    
    
    AbstractGame game = null;
    
    switch (RUNNING) {
    case BASE:
      // Construct sized version of the base game you've written
      game = new AScrollingGame(8, 14);
      
      // Could be instead
      // Test many constructor calls (and more)
      //game = new AScrollingGame();
      //game = new AScrollingGame(GridController.DEFAULT_GRID_H, GridController.DEFAULT_GRID_W );

      System.out.println("From GameLauncher main: Running AScrollingGame version");
      break;
    case CREATIVE:
      // Construct sized version of the creative game you've written
      //game = new CreativeGame(GridController.DEFAULT_GRID_H, GridController.DEFAULT_GRID_W );
      game = new CreativeGame(7, 14);
      System.out.println("From GameLauncher main: Running a CreativeGame");
      break;
    default:
      System.out.println("Not sure which version you meant...");
      
    }
    
    // Launch the instantiated game version
    // Make sure to trace the run method in GameCore
    if (game != null) {
      ((CreativeGame)game).run(3);
    }
    else 
      System.out.println("Check that a Game is instantiated in GameLauncher");
    
  }
  
}
