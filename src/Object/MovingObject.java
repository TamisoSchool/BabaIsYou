package Object;

import javax.swing.*;
import java.awt.*;
/**
 * must be implemented for object that can be affected by other objects.
 */
public interface MovingObject {
    void update(Point speed);

    boolean is_moving();

    JComponent getComp();
}
