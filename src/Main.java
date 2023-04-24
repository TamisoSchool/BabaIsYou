import Game.*;
import Object.GameObject;
import Object.*;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        GameView gv = new GameView();
        GameModel gm = new GameModel(gv);

        PlayerController player = new PlayerController(gm);

        GameMap map1 = new GameMap();



       // map1.objects.addAll(GameMap.rectangle_border(0, 0, ObjectType.WALL, 5, 4));
        ArrayList<GameObject> playerRoom = GameMap.rectangle_border(10, 10, ObjectType.WALL, 10, 10);
        GameObject player_object = new GameObject(13*GameModel.objectWidth, 13*GameModel.objectHeight, ObjectType.BABA);
        map1.objects.add(new GameObject(14*GameModel.objectWidth, 14*GameModel.objectHeight, ObjectType.KEY));

        playerRoom.get(14).set_object_type(ObjectType.DOOR);

        map1.objects.addAll(GameMap.sentence_generator_grid(1, 1, ObjectType.WALL, OperatorText.IS, PropertyTypeText.STOP, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 2, ObjectType.KEY, OperatorText.IS, PropertyTypeText.OPEN, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 3, ObjectType.DOOR, OperatorText.IS, PropertyTypeText.SHUT, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 4, ObjectType.KEY, OperatorText.IS, PropertyTypeText.PUSH, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 5, ObjectType.BABA, OperatorText.IS, PropertyTypeText.YOU, gm, Direction.Right));


        map1.objects.addAll(playerRoom);

        map1.objects.add(player_object);
        gm.start_new_level(map1);

    }
}
