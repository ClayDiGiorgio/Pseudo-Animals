import java.util.ArrayList;

/**
 * Write a description of class Node here.
 * 
 * @author Clay DiGiorgio 
 * @version 2.0
 */
public class Node{
    public ArrayList<NodeConnection> connections = new ArrayList<NodeConnection>();
    
    private int excitement = 0;
    public boolean inhibited = false;
    
    public int age = 0;
    
    public Node(){}
    
    public int addExcitement(int amt){
        excitement += amt;
        return 0;
    }
    
    public void subtractExcitement(int amt){
        excitement -= amt;
    }
    
    public int getExcitement(){
        return excitement;
    }
    
    //creates this node and its nodeConnections 
    public Node(int ownIndex, int connectionCount){
        for(int i = 0; i < connectionCount; i++){
            connections.add(new NodeConnection(ownIndex, i));
        }
    }
    
    //randomly decides to interact with other nodes and returns the connections it interacted with 
    public ArrayList<NodeConnection> update(){
        //increment age
        age++;
        
        if(excitement == 0 && false){ //if there's no excitement, attempt to induce inhibition   
            //create an ArrayList of connections for the weighted randomness operation
            ArrayList indexesLeft = new ArrayList();
            for(int i = 0; i < connections.size(); i++){
                for(int j = 0; j < connections.get(i).strength; j++){
                    indexesLeft.add(i); //add i to indexesLeft so that its connection strength is equal to the number of times it shows up in the array      
                    //this allows for a weighted randomization system
                }
            }
            
            NodeConnection connectionChosen = connections.get((int)indexesLeft.get((int)(Math.random() * indexesLeft.size())));
            
            //50% of the time, inhibit the chosen connection
            if(Math.random() <= 0.5){
                connectionChosen.transferringInhibition = true;
                connectionChosen.strength++;
            }
        } else if (!inhibited){
            //decide how many nodes to spread excitement to
            int nodeSpreadCount = (int)(Math.random() * connections.size() + 0.5);
            
            //determine which nodeConnections to excite
            ArrayList indexesLeft = new ArrayList();
            for(int i = 0; i < connections.size(); i++){
                for(int j = 0; j < connections.get(i).strength; j++){
                    indexesLeft.add(i); //add i to indexesLeft so that its connection strength is equal to the number of times it shows up in the array      
                    //this allows for a weighted randomization system
                }
            }
            
            ArrayList<NodeConnection> connectionsChosen = new ArrayList<NodeConnection>();
            for(int i = 0; i < nodeSpreadCount; i++){
                int indexOfIndex = (int)(Math.random() * indexesLeft.size() - 0.5);
                int index = (int)indexesLeft.get(indexOfIndex);
                
                connectionsChosen.add(connections.get(index));
                indexesLeft.remove(indexOfIndex);
            }
            
            //excite the chosen connections. A connection may appear multiple times.
            for(NodeConnection c : connectionsChosen){
                if(excitement > 0){
                    c.transferringExcitement++;
                    c.strength++;
                    this.excitement--;
                }
            }
            
            return connectionsChosen;
        }
        
        //this is just here to keep the compiler happy
        return new ArrayList();
    }
    
    //create a new Node identical to this one in all but age and return it
    public Node clone(){
        Node clone = new Node(connections);
        clone.excitement = this.excitement;
        clone.inhibited = this.inhibited;
        
        return clone;
    }
    
    //this constructor is to be used only for cloning
    private Node(ArrayList<NodeConnection> c){
        for(NodeConnection nC : c){
            connections.add(nC.clone()); //add clones of every nodeConnection in the ArrayList passed in
        }
    }
}
