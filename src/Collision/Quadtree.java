package Collision;

import Object.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Quad tree that is used to find objects that intersect
 * in a more efficient way then doing a for loop of every item but still less efficient than a grid system.
 */
public class Quadtree {
    /**
     * Max Objects before it splits into four parts
     */
    private static final int MAX_OBJECTS = 10;

    /**
     * To know how deep it is
     */
    private final int level;
    /**
     * Bounds of the quad
     */
    private final Rectangle bounds;

    /**
     * children nodes
     */
    private final Quadtree[] nodes;
    /**
     * parent node
     */
    private final Quadtree parentNode;
    /**
     * Objects in this bound.
     */
    private final ArrayList<GameObject> objects;

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
    /**
     * Split the quad bound into four equal parts.
     */
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

    /**
     * add object to the child quads it intersects with
     */
    private void add_to_intersecting_children(GameObject objectToAdd){
        if(nodes[0] == null)
            return;
        for (Quadtree child : nodes){
            if(objectToAdd.getRectangle().intersects(child.bounds)){
                child.insert(objectToAdd);
            }
        }
    }

    /**
     * Is being used in Game model
     * inserts an object to the quad, if the object is outside the boundaries, it will try to insert to the parent node.
     * if children nodes does not exist, it will just be added to this quad.
     * if children nodes exist, it will be inserted to the children that intersect with it.
     */
    public void insert(GameObject gObject) {

        if (nodes[0] != null) {
            this.add_to_intersecting_children(gObject);
            return;
        }

        if (this.objects.size() > MAX_OBJECTS) {
            split();
            Iterator<GameObject> iterator = this.objects.iterator();
            while (iterator.hasNext()) {
                GameObject ob = iterator.next();
                ob.quads.remove(this);

                add_to_intersecting_children(ob);
            }
            this.insert(gObject);
        } else{
            this.objects.add(gObject);
            gObject.quads.add(this);
        }
    }
    /**
    *
     */
    private void insert_Intersecting(GameObject gObject){
        if (!bounds.contains(gObject.getRectangle())) {
            if(parentNode == null)
                return;
            parentNode.insert_Intersecting(gObject);
            return;
        }
        insert(gObject);
    }
    /**
    * retrieves all the objects that exist within the quad the rectangle belongs to.
     */
    public ArrayList<GameObject> retrieve_objects_in_vicinity(Rectangle searchArea) {
        if (!get_bounds().contains(searchArea)) {
            if (parentNode == null)
                return null;

            return parentNode.retrieve_objects_in_vicinity(searchArea);
        }
        return all_gameObject(searchArea);

    }

    /**
     * Updates the quads references belonging to a specific game object.
     * @param gameObject object quad refrences to update.
     */

    public void update_object(GameObject gameObject) {
        Quadtree quad = gameObject.quads.get(0);
        for (int i = 0; i < gameObject.quads.size(); i++){
            gameObject.quads.get(i).objects.remove(gameObject);
        }
        gameObject.quads.clear();
        quad.insert_Intersecting(gameObject);
    }
    /**
     * get all game objects rect that exist within this quad and it children
     */
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

    /**
     * get all Game objects that exist within this quad and it children
     */
    private ArrayList<GameObject> all_gameObject(){
        ArrayList<GameObject> gameObjects = new ArrayList<>(objects);
        if(nodes[0] != null) {
            gameObjects.addAll(nodes[0].all_gameObject());
            gameObjects.addAll(nodes[1].all_gameObject());
            gameObjects.addAll(nodes[2].all_gameObject());
            gameObjects.addAll(nodes[3].all_gameObject());
        }
        return gameObjects;
    }
    /**
     * get all Game objects that intersect with specific rect, only checks this quad and it children.
     */
    private ArrayList<GameObject> all_gameObject(Rectangle rect){
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
    /**
     * removes the game object from the quad.
     * Happens when an object get deleted.
     */
    public void remove(GameObject ob){
        this.objects.remove(ob);
    }
}
