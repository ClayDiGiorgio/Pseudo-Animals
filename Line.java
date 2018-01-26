import java.awt.Color;

/**
 * This class stores all the information needed to draw a specific line.
 * 
 * @author Clay DiGiorgio
 * @version 1.0
 */
public class Line{
    int x, y, x2, y2 = 0;
    int thickness = 1;
    Color color = Color.GRAY;
    
    public Line(int a, int b, int a2, int b2, int thick, Color c){
        x = a;
        y = b;
        x2 = a2;
        y2 = b2;
        thickness = thick;
        color = c;
    }
}
