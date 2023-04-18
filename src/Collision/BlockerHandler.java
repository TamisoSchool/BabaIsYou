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
    private final CollisionHandler next_handler;
    private static Set<GameObject> objectsUpdate = new HashSet<GameObject>();
    public BlockerHandler(CollisionHandler next_handler) {
        this.next_handler = next_handler;
    }

    @Override
    public int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        ObjectStatus status2 = model.getObjectStatus(object2.Type());
        objectsUpdate.add(object1);
        if (status2.get_property(PropertyTypeText.PUSH)) {

            ArrayList<GameObject> ob = model.intersect(object2.getRectangle(), object2.quads.get(0));
            objectsUpdate.add(object2);

            if (ob.size() == 0) {

                return 5;
            }

            for (GameObject el : ob) {
                int index = model.collision_handler().get(0).handle_collision(object2, el, speed, model);
                if(index == 6){
                    objectsUpdate.clear();
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
            objectsUpdate.clear();
            return 6;
        }
        else{ //
            return 5;
        }
    }

}
