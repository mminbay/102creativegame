import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * @author Elodie Fourquet 
 * @date March, 2021

  Very similar to the AbstractGame of Tetris

  An instance of AbstractGame 
     - IS-A JFrame
       manages the overall window, including title bar, corner buttons 
     - HAS-A GameGrid instance at its center
*/

public abstract class AbstractGame extends JFrame implements KeyListener {
  
  //----------------  Constants and one class variable -----------------/
   
  //no key has been pressed value
  protected static final int NO_KEY = -1; 
  
  protected static final int KEY_QUIT_GAME = KeyEvent.VK_Q;
  protected static final int KEY_PAUSE_GAME = KeyEvent.VK_P;    
  protected static final int KEY_SCREENSHOT = KeyEvent.VK_S;

  //key used to advance past a splashscreen
  protected static int DEFAULT_SCREEN_ADVANCING_KEY = KeyEvent.VK_ENTER;
  
  
  //default milliseconds of sleep during each gameloop iteration
  protected static final int DEFAULT_TIMER_DELAY = 150;
  //minimum sleep (will produce very fast animation)
  protected static final int MIN_TIMER_DELAY = 20;
  
  private static final int FACTOR = 3;
  
  
  private static final String CAPTURE_PATH = "screencapture/saveScreen";

  private static int captureCounter = 0;
  
  //---------------- Instance Variables -----------------// 
  
  // Permit to modify game speed. Used by AScrollingGame slowDown/Up methods
  protected int currentTimerDelay;  
  // Retain default speed, used to reset timing 
  private int defaultTimerDelay;
  
  // Keep track of total number of gameLoop() iteration 
  protected int ticksElapsed = 0;  
  
  // Store the key to advance still screen
  private int splashScreenAdvancingKey;
  // Store the last key pressed by player — CHANGED TO PROTECTED TO OVERRIDE
  protected int lastKeyPressed;
  
  protected boolean isPaused = false;  
  private boolean splashScreen = false;
  
  // Main state is a GameGrid, a matrix of cells, in which assets are found
  protected GameGrid grid;
  
  /**********************     Constructors   **********************/
  
  public AbstractGame(int hdim, int wdim) {
    this(hdim, wdim, DEFAULT_TIMER_DELAY);
  }
  
  //hdim/wdim specify numbers of rows/columns of game grid respectively
  //timerDelay specifies initial game speed
  public AbstractGame(int hdim, int wdim, int timerDelay) {
    super("Game Title");
    init(hdim, wdim, timerDelay);
  }    
  
  /**********************     Instance Methods   **********************/
  
  //Runs the game, including displaying pre- and post-game screens
  public void run(){     
    startGame();   //display splashscreen(s)
    gameLoop();    //run main animated & interactive screen 
    endGame();     //display splashscreen(s) 
  }
  
  
  // Handle KEY at the window level, i.e. exit, pause, advance splashscreen
  // Return key code so children can expand to game play level response 
  protected int handleKeyPress() {
    int key = checkLastKeyPressed();
    //System.out.println("abstract game key pressed");
    
    if (key == KEY_QUIT_GAME)
      System.exit(0);
    else if (key == KEY_PAUSE_GAME)
      isPaused = !isPaused;  
    else if (key == splashScreenAdvancingKey)
      splashScreen = false;
    else if (key == KEY_SCREENSHOT) 
      grid.takeScreenShot(CAPTURE_PATH + (captureCounter++) + ".png");
    
    return key;
  }
  
  //---------------- Window and key features ------------//
  //    You might want to CALL THOSE especially for Part II 
  // as your implement creative features
  
  
  protected void resetSpeed() {
    currentTimerDelay = defaultTimerDelay;
  }
  
  protected void setsplashScreenAdvancingKey(int advanceKey){
    splashScreenAdvancingKey = advanceKey;
  }
  
  //display splashscreens to create an intro and conclusion to your game
  //they are still, meaning no moving part, but should respond to at least
  //one key to transition to main game play or exit game
  protected void displaySplashScreen(String screen){
    runSplashScreen(screen);
  }  
 
  // Handle MOUSE CLICK in the game window (similar to handleKeyPress above
  // Return the Location within which the mouse cursor click occured;
  //         null if mouse isn't clicked 
  protected Location handleMouseClick() {
    
    Location loc = grid.checkLastLocationClicked();
    
    if (loc != null) 
      System.out.println("You clicked on the GameGrid cell " + loc);
    
    return loc;
  }
  
