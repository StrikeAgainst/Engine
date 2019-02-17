package engine;

import core.Point3;
import core.RGB;
import core.Vector3;
import engine.collision.bounding.BoundingBox;
import engine.collision.bounding.BoundingSphere;
import engine.collision.bounding.ObjectBounding;
import object.*;

public class ObjectFactory {

    public static Ball createBall(Point3 position, float radius, float mass) {
        Ball ball = new Ball(position, radius, mass);
        ObjectBounding bounding = new BoundingSphere(ball, radius);
        ball.initBounding(bounding);

        ObjectContainer.get().add(ball);
        return ball;
    }

    public static Box createBox(Point3 position, Vector3 size, float mass) {
        Box box = new Box(position, size, mass);
        ObjectBounding bounding = new BoundingBox(box, size.scaled(0.5f));
        box.initBounding(bounding);

        ObjectContainer.get().add(box);
        return box;
    }

    public static Pawn createPawn(Point3 position, float height, float radius) {
        return createPawn(position, 0, height, radius);
    }

    public static Pawn createPawn(Point3 position, float yaw, float height, float radius) {
        Pawn pawn = new Pawn(position, yaw, height, radius);
        ObjectBounding bounding = new BoundingBox(pawn, new Vector3(radius, radius, height/2));
        pawn.initBounding(bounding);

        ObjectContainer.get().add(pawn);
        return pawn;
    }

    public static Tile createTile(Point3 position, float size) {
        return createTile(position, size, new RGB(0.8f, 0.8f, 0.8f));
    }

    public static Tile createTile(Point3 position, float size, RGB color) {
        Tile tile = new Tile(position, size, color);
        ObjectBounding bounding = new BoundingBox(tile, new Vector3(size/2, size/2, 0.01f));
        tile.initBounding(bounding);

        ObjectContainer.get().add(tile);
        return tile;
    }

    public static Wall createWall(Point3 position, float width, float height, boolean vertical) {
        return createWall(position, width, height, vertical, new RGB(0.5f, 0.5f, 0.5f));
    }

    public static Wall createWall(Point3 position, float width, float height, boolean vertical, RGB color) {
        Wall wall = new Wall(position, width, height, vertical, color);
        ObjectBounding bounding = new BoundingBox(wall, new Vector3((vertical?width/2:0.01f), (vertical?0.01f:width/2), height/2));
        wall.initBounding(bounding);

        ObjectContainer.get().add(wall);
        return wall;
    }
}
