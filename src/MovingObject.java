import javax.swing.*;
import java.awt.*;

public interface MovingObject {
    void update(Point speed);

    boolean isMoving();

    JComponent getComp();
}
