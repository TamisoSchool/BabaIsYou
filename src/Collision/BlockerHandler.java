package Collision;


import Game.GameModel;
import Object.GameObject;
import Object.ObjectStatus;
import Object.PropertyTypeText;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BlockerHandler implements CollisionHandler {
    private CollisionHandler next_handler;
    private static Set<GameObject> objects_update = new HashSet<GameObject>();
    public static Set<GameObject> object_update_push()
    {
        Set<GameObject> gg = new HashSet<GameObject>(objects_update);
        objects_update.clear();
        return gg;
    }


    @Override
    public int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        ObjectStatus status2 = model.getObjectStatus(object2.Type());
        objects_update.add(object1);
        if (status2.get_property(PropertyTypeText.PUSH)) {

            ArrayList<GameObject> ob = model.intersect(model.raycast_object(object2.getRectangle(), speed), object2.quads.get(0));
            objects_update.add(object2);

            if (ob.size() == 0) {

                return 5;
            }

            for (GameObject el : ob) {
                int index = model.collision_handler().handle_collision(object2, el, speed, model);
                if(index == 6){
                    objects_update.clear();
                    return index;
                }
                else if(index == 5){
                    return 5;
                }
                else{
                    return 5;
                }

            }
            return 5;
        }
        else if(status2.get_property(PropertyTypeText.STOP)){
            objects_update.clear();
            return 6;
        }
        else{ //
            return 5;
        }
    }

    @Override
    public void set_next_handler(CollisionHandler next) {
        this.next_handler = next;
    }

}
