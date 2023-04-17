import java.awt.*;
import java.io.Console;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        GameView gv = new GameView();
        GameModel gm = new GameModel(gv);

        PlayerController player = new PlayerController(gm);

        GameMap map1 = new GameMap();
        map1.objects.addAll(GameMap.MultipleObject(0, 0, ObjectType.Wall, 10, Direction.Down));
        map1.objects.add(player.playerObject);
        map1.objects.add(new GameObject(150, 150, ObjectType.Wall , Color.black));
        map1.objects.add(new GameObject(150 + GameModel.objectWidth, 150, ObjectType.Wall , Color.black));
        gm.StartNewMap(map1);


    }
}
