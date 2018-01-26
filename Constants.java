
/**
 * Contains all of the constants used in this program for easy adjustment. This is like the config
 * file for the whole program.
 * 
 * @author Clay DiGiorgio
 * @version 1.0
 */
public final class Constants{
    //NodeConnection-specific constants
    public static final int INITIAL_CONNECTION_STRENGTH = 1;
    
    //Node/SensoryNode-specific constants
    public static final int MAX_SENSORY_EXCITEMENT = 5;
    
    //NodeMap/Creature shared constants
    public static final int CREATURE_SENSORY_COUNT = 4; //DO NOT CHANGE THIS!!
    public static final int CREATURE_NODE_COUNT = 25;
    public static final double CREATURE_STARTING_FOOD = 50;
    public static final double CREATURE_HUNGER_RATE = 0.01;
    public static final double CREATURE_REPRODUCTION_THRESHOLD = 51; //2 * CREATURE_STARTING_FOOD;
    
    //NodeMap-specific constants
    public static final int NODE_MAP_STARTING_EXCITEMENT = MAX_SENSORY_EXCITEMENT * (CREATURE_SENSORY_COUNT + 1);
    public static final int NODE_SIZE = 32;
    public static final double NODE_DIAGONAL_SIZE = Math.sqrt(2 * NODE_SIZE * NODE_SIZE);
    public static final int SPACE_BETWEEN_NODES = 10;
    
    //World-specific constants
    public static final int WORLD_GRID_SIZE = 20;
    public static final int WORLD_TILE_SIZE = 32;
    public static final int CREATURE_COUNT = 3;
    public static final int FOOD_COUNT = 15;
    public static final int WALL_COUNT = 75;
    
    public static final int GRID_EMPTY_SPACE = 0;
    public static final int GRID_WALL = 1;
    public static final int GRID_FOOD = 3;
    
    public static final int FOOD_QUALITY = 1;
    
    //Main-specific constants
    public static final int TIME_BETWEEN_LOOPS = 100;
    
    //code used from: http://www.javapractices.com/topic/TopicAction.do?Id=2
    private Constants(){
        //this prevents even the native class from 
        //calling this constructor as well
        throw new AssertionError();
    }
}
