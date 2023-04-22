package Game;

import Object.GameObject;
import Object.ObjectType;

import java.util.ArrayList;
import Object.*;

public class GameMap {
    public ArrayList<GameObject> objects = new ArrayList<>();

    public static ArrayList<TextObject> sentence_generator(int startX, int startY, ObjectType first,
                                                           OperatorText second, PropertyTypeText third, GameModel gm, Direction dir){
        TextObject one = new TextObject(startX, startY, first, gm);
        TextObject two;
        TextObject three;
        ArrayList<TextObject> txt = new ArrayList<TextObject>();
        if(dir == Direction.Right) {
            two = new TextObject(startX + GameModel.objectWidth, startY, second, gm);
            three = new TextObject(startX + GameModel.objectWidth*2, startY, third, gm);

            txt.add(one);
            txt.add(two);
            txt.add(three);
        }
        if(dir == Direction.Down){
            two = new TextObject(startX, startY + GameModel.objectHeight, second, gm);
            three = new TextObject(startX, startY + GameModel.objectHeight * 2, third, gm);

            txt.add(one);
            txt.add(two);
            txt.add(three);
        }
        return txt;
    }
    public static ArrayList<TextObject> sentence_generator(int startX, int startY, ObjectType first, OperatorText second,
                                                           ObjectType third, GameModel gm, Direction dir){
        TextObject one = new TextObject(startX, startY, first, gm);
        TextObject two;
        TextObject three;
        ArrayList<TextObject> txt = new ArrayList<TextObject>();
        if(dir == Direction.Right) {
            two = new TextObject(startX + GameModel.objectWidth, startY, second, gm);
            three = new TextObject(startX + GameModel.objectWidth*2, startY, third, gm);

            txt.add(one);
            txt.add(two);
            txt.add(three);
        }
        if(dir == Direction.Down){
            two = new TextObject(startX, startY + GameModel.objectHeight, second, gm);
            three = new TextObject(startX, startY + GameModel.objectHeight * 2, third, gm);

            txt.add(one);
            txt.add(two);
            txt.add(three);
        }
        return txt;
    }
    public static ArrayList<TextObject> sentence_generator_grid(int gridx, int gridy, ObjectType first, OperatorText second, PropertyTypeText third, GameModel gm, Direction dir){
        return sentence_generator(gridx*GameModel.objectWidth, gridy*GameModel.objectHeight, first, second, third, gm, dir);
    }
    public static ArrayList<TextObject> sentence_generator_grid(int gridx, int gridy, ObjectType first, OperatorText second, ObjectType third, GameModel gm, Direction dir){
        return sentence_generator(gridx*GameModel.objectWidth, gridy*GameModel.objectHeight, first, second, third, gm, dir);
    }
    public static ArrayList<GameObject> multiple_object(int startX, int startY, ObjectType type, int numberOfObject, Direction dir) {
        int amountX = 0;
        int amountY = 0;
        switch (dir) {
            case Right -> amountX = GameModel.objectWidth;
            case Left -> amountX = -GameModel.objectWidth;
            case Up -> amountY = -GameModel.objectHeight;
            case Down -> amountY = GameModel.objectHeight;
        }
        ArrayList<GameObject> rList = new ArrayList<>();
        for (int i = 0; i <= numberOfObject; i++) {
            int xPos = startX + amountX * i;
            int yPos = startY + amountY * i;
            if (xPos < 0 || xPos + GameModel.objectWidth > 1920 || yPos < 0 || yPos + GameModel.objectHeight > 1080)
                continue;

            GameObject g = new GameObject(xPos, yPos, type);
            rList.add(g);
        }
        return rList;
    }

    public static ArrayList<GameObject> multiplie_object_grid(int gridX, int gridY, ObjectType type, int numberOfObject, Direction dir){
        return multiple_object(gridX*GameModel.objectWidth, gridY*GameModel.objectHeight, type, numberOfObject, dir);
    }

}
