package Collision;

import Game.GameModel;
import Object.GameObject;
import Object.ObjectType;
import Object.PropertyTypeText;

import java.awt.*;

public class OutcomeHandler implements CollisionHandler {
    private final CollisionHandler next_handler;

    public OutcomeHandler(CollisionHandler nextHandler) {
        this.next_handler = nextHandler;
    }

    @Override
    public int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {
        if (object1.Type() == ObjectType.PLAYER || object2.Type() == ObjectType.PLAYER) {

            var objectStatus1 = model.getObjectStatus(object1.Type());
            var objectStatus2 = model.getObjectStatus(object2.Type());

            boolean float1 = objectStatus1.get_property(PropertyTypeText.FLOAT);
            boolean float2 = objectStatus2.get_property(PropertyTypeText.FLOAT);

            if (float1 != float2) {
                return next_handler.handle_collision(object1, object2, speed, model);
            } else {
                boolean defeat1 = objectStatus1.get_property(PropertyTypeText.DEFEAT);
                boolean defeat2 = objectStatus2.get_property(PropertyTypeText.DEFEAT);
                if (defeat1 || defeat2) {
                    // lose level
                    return 0;
                } else {
                    boolean win1 = objectStatus1.get_property(PropertyTypeText.WIN);
                    boolean win2 = objectStatus2.get_property(PropertyTypeText.WIN);

                    if(win1 || win2){
                        // win
                        return 0;
                    }

                    return next_handler.handle_collision(object1, object2, speed, model);
                }
            }
        }
        return 0;
    }
}
