import javax.swing.*;
import java.awt.*;

public interface MovingObject {
    void update(Point speed);

    boolean is_moving();

    JComponent getComp();
}
