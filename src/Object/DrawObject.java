package Object;

import java.awt.*;

/**
 * Used in GameObject as a way to uniquely paint different ObjectTypes.
 */
public interface DrawObject {
    void onPaint(Graphics g);
}
