import Game.*;
import Object.GameObject;
import Object.*;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        GameView gv = new GameView();

        GameMap map1 = new GameMap();
        GameMap map2 = new GameMap();
        ArrayList<GameMap> maps = new ArrayList<>();

        GameModel gm = new GameModel(gv, maps);

        PlayerController player = new PlayerController(gm);



        maps.add(map1);
        maps.add(map2);



        ArrayList<GameObject> playerRoom = GameMap.rectangle_border(10, 10, ObjectType.WALL, 10, 10);
        GameObject player_object = new GameObject(13*GameModel.objectWidth, 13*GameModel.objectHeight, ObjectType.BABA);
        map1.objects.add(new GameObject(14*GameModel.objectWidth, 14*GameModel.objectHeight, ObjectType.KEY));

        playerRoom.get(14).set_object_type(ObjectType.DOOR);

        map1.objects.addAll(GameMap.sentence_generator_grid(1, 1, ObjectType.WALL, OperatorText.IS, PropertyTypeText.STOP, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 2, ObjectType.KEY, OperatorText.IS, PropertyTypeText.OPEN, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 3, ObjectType.DOOR, OperatorText.IS, PropertyTypeText.SHUT, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 4, ObjectType.KEY, OperatorText.IS, PropertyTypeText.PUSH, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 5, ObjectType.BABA, OperatorText.IS, PropertyTypeText.YOU, gm, Direction.Right));
        map1.objects.addAll(GameMap.sentence_generator_grid(1, 6, ObjectType.FLAG, OperatorText.IS, PropertyTypeText.WIN, gm, Direction.Right));
        map1.objects.addAll(GameMap.rectangle_border(10, 9, ObjectType.FLAG, 1, 0));

        map1.objects.addAll(GameMap.rectangle_border(0, 0, ObjectType.WALL, 5, 7));


        map1.objects.addAll(playerRoom);

        map1.objects.add(player_object);

        map2.objects.addAll(GameMap.rectangle_border(10, 11, ObjectType.WALL, 10, 10));
        map2.objects.addAll(GameMap.rectangle_border(13, 12, ObjectType.FLAG, 1, 0));
        map2.objects.addAll(GameMap.rectangle_border(4, 4, ObjectType.WALL, 10, 5));

        map2.objects.add(GameMap.generate_word_object(5, 5, ObjectType.FLAG, gm));
        map2.objects.add(GameMap.generate_word_object(20, 20, PropertyTypeText.WIN, gm));
        map2.objects.addAll(GameMap.sentence_generator_grid(30, 30, ObjectType.BABA, OperatorText.IS, PropertyTypeText.YOU, gm, Direction.Down));
        map2.objects.addAll(GameMap.sentence_generator_grid(14, 12, ObjectType.WALL, OperatorText.IS, PropertyTypeText.STOP, gm, Direction.Down));
        GameObject map2_player = new GameObject(16*GameModel.objectWidth, 13*GameModel.objectHeight, ObjectType.BABA);
        map2.objects.add(map2_player);
        gm.start_new_level();

    }
}
