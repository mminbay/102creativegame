Student name: Mete Orhun Minbay
---
THEME & CONCEPT

"Adventures of the Space Rabbit" is a side scroller game where we play as the "space rabbit" who wants to buy a new pair of sunglasses.
To do so, space rabbit needs to collect a certain number of carrots throughout the game. However, enemy snakes will attack space rabbit
to make things harder. Hitting a snake loses a life, and losing all lives in a stage loses the game. If space rabbit collects enough 
carrots, the game will proceed to the next stage. Completing the three stages wins the game.
---
ADDITIONAL FEATURES COMPARED TO ASCROLLINGGAME.JAVA

My CreativeGame class extends the AScrollingGame class. Hence, I made some adjustments to the parent classes in order to be able to implement
the new features in a better way. In AbstractGame, for example, I had to change lastKeyPressed to protected in order to make use of it in 
CreativeGame. Some methods in AScrollingGame have also been modified to work with multiple parameters instead of constant values, and some new
variables have been added.

New feature #1: Jump powerup
Pressing the space bar causes the space rabbit to move 3 squares in the last direction the user had moved. The 2 squares that have been skipped
do not count as collisions. This power uses 30 points to use, and having less than 30 points will result in the power not working.

New feature #2: Obstacle walls

In contrast to the AScrollingGame populate() method which always ensured that there was a fair path for the player, CreativeGame is edited to
occasionally spawn entire columns of obstacles that are impossible to pass through by moving regularly. This was added to encourage (and force)
the player to use the Jump powerup. Walls will not spawn unless the player has enough points to jump. Also, a second wall will not spawn until at
least 8 columns have been populated normally since the last wall. This ensures that the difficulty is manageable and that the player has enough
time to gather a few points until another wall spawns.

The fair path algorithm is maintained in CreativeGame and works with the obstacle walls. After a wall is spawned, the fair path continues where it
left off before the wall was spawned (this is not visible as the fair path is no longer colored blue, but uncommenting line 235 in AScrollingGame
restores the blue color).

New feature #3: Multiple levels

CreativeGame has a new run() method that takes an amount of levels to run. Each level modifies game variables (points required to win, hits allowed to
lose, splash screens to display) before running the game, allowing for multiple levels with different difficulties to be run from a single instance
of CreativeGame.

New feature #4: Additional player assets
There are 6 new assets for the player, which are (1) resting, (2) jumping, (3) point acquired, (4) hit suffered, (5) won, (6) lost. handleCollisionWith()
method is modified to change the player asset as well; this results in the player asset changing for every movement and every column spawn. Assets (1)
and (2) are alternated between regularly to create a "hopping" animation when the player is moving. Grabbing a point changes to (3), hitting an enemy
changes to (4), winning the game changes to (5) and losing changes to (6).
---
USED ASSETS

The assets for the player, enemies, and carrots were drawn by me on pixilart.com.

Background image for the game was provided from https://www.pexels.com/tr-tr/fotograf/gece-boyunca-samanyolu-gokadasi-1252890/

Background image for the splash screens was provided from https://www.pexels.com/tr-tr/fotograf/gri-ve-siyah-galaxy-duvar-kagidi-2150/

Splash screens were created on canva.com. Images in the splash screens such as the arrows and the sunglasses were provided by canva.com.