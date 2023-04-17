import java.awt.*;
import java.util.ArrayList;

enum Direction {
    Right,
    Left,
    Up,
    Down,
}

public class GameMap {
    public ArrayList<GameObject> objects = new ArrayList<>();

    public static ArrayList<GameObject> MultipleObject(int startX, int startY, ObjectType type, int numberOfObject, Direction dir) {
        int amountX = 0;
        int amountY = 0;
        switch (dir) {
            case Right -> amountX = GameModel.objectWidth;
            case Left -> amountX = -GameModel.objectWidth;
            case Up -> amountY = -GameModel.objectHeight;
            case Down -> amountY = GameModel.objectHeight;
        }
        ArrayList<GameObject> rList = new ArrayList<>();
        for (int i = 0; i < numberOfObject; i++) {
            int xPos = startX + amountX * i;
            int yPos = startY + amountY * i;
            if (xPos < 0 || xPos + GameModel.objectWidth > 1920 || yPos < 0 || yPos + GameModel.objectHeight > 1080)
                continue;

            GameObject g = new GameObject(xPos, yPos, type, Color.black);
            rList.add(g);
        }
        return rList;
    }

}
