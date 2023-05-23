package Game;

import Object.GameObject;

import java.util.ArrayList;

/**
 * Need to be implemented for the views used
 */
public interface View_V {

    /**
     * show the map in the view
     * @param objects
     */
    public void on_start_map(ArrayList<GameObject> objects);

    /**
     * on gameobject moves, update on view
     */
    public void on_gameobject_update(GameObject object);


    /**
     * Open menu for view
     */

    public void open_menu();


}
