
/**
 * This class manages the whole program.
 * 
 * @author Clay DiGiorgio
 * @version 2.0
 */
public class Main{
    public static void main(String args[]){ 
        (new Main()).run(); 
    }
    
    public Main(){}
    
    public void run(){
        boolean continueProgram = true;
        NodeMap test = new NodeMap(15);
        World world = new World();
        
        while(continueProgram){
            world.update();
            
            //test.update();
            
            //Make the loop pause at the end of each run so that it doesn't run at light speed
            try{Thread.sleep(Constants.TIME_BETWEEN_LOOPS);}catch(Exception e){}
        }
    }
}
