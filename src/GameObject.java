import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameObject extends JComponent implements MovingObject {
    public final int uniqueNr = ThreadLocalRandom.current().nextInt(0, 1000000000);
    int x, y;
    int width, height;
    ObjectType type;
    Color color;
    public ArrayList<Quadtree> quads = new ArrayList<>();

    public GameObject(int x, int y, ObjectType type, Color color) {
        this(x, y, type, GameModel.objectWidth, GameModel.objectHeight, color);

    }

    public GameObject(int x, int y, ObjectType type, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;

        setLayout(null);
        setBounds(x, y, width, height);
        this.color = color;
    }

    public void move() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    @Override
    public void update(Point speed) {
        this.x += speed.x;
        this.y += speed.y;
        setBounds(x, y, width, height);
        if(speed.x != 0 || speed.y != 0)
            quads.get(0).updateObject(this);
        repaint();
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public JComponent getComp() {
        return this;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, GameModel.objectWidth, GameModel.objectHeight);
    }
}
