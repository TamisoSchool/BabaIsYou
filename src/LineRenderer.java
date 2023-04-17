import java.awt.*;
import java.util.ArrayList;

public class LineRenderer {
    public static void drawBorderRectangle(Graphics g, ArrayList<Rectangle> rects){
        g.setColor(Color.black);
        for (Rectangle r: rects){
            g.drawRect(r.x, r.y, r.width, r.height);
        }

    }
}
