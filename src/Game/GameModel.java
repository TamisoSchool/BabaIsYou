package Game;

import Collision.*;
import Object.GameObject;
import Object.ObjectStatus;
import Object.ObjectType;
import Object.TextModel;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import Object.*;

public class GameModel {
    public static final int objectWidth = 32;
    public static final int objectHeight = 32;
    private final HashMap<ObjectType, ObjectStatus> objectStatus = new HashMap<>();
    private final CollisionHandler collision_handler;
    private final ArrayList<TextObject> txt_object_type = new ArrayList<TextObject>();
    private ObjectType[] playerType;
    private HashMap<ObjectType, ArrayList<GameObject>> allObjects = new HashMap<ObjectType, ArrayList<GameObject>>();

    public CollisionHandler collision_handler() {
        return collision_handler;
    }

    GameView view;
    Quadtree root;
    private final TextModel textModel;

    public GameModel(GameView view) {
        this.view = view;
        root = new Quadtree(5, new Rectangle(0, 0, 1920, 1080), null);

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

        this.textModel = new TextModel();

        CollisionHandler collision_handler = new FloatHandler();
        CollisionHandler outcome_handler = new OutcomeHandler();
        CollisionHandler destroy_handler = new DestroyHandler();
        CollisionHandler blocker_handler = new BlockerHandler();

        collision_handler.set_next_handler(outcome_handler);
        outcome_handler.set_next_handler(destroy_handler);
        destroy_handler.set_next_handler(blocker_handler);

        this.collision_handler = collision_handler;
        default_status();

    }
    private void default_status(){
        for(var status: objectStatus.values()){
            status.clear_all_property();
        }
        objectStatus.get(ObjectType.TEXT).set_property(PropertyTypeText.PUSH, true);
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
                if(((TextObject)ob).object_type_txt() == 0){
                    this.txt_object_type.add((TextObject) ob);
                }
            }
        }
        view.quads = root.all_rect();
        view.add_objects(map.objects);
        update_text_attributes();
        int x = 5;

    }

    public void update_text_attributes(){
        default_status();
        check_text_attribute(true);
        check_text_attribute(false);
    }

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
                          if(this.playerType[0] == null)
                              this.playerType[0] = first_txt;
                          else
                              this.playerType[1] = first_txt;
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
                                      if(playerType[0] == null){
                                          playerType[0] = first_txt;
                                      }
                                      else{
                                          playerType[1] = first_txt;
                                      }
                                  }
                                  else {
                                      this.objectStatus.get(first_txt).set_property(last_prop, true);
                                  }

                              }
                              else {
                                  if(third.get_txt_enum(PropertyTypeText.class) == PropertyTypeText.YOU){
                                      if(playerType[0] == null){
                                          playerType[0] = last_obj;
                                      }
                                      else{
                                          playerType[1] = last_obj;
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

    public boolean check_position_Y_between_rect(Rectangle rect1, Rectangle rect2,int thresholdY){
        return Math.abs(rect1.getMinY() - rect2.getMinY()) < thresholdY ||
                Math.abs(rect1.getMaxY() - rect2.getMaxY()) < thresholdY;
    }

    public ArrayList<GameObject> get_player(ObjectType type){
        return allObjects.get(this.playerType);
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



    public boolean check_position_X_between_rect(Rectangle rectangle, Rectangle rectangle1, int thresholdX) {
        return Math.abs(rectangle.getMinX() - rectangle1.getMinX()) < thresholdX ||
                Math.abs(rectangle.getMaxX() - rectangle1.getMaxX()) < thresholdX;
    }
}
