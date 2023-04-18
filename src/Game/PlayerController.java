package Game;


import Object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerController implements KeyListener, MovingObject {
    private final int rayCastLine = 1;
    private final GameObject rayCast;
    public GameObject playerObject;
    GameModel model;
    Point point_speed = new Point(0, 0);
    boolean blocked = false;
    Point point_speed_last_update = new Point(0, 0);
    ArrayList<Integer> dir = new ArrayList<Integer>();

    public PlayerController(GameModel model) {
        this.model = model;
        playerObject = new GameObject(100, 100, ObjectType.PLAYER, Color.RED);
        rayCast = new GameObject(10, 10, ObjectType.WALL, 10, 10, Color.GREEN);
        this.model.test_add_object(rayCast);

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

    private Rectangle raycast_object(Rectangle origin, Point direction) {
        int width = rayCastLine * Math.abs(direction.x) + Math.abs(direction.y) * GameModel.objectWidth;
        int height = GameModel.objectHeight * Math.abs(direction.x) + Math.abs(direction.y) * rayCastLine;

        int xPos = origin.x;
        if (direction.x < 0)
            xPos = origin.x - width;
        if (direction.x > 0)
            xPos = origin.x + GameModel.objectWidth;

        int yPos = origin.y;
        if (direction.y < 0)
            yPos = origin.y - height;
        if (direction.y > 0)
            yPos = origin.y + GameModel.objectHeight;

        return new Rectangle(xPos, yPos, width, height);
    }

    @Override
    public void update(Point point) {
        if (is_moving()) {

            Rectangle playerRayCast = raycast_object(playerObject.getRectangle(), point_speed);
            blocked = false;

            this.rayCast.set_val(playerRayCast.x, playerRayCast.y, playerRayCast.width, playerRayCast.height);
            this.rayCast.repaint();


            ArrayList<GameObject> intersecting = this.model.intersect(playerRayCast, playerObject.quads.get(0));

            ArrayList<GameObject> objectsToUpdatePush = new ArrayList<>();
            ObjectStatus playerStatus = this.model.getObjectStatus(playerObject.Type());
            boolean isFloatPlayer = playerStatus.get_property(PropertyTypeText.FLOAT);
            if(intersecting != null) {
                for (GameObject ob : intersecting // loop objects that intersect with player
                ) {

                    ObjectStatus status = this.model.getObjectStatus(ob.Type());
                    if (status.get_property(PropertyTypeText.FLOAT) != isFloatPlayer)
                        continue;

                    if (status.get_property(PropertyTypeText.DEFEAT)) {
                        // lose level
                        // restart level
                        return;
                    }
                    boolean objectPush = status.get_property(PropertyTypeText.PUSH);
                    if (!objectPush && !blocked) {
                        blocked = true;
                    } else if (objectPush && !blocked) {
                        var objectTuple = this.model.intersect(raycast_object(ob.getRectangle(), point_speed), ob.quads.get(0));
                        if (objectTuple == null) { // out of frame
                            blocked = true;
                            break;
                        }
                        objectsToUpdatePush.add(ob);

                        if (objectTuple.size() == 0) { // if nothing is blocking
                            continue;
                        } else {
                            var iterator = objectTuple.iterator();
                            while (iterator.hasNext()) {
                                GameObject o = iterator.next();
                                ObjectStatus status1 = this.model.getObjectStatus(o.type);
                                if (status1.isFloat)
                                    continue;
                                if (status1.isPushable) {
                                    objectsToUpdatePush.add(o);
                                    var it = this.model.intersect(raycast_object(o.getRectangle(), point_speed), o.quads.get(0));
                                    if (it == null) { // out of frame
                                        blocked = true;
                                        break;
                                    }
                                    iterator = it.iterator();
                                } else {
                                    blocked = true;
                                    break;
                                }
                            }
                        }
                    }
                }


                if (!blocked) {
                    for (GameObject ob : objectsToUpdatePush) {
                        ob.update(point_speed);
                    }
                    this.playerObject.update(point_speed);
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
        return playerObject;
    }
}
