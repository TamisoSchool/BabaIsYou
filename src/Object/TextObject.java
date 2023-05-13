package Object;

import Game.GameModel;

import java.awt.*;
import java.util.ArrayList;
/**
 * All the operator text, those that gives meaning to a sentence
 */
public class TextObject extends GameObject {
    private final String text;
    private final GameModel gm;
    /**
     * Used as a way to know if the sentence position is valid.
     */
    private int type = 0;



    public TextObject(int x, int y, PropertyTypeText txt, GameModel gm) {
        this(x, y, txt.toString(), gm);
        type = 2;
    }

    public TextObject(int x, int y, ObjectType txt, GameModel gm) {
        this(x, y, txt.toString(), gm);
    }

    public TextObject(int x, int y, OperatorText txt, GameModel gm){
        this(x,y,txt.toString(), gm);
        this.type = 1;
    }

    private TextObject(int x, int y, String txt, GameModel gm) {
        super(x, y, ObjectType.TEXT);
        this.gm = gm;
        this.text = txt;

    }

    @Override
    public void update(Point speed) {
        super.update(speed);
        gm.update_text_attributes();
    }

    public <T extends Enum<T>> T get_txt_enum(Class<T> e){
        return T.valueOf(e, text);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        int x = (width - textWidth) / 2;
        int y = height / 2 + 5;

        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }

    public int object_type_txt(){return this.type;}
    /**
     * Checks for text object to the right of it origin
     */
    public TextObject check_attribute_X(GameModel model){
        ArrayList<GameObject> objects =model.intersect(model.raycast_object(getRectangle(), new Point(1,0)), quads.get(0));

        for(GameObject txt: objects){
            if(txt instanceof TextObject &&
                    model.check_position_Y_between_rect(txt.getRectangle(), getRectangle(), 5)) {
                return (TextObject) txt;
            }
        }
        return null;
    }
    /**
     * Checks for text object down of its origin.
     */
    public TextObject check_attribute_Y(GameModel model){
        ArrayList<GameObject> objects =model.intersect(model.raycast_object(getRectangle(), new Point(0,1)), quads.get(0));

        for(GameObject txt: objects){
            if(txt instanceof TextObject &&
                    model.check_position_X_between_rect(txt.getRectangle(), getRectangle(), 5)) {
                return (TextObject) txt;
            }
        }
        return null;
    }
}
