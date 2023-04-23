package Game;

import Object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView extends JFrame {

    public ArrayList<Rectangle> quads = new ArrayList<>();

    public GameView() {
        super();
        setLayout(null);
        setUndecorated(true);
        setVisible(true);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void add_objects(ArrayList<GameObject> gameObjects) {
        Point p = new Point(0,0);
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(p);
            add(gameObjects.get(i));
            gameObjects.get(i).update(new Point(0,0));
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
       // LineRenderer.draw_border_rectangle(g, this.quads);
    }
}