
/**
 * Write a description of class NodeConnection here.
 * 
 * @author Clay DiGiorgio
 * @version 2.0
 */
public class NodeConnection{
    public int indexOfOriginalNode;
    public int indexOfRecievingNode;
    
    public int strength = Constants.INITIAL_CONNECTION_STRENGTH;
    public int transferringExcitement = 0;
    public boolean transferringInhibition = false;
    
    public NodeConnection(int from, int to){
        indexOfOriginalNode = from;
        indexOfRecievingNode = to;
    }
    
    //return an identicle copy of this NodeConnection
    public NodeConnection clone(){
        NodeConnection clone = new NodeConnection(this.indexOfOriginalNode, this.indexOfRecievingNode);
        clone.strength = this.strength;
        clone.transferringExcitement = this.transferringExcitement;
        clone.transferringInhibition = this.transferringInhibition;
        
        return clone;
    }
}
