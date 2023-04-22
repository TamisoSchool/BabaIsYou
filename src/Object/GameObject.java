package Object;

import Collision.Quadtree;
import Game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class GameObject extends JComponent implements MovingObject {
    protected int x, y;
    protected int width, height;
    protected ObjectType type;
    protected DrawObject objectDrawing;


    public ObjectType Type() {
        return type;
    }

    protected Color color;
    public ArrayList<Quadtree> quads = new ArrayList<>();

    public GameObject(int x, int y, ObjectType type) {
        this(x, y, type, GameModel.objectWidth, GameModel.objectHeight);

    }

    public GameObject(int x, int y, ObjectType type, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;

        setLayout(null);
        setBounds(x, y, width, height);
        set_object_type(type);
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

        if(objectDrawing != null)
            objectDrawing.onPaint(g);
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
        repaint();
    }

    public void set_object_type(ObjectType newType){
        switch (newType){
            case KEY -> objectDrawing =  g -> {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(Color.black);
                g2.setStroke(new BasicStroke(2));

                g.drawOval(0, 0, width/2, height);
                g.drawRect(width/2,height/2, width/2, height/4);
                g.fillRect(width/2 + width/4,height/2 + height/4, width/2, height/4);

            };

            case WALL -> objectDrawing = g -> {
                super.paintComponent(g);
                g.setColor(color);
                g.fillRect(0, 0, width, height);
            };

            case FLAG -> objectDrawing = g -> {
                super.paintComponent(g);
                ((Graphics2D)(g)).setStroke(new BasicStroke(5));
                g.setColor(Color.RED);

                g.fillRect(0, 0, width, height/2);

                g.setColor(Color.BLACK);

                g.drawLine(0, 0, 0, height);
            };

            case WATER -> objectDrawing = g -> {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                Color lightBlue = new Color(173, 216, 230);
                Color darkBlue = new Color(0, 0, 128);
                GradientPaint gradient = new GradientPaint(x, y, lightBlue, x, y + height, darkBlue);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
            };

            case PLAYER -> objectDrawing = g -> {
                super.paintComponent(g);

                // Determine the size of the stick figure
                int size = (int)Math.min(width * 0.8, height * 0.8);

                // Determine the position of the stick figure
                int x = (width - size) / 2;
                int y = (height - size) / 2;

                // Calculate the bounds of the collision rectangle
                int collisionX = x;
                int collisionY = y;
                int collisionWidth = size;
                int collisionHeight = size;

                // Draw head
                g.drawOval(x + size/4, y, size/2, size/4);

                // Draw body
                g.drawLine(x + size/2, y + size/4, x + size/2, y + 3*size/4);

                // Draw legs
                g.drawLine(x + size/2, y + 3*size/4, collisionX, collisionY + collisionHeight);
                g.drawLine(x + size/2, y + 3*size/4, collisionX + collisionWidth, collisionY + collisionHeight);

                // Draw arms
                g.drawLine(x + size/2, y + size/2, collisionX, collisionY + size/2);
                g.drawLine(x + size/2, y + size/2, collisionX + collisionWidth, collisionY + size/2);

            };
            case ROCK -> objectDrawing = g->{
                g.setColor(new Color(101, 67, 33));
                g.fillOval(0, 0, width, height);
                g.fillArc(width/4, height/4, width/2, height/2, 0, 180);
                g.fillArc(width/4, height/2, width/2, height/2, 180, 180);
            };
            case DOOR -> objectDrawing = g -> {
                super.paintComponent(g);
                g.setColor(new Color(150, 75, 0));

                g.fillArc(0, 0, width, width, 0, 180);
                g.fillRect(0, height/2, width, height/2);


            };
        }
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
