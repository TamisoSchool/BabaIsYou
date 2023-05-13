package Collision;


import Game.GameModel;
import Object.GameObject;
import Object.ObjectStatus;
import Object.PropertyTypeText;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * Last in the chain of collection
 */
public class BlockerHandler implements CollisionHandler {
    private CollisionHandler next_handler;
    private static final Set<GameObject> objects_update = new HashSet<GameObject>();
    public static Set<GameObject> object_update_push()
    {
        Set<GameObject> gg = new HashSet<GameObject>(objects_update);
        objects_update.clear();
        return gg;
    }

    /**
     * Check if the object is blocked or pushable
     */
    @Override
    public PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        ObjectStatus status2 = model.getObjectStatus(object2.Type());
        objects_update.add(object1);
        if(status2.get_property(PropertyTypeText.SHUT)){
            objects_update.clear();
            return PropertyTypeText.SHUT;
        }
        else if (status2.get_property(PropertyTypeText.PUSH)) {

            ArrayList<GameObject> ob = model.intersect(model.raycast_object(object2.getRectangle(), speed), object2.quads.get(0));
            objects_update.add(object2);
            if(ob == null) // out of frame
            {
                objects_update.clear();
                return PropertyTypeText.STOP;
            }
            if (ob.size() == 0) {

                return PropertyTypeText.PUSH;
            }
            PropertyTypeText ret = PropertyTypeText.PUSH;
            for (GameObject el : ob) {
                PropertyTypeText index = model.collision_handler().handle_collision(object2, el, speed, model);
                if(index == PropertyTypeText.STOP || index == PropertyTypeText.SHUT){

                    ret = PropertyTypeText.STOP;
                }
            }
            if(ret == PropertyTypeText.STOP)
                objects_update.clear();

            return ret;
        }
        else if(status2.get_property(PropertyTypeText.STOP)){ //
            objects_update.clear();
            return PropertyTypeText.STOP;
        }
        return PropertyTypeText.NULL;
    }

    @Override
    public void set_next_handler(CollisionHandler next) {
        this.next_handler = next;
    }

}
