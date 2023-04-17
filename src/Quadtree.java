import java.awt.*;
import java.util.*;

public class Quadtree {
    private static final int MAX_OBJECTS = 10;
    private static final int MAX_LEVELS = 5;

    private final int level;
    private final Rectangle bounds;
    private final Quadtree[] nodes;
    private final Quadtree parentNode;
    private ArrayList<GameObject> objects;
    public Quadtree(int level, Rectangle bounds, Quadtree parentNode) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.nodes = new Quadtree[4];
        this.parentNode = parentNode;

    }

    public Rectangle get_bounds() {
        return bounds;
    }

    public void clear() {
        this.objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    public void split() {
        int subWidth = this.bounds.width / 2;
        int subHeight = this.bounds.height / 2;

        int x = this.bounds.x;
        int y = this.bounds.y;

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight), this); // top right
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight), this); // top left
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight), this); // bot left
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight), this); // bot right
    }


    private void add_to_intersecting_children(GameObject objectToAdd){
        if(nodes[0] == null)
            return;
        for (Quadtree child : nodes){
            if(objectToAdd.getRectangle().intersects(child.bounds)){
                child.insert(objectToAdd);
            }
        }
    }

    public void insert(GameObject gObject) {

        if (nodes[0] != null) {
            this.add_to_intersecting_children(gObject);
            return;
        }

        if (this.objects.size() > MAX_OBJECTS) {
            split();
            Iterator<GameObject> iterator = this.objects.iterator();
            while (iterator.hasNext())
             {
                    GameObject ob = iterator.next();
                    ob.quads.remove(this);

                    add_to_intersecting_children(ob);
            }
            this.insert(gObject);
        }
        else{
            this.objects.add(gObject);
            gObject.quads.add(this);
        }


    }

    public void insert_Intersecting(GameObject gObject){
        if (!bounds.contains(gObject.getRectangle())) {
            if(parentNode == null)
                return;
            parentNode.insert_Intersecting(gObject);
            return;
        }
        insert(gObject);
    }
    public ArrayList<GameObject> retrieve_objects_in_vicinity(Rectangle searchArea) {
        if (!get_bounds().contains(searchArea)) {
            if (parentNode == null)
                return null;

            return parentNode.retrieve_objects_in_vicinity(searchArea);
        }
        return all_gameObject(searchArea);

    }

    public void update_object(GameObject gameObject) {
        Quadtree quad = gameObject.quads.get(0);
        for (int i = 0; i < gameObject.quads.size(); i++){
            gameObject.quads.get(i).objects.remove(gameObject);
        }
        gameObject.quads.clear();
        quad.insert_Intersecting(gameObject);
    }

    public ArrayList<Rectangle> all_rect(){
        ArrayList<Rectangle> rects = new ArrayList<>();
        rects.add(get_bounds());
        if(nodes[0] != null) {
            rects.addAll(nodes[0].all_rect());
            rects.addAll(nodes[1].all_rect());
            rects.addAll(nodes[2].all_rect());
            rects.addAll(nodes[3].all_rect());
        }
        return rects;
    }
    public  ArrayList<GameObject> all_gameObject(){
        ArrayList<GameObject> gameObjects = new ArrayList<>(objects);
        if(nodes[0] != null) {
            gameObjects.addAll(nodes[0].all_gameObject());
            gameObjects.addAll(nodes[1].all_gameObject());
            gameObjects.addAll(nodes[2].all_gameObject());
            gameObjects.addAll(nodes[3].all_gameObject());
        }
        return gameObjects;
    }
    public ArrayList<GameObject> all_gameObject(Rectangle rect){
        Set<GameObject> gameObjects = new HashSet<>(objects);
        if(nodes[0] != null) {
            if(rect.intersects(nodes[0].bounds))
                gameObjects.addAll(nodes[0].all_gameObject(rect));

            if(rect.intersects(nodes[1].bounds))
                gameObjects.addAll(nodes[1].all_gameObject(rect));

            if(rect.intersects(nodes[2].bounds))
                gameObjects.addAll(nodes[2].all_gameObject(rect));

            if(rect.intersects(nodes[3].bounds))
                gameObjects.addAll(nodes[3].all_gameObject(rect));
        }
        return new ArrayList<>(gameObjects);
    }
    public void remove(GameObject ob){
        this.objects.remove(ob);
    }
}
