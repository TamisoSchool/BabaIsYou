package Object;

import Game.GameModel;

import java.awt.*;
import java.util.ArrayList;

public class TextObject<T> extends GameObject {
    private String text;
    private int type = 0;

    public T getTxtEnum(){
        return ObjectType.valueOf(text);
    }

    public PropertyTypeText getTxtEnum(){
        return ObjectType.valueOf(text);
    }

    public TextObject(int x, int y, Color color, PropertyTypeText txt) {
        this(x, y, color, txt.toString());
        type = 1;
    }

    public TextObject(int x, int y, Color color, ObjectType txt) {
        this(x, y, color, txt.toString());
    }

    public TextObject(int x, int y, Color color, OperatorText txt){
        this(x,y,color,txt.toString());
        this.type = 2;
    }

    private TextObject(int x, int y, Color color, String txt) {
        super(x, y, ObjectType.TEXT, color);
        this.text = txt;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(text, width/2 - text.length()*3, height/2 + 5);
    }

    public int object_type_txt(){return  this.type;}

    public TextObject check_attribute(GameModel model){
        ArrayList<GameObject> objects =model.intersect(model.raycast_object(getRectangle(), new Point(1,0)), quads.get(0));

        for(GameObject txt: objects){
            if(txt instanceof TextObject) {
                return (TextObject) txt;
            }
        }
        return null;
    }
}
