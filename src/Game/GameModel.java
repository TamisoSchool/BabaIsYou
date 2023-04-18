package Game;

import Collision.CollisionHandler;
import Collision.Quadtree;
import Object.GameObject;
import Object.ObjectStatus;
import Object.ObjectType;
import Object.TextModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {
    public static final int objectWidth = 32;
    public static final int objectHeight = 18;
    public ArrayList<GameObject> allObjects = new ArrayList<>();
    private HashMap<ObjectType, ObjectStatus> objectStatus = new HashMap<>();
    private ArrayList<CollisionHandler> collision_handler;

    public ArrayList<CollisionHandler> collision_handler() {
        return collision_handler;
    }

    GameView view;
    Quadtree root;
    private Timer timer;

    private TextModel textModel;

    public GameModel(GameView view) {
        this.view = view;
        root = new Quadtree(5, new Rectangle(0, 0, 1920, 1080), null);

        objectStatus.put(ObjectType.KEY, new ObjectStatus(ObjectType.KEY));
        objectStatus.put(ObjectType.WALL, new ObjectStatus(ObjectType.WALL));
        objectStatus.put(ObjectType.FLAG, new ObjectStatus(ObjectType.FLAG));
        objectStatus.put(ObjectType.PLAYER, new ObjectStatus(ObjectType.PLAYER));
        objectStatus.put(ObjectType.WATER, new ObjectStatus(ObjectType.PLAYER));

        this.textModel = new TextModel();
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
            root.insert(map.objects.get(i));
        }
        view.quads = root.all_rect();
        view.add_objects(map.objects);

    }
/// test function
public void test_add_object(GameObject object) {
    view.add(object);
}
}
