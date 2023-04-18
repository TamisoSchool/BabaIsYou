package Collision;

import Game.GameModel;
import Object.*;

import java.awt.*;

public class DestroyHandler implements CollisionHandler {
    private final CollisionHandler next_handler;

    public DestroyHandler(CollisionHandler nextHandler) {
        this.next_handler = nextHandler;
    }

    @Override
    public int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        var objectStatus1 = model.getObjectStatus(object1.Type());
        var objectStatus2 = model.getObjectStatus(object2.Type());
        if (objectStatus1.get_property(PropertyTypeText.HOT) && objectStatus2.get_property(PropertyTypeText.MELT)) {
            object2.destroy();
            return 2;
        } else if (objectStatus1.get_property(PropertyTypeText.MELT) && objectStatus2.get_property(PropertyTypeText.HOT)) {
            object1.destroy();
            return 2;
        }

        else if(objectStatus1.get_property(P))

    }
}

