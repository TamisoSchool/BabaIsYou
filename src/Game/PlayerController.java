package Game;


import Collision.BlockerHandler;
import Object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerController implements KeyListener, MovingObject {
    public static GameObject player_object;
    public static GameObject get_player_object(){return player_object;}
    GameModel model;
    Point point_speed = new Point(0, 0);
    boolean blocked = false;
    ArrayList<Integer> dir = new ArrayList<Integer>();

    public PlayerController(GameModel model) {
        this.model = model;
        player_object = new GameObject(100, 100, ObjectType.PLAYER);

        Timer gameUpdate = new Timer(10, e -> {
            this.update(new Point(0,0));
        });
        gameUpdate.start();
        model.attach_keyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_UP -> {
                if (!dir.contains(KeyEvent.VK_UP))
                    dir.add(KeyEvent.VK_UP);

                point_speed = new Point(0, -1);
            }
            case KeyEvent.VK_DOWN -> {
                if (!dir.contains(KeyEvent.VK_DOWN))
                    dir.add(KeyEvent.VK_DOWN);
                point_speed = new Point(0, 1);
            }
            case KeyEvent.VK_LEFT -> {
                if (!dir.contains(KeyEvent.VK_LEFT))
                    dir.add(KeyEvent.VK_LEFT);
                point_speed = new Point(-1, 0);
            }
            case KeyEvent.VK_RIGHT -> {
                if (!dir.contains(KeyEvent.VK_RIGHT))
                    dir.add(KeyEvent.VK_RIGHT);
                point_speed = new Point(1, 0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        dir.remove(Integer.valueOf(e.getKeyCode()));
        if (dir.size() == 0) {
            point_speed = new Point(0, 0);
            return;
        }
        switch (dir.get(dir.size() - 1)) {
            case KeyEvent.VK_UP -> {
                point_speed = new Point(0, -1);
            }
            case KeyEvent.VK_DOWN -> {
                point_speed = new Point(0, 1);
            }
            case KeyEvent.VK_LEFT -> {
                point_speed = new Point(-1, 0);
            }
            case KeyEvent.VK_RIGHT -> {
                point_speed = new Point(1, 0);
            }
        }
    }



    @Override
    public void update(Point point) {
        if (is_moving()) {

            Rectangle playerRayCast = this.model.raycast_object(player_object.getRectangle(), point_speed);
            blocked = false;

            ArrayList<GameObject> intersecting = this.model.intersect(playerRayCast, player_object.quads.get(0));
            if(intersecting != null)
                if(intersecting.size() > 0) {
                    for (GameObject ob : intersecting) {
                        PropertyTypeText index = this.model.collision_handler().handle_collision(this.player_object, ob, point_speed, this.model);
                        if(index == PropertyTypeText.STOP || index == PropertyTypeText.SHUT){
                            blocked = true;
                        }
                    }
                    for (GameObject p : BlockerHandler.object_update_push()) {
                        if(!blocked)
                            p.update(point_speed);
                    }
                }
                else{
                    player_object.update(point_speed);
                }
            }
    }




    @Override
    public boolean is_moving() {
        return point_speed.x != 0 || point_speed.y != 0;
    }

    @Override
    public JComponent getComp() {
        return player_object;
    }
}
