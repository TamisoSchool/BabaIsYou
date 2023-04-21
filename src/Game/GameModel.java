package Game;

import Collision.*;
import Object.GameObject;
import Object.ObjectStatus;
import Object.ObjectType;
import Object.TextModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import Object.*;
import org.w3c.dom.css.Rect;

public class GameModel {
    public static final int objectWidth = 32;
    public static final int objectHeight = 18;
    public ArrayList<GameObject> allObjects = new ArrayList<>();
    private final HashMap<ObjectType, ObjectStatus> objectStatus = new HashMap<>();
    private final CollisionHandler collision_handler;
    private final ArrayList<TextObject> txt_object_type = new ArrayList<TextObject>();

    public CollisionHandler collision_handler() {
        return collision_handler;
    }

    GameView view;
    Quadtree root;
    private Timer timer;

    private final TextModel textModel;

    public GameModel(GameView view) {
        this.view = view;
        root = new Quadtree(5, new Rectangle(0, 0, 1920, 1080), null);

        objectStatus.put(ObjectType.KEY, new ObjectStatus(ObjectType.KEY));
        objectStatus.put(ObjectType.WALL, new ObjectStatus(ObjectType.WALL));
        objectStatus.put(ObjectType.FLAG, new ObjectStatus(ObjectType.FLAG));
        objectStatus.put(ObjectType.PLAYER, new ObjectStatus(ObjectType.PLAYER));
        objectStatus.put(ObjectType.WATER, new ObjectStatus(ObjectType.PLAYER));
        objectStatus.put(ObjectType.TEXT, new ObjectStatus(ObjectType.PLAYER));

        objectStatus.get(ObjectType.WALL).set_property(PropertyTypeText.PUSH, true);
        objectStatus.get(ObjectType.TEXT).set_property(PropertyTypeText.PUSH, true);

        this.textModel = new TextModel();

        CollisionHandler collision_handler = new FloatHandler();
        CollisionHandler outcome_handler = new OutcomeHandler();
        CollisionHandler destroy_handler = new DestroyHandler();
        CollisionHandler blocker_handler = new BlockerHandler();

        collision_handler.set_next_handler(outcome_handler);
        outcome_handler.set_next_handler(destroy_handler);
        destroy_handler.set_next_handler(blocker_handler);

        this.collision_handler = collision_handler;

    }

    public ObjectStatus getObjectStatus(ObjectType type){
        return objectStatus.get(type);
    }

    public void win_level() {

    }

    public void lose_level(){

    }

    public ArrayList<GameObject> intersect(Rectangle rect, Quadtree startFrom){
        return intersect(rect.x, rect.y, rect.width, rect.height, startFrom);
    }

    public ArrayList<GameObject> intersect(int x, int y, int width, int height, Quadtree startFrom) {

        Rectangle toCheck = new Rectangle(x, y, width, height);
        ArrayList<GameObject> gameObjects;

        if(startFrom == null)
            gameObjects = root.retrieve_objects_in_vicinity(toCheck);
        else
            gameObjects = startFrom.retrieve_objects_in_vicinity(toCheck);

        if(gameObjects == null) // search area was out of frame
            return null;


        ArrayList<GameObject> objectsIntersect = new ArrayList<>();
        for (int i = 0; i < gameObjects.size(); i++) {
            if (toCheck.intersects(gameObjects.get(i).getRectangle()))
                objectsIntersect.add(gameObjects.get(i));
        }
        return objectsIntersect;
    }

    public void attach_keyListener(KeyListener l) {
        this.view.addKeyListener(l);
    }

    public void start_new_level(GameMap map) {
        root.clear();
        for (int i = 0; i < map.objects.size(); i++) {
            GameObject ob = map.objects.get(i);
            root.insert(map.objects.get(i));
            if(ob instanceof TextObject){
                if(((TextObject)ob).object_type_txt() == 1){
                    this.txt_object_type.add((TextObject) ob);
                }
            }
        }
        view.quads = root.all_rect();
        view.add_objects(map.objects);

    }

    public void update_text_attributes(){
        for (TextObject txt: this.txt_object_type){
            TextObject two = txt.check_attribute(this);
            if(two.object_type_txt() == 1){
              TextObject third =  two.check_attribute(this);
              if(third.object_type_txt() == 2){
                ObjectType first_objectType = txt.Type();
              }
              else if(third.object_type_txt() == 0){

              }
            }
        }
    }
    public boolean check_position_Y_between_rect(Rectangle rect1, Rectangle rect2,int thresholdY){
        return Math.abs(rect1.getMinY() - rect2.getMinY()) < thresholdY ||
                Math.abs(rect1.getMaxY() - rect2.getMaxY()) < thresholdY;
    }

    public Rectangle raycast_object(Rectangle origin, Point direction) {
        int width = Math.abs(direction.x) + Math.abs(direction.y) * GameModel.objectWidth;
        int height = GameModel.objectHeight * Math.abs(direction.x) + Math.abs(direction.y);

        int xPos = origin.x;
        if (direction.x < 0)
            xPos = origin.x - width;
        if (direction.x > 0)
            xPos = origin.x + GameModel.objectWidth;

        int yPos = origin.y;
        if (direction.y < 0)
            yPos = origin.y - height;
        if (direction.y > 0)
            yPos = origin.y + GameModel.objectHeight;

        return new Rectangle(xPos, yPos, width, height);
    }
/// test function
public void test_add_object(GameObject object) {
    view.add(object);
}
}
