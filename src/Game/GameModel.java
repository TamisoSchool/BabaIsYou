package Game;

import Collision.*;
import Object.*;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Has most of the logic of the game as methods that is being used by the Player Controller.
 */
public class GameModel {
    /**
     * Width of every Game Object
     */
    public static final int objectWidth = 32;
    /**
     * Height of every Game Object
     */
    public static final int objectHeight = 32;
    /**
     * The status of every object type that exist.
     */
    private final HashMap<ObjectType, ObjectStatus> objectStatus = new HashMap<>();

    /**
     * First Collision handler, if this one cannot handle the collision, it has a reference to next CollisionHandler
     */
    private final CollisionHandler collision_handler;
    /**
     * text objects that can be first in a sentence. Used to check for new attributes.
     */
    private final ArrayList<TextObject> txt_object_type = new ArrayList<TextObject>();
    /**
     * Player object can have up to 2 different Object types. Meaning it could be many objects and still they are all considered the player.
     */
    private final ObjectType[] player_type = new ObjectType[2];
    public ObjectType[] player_types(){return player_type;}
    /**
     * Every Object in existence in this map
     */
    private final HashMap<ObjectType, ArrayList<GameObject>> allObjects = new HashMap<ObjectType, ArrayList<GameObject>>();

    /**
     * All the available maps that is left.
     */
    private ArrayList<GameMap> maps = new ArrayList<>();

    public CollisionHandler collision_handler() {
        return collision_handler;
    }

    /**
     * Our GameView
     */
    GameView view;
    /**
     * Our Quad tree root. that has all objects.
     */
    Quadtree root;

    public GameModel(GameView view, ArrayList<GameMap> maps) {
        this.view = view;
        this.maps = maps;
        root = new Quadtree(5, new Rectangle(0, 0, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT), null);

        objectStatus.put(ObjectType.KEY, new ObjectStatus(ObjectType.KEY));
        objectStatus.put(ObjectType.WALL, new ObjectStatus(ObjectType.WALL));
        objectStatus.put(ObjectType.FLAG, new ObjectStatus(ObjectType.FLAG));
        objectStatus.put(ObjectType.BABA, new ObjectStatus(ObjectType.BABA));
        objectStatus.put(ObjectType.WATER, new ObjectStatus(ObjectType.BABA));
        objectStatus.put(ObjectType.TEXT, new ObjectStatus(ObjectType.BABA));
        objectStatus.put(ObjectType.ROCK, new ObjectStatus(ObjectType.BABA));
        objectStatus.put(ObjectType.DOOR, new ObjectStatus(ObjectType.BABA));

        allObjects.put(ObjectType.KEY, new ArrayList<>());
        allObjects.put(ObjectType.WALL, new ArrayList<>());
        allObjects.put(ObjectType.FLAG, new ArrayList<>());
        allObjects.put(ObjectType.BABA, new ArrayList<>());
        allObjects.put(ObjectType.WATER, new ArrayList<>());
        allObjects.put(ObjectType.ROCK, new ArrayList<>());
        allObjects.put(ObjectType.DOOR, new ArrayList<>());
        allObjects.put(ObjectType.TEXT, new ArrayList<>());


        CollisionHandler float_handler = new FloatHandler();
        CollisionHandler outcome_handler = new OutcomeHandler();
        CollisionHandler destroy_handler = new DestroyHandler();
        CollisionHandler blocker_handler = new BlockerHandler();

        float_handler.set_next_handler(outcome_handler);
        outcome_handler.set_next_handler(destroy_handler);
        destroy_handler.set_next_handler(blocker_handler);

        this.collision_handler = float_handler;
        default_status();

    }

    /**
     * Default status for the attributes, push, sink, open etc.
     * default only text is pushable and rest is cleared.
     */
    private void default_status(){
        for(var status: objectStatus.values()){
            status.clear_all_property();
        }
        objectStatus.get(ObjectType.TEXT).set_property(PropertyTypeText.PUSH, true);
    }

    public ObjectStatus getObjectStatus(ObjectType type){
        return objectStatus.get(type);
    }

    /**
     * When you win a level, Start a new one directly
     */
    public void win_level() {
        start_new_level();
    }
    /**
     * When you lose a level, Replay it
     */
    public void lose_level(){

    }
    /**
     * Checking for collision with the objects
     * @param rect the rectangle checking with.
     * @param startFrom our starting quadtree, so we dont have to start from beginning.
     */
    public ArrayList<GameObject> intersect(Rectangle rect, Quadtree startFrom){
        return intersect(rect.x, rect.y, rect.width, rect.height, startFrom);
    }

    /**
     * same as above but you write in x,y,width,height instead of a rectangle.
     */
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

    /**
     *
     * Attaches the key listener from PlayerController to View
     */
    public void attach_keyListener(KeyListener l) {
        this.view.addKeyListener(l);
    }


    /**
     * deletes current level and starts a new one
     */
    public void start_new_level() {
        Iterator<ArrayList<GameObject>> it = allObjects.values().iterator();
        while (it.hasNext()){
            ArrayList<GameObject> objects = it.next();
            for(GameObject o: objects){
                view.remove(o);
                o.destroy();
            }
            objects.clear();
        }
        this.txt_object_type.clear();

        view.repaint();
        root.clear();
        root = new Quadtree(0, new Rectangle(0,0, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT), null);
        if(maps.size() == 0){
            // no more maps
            return;
        }

        GameMap map = maps.get(0);
        maps.remove(0);

        for (int i = 0; i < map.objects.size(); i++) {
            GameObject ob = map.objects.get(i);
            allObjects.get(ob.Type()).add(ob);
            root.insert(ob);
            if(ob instanceof TextObject){
                if(((TextObject)ob).object_type_txt() == 0){
                    this.txt_object_type.add((TextObject) ob);
                }
            }
        }
        view.add_objects(map.objects);
        update_text_attributes();
    }

