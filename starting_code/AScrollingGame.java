//Student name: Mete Orhun Minbay

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

/**
 * @author Elodie Fourquet 
 * @date March, 2021

 Most of your code for Project 2 for
    + Part I has to be written in this file (read carefully comments below)
    and
    + Part II in a child of this class!
 
 For Part I, make sure your code 
 - relies/make use on its abstract parent class, i.e. call the provided
 features of timing and window level managment the parent is responsible of
 (so skim it enough and often) -- AScrollingGame IS-A AbstractGame
 and
 - calls the GameGrid instance methods, which are declared in the
 interface GridController
 
 For Part II, follow a similar process where the parent class would be this
 one, called from you class CreativeGame
 */

public class AScrollingGame extends AbstractGame {
  
	//---------------- Class Variables and Constants -----------------//
  
  	protected static final int KEY_DEBUG = KeyEvent.VK_D;
 
  	protected static final int KEY_SPEED_UP = KeyEvent.VK_9;
  	protected static final int KEY_SLOW_DOWN = KeyEvent.VK_0;
  	protected static final int KEY_RESET_SPEED = KeyEvent.VK_R;
  
  	protected static final int KEY_MOVE_UP = KeyEvent.VK_UP;
  	protected static final int KEY_MOVE_DOWN = KeyEvent.VK_DOWN;
  	protected static final int KEY_MOVE_LEFT = KeyEvent.VK_LEFT;
  	protected static final int KEY_MOVE_RIGHT = KeyEvent.VK_RIGHT;
  	protected static final int[] MOVE_KEYS = {
  		KEY_MOVE_UP,
  		KEY_MOVE_DOWN,
  		KEY_MOVE_LEFT,
  		KEY_MOVE_RIGHT,
  	};
  	
  	
  	// ADD more, such as  
  	//   public static final int[] MOVE_KEYS = { KEY_MOVE_DOWN, 
  	//                                         ... };
  	
  	//default movement for the movePlayer() method
  	protected static final int DEFAULT_MOVEMENT = 1;
  
  	//USE THIS object! to provide you randomness
  	//  (don't create local Random objects, just use this one)
  	protected static final Random DICE = new Random();   
  
  	protected static final String INTRO_SCREEN = "assets/splash1.png";
  
  	protected static final String[] SPLASH_SCREENS = {
  		"assets/splash1.png",
  		"assets/splash2.png",
  		"assets/splash3.png",
  	};
  	
  	protected static final String PLAYER_IMG = "assets/player_rest_1.png";  // player image file
  	protected static final String AVOID_IMG = "assets/avoid.png";
  	protected static final String GET_IMG = "assets/get.png";
  
  	// ADD more class variables or constants for other assets
  	// USE ArrayList to group different GETs or similarly AVOIDs.
  	protected String splashToDisplay = SPLASH_SCREENS[0];
  
  	protected boolean debug = false;

  	// store last mouse click location 
  	// (use it if you want mouse interaction in your creative game)
  	protected Location clickCoord;
  
  	//game end conditions. NOT declared as static finals as they can be
  	//modified to replay the game with different difficulty!
  	protected static int pointsToWin = 100;
  	protected static int hitsToLose = 3;
  	
  	// make sure to update it
  	protected Location playerCoord;
  
  	protected int score;
  	protected int hits;
  	protected boolean ignoreMovementInput = true;
  	
  	//Variable for populate() method - ensures that there is always a path!
  	protected int lastEntryRow = -1;
  	/**********************     Constructors   **********************/

  	public AScrollingGame() {
  		this(GridController.DEFAULT_GRID_H, GridController.DEFAULT_GRID_W);
  	}
  
  	public AScrollingGame(int grid_h, int grid_w){
  		this(grid_h, grid_w, DEFAULT_TIMER_DELAY);
  	}
  
  	public AScrollingGame(int hdim, int wdim, int init_delay_ms) {
  		super(hdim, wdim, init_delay_ms);        
  	}
  


  	/****************** Abstract Methods of Parent ******************/

  	//----- The three below are implemented for you
  	// CALL them as you complete ScrollingGame features below
  
  	protected void slowDown(int msDelay){
  		//increase gameLoop delay (i.e. sleep) to slow down the game 
  		currentTimerDelay += msDelay;
  	}
  