  /********************** Abstract methods **********************
       to be implemented in AScrollingGame **********************/
  
  //checks to see if the game is over
  protected abstract boolean isGameOver();
  //handles all of the tasks done on each tick
  protected abstract int performGameUpdates();
  //handles all of the tasks done only on each "render tick"
  protected abstract void performRenderUpdates();
  
  //contains all of the tasks that need to be done when game starts/ends
  protected abstract void startGame();
  protected abstract void endGame();
  
  //implementation ALREADY provided in children
  protected abstract void updateTitle(String title);
  protected abstract void slowDown(int msDelay);
  protected abstract void speedUp(int msDelay);
  
  

  
  /********************** PRIVATE helper methods,
                        User interface and LOW LEVEL ones
                   Don't worry about ANYTHING below **********************/
  
  private void init(int hdim, int wdim, int timerDelay){
    //allocate the memory and create all the grid cells
    this.grid = new GameGrid(hdim, wdim);
    
    initUI();
    initTiming(timerDelay);
  }
  
  // Display splashscreen, which is still, no animation/update present 
  private void runSplashScreen(String screen){
    
    grid.setSplashScreen(screen);
    splashScreen = true;
    
    while (splashScreen) {
      this.sleep(currentTimerDelay); // requires sleep as in gameLoop()
      // Handle key press to break out of otherwise infinite loop 
      handleKeyPress(); // requires to capture advancing key
                        // default: ENTER 
    }
    
    grid.setSplashScreen(null);
  }
  
  // Display and manage the actual game
  //  - update and render the game until the game is over
  //  - note that rendering (animation) occurs less frequently than
  //    game player update are taken into account
  //      player's input is checked more often than rendering & animation 
  private void gameLoop() {
    // Loop until the game is over: each iteration is a game "tick"
    System.out.println("gameLoop");
    
    while (!isGameOver()){            
      this.sleep(currentTimerDelay); 
      
      // Player's input is checked every tick and even when game is paused
      performGameUpdates();
      if (!isPaused) {
        // Game is rendered/animated only half the time (check FACTOR)
        if (isRenderTick())
          performRenderUpdates();
        
        ticksElapsed++;
      } 
    }   
  }


 
  private void initUI() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);
    getContentPane().add(grid);
    int cellSize = grid.getCellSize();
    
    setPreferredSize(new Dimension(cellSize *  grid.getTotalCols(), 
      cellSize * grid.getTotalRows() + 26)); //border of container
    
    pack();
    setVisible(true);
    System.out.println(cellSize + " " + grid.getInsets().top);
    
    lastKeyPressed = NO_KEY;
    splashScreenAdvancingKey = DEFAULT_SCREEN_ADVANCING_KEY;
    
  }
  
  // pauses execution for provided milliseconds
  // essential to any gameLoop 
  //     -  CPU bandwidth needs to be shared with other running applications 
  //     -  player needs to be given reaction time (20 ms min)
  private static void sleep(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } 
    catch(Exception e) { 
      //shouldn't ever reach here, but try/catch is necessary due to 
      //Java's implementation of Thread.sleep function
    }
  }
    
  // determines if the current iteration is a "render tick" 
  private boolean isRenderTick(){
    return (ticksElapsed % FACTOR == 0);
  }
  
  private void initTiming(int timerDelay) {
    this.currentTimerDelay = timerDelay;
    this.defaultTimerDelay = timerDelay;
  }
  
    
  //returns the last keyboard key pressed 
  private int checkLastKeyPressed() {
    int key = lastKeyPressed;
    lastKeyPressed = NO_KEY;
    return key;
  }
  
  //updates the last keyboard key pressed by the user
  public void keyPressed(KeyEvent e) {
    lastKeyPressed = e.getKeyCode();
    
    if (lastKeyPressed != NO_KEY)
      System.out.println("DEBUG: Key Pressed: " +  keyToString(e));
    
  }
  
  
  
  private static String keyToString(KeyEvent e){
      switch (e.getKeyCode()){
          case KeyEvent.VK_UP:
              return "up arrow";
          case KeyEvent.VK_DOWN:
              return "down arrow";
          case KeyEvent.VK_LEFT:
              return "left arrow";
          case KeyEvent.VK_RIGHT:
              return "right arrow";
          default:
              return Character.toString(e.getKeyChar());
      }  
  }
  
  
  //required by key listener, not implemented
  public void keyReleased(KeyEvent e) { }  
  public void keyTyped(KeyEvent e) { }
    
  
}
