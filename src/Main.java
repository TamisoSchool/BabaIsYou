import Game.*;
import Object.GameObject;
import Object.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        GameView gv = new GameView();
        GameModel gm = new GameModel(gv);

        PlayerController player = new PlayerController(gm);

        GameMap map1 = new GameMap();

        map1.objects.addAll(GameMap.multiplie_object_grid(0, 0, ObjectType.WALL, 3, Direction.Right));
        map1.objects.addAll(GameMap.multiplie_object_grid(3, 1, ObjectType.WALL, 3, Direction.Down));

        map1.objects.addAll(GameMap.multiplie_object_grid(0, 5, ObjectType.WALL, 3, Direction.Right));

        map1.objects.addAll(GameMap.sentence_generator_grid(0, 1, ObjectType.WALL, OperatorText.IS, PropertyTypeText.STOP, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(0, 2, ObjectType.KEY, OperatorText.IS, PropertyTypeText.OPEN, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(0, 3, ObjectType.DOOR, OperatorText.IS, PropertyTypeText.SHUT, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(0, 4, ObjectType.KEY, OperatorText.IS, PropertyTypeText.PUSH, gm, Direction.Right));
        map1.objects.add(new GameObject(400, 400, ObjectType.DOOR));
        map1.objects.add(new GameObject(500, 400, ObjectType.KEY));

        map1.objects.add(player.player_object);
        gm.start_new_level(map1);

    }
}