  	protected void speedUp(int msDelay){
  		//System.out.println("Previous delay " + currentTimerDelay);
  		currentTimerDelay = Math.max(currentTimerDelay - msDelay, MIN_TIMER_DELAY);
  		//System.out.println("New delay " + currentTimerDelay);
  	}
  
  	// update the title bar of the game window 
  	protected void updateTitle(String title) {
  		super.setTitle(title);
  	}  
  
  	//----- The abstract methods below are minimally implemented (dummy to run)
  	// INCOMPLETE abstract methods below: Implement them for Part I
  	// Complete, change and add at your convenience
  
  
  	protected void startGame() {
  		updateTitle("Adventures of Space Rabbit! --- Get ready...");   
  		displaySplashScreen(splashToDisplay);
    
  		// Uncomment/experiment with code to execute many splashscreens
  		/*
  		ArrayList<String> splashImages = new ArrayList<String>();
  		splashImages.add(INTRO_SCREEN);    
  		splashImages.add(..); // another intro screen...
    
  		for (String screen: splashImages)
  			displaySplashScreen(screen);
  		*/
    
  		resetGamePlayParam();
  	}
  
  
  	//Call methods that check for user input
  	//   key press and mouse click
  	protected int performGameUpdates() {
  		clickCoord = handleMouseClick();
  		if (clickCoord != null)
  			System.out.println("Mouse clicked at : " + clickCoord);
    
  		return handleKeyPress();
  	}

 
  
  	//Call methods that modify assets at each "render ticks"
  	//Some assets move each "render ticks", new ones are created
  	protected void performRenderUpdates(){
  		// To be completed
  		scroll();
  		populate();
  	}
  
  	// return true if when game is over, false otherwise 
  	//   (called each gameLoop update iteration)
  	// dummy implementation for now
  	protected boolean isGameOver() {
  		return this.score >= pointsToWin || this.hits >= hitsToLose;
  	}
  	
  	protected boolean isGameWon() {
  		return this.score >= pointsToWin;
  	}
  
  	// similar to startGame, but executed after game play
  	protected void endGame(){
  		ignoreMovementInput = true;
  		if (isGameWon()) {
  			updateTitle("YOU WIN!");
  		}
  		else if (isGameOver()) {
  			updateTitle("GAME OVER...");
  		}
  		//potentially display screen as in startGame
    
  		//minimum
  		//updateTitle("GAME OVER ... //exists in partent
  	}
  
  
  	/****************** ScrollingGame specific instance methods 
      	to be implemented for Part I  ******************/
  
      	//Update game state with new assets 
      	// such as adding in A/G images in the right-most column
    protected void populate() {
    		/*ensures that a path exists. entryRow is a row index that is CERTAIN
    		to be either a G or an empty cell. the cells between the last populated
    		column's entryRow (which is stored in the variable lastEntryRow)
    		are also CERTAIN to be a G or an empty cell.*/
    		int entryRow = generateEntryRow();
    		
    		int pointLocation = DICE.nextInt(grid.getTotalRows());
    		
    		for (int i = 0; i < grid.getTotalRows(); i++) {
    			//int to determine whether the next cell to be spawned is (0) a G asset,
    			//(1) an A asset, or (>1) an empty cell
    			
    			int type = 0;
    			
    			//color and asset of the cell to be spawned
    			String nextAsset = null;
    			Color nextColor = null;
    			
    			
    			
    			//if this cell is a cell between this column's entryRow and the
    			//previous one (lastEntryRow) then this cell should be empty
    			if (isBetween(i, entryRow, lastEntryRow)) {
    				type = 2;
    				//nextColor = Color.BLUE;
    			} 
    			//if it is not, roll dice to choose between empty cell and A asset
    			else {
    				type = DICE.nextInt(3) + 1;	
    			}
    			
    			//if this cell is the point location, spawn point
    			if(i == pointLocation) {
    				type = 0;
    			}

    			switch (type) {
    				case 0:
    					nextAsset = GET_IMG;    					
    					break;
    				case 1:
    					nextAsset = AVOID_IMG;
    					break;
    				default:
    					break;   					
    			}
    			Location toSet = new Location(i, grid.getTotalCols() - 1);
    			//if player is occupying the cell to be populated, collide player
    			//with next asset immediately instead of spawning a new asset in
    			//the player's location
    			if (toSet.equals(playerCoord)) {
    				handleCollisionWith(nextAsset);
    			}
    			//if not, spawn the next asset
    			else {
				grid.setAsset(toSet, nextAsset);
				grid.setAColor(toSet, nextColor);
    			}
    		}
    		
    		//set this column's entryRow to the instance variable lastEntryRow. the
    		//next column to be populated will take it as a reference when choosing
    		//its own entryRow
    		this.lastEntryRow = entryRow;
    }
  
