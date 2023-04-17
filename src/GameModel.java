import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {
    public static final int objectWidth = 32;
    public static final int objectHeight = 18;
    private final ArrayList<MovingObject> objects = new ArrayList<>();
    public ArrayList<GameObject> allObjects = new ArrayList<>();
    private HashMap<ObjectType, ObjectStatus> objectStatus = new HashMap<>();
    GameView view;
    Quadtree root;
    private Timer timer;

    public GameModel(GameView view) {
        this.view = view;
        root = new Quadtree(5, new Rectangle(0, 0, 1920, 1080), null);
        Point point = new Point(0,0);
        Timer gameUpdate = new Timer(10, e -> {
            for (MovingObject gameObject : ((ArrayList<MovingObject>) objects.clone())
            ) {
                gameObject.update(point);
            }
        });
        gameUpdate.start();

        objectStatus.put(ObjectType.Key, new ObjectStatus(ObjectType.Key, false, true, false));
        objectStatus.put(ObjectType.Wall, new ObjectStatus(ObjectType.Wall, false, true, false));
        objectStatus.put(ObjectType.Flag, new ObjectStatus(ObjectType.Flag, false, false, false));
        objectStatus.put(ObjectType.Player, new ObjectStatus(ObjectType.Player, false, false, false));
    }
    public ObjectStatus getObjectStatus(ObjectType type){
        return objectStatus.get(type);
    }
    public void win_level() {

    }

    public ArrayList<GameObject> Intersect(Rectangle rect, Quadtree startFrom){
         return Intersect(rect.x, rect.y, rect.width, rect.height, startFrom);
    }
    public ArrayList<GameObject> Intersect(int x, int y, int width, int height, Quadtree startFrom) {

        Rectangle toCheck = new Rectangle(x, y, width, height);
        ArrayList<GameObject> gameObjects;

        if(startFrom == null)
            gameObjects = root.RetrieveObjectsInVicinity(toCheck);
        else
            gameObjects = startFrom.RetrieveObjectsInVicinity(toCheck);

        if(gameObjects == null) // search area was out of frame
            return null;



        ArrayList<GameObject> objectsIntersect = new ArrayList<>();
        for (int i = 0; i < gameObjects.size(); i++) {
            if (toCheck.intersects(gameObjects.get(i).getRectangle()))
                    objectsIntersect.add(gameObjects.get(i));
        }
        return objectsIntersect;
    }



    public void Attach_Object(MovingObject toAttach) {
        objects.add(toAttach);
    }

    public void Attach_KeyListener(KeyListener l) {
        this.view.addKeyListener(l);
    }

    public void StartNewMap(GameMap map) {
        root.clear();
        for (int i = 0; i < map.objects.size(); i++) {
            root.insert(map.objects.get(i));
        }
        view.quads = root.allRect();
        view.Add_Objects(map.objects);

    }

    public void TestAddObject(GameObject object) {
        view.add(object);
    }
}