    /**
     * Every time a text object moves, we update all attributes for the objects again.
     */
    public void update_text_attributes(){
        default_status();
        player_type[0] = null;
        player_type[1] = null;
        check_text_attribute(true);
        check_text_attribute(false);
    }
    /**
     * Checks attributes in a  row manner or vertical.
     * Attributes can take effect both if written down or written to the right.
     * @param checkX depending on if it is false or true, we check row or vertically.
     */
    private void check_text_attribute(boolean checkX) {

        for (TextObject one: this.txt_object_type){

            TextObject two;
            if(checkX)
                two = one.check_attribute_X(this);
            else
                two = one.check_attribute_Y(this);

            if(two != null && two.object_type_txt() == 1){ // operator txt

                TextObject third;
                if(checkX)
                    third = two.check_attribute_X(this);
                else
                    third = two.check_attribute_Y(this);

              ObjectType first_txt = one.get_txt_enum(ObjectType.class);
              OperatorText second_txt = two.get_txt_enum(OperatorText.class);
              if(second_txt == OperatorText.IS && third != null) {
                  if (third.object_type_txt() == 2) {

                      PropertyTypeText third_txt = third.get_txt_enum(PropertyTypeText.class);
                      if(third_txt == PropertyTypeText.YOU){
                          if(this.player_type[0] == null)
                              this.player_type[0] = first_txt;
                          else
                              this.player_type[1] = first_txt;
                      }
                      else {
                          objectStatus.get(first_txt).set_property(third_txt, true);
                      }


                  } else if (third.object_type_txt() == 0) {
                      ObjectType third_txt = third.get_txt_enum(ObjectType.class);
                      ArrayList<GameObject> t = new ArrayList<GameObject>(allObjects.get(first_txt));
                      allObjects.get(first_txt).clear();
                      for (GameObject g : t) {
                          g.set_object_type(third_txt);
                          allObjects.get(first_txt).add(g);

                          var intersecting_objects = intersect(g.getRectangle(), g.quads.get(0)); // update gamevariabel when it becomes a new one
                          for (GameObject intersect : intersecting_objects) {
                              this.collision_handler.handle_collision(g, intersect, new Point(0, 0), this);
                          }
                      }
                  }
                  TextObject fourth = null;
                  if(checkX)
                      fourth = third.check_attribute_X(this);
                  else
                      fourth = third.check_attribute_Y(this);
                  if(fourth != null){
                      var fourth_txt = fourth.get_txt_enum(OperatorText.class);
                      if(fourth_txt == OperatorText.AND){

                          TextObject fifth = null;
                          if(checkX)
                              fifth = third.check_attribute_X(this);
                          else
                              fifth = third.check_attribute_Y(this);

                          if(fifth != null){
                              var last_obj =fifth.get_txt_enum(ObjectType.class);
                              var last_prop =fifth.get_txt_enum(PropertyTypeText.class);
                              if(last_prop != null){
                                  if(last_prop == PropertyTypeText.YOU){
                                      if(player_type[0] == null){
                                          player_type[0] = first_txt;
                                      }
                                      else{
                                          player_type[1] = first_txt;
                                      }
                                  }
                                  else {
                                      this.objectStatus.get(first_txt).set_property(last_prop, true);
                                  }

                              }
                              else {
                                  if(third.get_txt_enum(PropertyTypeText.class) == PropertyTypeText.YOU){
                                      if(player_type[0] == null){
                                          player_type[0] = last_obj;
                                      }
                                      else{
                                          player_type[1] = last_obj;
                                      }
                                  }
                              }
                          }
                      }
                  }

              }
            }
        }
    }



    public ArrayList<GameObject> get_player(){

        ArrayList<GameObject> ret = new ArrayList<>();
        if(this.player_type[0] != null)
            ret.addAll(allObjects.get(this.player_type[0]));

        if(this.player_type[1] != null)
            ret.addAll(allObjects.get(this.player_type[1]));
        return ret;
    }
    /**
     * Used to check if an object hits something without it actually hit it but very close to hitting it
     * @param direction the speed the object moves in.
     */
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

    /**
     * Checks if two rectangles have the same y position.
     * @param thresholdY The maximum y position they can differ.
     */
    public boolean check_position_Y_between_rect(Rectangle rect1, Rectangle rect2,int thresholdY){
        return Math.abs(rect1.getMinY() - rect2.getMinY()) < thresholdY ||
                Math.abs(rect1.getMaxY() - rect2.getMaxY()) < thresholdY;
    }

    /**
     * Checks if two rectangles have the same x position.
     * @param thresholdX The maximum position x they can differ.
     */
    public boolean check_position_X_between_rect(Rectangle rectangle, Rectangle rectangle1, int thresholdX) {
        return Math.abs(rectangle.getMinX() - rectangle1.getMinX()) < thresholdX ||
                Math.abs(rectangle.getMaxX() - rectangle1.getMaxX()) < thresholdX;
    }

    public void open_menu(){

    }
}
