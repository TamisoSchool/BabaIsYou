package Game;

import java.awt.event.ActionListener;

/**
 * Used in GameView
 */
public interface OnGameView {

    /**
     * On menu start clicked event
     */
    void on_start_menu_add(ActionListener l);
    /**
     * On menu resume button click event
     */
    void on_resume_menu_add(ActionListener l);
}
