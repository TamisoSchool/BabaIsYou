package Collision;

import Game.GameModel;
import Object.*;
import java.awt.*;

public class FloatHandler implements CollisionHandler {
    private CollisionHandler next_handler;

    @Override
    public PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        var objectStatus1 = model.getObjectStatus(object1.Type());
        var objectStatus2 = model.getObjectStatus(object2.Type());
        if(objectStatus1.get_property(PropertyTypeText.FLOAT) == objectStatus2.get_property(PropertyTypeText.FLOAT))
            return this.next_handler.handle_collision(object1, object2, speed, model);
        return PropertyTypeText.FLOAT;


    }

    @Override
    public void set_next_handler(CollisionHandler next) {
        this.next_handler = next;
    }
}
