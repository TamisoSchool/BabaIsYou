import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView extends JFrame {

    PlayerController player;
    boolean init = false;
    public ArrayList<Rectangle> quads = new ArrayList<>();

    public GameView() {
        super();
        setLayout(null);
        setUndecorated(true);
        setVisible(true);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("Width:" + getWidth()); // 30 width
        System.out.println("Height:" + getHeight());


    }

    public void Add_Objects(ArrayList<GameObject> gameObjects) {
        Point p = new Point(0,0);
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(p);
            add(gameObjects.get(i));
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        LineRenderer.drawBorderRectangle(g, this.quads);
    }
}