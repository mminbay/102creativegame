//Student name: Mete Orhun Minbay


import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

class CreativeGame extends AScrollingGame {
	/**********************     Constants   **********************/
	
	protected static final int JUMP_MOVEMENT = 3;
	protected static final int KEY_JUMP = KeyEvent.VK_SPACE;
	protected static final int JUMP_COST = 30;
	
	protected static final int WALL_COOLDOWN = 7;
	
	protected static final String PLAYER_IMG_1 = "assets/player_rest_1.png";
	protected static final String PLAYER_IMG_2 = "assets/player_rest_2.png";
	protected static final String PLAYER_HIT = "assets/player_hit.png";
	protected static final String PLAYER_SCORE = "assets/player_score.png";
	protected static final String PLAYER_LOST = "assets/player_lost.png";
	protected static final String PLAYER_WON = "assets/player_won.png";
	protected static final String BACKGROUND = "assets/background.jpg";
	
	/**********************     Instance Variables   **********************/
	
	//indicates whether player animation is "jumping" or "resting"
	protected boolean playerAssetJumping = true;
	
	protected int level = 1;
	
	protected int lastMovementDirection = -1;
	
	protected int columnsSinceLastWall = 0;
	
  	/**********************     Constructors   *********************/

  	public CreativeGame() {
  		super();
  		this.grid.setBackgroundImage(BACKGROUND);
  	}
  
  	public CreativeGame(int grid_h, int grid_w){
  		super(grid_h, grid_w);	
  		this.grid.setBackgroundImage(BACKGROUND);
  	}
  
  	public CreativeGame(int hdim, int wdim, int init_delay_ms) {
  		super(hdim, wdim, init_delay_ms);   		
  		this.grid.setBackgroundImage(BACKGROUND);
  	}
  	
  	/********************* Overriden Methods ***********************/
  	
  	//handleKeyPress is modified to let player use the "jump" powerup
  	protected int handleKeyPress() {
  		int key = super.handleKeyPress();
  		
  		if (key == KEY_JUMP && !isPaused && !ignoreMovementInput) {
  			jumpPlayer();
  		}
  		
  		return key;
  	}
  	
  	protected String titleText() {
  		return "Adventures of Space Rabbit! --- Level " + level
  		+ " | SCORE: " + this.score + "/" + this.pointsToWin
  		+ " | hits: " + this.hits + "/" + this.hitsToLose;
  	}
  	
  	/*Special Feature #3: Walls
  	to encourage the player to use the "jump" powerup, populate() now
  	randomly spawns walls of A assets that are impossible to cross without
  	using the jump feature. walls will not spawn if player does not have
  	enough points to jump
  	*/
  	protected void populate() {
  		columnsSinceLastWall++;
  		if (this.score >= JUMP_COST && columnsSinceLastWall >= WALL_COOLDOWN) {
  			int spawnWall = DICE.nextInt(6);
  			if (spawnWall == 0) {
  				columnsSinceLastWall = 0;
  				
  				System.out.println("Spawning wall...");
  				for (int i = 0; i < grid.getTotalRows(); i++) {
  					Location toSet = new Location(i, grid.getTotalCols() - 1);
  					if (toSet.equals(playerCoord)) {
  						handleCollisionWith(AVOID_IMG);
    					}
    					else {
    						grid.setAsset(toSet, AVOID_IMG);
					}
				}
				return;
  			}
  		}
  		super.populate();
  	}
  	
  	//update player asset at game end
  	protected void endGame() {
  		super.endGame();
  		if (isGameWon()) {
  			updatePlayerAsset(PLAYER_WON);
  		} else {
  			updatePlayerAsset(PLAYER_LOST);
  		}
  	}
  	
  	
  	//movePlayer is modified to keep track of the last direction the
  	//player moved in (useful for the "jump" powerup)
  	protected boolean movePlayer(int index, int distance) {
  		lastMovementDirection = index;
  		
  		return super.movePlayer(index, distance);
  	}
  	
  	//need to override this method for smoothness of the jump power
  	public void keyPressed(KeyEvent e) {
  		if (e.getKeyCode() == KEY_JUMP) {
  			int index = isMovementKey(lastKeyPressed);
  			if (index != -1) {
  				lastMovementDirection = index;
  			}
  		}
  		super.keyPressed(e);
  	}
  	
  	//overriding this method to handle player animations as well.
  	//player asset is updated for every time the player moves and 
  	//when the map scrolls. handleCollisionWith() is also called
  	//for all these instances, so we can update the player asset
  	//in that function
  	
  	protected void handleCollisionWith(String target) {
  		super.handleCollisionWith(target);
  		updatePlayerAsset(target);
  	}
  	

  	
  	/********************* New Methods ***********************/
  	
  	/*Special Feature #1: jump powerup 
  	causes player to skip over 2 cells towards the last direction they
  	moved. collision IS NOT considered for the skipped cells.
  	jumping costs points!
  	*/
  	protected void jumpPlayer() {
  		//if lastMovementDirection exists and the player collected a powerup,
  		//try jumping
  		if (lastMovementDirection != -1 && this.score >= JUMP_COST) {
  			//if jump is successful, remove one powerup
  			if (this.movePlayer(lastMovementDirection, JUMP_MOVEMENT)) {
  				this.score -= JUMP_COST;
  				updateTitle(titleText());
  			};		
  		}
  	}
  	
  	
  	/* Special Feature #4: Animations for player */
  	protected void updatePlayerAsset(String target) {
  		if (target == null) {
  			if (playerAssetJumping) {
  				this.grid.setAsset(playerCoord, PLAYER_IMG_1);
  			} else {
  				this.grid.setAsset(playerCoord, PLAYER_IMG_2);
  			}
  			playerAssetJumping = !playerAssetJumping;
  			return;
  		}
  		else if (target.equals(GET_IMG)) {
  			this.grid.setAsset(playerCoord, PLAYER_SCORE);
  		}
  		else if (target.equals(PLAYER_LOST)) {
  			this.grid.setAsset(playerCoord, PLAYER_LOST);
  		}
  		else if (target.equals(PLAYER_WON)) {
  			this.grid.setAsset(playerCoord, PLAYER_WON);
  		}
  		else {
  			this.grid.setAsset(playerCoord, PLAYER_HIT);
  		}
  	}
  	
  	/* Special Feature #2: Multiple levels
  	the same CreativeGame object can be run multiple times with increasing
  	difficulty */
  	
  	//new run() method that takes parameter that let the game be run
  	//multiple times. modifies game variables and calls super.run()
  	public void run(int levels) {
  		int level = 0;
  		do {
  			this.splashToDisplay = SPLASH_SCREENS[level];
  			this.pointsToWin = 100 + (level * 50);
  			this.hitsToLose = 3 - level;
  			this.level = level + 1;
  			if (this.hitsToLose < 1) {
  				this.hitsToLose = 1;
  			}
  			super.run();
  			
  			level++;
  		} while (level < levels && isGameWon());  		
  	}
}