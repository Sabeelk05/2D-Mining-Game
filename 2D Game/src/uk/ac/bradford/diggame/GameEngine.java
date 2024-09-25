package uk.ac.bradford.diggame;

import java.util.Random;
import uk.ac.bradford.diggame.Tile.TileType;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and moles, as well as updating information when a
 * key is pressed (processed by the InputHandler) while the game is running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 35;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 18;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to place the player and
     * moles, and to randomise movement etc. Passing an integer (e.g. 123) to
     * the constructor called here will give fixed results - the same numbers
     * will be generated every time WHICH CAN BE VERY USEFUL FOR TESTING AND
     * BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional moles and reducing patience etc.
     */
    private int levelNumber = 1;  //current level

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 0;

    /**
     * The current score in this game.
     */
    private int score = 0;

    /**
     * The current mining strength of the player, used to calculate durability
     * reductions when the player mines a tile.
     */
    private int miningStrength = 5;

    /**
     * The GUI associated with this GameEngine object. This link allows the
     * engine to pass level and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles that represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created. This is the array that is used to draw images to the
     * screen by the GUI class.
     */
    private Tile[][] level;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including energy and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player Sabeel;

    /**
     * An array of Mole objects that represents the moles in the current
     * level of the game. Elements in this array should be either an object of
     * the type Mole or should be null (which means nothing is
     * drawn or processed for movement). null values in this array are skipped
     * during drawing and movement processing. Moles that "explode" can be
     * replaced with the value null in this array which
     * removes them from the game, using syntax such as moles[i] = null
     */
    private Mole[] moles;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information
     * to in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. This method should instantiate the level array,
     * which is an attribute of the GameEngine class and is declared above,
     * and then fill it with Tile objects that are created inside this method.
     * It is recommended that for your first version of this method you fill
     * the 2D array using for loops, and create new Tile objects inside the
     * inner loop, assigning them to an element in the array. For the first
     * version you should use just Tile objects with the type EMPTY. You will
     * need to check the constructor from the Tile class in order to create
     * new Tile objects inside your nested loops.
     * 
     * Later tasks will require additions to this method to add new content,
     * see the specification document for more details.
     */
    private void generateLevel() {
        
        level = new Tile[LEVEL_WIDTH][LEVEL_HEIGHT]; //Making a new array named level with dimensions of LEVEL_WIDTH and LEVEL_HEIGHT. 
        for( int i = 0; i < LEVEL_WIDTH; i++){
            for(int j = 0; j < LEVEL_HEIGHT; j++){
            Random random = new Random();
            int type = random.nextInt(100); //generates a random number is between 0 and 99 and assigns the number to a variable type
            if (type < 25) { // if type is less than 25 the code generates a Empty tile
                level[i][j] = new Tile (TileType.EMPTY);
            }
            else if (type >=25 && type < 45){ //if type is greater than or equal to 25 and less than 45, a DIRT tile is generated
                level[i][j] = new Tile(TileType.DIRT);
            }
            else if (type >= 45 && type < 65){ //if type is greater than or equal to 45 and less than 65, a HARD_DIRT tile is generated
                level[i][j] = new Tile(TileType.HARD_DIRT);
            }
            else if (type >= 65 && type < 70){//if type is greater than or equal to 65 and less than 70, a ROCK tile is generated
                level[i][j] = new Tile(TileType.ROCK);
            }
            else if (type >= 70 && type < 82){//if type is greater than or equal to 70 and less than 82, a COPPER tile is generated
                level[i][j] = new Tile(TileType.COPPER);
            }
            else if (type >= 82 && type < 97){//if type is greater than or equal to 82 and less than 97, a SILVER tile is generated 
                level[i][j] = new Tile(TileType.SILVER);
            }
            else if (type >= 97){//if type is greater thanor equal to 97, URANIUM tile is made, had to change this to 97 and the rest of the tiles to make uranium rare
                level[i][j] = new Tile(TileType.URANIUM);
            }
            }
        } 
        
           level[Sabeel.getX()][Sabeel.getY()] = new Tile(TileType.BASE); //assigning TileType.BASE accordingly to the same X and Y Values in which the player spawns 
    }

    /**
     * Adds moles in suitable locations in the current level. The first version
     * of this method should picked fixed positions for moles by calling the
     * constructor for the Mole class and using fixed values for the fullness,
     * X and Y positions of the Mole to be added.
     * 
     * Mole objects created this way should then be added into the
     * moles array that is part of the GameEngine class. Mole objects added
     * to the moles array will then be drawn to the screen using the existing
     * code in the GameGUI class.
     *
     * The second version of this method (described in a later task) should
     * improve the placement of moles by generating random values for the X and
     * Y position of the mole before instantiating the Mole object. You may
     * like to use a loop to do this.
     */
    private void addMoles() {
       
        moles = new Mole[7]; //making an array called moles, which creates 7 mole objects 
        for (int i = 0; i < moles.length; i++) { //loop will stop once it hits 7 mole objects
            int x = (int) (Math.random() * LEVEL_WIDTH); //randomly generates the X-Value for the mole object to spawn accordingly to the LEVEL_WIDTH given   
            int y = (int) (Math.random() * LEVEL_HEIGHT); //randomly generates the Y-Value for the mole object to spawn accordingly to the LEVEL_HEIGHT given 
            moles[i] = new Mole(45,x,y); //assigning attributes to the mole object where maxFullness is equal to 45 and the X-Value is equal to the integer generated from the "int x" line and the Y-Value is equal to the integer generated from the "int y" line 
    
        }
        
    }

    /**
     * Creates a Player object  in the game. The method instantiates the Player
     * class and assigns values for the energy and position.
     *
     * The first version of this method should use a fixed position for
     * the player to start, by setting fixed X and Y values when calling the
     * constructor in the Player class. The object created should be assigned to
     * the player attribute of this class.
     *
     * The second version of this method should use a suitable method to
     * determine where the BASE in the level is and place the Player on the tile
     * representing the BASE.                                                                                                                                          
     *
     */
    private void createPlayer() {
    
    int x = (int) (Math.random() * LEVEL_WIDTH); //randomly generates the X-Value for the Player object to spawn accordingly to the LEVEL_WIDTH given  
    int y = (int) (Math.random() * LEVEL_HEIGHT);//randomly generates the Y-Value for the Player object to spawn accordingly to the LEVEL_HEIGHT given 
    Sabeel = new Player(100, x,y ); //setting the max energy to 100, x-value to the random X-Value generated in "int x" line created above and y-value to the random Y-Value created in "int y" lineabove.
  
    }
   
  
    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the GameInputHandler class when
     * the user has pressed one of the arrow keys on the keyboard. The method
     * should check which direction for movement is required, by checking which
     * character was passed to this method (see parameter description below).
     * Your code should alter the X and Y position of the player to place them
     * in the correct tile based on the direction of movement.
     * 
     * If the target tile is not EMPTY then the player should not
     * be moved, but other effects may happen such as mining. To achieve this,
     * the target tile should be checked to determine the type of tile and
     * appropriate methods called or attribute values changed.
     *
     * @param direction A char representing the direction that the player should
     * move. N is up, S is down, W is left and E is right.
     */
    public void movePlayer(char direction) {
        
        int x = Sabeel.getX();//acquires current X-Value of the obejct Sabeel
        int y = Sabeel.getY();//acquires current Y-Value of the object Sabeel
        switch(direction) {//The switch statement determines which of the direction codes, 'N','E','S','W', to execute. 
                           //The case keyword in the movePlayer() method is used to separate directional chunks of code. Each direction has its own code block. 
           case 'N' -> {//the code block that runs when when the up arrow on the keyboardis hit
                if((level[x][y-1].getType() != TileType.EMPTY && level[x][y-1].getType() != TileType.BASE)){ //Had to change operator from OR to AND. OR operator would cause the base to mine 
                    if(Sabeel.getEnergy() > 0){ //if energy is 0 or less, player will be prevented from mining 
                        level[x][y-1].mine(3);
                        if((level[x][y-1].getType() ==TileType.ROCK)){//if desired TileType == ROCK the code adds 1 to the current score
                            score = score + 1;
                        }
                        else if((level[x][y-1].getType() == TileType.COPPER)){ //if desired TileType == COPPER the code adds 2 to the current score 
                            score = score + 2;
                        }
                        else if((level[x][y-1].getType() == TileType.SILVER)){//if desired TileType == SILVER the code adds 6 to the current score 
                            score = score + 6;
                        }
                        else if((level[x][y-1].getType() == TileType.URANIUM)){//if desired TileType == URANIUM the code adds 12 to the current score 
                            score = score + 12;
                        }
                        Sabeel.changeEnergy(-1);
                    }
                }
                if((level[x][y-1].getType() == TileType.BASE)){ 
                    Sabeel.refillEnergy(); //if desired tile is TypeTile.BASE, the method is called to refill the players energy.
                }    
                    if((level[x][y-1].getType() == TileType.EMPTY || level[x][y-1].getType() == TileType.BASE)){
                        if( y > 0){ //only execute the change in the y-value if y is greater than 0. Prevents the situation where y = -1 which then the object goes off screen 
                            y = y - 1;
                        }
                        break;
                    }
            }
            case 'E' -> {//the code block that runs when when the right arrow on the keyboardis hit
                if((level[x+1][y].getType() != TileType.EMPTY && level[x+1][y].getType() != TileType.BASE)){
                    if(Sabeel.getEnergy() > 0){//if energy is 0 or less, player will be prevented from mining
                        level[x+1][y].mine(3);
                        if((level[x+1][y].getType() ==TileType.ROCK)){//if desired TileType == ROCK the code adds 1 to the current score
                            score = score + 1;
                        }
                        else if((level[x+1][y].getType() == TileType.COPPER)){ //if desired TileType == COPPER the code adds 2 to the current score 
                            score = score + 2;
                        }
                        else if((level[x+1][y].getType() == TileType.SILVER)){//if desired TileType == SILVER the code adds 6 to the current score 
                            score = score + 6;
                        }
                        else if((level[x+1][y].getType() == TileType.URANIUM)){//if desired TileType == URANIUM the code adds 12 to the current score 
                            score = score + 12;
                        }
                        Sabeel.changeEnergy(-1);
                    }    
                }
                if((level[x+1][y].getType() == TileType.BASE)){
                    Sabeel.refillEnergy();//if desired tile is TypeTile.BASE, the method is called to refill the players energy.
                }
                    if((level[x+1][y].getType() == TileType.EMPTY|| level[x+1][y].getType() == TileType.BASE)){
                        if( x < LEVEL_WIDTH){//only execute the change in the x-value if x is less than LEVEL_WIDTH
                            x = x + 1;
                        }
                        break;
                    }    
            }
            case 'S' -> {//the code block that runs when when the down arrow on the keyboardis hit
                if((level[x][y+1].getType() != TileType.EMPTY && level[x][y+1].getType() != TileType.BASE)){
                    if(Sabeel.getEnergy() > 0){//if energy is 0 or less, player will be prevented from mining
                        level[x][y+1].mine(3);
                        if((level[x][y+1].getType() ==TileType.ROCK)){//if desired TileType == ROCK the code adds 1 to the current score
                            score = score + 1;
                        }
                        else if((level[x][y+1].getType() == TileType.COPPER)){ //if desired TileType == COPPER the code adds 2 to the current score 
                            score = score + 2;
                        }
                        else if((level[x][y+1].getType() == TileType.SILVER)){//if desired TileType == SILVER the code adds 6 to the current score 
                            score = score + 6;
                        }
                        else if((level[x][y+1].getType() == TileType.URANIUM)){//if desired TileType == URANIUM the code adds 12 to the current score 
                            score = score + 12;
                        }
                        Sabeel.changeEnergy(-1);
                    }    
                }
                if((level[x][y+1].getType() == TileType.BASE)){
                    Sabeel.refillEnergy();//if desired tile is TypeTile.BASE, the method is called to refill the players energy.
                }
                    if((level[x][y+1].getType() == TileType.EMPTY|| level[x][y+1].getType() == TileType.BASE)){
                        if( y < LEVEL_HEIGHT){//only execute the change in the y-value if y is less than LEVEL_HEIGHT
                            y = y + 1;
                        }
                        break;
                    }
            }
            case 'W' -> {//the code block that runs when when the left arrow on the keyboardis hit
                if((level[x-1][y].getType() != TileType.EMPTY && level[x-1][y].getType() != TileType.BASE)){
                    if(Sabeel.getEnergy() > 0 ){//if energy is 0 or less, player will be prevented from mining
                        level[x-1][y].mine(3);
                        if((level[x-1][y].getType() ==TileType.ROCK)){//if desired TileType == ROCK the code adds 1 to the current score 
                            score = score + 1;
                        }
                        else if((level[x-1][y].getType() == TileType.COPPER)){ //if desired TileType == COPPER the code adds 2 to the current score 
                            score = score + 2;
                        }
                        else if((level[x-1][y].getType() == TileType.SILVER)){//if desired TileType == SILVER the code adds 6 to the current score 
                            score = score + 6;
                        }
                        else if((level[x-1][y].getType() == TileType.URANIUM)){//if desired TileType == URANIUM the code adds 12 to the current score 
                            score = score + 12;
                        }
                        Sabeel.changeEnergy(-1);
                    }    
                }
                if((level[x-1][y].getType() == TileType.BASE)){
                    Sabeel.refillEnergy();//if desired tile is TypeTile.BASE, the method is called to refill the players energy.
                }
                    if ((level[x-1][y].getType() == TileType.EMPTY || level[x-1][y].getType() == TileType.BASE)){
                        if( x > 0){//only execute the change in the x-value if x is greater than 0. Prevents the situation where x = -1 which then the object goes off screen
                            x = x - 1;
                        }
                        break;
                    }
            }

            
        }
            Sabeel.setPosition(x,y);// update the players current x and y value 

            } 

    /**
     * Moves all moles on the current level. This method iterates over all
     * elements of the moles array (using a for loop) and checks if each one is
     * null (using an if statement inside that for loop). For every element of
     * the array that is NOT null, this method calls the moveMole method and
     * passes it the current array element (i.e. the current mole object
     * being used in the loop).
     */
    private void moveAllMoles() {
        for (int i = 0; i < moles.length; i++) { // a for loop is used, and will only loop x many times depnding on how many mole objects there are 
            if (moles[i] != null){ //an if statement is used to check if the moles created in the array are null or not. 
                moveMole(moles[i]);//if the moles are not == null the moveMole method is called in which the mole obejcts are passed onto
            }
        }
        
    }

    /**
     * Moves a specific mole in the game. The method updates the X and Y
     * attributes of the Mole object passed to the method to set its new
     * position.
     *
     * @param m The Mole that needs to be moved
     */
    private void moveMole(Mole m) {
        int direction =(int) (Math.random() * 4); // in this line, the math random function will pick an integer between 0-3 and then 4 case statements are assigned to each number for each direction     
         int x = m.getX(); 
         int y = m.getY();
        if(Sabeel.getEnergy() != 0){// if player energy is zero, do not run the mole movement code. 
            if(direction == 0){
                if((level[x][y-1].getType() != TileType.EMPTY && level[x][y-1].getType() != TileType.BASE)){ //if the desired tile isn't EMPTY/BASE the mole will mine the tile 
                    level[x][y-1].mine(2);
                    // m.changeFullness(level[x][y-1].getMaxDurability());
                }
                
                if( y > 0 ){
                    if((level[Sabeel.getX()][Sabeel.getY()].getType() != level[m.getX()][m.getY()].getType())){// if the desired Tile the mole is going to move to is not equal to the TileType the player is already in, the code allows the mole to move
                        y = y - 1;
                    }
                }

            }
            else if(direction == 1){
                if((level[x+1][y].getType() != TileType.EMPTY && level[x+1][y].getType() != TileType.BASE)){//if the desired tile isn't EMPTY/BASE the mole will mine the tile 
                    level[x+1][y].mine(2);
                    // m.changeFullness(level[x+1][y].getMaxDurability());
                }
                if(x < LEVEL_WIDTH){
                    if((level[Sabeel.getX()][Sabeel.getY()].getType() != level[m.getX()][m.getY()].getType())){// if the desired Tile the mole is going to move to is not equal to the TileType the player is already in, the code allows the mole to move
                        x = x + 1;
                    }    
                }    
            }
            else if(direction == 2){
                if((level[x][y+1].getType() != TileType.EMPTY && level[x][y+1].getType() != TileType.BASE)){//if the desired tile isn't EMPTY/BASE the mole will mine the tile 
                    level[x][y+1].mine(2);
                    // m.changeFullness(level[x][y+1].getMaxDurability());
                }
                if(y < LEVEL_HEIGHT){
                    if((level[Sabeel.getX()][Sabeel.getY()].getType() != level[m.getX()][m.getY()].getType())){// if the desired Tile the mole is going to move to is not equal to the TileType the player is already in, the code allows the mole to move
                        y = y + 1;
                    }    
                }    
            }
            else if(direction == 3){
                if((level[x-1][y].getType() != TileType.EMPTY && level[x-1][y].getType() != TileType.BASE)){//if the desired tile isn't EMPTY/BASE the mole will mine the tile 
                    level[x-1][y].mine(2);
                    // m.changeFullness(level[x-1][y].getMaxDurability());
                }
                if(x > 0){
                    if((level[Sabeel.getX()][Sabeel.getY()].getType() != level[m.getX()][m.getY()].getType())){// if the desired Tile the mole is going to move to is not equal to the TileType the player is already in, the code allows the mole to move
                        x = x - 1;
                    }    
                }    
            }
        }
        m.setPosition(x, y);// Update current x and y value of the mole 
        
    }    
    /**
     * This method is used to make a mole "explode" when its fullness value
     * reaches or exceeds its maximum fullness. This method should store the
     * mole's X and Y co-ordinates and then "mine" the tiles around this
     * position out to a fixed radius.
     * @param m the mole that is exploding
     */
    private void explode(Mole m) {
        //YOUR CODE HERE
        // int x = m.getX();// acquires moles current X-Value 
        // int y = m.getY();// acquires moles current Y-Value
        // if( m.getFullness() > m.getMaxFullness()){
        //     if( y >= 2 ){// if Y-Value acquired is greater than or equal to 2, mine 
        //         level[x][y-2].mine(100);
        //     }
        //     if( y <= 15){// if Y-Value acquired is less than or equal to 15, mine 
        //         level[x][y+2].mine(100);
        //     }
        //     if(x <= 32){// if X-Value acquired is less than or equal to 32, mine 
        //         level[x+2][y].mine(100);
        //     }
        //     if(x >= 2){// if X-Value is greater than or equal to 2, mine 
        //         level[x-2][y].mine(100);
        //     }
        // }
    }
    
    /**
     * This method should iterate over the moles array, checking each Mole
     * object to see if its current fullness os greater than or equal to its
     * maximum fullness (i.e. it "exploded" this turn). If it has, it should 
     * be set to null in the moles array. You will need to check if the array
     * element currently being examined is null, before you attempt to call
     * any methods on the array element.
     */
    private void clearExplodedMoles() {
        //YOUR CODE HERE
        
        
    }

    /**
     * This method is called when the player "mines" all ore tiles (i.e. 
     * COPPER, SILVER and URANIUM tiles, and returns to the BASE "completing"
     * the level.
     *
     * This method should increase the current level number, create a new level
     * by calling the generateLevel method and setting the level attribute using
     * the returned 2D array, add new Moles, and finally place the player in
     * the new level.
     * 
     */
    private void nextLevel() {
        //YOUR CODE HERE
    }

    /**
     * The first version of this method should place the player in the game
     * level by setting new, fixed X and Y values for the player object in this
     * class.
     *
     * The second version of this method in a later task should place the player
     * in a game level by choosing a position corresponding to a BASE tile.
     */
    private void placePlayer() {
        //YOUR CODE HERE
    }

    /**
     * Checks if all "ore tiles" (copper, silver and uranium) have been mined
     * in the level, i.e. if no ore tiles remain in the level array in this class.
     * This method should iterate over the entire 2D level array and if an ore
     * tile is found it shoudl return true. If all elements in the level array
     * have been searched and no ore tiles were found then it should return false.
     * 
     * @return true if no ore tiles exist in the level, false otherwise.
     */
    private boolean allOreMined() {
        //YOUR CODE HERE
        return false;   //return the calculated value here instead of false
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method clears exploded moles, periodically moves any moles
     * in the level, and increments the turn number. Finally it requests
     * the GUI to redraw the game level by passing it the level, player and
     * moles objects for the current level.
     * 
     */
    public void doTurn() {
        turnNumber++;
        if (turnNumber % 4 == 0) {
            moveAllMoles();
        }
        clearExplodedMoles();
        gui.updateDisplay(level, Sabeel, moles);
    }

    /**
     * Starts a game. This method generates a level, adds moles and the player
     * and then requests the GUI to update the level on screen using the
     * information on level, player and moles.
     */
    public void startGame() {
        createPlayer();
        addMoles();       // Switched the order in which startGame method occurs. Switched createPlayer() with generateLevel(), so that the TypeTile.BASE can get randomly generated with the same x and y values of the createPlayer() method 
        generateLevel();
        gui.updateDisplay(level, Sabeel, moles);
    }
}