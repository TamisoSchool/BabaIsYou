package Collision;

import Game.GameModel;
import Object.GameObject;
import Object.PropertyTypeText;

import java.awt.*;

/**
 * Collision Handler must implement this interface.
 * Root Collision exist in Game Model.
 */
public interface CollisionHandler {
    /**
     * a unique method for how it deals with collision with 2 objects
     */
    PropertyTypeText handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model);

    /**
     * a method for next handler if this one cannot deal with the objects.
     */
    void set_next_handler(CollisionHandler next);
}
