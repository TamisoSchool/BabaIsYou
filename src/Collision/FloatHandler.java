package Collision;

import Game.GameModel;
import Object.*;
import java.awt.*;

public class FloatHandler implements CollisionHandler {
    private final CollisionHandler next_handler;

    public FloatHandler(CollisionHandler nextHandler) {
        this.next_handler = nextHandler;
    }

    @Override
    public int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        var objectStatus1 = model.getObjectStatus(object1.Type());
        var objectStatus2 = model.getObjectStatus(object2.Type());
        if(objectStatus1.get_property(PropertyTypeText.FLOAT) == objectStatus2.get_property(PropertyTypeText.FLOAT))
            return this.next_handler.handle_collision(object1, object2, speed, model);
        return 0;


    }
}
