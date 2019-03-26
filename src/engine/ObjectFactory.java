package engine;

import core.Point3;
import core.Vector3;
import engine.collision.bounding.BoundingBox;
import engine.collision.bounding.BoundingSphere;
import engine.collision.bounding.ObjectBounding;
import object.*;

public class ObjectFactory {

    public static Ball createBall(Point3 position, double radius, double mass, Material material) {
        Ball ball = new Ball(position, radius, mass);
        ball.setMaterial(material);
        ObjectBounding bounding = new BoundingSphere(ball, radius);
        ball.initBounding(bounding);

        ObjectContainer.get().add(ball);
        return ball;
    }

    public static Box createBox(Point3 position, Vector3 size, double mass, Material material) {
        Box box = new Box(position, size, mass);
        box.setMaterial(material);
        ObjectBounding bounding = new BoundingBox(box, size.scaled(0.5));
        box.initBounding(bounding);

        ObjectContainer.get().add(box);
        return box;
    }

    public static Pawn createPawn(Point3 position, double height, double radius) {
        return createPawn(position, 0, height, radius, Material.Wood);
    }

    public static Pawn createPawn(Point3 position, double yaw, double height, double radius, Material material) {
        Pawn pawn = new Pawn(position, yaw, height, radius);
        pawn.setMaterial(material);
        ObjectBounding bounding = new BoundingBox(pawn, new Vector3(radius, radius, height/2));
        pawn.initBounding(bounding);

        ObjectContainer.get().add(pawn);
        return pawn;
    }

    public static Platform createPlatform(Point3 position, Vector3 size, Material material) {
        Platform platform = new Platform(position, size);
        platform.setMaterial(material);
        ObjectBounding bounding = new BoundingBox(platform, size.scaled(0.5));
        platform.initBounding(bounding);

        ObjectContainer.get().add(platform);
        return platform;
    }
}
