import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Color;

/**
 * This class is the top-level class as far as simulation is concerned. It contains and updates
 * all of the creature objects and the walls and food, as well as the space between them.
 * 
 * @author Clay DiGiorgio
 * @version 2.0
 */
public class World{
    ArrayList<Creature> creatures = new ArrayList<Creature>();
    int[][] grid = new int[Constants.WORLD_GRID_SIZE][Constants.WORLD_GRID_SIZE];

    //display variables
    JFrame frame = new JFrame("Simulation World");
    JPanel panel = new JPanel();
    ArrayList<JLabel> creatureLabels = new ArrayList<JLabel>();
    ArrayList<JLabel> foodLabels = new ArrayList<JLabel>();
    ArrayList<JLabel> wallLabels = new ArrayList<JLabel>();

    public World(){
        //fill the grid array with 0s, to show that it's empty
        for(int i = 0; i < Constants.WORLD_GRID_SIZE; i++){
            for(int j = 0; j < Constants.WORLD_GRID_SIZE; j++){
                grid[i][j] = Constants.GRID_EMPTY_SPACE;
            }
        }

        //instantiate all of the creatures and put them on the grid
        for(int i = 0; i < Constants.CREATURE_COUNT; i++){
            Creature c = new Creature();
            creatures.add(c);

            //pick a random, unnocupied grid space
            int x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
            int y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);

            while(grid[x][y] != Constants.GRID_EMPTY_SPACE){
                x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
                y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
            }

            grid[x][y] = -c.id;
            c.worldX = x; 
            c.worldY = y;
        }

        //fill the world with food and walls
        JLabel foodLabel;
        for(int i = 0; i < Constants.FOOD_COUNT; i++){
            spawnFood(-1);
        }

        JLabel wallLabel;
        for(int i = 0; i < Constants.WALL_COUNT; i++){
            //pick a random, unnocupied grid space
            int x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
            int y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);

            while(grid[x][y] != Constants.GRID_EMPTY_SPACE){
                x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
                y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
            }

            grid[x][y] = Constants.GRID_WALL;

