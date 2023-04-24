package Collision;

import Game.GameModel;
import Game.PlayerController;
import Object.GameObject;
import Object.ObjectType;
import Object.PropertyTypeText;

import java.awt.*;

public class OutcomeHandler implements CollisionHandler {
    private CollisionHandler next_handler;

    @Override
    public PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model) {

        var type2 = model.player_types()[1];
        boolean pass = false;
        if(type2 != null){
            if (object1.Type() == type2 ||object2.Type() == type2){
                pass = true;
            }
        }
        if (object1.Type() == model.player_types()[0] ||object2.Type() == model.player_types()[0] || pass){

            var objectStatus1 = model.getObjectStatus(object1.Type());
            var objectStatus2 = model.getObjectStatus(object2.Type());





                boolean defeat1 = objectStatus1.get_property(PropertyTypeText.DEFEAT);
                boolean defeat2 = objectStatus2.get_property(PropertyTypeText.DEFEAT);
                if (defeat1 || defeat2) {
                    // lose level
                    return PropertyTypeText.DEFEAT;
                } else {
                    boolean win1 = objectStatus1.get_property(PropertyTypeText.WIN);
                    boolean win2 = objectStatus2.get_property(PropertyTypeText.WIN);

                    if(win1 || win2){
                        // win
                        return PropertyTypeText.WIN;
                    }

                    return next_handler.handle_collision(object1, object2, speed, model);
                }
            }

        return next_handler.handle_collision(object1, object2, speed, model);
    }

    @Override
    public void set_next_handler(CollisionHandler next) {
        this.next_handler = next;
    }
}