    //Update game state with motions
  	// such as scrolling items from left to right by one column
  	protected void scroll() {
  		for (int i = 0; i < grid.getTotalCols() - 1; i++) {
  			for (int j = 0; j < grid.getTotalRows(); j++) {
  				Location thisLoc = new Location(j, i);
  				Location toScroll = new Location(j, i + 1);
  				
  				//if this cell is the player, collide it with upcoming cell on
  				//the right
  				if (thisLoc.equals(playerCoord)) {
  					checkForCollisionAt(toScroll);
  				}
  				//if it is not, try scrolling the upcoming cell
  				else {
  					//if the upcoming cell is the player, set this cell to null
  					//(instead of scrolling the player to this cell)
  					if (toScroll.equals(playerCoord)) {
  						grid.setAsset(thisLoc, null);
  					}
  					//if it is not, scroll the contents of upcoming cell to
  					//this cell
  					else {
  						grid.moveAsset(toScroll, thisLoc);
  					}
  				}
  			}
  		}
  	}
  
  	//Check for collision with target
  	// could be called with USER for example
  	protected void handleCollisionWith(String target) {
  		if (target == null) {
  			return;
  		}
  		else if (target.equals(GET_IMG)) {
  			this.score += 10;
  		}
  		else {
  			this.hits += 1;
  		}
  		updateTitle(titleText());
  	}
  
  	// Rely on parent and
  	// ADD key press for the game play
  	protected int handleKeyPress() {
    
  		int key = super.handleKeyPress(); //call parent for window level keys
    
  		if (key == KEY_DEBUG) {
    			debug = !debug;     
    			grid.toggleLines(debug);
    		}
    		else if (isMovementKey(key) != -1 && !isPaused && !ignoreMovementInput) {
    			movePlayer(isMovementKey(key), DEFAULT_MOVEMENT);
    		}
    		else if (key == KEY_SPEED_UP) {
    			speedUp(10);
    		}
    		else if (key == KEY_SLOW_DOWN) {
    			slowDown(10);
    		}
    		else if (key == KEY_RESET_SPEED) {
    			resetSpeed();
    		}
    
    		return key;
    }
  
  
    // Consider adding an handleCollisionWith giving a Location. Could be
    // private/protected void/boolean checkForCollisionAt(Location target) {
    //...
    //}
    private void checkForCollisionAt(Location loc) {
    		String toCollide = grid.getAsset(loc);
    		handleCollisionWith(toCollide);
    }

  
  
    /****************************** Helper methods ************************/
    
    // Feel free to add, change or overwite here or in CreativeGame version

    protected void resetGamePlayParam() {
    		score = 0; 
    		hits = 0;
    		lastEntryRow = -1;
    		ignoreMovementInput = false;
    		updateTitle(titleText());
        
    		// EXPERIMENT with the lines below, including the private helper
    		// feel free to CHANGE/COMMENT/DELETE
    		clearGrid();
    		initPlayer();

    		//debug = true;
    		//grid.toggleLines(true);
    		//System.out.println("debug mode " + debug + " grid lines shown");
    
    } 
 
    // Feel free to expand/change
    private void initPlayer() {
 
    		int startRow = grid.getTotalRows() / 2;
    
    		// store and initialize user position
    		playerCoord = new Location(startRow, 0);
    		grid.setAsset(playerCoord, PLAYER_IMG);
    }
    
    //Returns the index of the movement key entered
    protected int isMovementKey(int keyCode) {
    		int result = -1;
    		for (int i = 0; i < MOVE_KEYS.length; i++) {
    			if (keyCode == MOVE_KEYS[i]) {
    				result = i;
    			}
    		}
    		return result;
    }
    