            //add the wall to the display
            wallLabel = new JLabel(new ImageIcon("imgs/wall.png"));
            wallLabel.setSize(Constants.WORLD_TILE_SIZE, Constants.WORLD_TILE_SIZE);
            wallLabel.setLocation(x * Constants.WORLD_TILE_SIZE, y * Constants.WORLD_TILE_SIZE);
            panel.add(wallLabel);
            wallLabels.add(wallLabel);
        }

        //set up display for creatures
        for(Creature c : creatures){
            JLabel l = new JLabel(new ImageIcon("imgs/creature.gif"));
            l.setSize(Constants.WORLD_TILE_SIZE, Constants.WORLD_TILE_SIZE);
            l.setLocation(c.worldX * Constants.WORLD_TILE_SIZE, c.worldY * Constants.WORLD_TILE_SIZE);

            panel.add(l);
            creatureLabels.add(l);
            
            //Label each creature with its id number
            l.setForeground(Color.WHITE);
            l.setText("" + c.id);
            l.setHorizontalTextPosition(JLabel.CENTER);
            l.setFont(l.getFont().deriveFont(12.0f));
        }

        panel.setLayout(null);
        panel.setSize(Constants.WORLD_GRID_SIZE * Constants.WORLD_TILE_SIZE, Constants.WORLD_GRID_SIZE * Constants.WORLD_TILE_SIZE);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((Constants.WORLD_GRID_SIZE + 2) * Constants.WORLD_TILE_SIZE, (Constants.WORLD_GRID_SIZE+2) * Constants.WORLD_TILE_SIZE);
        frame.setVisible(true);
    }

    /* * * * * * * * * * * * * * *
     *                           *
     *   SIMULATION FUNCTIONS    *
     *                           *
     * * * * * * * * * * * * * * */

    //this function serves to make retriving tile data from the int array easy without getting errors
    public int getGrid(int x, int y){
        if(x >= Constants.WORLD_GRID_SIZE || y >= Constants.WORLD_GRID_SIZE || x < 0 || y < 0){
            return Constants.GRID_WALL;
        } else {
            return grid[x][y];
        }
    }

    public void update(){
        ArrayList<Creature> remove = new ArrayList<Creature>();
        ArrayList<Creature> add = new ArrayList<Creature>();
        //update all creatures
        for(Creature c : creatures){
            c.update();

            //check for movement
            int checkTile = getGrid(c.worldX + c.horizontalMovement, c.worldY + c.verticalMovement);
            if(checkTile == Constants.GRID_EMPTY_SPACE){ //if the tile is empty space, move the creature
                grid[c.worldX + c.horizontalMovement][c.worldY + c.verticalMovement] = -c.id;
                grid[c.worldX][c.worldY] = 0;

                c.worldX += c.horizontalMovement;
                c.worldY += c.verticalMovement;
            } else if (checkTile == Constants.GRID_FOOD){ //if the tile is food, have the creature eat it and spawn a new one    
                grid[c.worldX + c.horizontalMovement][c.worldY + c.verticalMovement] = -c.id;
                grid[c.worldX][c.worldY] = 0;

                c.worldX += c.horizontalMovement;
                c.worldY += c.verticalMovement;

                c.food += Constants.FOOD_QUALITY;

                //find which foodLabel corresponds to the eaten food
                int indexOfEaten = -1;
                for(int i = 0; i < foodLabels.size(); i++){
                    if(foodLabels.get(i).getX() == (int)(c.worldX * Constants.WORLD_TILE_SIZE) && foodLabels.get(i).getY() == (int)(c.worldY * Constants.WORLD_TILE_SIZE)){
                        indexOfEaten = i;
                        break;
                    }
                }
                spawnFood(indexOfEaten);
            }
            
            //check for reproduction/death
            if(c.food > Constants.CREATURE_REPRODUCTION_THRESHOLD){ System.out.println(c.id + "   budding");
                Creature baby = c.clone();
                
                //find the nearest empty tile
                int foundX = -1;
                int foundY = -1;
                
                //check each ring of tiles surrounding the parent, starting with the smallest ring
                int checkX = c.worldX;
                int checkY = c.worldY;
                for(int radius = 1; radius <= Constants.WORLD_GRID_SIZE; radius++){
                    for(int dx = -radius; dx <= radius; dx++){
                        for(int dy = -radius; dy <= radius; dy++){
                            if(Math.abs(dx+dy) > radius){ //only preform the check if this tile hasn't been checked before
                                if(getGrid((c.worldX + dx),(c.worldY + dy)) == Constants.GRID_EMPTY_SPACE){ //if this tile is empty, record it and end the search
                                    foundX = c.worldX + dx; foundY = c.worldY + dy; break;
                                }
                            }
                        }
                    }
                }
                
                //if a tile was found, place the baby and evenly divide the parent's food
                if(foundX != -1 && foundY != -1){
                    grid[foundX][foundY] = -baby.id;
                    add.add(baby);
                    
                    c.food /= 2;
                    baby.food = c.food;
                    
                    //add a JLabel of the baby to the main frame
                    JLabel l = new JLabel(new ImageIcon("imgs/creature.gif"));
                    l.setSize(Constants.WORLD_TILE_SIZE, Constants.WORLD_TILE_SIZE);
                    l.setLocation(c.worldX * Constants.WORLD_TILE_SIZE, c.worldY * Constants.WORLD_TILE_SIZE);
        
                    panel.add(l);
                    creatureLabels.add(l);
                    
                    //Label the baby with its id number
                    l.setForeground(Color.WHITE);
                    l.setText("" + c.id);
                    l.setHorizontalTextPosition(JLabel.CENTER);
                    l.setFont(l.getFont().deriveFont(12.0f)); System.out.println(c.id + "   successfully budded");
                }
            } else if (c.food < 0){
                grid[c.worldX][c.worldY] = Constants.GRID_EMPTY_SPACE;
                remove.add(c);
                creatureLabels.get(c.id).setVisible(false);
            }
        }
        
        //since you can't remove an object from an arraylist in a for loop using that arraylist, remove/add all creatures scheduled for removal/adding here
        for(Creature c : remove){
            creatures.remove(c);
        }
        for(Creature c : add){
            creatures.add(c);
        }
        
        updateDisplay();
    }

    public void spawnFood(int indexToReplace){
        //pick a random, unnocupied grid space
        int x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
        int y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);

        while(grid[x][y] != Constants.GRID_EMPTY_SPACE){
            x = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
            y = (int)(Math.random() * Constants.WORLD_GRID_SIZE - 1);
        }

        grid[x][y] = Constants.GRID_FOOD;
        
        //update the display properly
        //if indexToReplace is out of bounds of foodLabels, create a new label
        if(indexToReplace > 0 && indexToReplace < foodLabels.size()){
            foodLabels.get(indexToReplace).setLocation(x * Constants.WORLD_TILE_SIZE, y * Constants.WORLD_TILE_SIZE);
        } else {
            JLabel foodLabel = new JLabel(new ImageIcon("imgs/food.png"));
            foodLabel.setSize(Constants.WORLD_TILE_SIZE, Constants.WORLD_TILE_SIZE);
            foodLabel.setLocation(x * Constants.WORLD_TILE_SIZE, y * Constants.WORLD_TILE_SIZE);
            panel.add(foodLabel);
            foodLabels.add(foodLabel);
        }
    }

    /* * * * * * * * * * * * * * *
     *                           *
     *     DISPLAY FUNCTIONS     *
     *                           *
     * * * * * * * * * * * * * * */

    public void updateDisplay(){
        //update the locations of the creatures
        Creature c;
        int tileSize = Constants.WORLD_TILE_SIZE;
        for(int i = 0; i < creatures.size(); i++){
            c = creatures.get(i);
            creatureLabels.get(i).setLocation(c.worldX * tileSize, c.worldY * tileSize);
        }
    }
}
