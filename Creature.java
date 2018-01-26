
/**
 * This is the object that handles a NodeMap's interaction with other NodeMaps and the world around it
 * 
 * @author Clay DiGiorgio
 * @version 2.0
 */
public class Creature{
    public static int ID_COUNT = 0;
    
    public int age = 0;
    public int id = ID_COUNT;
    
    NodeMap nodeMap = new NodeMap(Constants.CREATURE_NODE_COUNT);
    public int horizontalMovement = 0, verticalMovement = 0;
    public final int upNode = Constants.CREATURE_SENSORY_COUNT, downNode = Constants.CREATURE_SENSORY_COUNT + 1, leftNode = Constants.CREATURE_SENSORY_COUNT + 2, rightNode = Constants.CREATURE_SENSORY_COUNT + 3;
    public int worldX, worldY;
    
    public double food = Constants.CREATURE_STARTING_FOOD;
    
    public Creature(){
        ID_COUNT++;
        nodeMap.setFrameTitle("" + id);
    }
    
    public void update(){
        age++;
        
        nodeMap.update();
        
        horizontalMovement = nodeMap.nodes.get(upNode).getExcitement() - nodeMap.nodes.get(downNode).getExcitement();
        verticalMovement = nodeMap.nodes.get(rightNode).getExcitement() - nodeMap.nodes.get(leftNode).getExcitement();
        
        //restrict mazimum movement to two tiles
        horizontalMovement = (horizontalMovement > 2)? 2 : horizontalMovement;
        verticalMovement   = (verticalMovement   > 2)? 2 : verticalMovement;
        
        food -= Constants.CREATURE_HUNGER_RATE;
    }
    
    public void sense(int[][] grid, int x, int y){
        int above = grid[x][y-1];
        int below = grid[x][y+1];
        int left  = grid[x-1][y];
        int right = grid[x+1][y];
        
        if(above > 0){
            ((SensoryNode)nodeMap.nodes.get(0)).senseIntensity = above;
        }
        if(below > 0){
            ((SensoryNode)nodeMap.nodes.get(1)).senseIntensity = below;
        }
        if(left > 0){
            ((SensoryNode)nodeMap.nodes.get(2)).senseIntensity = left;
        }
        if(right > 0){
            ((SensoryNode)nodeMap.nodes.get(3)).senseIntensity = right;
        }
        
        if(above < 0){
            ((SensoryNode)nodeMap.nodes.get(0)).senseIntensity = 2;
        }
        if(below < 0){
            ((SensoryNode)nodeMap.nodes.get(1)).senseIntensity = 2;
        }
        if(left < 0){
            ((SensoryNode)nodeMap.nodes.get(2)).senseIntensity = 2;
        }
        if(right < 0){
            ((SensoryNode)nodeMap.nodes.get(3)).senseIntensity = 2;
        }
    }
    
    public Creature clone(){
        Creature clone = new Creature();
        clone.nodeMap = nodeMap.clone();
        nodeMap.setFrameTitle("" + id);
        
        return clone;
    }
}
