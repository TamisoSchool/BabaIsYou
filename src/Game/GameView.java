package Game;

import Object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Screen for the Game
 */
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


    /**
     * Add the game objects to the game view
     * @param gameObjects all the objects for the map
     */
    public void add_objects(ArrayList<GameObject> gameObjects) {
        Point p = new Point(0,0);
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(p);
            add(gameObjects.get(i));
            gameObjects.get(i).update(new Point(0,0));
        }
        repaint();
    }
}