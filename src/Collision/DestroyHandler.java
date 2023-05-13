package Collision;

import Game.GameModel;
import Object.*;

import java.awt.*;
/**
 * Number Two on the chain
 */
public class DestroyHandler implements CollisionHandler {
    private CollisionHandler next_handler;

    /**
     * collision handler that accounts for all the ways the objects can be destroyed.
     */
    @Override
    public PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        var objectStatus1 = model.getObjectStatus(object1.Type());
        var objectStatus2 = model.getObjectStatus(object2.Type());
        if (objectStatus1.get_property(PropertyTypeText.HOT) && objectStatus2.get_property(PropertyTypeText.MELT)) {
            object2.destroy();
            return PropertyTypeText.HOT;
        } else if (objectStatus1.get_property(PropertyTypeText.MELT) && objectStatus2.get_property(PropertyTypeText.HOT)) {
            object1.destroy();
            return PropertyTypeText.HOT;
        }
        else if(objectStatus1.get_property(PropertyTypeText.SHUT) && objectStatus2.get_property(PropertyTypeText.OPEN)){
            object1.destroy();
            object2.destroy();
            return PropertyTypeText.NULL;
        }
        else if(objectStatus1.get_property(PropertyTypeText.OPEN) && objectStatus2.get_property(PropertyTypeText.SHUT)){
            object1.destroy();
            object2.destroy();
            return PropertyTypeText.NULL;
        }
        else if(objectStatus1.get_property(PropertyTypeText.SINK) || objectStatus2.get_property(PropertyTypeText.SINK)){
            object1.destroy();
            object2.destroy();
            return PropertyTypeText.SINK;
        }
        return next_handler.handle_collision(object1, object2, speed, model);
    }

    @Override
    public void set_next_handler(CollisionHandler next) {
        this.next_handler = next;
    }
}