    //Handles player movement. Precondition: index is a movement key
    //returns whether movement was successful (needed for CreativeGame)
    protected boolean movePlayer(int index, int distance) {
    		int changeX = 0;
    		int changeY = 0;
    		//depending on entered key, determine the movement direction
    		switch (index) {
    			case 0:
    				changeY = distance * -1;
    				break;
    			case 1:
    				changeY = distance;
    				break;
    			case 2:
    				changeX = distance * -1;
    				break;
    			default:
    				changeX = distance * 1;
    				break;
    		}
    		//determine the new row and col resulting from the movement
    		int newX = playerCoord.getCol() + changeX;
    		int newY = playerCoord.getRow() + changeY;
    		
    		int boundX = grid.getTotalCols();
    		int boundY = grid.getTotalRows();
    		
    		//if the new row and col would be out of bounds, do nothing
    		if (newX < 0 || newX >= boundX
    			|| newY < 0 || newY >= boundY) {
    			return false;	
    		}
    		
    		//the movement doesn't result in out of bounds. create a new location
    		//for the player, check for collision in that location and move the
    		//player
    		Location nextCoord = new Location(newY, newX);
    		checkForCollisionAt(nextCoord);
    		grid.moveAsset(playerCoord, nextCoord);
    		
    		//update playerCoord
    		playerCoord = nextCoord;
    		return true;
    }
    
    //returns an int for the populate() method. the index returned by this method
    //will make sure that the corresponding row of the populated column is CERTAIN
    //to be a G asset or empty cell. this is useful for making sure there is 
    //a path
    private int generateEntryRow() {
    		//get dimensions of grid
    		int rows = grid.getTotalRows();
    		int cols = grid.getTotalCols();
    		
    		//lastEntryRow's default value is -1: this means that this is the first
    		//column to be populated (beginning of the game). selects a random row
    		//and sets is as the lastEntryRow. returns the random row.
    		if (this.lastEntryRow == -1) {
    			int result = DICE.nextInt(rows);
    			lastEntryRow = result;
    			return result;
    		}
    		
    		/*if lastEntryRow is not -1, it has been modified. lastEntryRow is the
    		row of the previously populated column that is CERTAINLY not an A asset.
    		knowing this, we can set a new entryRow for the current column that is not so
    		far away from the lastEntryRow. by making sure that the rows between
    		lastEntryRow and the current entryRow are not A assets, we can make sure
    		that there is always a fair path for the player*/
    		
    		//returns an integer between -2 and 2. this is because the player can
    		//move 3 cells per scroll (AbstractGame's FACTOR variable). if the entryRow
    		//is too far away from the lastEntryRow (e.g. 5 cells), there is a chance
    		//that all cells in between will be A assets and the player will not be
    		//able to move to the entryRow in time (because they cannot move 5 cells
    		//before the scroll occurs)
    		
    		int entryRowDistance = DICE.nextInt(5) - 2;
    		
    		//try determining a new entryRow for this column based on lastEntryRow 
    		//and the random entryRowDistance
    		int nextEntryRow = lastEntryRow + entryRowDistance;
    		
    		//if this entryRow is out of bounds, try again by computing another
    		while (nextEntryRow < 0 || nextEntryRow >= rows) {
    			entryRowDistance = DICE.nextInt(5) - 2;
    			nextEntryRow = lastEntryRow + entryRowDistance;
    		}
    		return nextEntryRow;
    }
    
    //clears map, useful for running multiple levels in a single instance
    //of Game object
    private void clearGrid() {
    		for (int i = 0; i < grid.getTotalRows(); i++) {
    			for (int j = 0; j < grid.getTotalCols(); j++) {
    				Location toSet = new Location(i, j);
    				grid.setAsset(toSet, null);
    				grid.setAColor(toSet, null);
    			}
    		}
    
    }
    
    //returns title text given the score and hits. will be overriden
    protected String titleText() {
    		return "Scrolling Game --- SCORE " + score + " ;hits " + hits;
    }
    
    //returns true if target is between bound1 and bound2, inclusive
    private boolean isBetween(int target, int bound1, int bound2) {
    		if (bound1 > bound2) {
    			int temp = bound2;
    			bound2 = bound1;
    			bound1 = temp;
    		}
    		return target >= bound1 && target <= bound2;
    }
}
