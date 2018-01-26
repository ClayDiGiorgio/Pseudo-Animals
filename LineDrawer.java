import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.util.ArrayList;

/**
 * Draws the specified lines in the specified Graphics object
 * 
 * @author Clay DiGiorgio
 * @version 1.0
 */
public class LineDrawer extends JComponent{
    ArrayList<Line> lines = new ArrayList<Line>();
    
    public void paintConnections(Graphics g){
        Graphics2D g2 = (Graphics2D) super.getGraphics();
        for(Line l : lines){
            g2.setColor(l.color);
            g2.setStroke(new BasicStroke(l.thickness));
            g2.drawLine(l.x, l.y, l.x2, l.y2);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        for(Line l : lines){
            g2.setColor(l.color);
            g2.setStroke(new BasicStroke(l.thickness));
            g2.drawLine(l.x, l.y, l.x2, l.y2);
        }
    }
}