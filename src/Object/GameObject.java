package Object;

import Collision.Quadtree;
import Game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameObject extends JComponent implements MovingObject {
    protected int x, y;
    protected int width, height;
    protected ObjectType type;

    public ObjectType Type() {
        return type;
    }

    protected Color color;
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

    public void set_val(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set_val(int x, int y) {
        set_val(x, y, width, height);
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
            quads.get(0).update_object(this);
        repaint();
    }

    public void destroy(){
        for (Quadtree q: quads)
            q.remove(this);
        this.width = 0;
        this.height = 0;
    }

    @Override
    public boolean is_moving() {
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
