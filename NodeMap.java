import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.geom.Line2D;

import java.util.ArrayList;

/**
 * This can be thought of as a "brain." Although it doesn't work quite like a real brain,
 * it serves the same function.
 * 
 * @author Clay DiGiorgio
 * @version 2.0
 */
public class NodeMap{
    //display variables
    private JFrame frame = new JFrame();
    private JLayeredPane panel = new JLayeredPane();
    private ArrayList<JLabel> nodeLabels = new ArrayList<JLabel>();
    private ArrayList<NodeConnection> updatedConnections = new ArrayList<NodeConnection>();
    private boolean isInGridDisplay = false;
    
    //simulation variables
    public ArrayList<Node> nodes = new ArrayList<Node>();
    private int nodeCount;
    private int age = 0;
    
    public NodeMap(int n){
        nodeCount = n;
        
        //set up the display variables
        int frameSize = (int)(Math.sqrt(nodeCount) + 1.5) * Constants.NODE_SIZE; //the frame will be a square big enough to fit all nodes
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        panel.setSize(frameSize, frameSize);
        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);
        
        //create 4 sensoryNodes
        //also create one JLabel for each node, and add them to their ArrayList
        JLabel label;
        for(int i = 0; i < Constants.CREATURE_SENSORY_COUNT; i++){
            nodes.add(new SensoryNode());
            
            label = new JLabel(new ImageIcon("imgs/nodeInactive_Sensory.png"));
            label.setSize(Constants.NODE_SIZE, Constants.NODE_SIZE);
            nodeLabels.add(label);
            panel.add(label);
            
            label.setForeground(Color.WHITE);
            label.setText("0");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(12.0f));
        }
        
        //create a number of regular nodes equal to the parameter n and add them to the ArrayList
        for(int i = 0; i < nodeCount-4; i++){
            nodes.add(new Node(i, nodeCount)); //create a new node and its connections and then add it to the ArrayList 
            
            String imageFilePath = (i < nodeCount - 8)? "imgs/nodeInactive.png" : "imgs/nodeInactive_Movement.png";
            label = new JLabel(new ImageIcon(imageFilePath));
            label.setSize(Constants.NODE_SIZE, Constants.NODE_SIZE);
            nodeLabels.add(label);
            panel.add(label);
            
            label.setForeground(Color.WHITE);
            label.setText("0");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(12.0f));
        }
        
        //set up the locations of the nodes
        setCircleDisplay();
        
        //Give the nodeMap its starting excitement
        nodes.get(nodes.size()-1).addExcitement(Constants.NODE_MAP_STARTING_EXCITEMENT);
    }
    
    /* * * * * * * * * * * * * * *
     *                           *
     *   SIMULATION FUNCTIONS    *
     *                           *
     * * * * * * * * * * * * * * */
    
    public void update(){
        //update nodes and add all updated connections to updatedConnections
        for(Node n : nodes){
            updatedConnections.addAll(n.update());
        }
        
        //update graphics
        //updateNodeLabels();
        updateConnections();
        
        //transfer excitement and inhibition through updated connections
        Node recievingNode;
        int lostExcitement = 0;
        for(NodeConnection nC : updatedConnections){
            recievingNode = nodes.get(nC.indexOfRecievingNode); //get the node that this connection referrs to
            
            //transfer excitement through the connection
            int returnExcitement = recievingNode.addExcitement(nC.transferringExcitement); //excite that node the appropriate amount and store the amount to return   
            nC.transferringExcitement = 0; //clear the excitement from this connection
            
            //return the excitement not accepted
            Node returnTo = nodes.get(nC.indexOfOriginalNode);
            lostExcitement += returnTo.addExcitement(returnExcitement); //return the excitement and store the amount that needs to be returned to somewhere (if any)
            
            //if the connection is transferring inhibition, inhibit the recieving node, otherwise, leave it alone
            recievingNode.inhibited = nC.transferringInhibition? true : recievingNode.inhibited;
            nC.transferringInhibition = false; //clear the inhibition transfer
        }
        nodes.get(nodes.size()-1).addExcitement(lostExcitement); //"return" all lost excitement to the original node
        updatedConnections.clear(); //clear the ArrayList to prepare for the next update cycle
        
        
        //update the node graphics so that none of the excitement is "trapped" in the connections
        updateNodeLabels();
        
        age++;
    }
    
    /* * * * * * * * * * * * * * *
     *                           *
     *     DISPLAY FUNCTIONS     *
     *                           *
     * * * * * * * * * * * * * * */
    
    //switches the display between grid and circle display, then returns true if it's currently in
    //grid display, and false if it's currently in circle display
    public boolean switchDisplay(){
        if(isInGridDisplay){
            setCircleDisplay();
        } else {
            setGridDisplay();
        }
        
        isInGridDisplay = !isInGridDisplay;
        return isInGridDisplay;
    }
    
    //arrange the nodes in a circle
    private void setCircleDisplay(){
        double circumfrence = Constants.NODE_DIAGONAL_SIZE * nodeCount;
        double radius = circumfrence / (2 * Math.PI);
        double thetaIncrement = Constants.NODE_DIAGONAL_SIZE / (radius);
        double currentTheta = 0;
        
        for(JLabel label : nodeLabels){
            //set the label's location in rectangular after calculating from its place in the circle (polar)
            label.setLocation((int)(radius * Math.cos(currentTheta) + radius + 0.5), (int)(radius * Math.sin(currentTheta) + radius + 0.5));
            
            //move currentTheta to the next place in the circle
            currentTheta += thetaIncrement;
        }
        
        //set the size of the frame to fit the circle 
        frame.setSize((int)(radius * 2 + 15) + 2 * Constants.NODE_SIZE, (int)(radius * 2 + 15) + 2 * Constants.NODE_SIZE);
    }
    
    //arrange the nodes in a grid
    private void setGridDisplay(){
        int nodesPerRow = (int)(Math.sqrt(nodeCount) + 0.5);
        int spaceBetween = Constants.NODE_SIZE + Constants.SPACE_BETWEEN_NODES;
        int count = 0; int column = 0; int row = 0;
        
        for(JLabel label : nodeLabels){
            label.setLocation(row * spaceBetween, column * spaceBetween);
            
            count++;
            row = count / nodesPerRow;
            column = count % nodesPerRow;
        }
        
        //set the size of the frame to fit the grid 
        frame.setSize((int)((nodesPerRow + 1.5) * spaceBetween), (int)((nodesPerRow + 1) * spaceBetween));
    }
    
    //change the color of the nodes to represent their states
    private void updateNodeLabels(){
        for(int i = 0; i < Constants.CREATURE_SENSORY_COUNT; i++){
            if(nodes.get(i).inhibited){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeBlocked_Sensory.png"));
            } else if(nodes.get(i).getExcitement() > 0){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeActive_Sensory.png"));
            } else {
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeInactive_Sensory.png"));
            }
            
            nodeLabels.get(i).setText(nodes.get(i).getExcitement() + "");
        }
        
        for(int i = Constants.CREATURE_SENSORY_COUNT; i < nodes.size() - 4; i++){
            if(nodes.get(i).inhibited){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeBlocked.png"));
            } else if(nodes.get(i).getExcitement() > 0){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeActive.png"));
            } else {
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeInactive.png"));
            }
            
            nodeLabels.get(i).setText(nodes.get(i).getExcitement() + "");
        }
        
        for(int i = nodes.size() - 4; i < nodes.size(); i++){
            if(nodes.get(i).inhibited){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeBlocked_Movement.png"));
            } else if(nodes.get(i).getExcitement() > 0){
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeActive_Movement.png"));
            } else {
                nodeLabels.get(i).setIcon(new ImageIcon("imgs/nodeInactive_Movement.png"));
            }
            
            nodeLabels.get(i).setText(nodes.get(i).getExcitement() + "");
        }
    }
    
    private void updateConnections(){
        if(!isInGridDisplay){
            ArrayList<Line> lines = new ArrayList<Line>();
            int fromX, toX, fromY, toY;
            Color color;
            JLabel label; //used to store the information of the current label
            
            for(NodeConnection nC : updatedConnections){
                //set label to the label of the originating node
                label = nodeLabels.get(nC.indexOfOriginalNode);
                fromX = label.getX(); 
                fromY = label.getY();
                
                //set label to the label of the recieving node
                label = nodeLabels.get(nC.indexOfRecievingNode);
                toX = label.getX(); 
                toY = label.getY();
                
                if(nC.transferringExcitement > 0){
                    color = Color.RED;
                } else if (nC.transferringInhibition){
                    color = Color.BLUE;
                } else {
                    color = Color.GRAY;
                }
                
                lines.add(new Line(fromX, fromY, toX, toY, 10, color));
            }
            
            drawConnections(lines);
        }
    }
    
    private void drawConnections(ArrayList<Line> lines){
        LineDrawer lineDrawer = new LineDrawer();
        lineDrawer.setVisible(false);
        panel.remove(lineDrawer);
        
        lineDrawer = new LineDrawer();
        lineDrawer.lines = lines;
        panel.add(lineDrawer, null, 10);
        panel.moveToFront(lineDrawer);
        lineDrawer.paintConnections(panel.getGraphics());
        
        panel.revalidate();
        panel.repaint();
    }
    
    public void setFrameTitle(String title){
        frame.setTitle(title);
    }
    
    public NodeMap clone(){
        NodeMap clone = new NodeMap(nodeCount);
        clone.nodes = new ArrayList<Node>();
        
        for(Node n : nodes){
            clone.nodes.add(n.clone());
        }
        
        return clone;
    }
}
