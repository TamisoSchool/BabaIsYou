package Collision;

import Game.GameModel;
import Object.GameObject;

import java.awt.*;
import Object.*;

public interface CollisionHandler {
    PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model);

    void set_next_handler(CollisionHandler next);


}
