import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerController implements KeyListener, MovingObject {
    private final int rayCastLine = 1;
    private final GameObject rayCast;
    GameObject playerObject;
    GameModel modeL;
    Point point_speed = new Point(0, 0);
    boolean blocked = false;
    Point point_speed_last_update = new Point(0, 0);
    ArrayList<Integer> dir = new ArrayList<Integer>();

    public PlayerController(GameModel model) {
        this.modeL = model;
        playerObject = new GameObject(100, 100, ObjectType.Player, Color.RED);
        rayCast = new GameObject(10, 10, ObjectType.Wall, 10, 10, Color.GREEN);
        this.modeL.TestAddObject(rayCast);

        model.Attach_Object(this);
        model.Attach_KeyListener(this);
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

    private Rectangle RaycastObject(Rectangle origin, Point direction) {
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
        if (isMoving()) {

            Rectangle playerRayCast = RaycastObject(playerObject.getRectangle(), point_speed);
            blocked = false;

            this.rayCast.height = playerRayCast.height;
            this.rayCast.width = playerRayCast.width;
            this.rayCast.x = playerRayCast.x;
            this.rayCast.y = playerRayCast.y;

            this.rayCast.repaint();


            ArrayList<GameObject> intersecting = this.modeL.Intersect(playerRayCast, playerObject.quads.get(0));

            ArrayList<GameObject> objectsToUpdatePush = new ArrayList<>();
            if(intersecting != null) {
                for (GameObject ob : intersecting // loop objects that intersect with player
                ) {
                    ObjectStatus status = this.modeL.getObjectStatus(ob.type);
                    if (status.isFloat)
                        continue;

                    if (status.isDeath) {
                        // lose level
                        // restart level
                        return;
                    }
                    if (!status.isPushable && !blocked) {
                        blocked = true;
                    } else if (status.isPushable && !blocked) {
                        var objectTuple = this.modeL.Intersect(RaycastObject(ob.getRectangle(), point_speed), ob.quads.get(0));
                        if (objectTuple == null) {
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
                                ObjectStatus status1 = this.modeL.getObjectStatus(o.type);
                                if (status1.isFloat)
                                    continue;
                                if (status1.isPushable) {
                                    objectsToUpdatePush.add(o);
                                    var it = this.modeL.Intersect(RaycastObject(o.getRectangle(), point_speed), o.quads.get(0));
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
    public boolean isMoving() {
        return point_speed.x != 0 || point_speed.y != 0;
    }

    @Override
    public JComponent getComp() {
        return playerObject;
    }
}
