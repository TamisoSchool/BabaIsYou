package Collision;

import Game.GameModel;
import Object.GameObject;

import java.awt.*;

public interface CollisionHandler {
    int handle_collision(GameObject object1, GameObject object2, Point speed, GameModel model);
}
