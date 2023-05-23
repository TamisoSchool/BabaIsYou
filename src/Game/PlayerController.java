package Game;


import Collision.BlockerHandler;
import Object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * All the inputs and game updates starts from this class.
 */
public class PlayerController implements KeyListener, MovingObject {
    GameModel model;
    private static final boolean pause = false;
    /**
     * Speed of the character in this frame
     */
    Point point_speed = new Point(0, 0);
    /**
     * If the character is being blocked by a wall or something that makes it so he cannot move.
     */
    boolean blocked = false;
    /**
     * a list to know if i let a key up to know if there exist another one pressed.
     */
    ArrayList<Integer> dir = new ArrayList<Integer>();

    public Timer game_update;

    private boolean game_on = false;
    /**
     * Creates a timer for updating the game
     * Adding on click events
     * Attaching keylistener to gameview.
     */
    public PlayerController(GameModel model, OnGameView gmListeners) {
        this.model = model;

        Timer gameUpdate = new Timer(10, e -> {
            update(new Point());
        });
        this.game_update = gameUpdate;
        gmListeners.on_start_menu_add(e -> {
            this.model.start_new_level();
            gameUpdate.start();
            game_on = true;
        });

        gmListeners.on_resume_menu_add(e -> {
            game_on = true;
        });
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if(!game_on)
            return;

        switch (keycode) { // check for a valid key to be pressed
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
            case KeyEvent.VK_ESCAPE -> {
                    this.model.open_menu();
                    game_on = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        dir.remove(Integer.valueOf(e.getKeyCode()));

        if (dir.size() == 0) { // if there is no keys pressed
            point_speed = new Point(0, 0);
            return;
        }
        switch (dir.get(dir.size() - 1)) { // if there is a key pressed
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


    /**
     * Happens every 10 millie seconds. Check if the player is moving and
     * if it is gonna collide with something. Then it uses methods from Game model.
     * @param point Does not matter in this context
     */
    @Override
    public void update(Point point) {

        if (is_moving()) { // Is moving

            ArrayList<GameObject> player_objects = this.model.get_player();
            for(GameObject player_object: player_objects) { // player can be multiplie objects, not implemented in the game
                Rectangle playerRayCast = this.model.raycast_object(player_object.getRectangle(), point_speed);
                blocked = false;

                ArrayList<GameObject> intersecting = this.model.intersect(playerRayCast, player_object.quads.get(0));
                if (intersecting != null)
                    if (intersecting.size() > 0) {
                        for (GameObject ob : intersecting) { // checks all objects the player collide with.
                            PropertyTypeText index = this.model.collision_handler().handle_collision(player_object, ob, point_speed, this.model);
                            if (index == PropertyTypeText.STOP || index == PropertyTypeText.SHUT) {
                                blocked = true;
                            }
                        }
                        for (GameObject p : BlockerHandler.object_update_push()) { // if objects are being pushed indirectly
                            if (!blocked)
                                p.update(point_speed);
                        }
                    } else {
                        player_object.update(point_speed);
                    }
            }
            }
    }




    @Override
    public boolean is_moving() {
        return point_speed.x != 0 || point_speed.y != 0;
    }

    @Override
    public JComponent getComp() {
        return null;
    }
}
