package Object;

import java.awt.*;

public class TextObject extends GameObject {
    String text;

    private TextObject(int x, int y, Color color, String text) {
        super(x, y, ObjectType.TEXT, color);
        this.text = text;
    }

    @Override
    public void update(Point speed) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(text, width/2 - text.length()*3, height/2 + 5);
    }

    public TextObject create_text_object(int x, int y, Color color, String text){
        try{
            ObjectType objectType = ObjectType.valueOf(text.toUpperCase());
            return new TextObject(x, y, color, text);
        } catch (Exception e){
            try{
                return new TextObject(x, y, color, text);
            } catch (Exception ee){
                return null;
            }
        }
    }

}
