package Object;

import Collision.Quadtree;
import Game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 * All objects within the screen must extend this class
 */
public class GameObject extends JComponent implements MovingObject {
    /**
     * the position of the object
     * being updated in the update function
     */
    protected int x, y;
    /**
     * GameModel has the width and height every object must have.
     * but it sets to 0 when destroyed
     */
    protected int width, height;
    /**
     * The type of object
     * To know which kind of special attributes it has.
     */
    protected ObjectType type;
    /**
     * unique way to be drawn depending on the object type.
     */
    protected DrawObject objectDrawing;


    public ObjectType Type() {
        return type;
    }

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(objectDrawing != null)
            objectDrawing.onPaint(g);
    }
    /**
     * Update the position of the game object in the game view
     * Update the quad references.
     */
    @Override
    public void update(Point speed) {
        this.x += speed.x;
        this.y += speed.y;
        setBounds(x, y, width, height);
        if(speed.x != 0 || speed.y != 0)
            quads.get(0).update_object(this);
        repaint();
    }

    /**
     * removes the object from the quad tree and set the width, height to zero and repainting.
     */
    public void destroy(){
        for (Quadtree q: quads)
            q.remove(this);
        this.width = 0;
        this.height = 0;
        repaint();
    }

    public void set_object_type(ObjectType newType){
        this.type = newType;
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
                g.setColor(Color.BLACK);
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

            case BABA -> objectDrawing = g -> {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, width, height);
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
