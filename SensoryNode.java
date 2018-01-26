import java.util.ArrayList;

/**
 * A SensoryNode is like a regular node, but it stores excitement (up to a certain maximum) and
 * transfers it when it's told that it senses something
 * 
 * @author Clay DiGiorgio
 * @version 1.0
 */
public class SensoryNode extends Node{
    public int senseIntensity = 0;
    
    public SensoryNode(){
        super();
    }
    
    public ArrayList<NodeConnection> update(){
        if(senseIntensity != 0 && getExcitement() > 0){
            //determine which nodeConnection to excite
            ArrayList<Integer> indexesLeft = new ArrayList<Integer>();
            for(int i = 0; i < connections.size(); i++){
                for(int j = 0; j < connections.get(i).strength; j++){
                    indexesLeft.add(i); //add i to indexesLeft so that its connection strength is equal to the number of times it shows up in the array      
                    //this allows for a weighted randomization system
                }
            }
            
            int indexOfIndex = (int)(Math.random() * indexesLeft.size() - 0.5);
            int index = (int)indexesLeft.get(indexOfIndex);
            NodeConnection nC = connections.get(index);
            
            nC.transferringExcitement += senseIntensity;
            nC.strength++;
            subtractExcitement(senseIntensity);
            
            //increment age
            age++;
            
            ArrayList<NodeConnection> connectionChosen = new ArrayList<NodeConnection>();
            connectionChosen.add(nC);
            return connectionChosen;
        } else {
            return new ArrayList<NodeConnection>();
        }
    }
    
    public int addExcitement(int incoming){
        int openExcitement = Constants.MAX_SENSORY_EXCITEMENT - getExcitement();
        int amountToAdd = (openExcitement < incoming)? openExcitement : incoming;
        int amountToReturn = incoming - amountToAdd;
        
        super.addExcitement(amountToAdd);
        return amountToReturn;
    }
}
